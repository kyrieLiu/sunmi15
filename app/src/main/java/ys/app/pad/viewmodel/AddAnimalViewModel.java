package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AddAnimalActivity;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.animalkind.AminalKindActivity;
import ys.app.pad.databinding.ActivityAddAnimalBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.AnimalTypeClassifyInfo;
import ys.app.pad.model.AnimalTypeInfo;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * 作者：lv
 * 时间：2017/3/18 11:31
 */
public class AddAnimalViewModel extends BaseActivityViewModel {

    private AddAnimalActivity mActivity;
    private ApiClient<BaseResult> mClient;
    public Serializable mSerializableExtra;
    public ObservableField<String> obAnimalName = new ObservableField<>("");
    public ObservableField<String> obAnimalGenderType = new ObservableField<>("");
    public ObservableField<String> obAnimalGender = new ObservableField<>("");
    public ObservableField<String> obAnimalAge = new ObservableField<>("");
    public ObservableField<String> obAnimalBirthday = new ObservableField<>("");
    public ObservableField<String> obAnimalBreed = new ObservableField<>("");
    public ObservableField<String> obAnimalBreedId = new ObservableField<>("");
    public ObservableField<String> obAnimalCategory = new ObservableField<>("");
    public ObservableField<String> obAnimalCategoryId = new ObservableField<>("");
    public ObservableField<String> obAnimalWeight = new ObservableField<>("");
    public ObservableField<String> obAnimalColor = new ObservableField<>("");
    public ObservableField<String> obAnimalIsYimiao = new ObservableField<>("");
    public ObservableField<String> obAnimalIsYimiaoType = new ObservableField<>("");
    public ObservableField<String> obAnimalIsQuchong = new ObservableField<>("");
    public ObservableField<String> obAnimalIsQuchongType = new ObservableField<>("");
    public ObservableField<String> obAnimalYimiaoDate = new ObservableField<>("");
    public ObservableField<String> obAnimalYimiaoPeriod = new ObservableField<>("");//疫苗周期设置
    public ObservableField<String> obAnimalQuchongDate = new ObservableField<>("");
    public ObservableField<String> neiquPeriod = new ObservableField<>("");//内驱时间设置
    public ObservableField<String> selectBreedHint = new ObservableField<>("请选择品种");
    public ObservableField<String> obAnimalWaiquTime = new ObservableField<>("");//外驱时间
    public ObservableField<String> obAnimalIsWaiqu = new ObservableField<>("");//是否外驱
    public ObservableField<String> waiquPeriod = new ObservableField<>("");//外驱提醒时间
    public ObservableBoolean selectWaiqu = new ObservableBoolean(false);
    public ObservableInt selectWaiquImg = new ObservableInt(R.mipmap.arrow_down_grey_solid);
    public ObservableInt selectBreedImg = new ObservableInt(R.mipmap.arrow_down_grey_solid);
    public ObservableInt selectQuchongImg = new ObservableInt(R.mipmap.arrow_down_grey_solid);
    public ObservableBoolean selectBreed = new ObservableBoolean(false);
    public ObservableBoolean selectQuchong = new ObservableBoolean(false);
    public ObservableBoolean selectYimiao = new ObservableBoolean(false);
    public ObservableInt selectYimiaoImg = new ObservableInt(R.mipmap.arrow_down_grey_solid);
    public ObservableInt selectYimiaoPeriodImg = new ObservableInt(R.mipmap.arrow_down_grey_solid);
    public ObservableBoolean obIsEdit = new ObservableBoolean(false);
    public ObservableField<String> obVipName = new ObservableField<>();
    public ObservableField<String> obVipPhone = new ObservableField<>();
    public ObservableField<String> isSkin = new ObservableField<>("");//是否有皮肤病
    public ObservableField<String> isBite = new ObservableField<>("");//是否咬人
    public ObservableField<String> petRemark = new ObservableField<>("");//宠物备注
    public int mVipId = 0;

    private RxManager mRxManager;
    private SelectDialog mSelectDialog;
    private SelectDialog mYimiaoSelectDialog;
    private SelectDialog mNeiquIsSelectDialog;
    private SelectDialog mNeiquPeriodSelectDialog;
    private SelectDialog mWaiquSelectDialog;//是否外驱选择框
    private TimeSelector mTimeWaiquSelector;//外驱时间
    private SelectDialog mWaiquPeriodSelctor;//外驱周期
    private SelectDialog mYiMiaoPeriodSelctor;//疫苗周期
    private SelectDialog skinSelectDialog;//皮肤病选择
    private SelectDialog biteSelectDialog;//是否咬人

    private TimeSelector mTimeQuchongSelector;
    private TimeSelector mTimeYimiaoSelector;
    private TimeSelector mTimeBirthdaySelector;
    private SelectDialog mAnimalTypeSelectDialog;
    private List<SelectInfo> genderInfos = new ArrayList<>();
    private List<AnimalTypeInfo> mAnimalTypeInfos;
    private long mAnimalId;
    private ActivityAddAnimalBinding binding;
    private String normalUserName;
    private String normalUserPhone;
    private SelectInfo skinSelect;
    private SelectInfo biteSelectInfo;


    public AddAnimalViewModel(AddAnimalActivity activity, ActivityAddAnimalBinding binding, Serializable serializableExtra, String normalUserName,
                              String normalUserPhone) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.mSerializableExtra = serializableExtra;
        this.binding = binding;
        this.normalUserName = normalUserName;
        this.normalUserPhone = normalUserPhone;
        getAnimalTypeFromDb();
        setIntentData();
    }

    private void setIntentData() {
        if (mSerializableExtra instanceof AnimalInfo) {//编辑
            AnimalInfo info = (AnimalInfo) mSerializableExtra;
            obIsEdit.set(true);

            obVipName.set(info.getUserName());
            obVipPhone.set(info.getUserPhone());
            mVipId = info.getUserId();
            mAnimalId = info.getId();
            obAnimalName.set(info.getName());
            obAnimalGenderType.set(info.getSex() + "");
            obAnimalGender.set(info.getSex() == 0 ? "公" : "母");
            obAnimalAge.set(info.getAge() + "");
            obAnimalCategoryId.set(info.getType() + "");
            obAnimalCategory.set(1 == info.getType() ? "猫" : "狗");
            obAnimalBreed.set(info.getVarietiesName());
            if (!StringUtils.isEmptyOrWhitespace(info.getVarietiesName())) {
                selectBreed.set(true);
                selectBreedImg.set(R.mipmap.arrow_down);
            }
            obAnimalWeight.set(info.getWeight() == 0 ? "" : info.getWeight() + "");
            obAnimalColor.set(info.getColour());
            if (info.getIsVaccine() == 1) {
                obAnimalIsYimiao.set("是");
                obAnimalIsYimiaoType.set("1");
                selectYimiaoImg.set(R.mipmap.arrow_down);
                selectYimiaoPeriodImg.set(R.mipmap.arrow_down);
                obAnimalYimiaoPeriod.set(info.getVaccineRemind());
                selectYimiao.set(true);
            }
            obAnimalYimiaoDate.set(info.getIsVaccine() == 1 ? DateUtil.longFormatDate(info.getVaccineTime()) : "");

            obAnimalBreedId.set(info.getVarieties() + "");
            if (info.getIsInsect() == 1) {
                obAnimalIsQuchongType.set("1");
                obAnimalIsQuchong.set("是");
                selectQuchong.set(true);
                neiquPeriod.set(info.getInsectRemind());
                obAnimalQuchongDate.set(DateUtil.longFormatDate(info.getInsectTime()));
                selectQuchongImg.set(R.mipmap.arrow_down);
            }
            if (info.getBirthday() != null) {
                obAnimalBirthday.set(DateUtil.longFormatDate(Long.parseLong(info.getBirthday())));
            }

            if (info.getIsInsectOut() == 1) {
                obAnimalIsWaiqu.set("是");
                selectWaiqu.set(true);
                waiquPeriod.set(info.getInsectOutRemind());
                obAnimalWaiquTime.set(DateUtil.longFormatDate(info.getInsectOutTime()));
                selectWaiquImg.set(R.mipmap.arrow_down);
            }
            switch (info.getIsSkin()) {
                case 0:
                    isSkin.set("正常");
                    break;
                case 1:
                    isSkin.set("敏感皮肤");
                    break;
                case 2:
                    isSkin.set("易感染皮肤");
                    break;
                case 3:
                    isSkin.set("轻微皮肤感染");
                    break;
                case 4:
                    isSkin.set("重度皮肤感染");
                    break;
            }
            switch (info.getIsBite()) {
                case 0:
                    isBite.set("正常");
                    break;
                case 1:
                    isBite.set("温顺");
                    break;
                case 2:
                    isBite.set("易激动");
                    break;
                case 3:
                    isBite.set("轻度攻击性");
                    break;
                case 4:
                    isBite.set("轻度防卫");
                    break;
                case 5:
                    isBite.set("敏感易攻击");
                    break;
                case 6:
                    isBite.set("劣性");
                    break;
            }
            petRemark.set(info.getComment());


        } else if (mSerializableExtra instanceof VipInfo) {//新增
            VipInfo info = (VipInfo) mSerializableExtra;

            obVipName.set(info.getName());
            obVipPhone.set(info.getPhone());
            mVipId = (int) info.getId();
        } else if (mSerializableExtra == null) {
            obVipName.set(normalUserName);
            obVipPhone.set(normalUserPhone);
            mVipId = 0;
        }
    }

    private void getAnimalTypeFromDb() {
        mAnimalTypeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao().loadAll();
        Logger.i("animalTypeInfos.size-->" + mAnimalTypeInfos.size());
        if (mAnimalTypeInfos != null && !mAnimalTypeInfos.isEmpty()) {
            SelectInfo genderInfo;
            for (AnimalTypeInfo info : mAnimalTypeInfos) {//添加种类
                genderInfo = new SelectInfo(info.getName(), String.valueOf(info.getId()));
                genderInfos.add(genderInfo);
            }
        }
        initPetTypeDialog();
    }

    //宠物物种选择
    private void initPetTypeDialog() {
        if (mAnimalTypeSelectDialog == null && !genderInfos.isEmpty()) {
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mAnimalTypeSelectDialog = new SelectDialog(mActivity, adapter);
            mAnimalTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    selectBreed.set(true);
                    selectBreedImg.set(R.mipmap.arrow_down);
                    if ("1".equals(genderInfo.getType())) {
                    } else {
                        selectBreedHint.set("请选择品种");
                        obAnimalBreed.set("");
                    }
                    obAnimalCategoryId.set(genderInfo.getType());
                    obAnimalCategory.set(genderInfo.getName());
                }
            });
        }
    }

    public void clickGenderButton(View v) {
        if (mSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("公", "0"));
            genderInfos.add(new SelectInfo("母", "1"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    obAnimalGenderType.set(genderInfo.getType());
                    obAnimalGender.set(genderInfo.getName());
                }
            });
        }
        mSelectDialog.show();
    }


    /**
     * 宠物品种
     *
     * @param v
     */
    public void clickBreedButton(View v) {
        if (!StringUtils.isEmpty(obAnimalCategory.get())) {
            Intent intent = new Intent(mActivity, AminalKindActivity.class);
            intent.putExtra("categoryID", obAnimalCategoryId.get());
            mActivity.startActivityForResult(intent, 10);
        } else {
            Toast.makeText(mActivity, "请选择宠物种类", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickCategory(View v) {
        if (mAnimalTypeSelectDialog != null) {
            mAnimalTypeSelectDialog.show();
        }
    }

    /**
     * 选择是否有皮肤病
     */
    public void selectIsSkin() {
        if (skinSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("正常", "0"));
            genderInfos.add(new SelectInfo("敏感皮肤", "1"));
            genderInfos.add(new SelectInfo("易感染皮肤", "2"));
            genderInfos.add(new SelectInfo("轻微皮肤感染", "3"));
            genderInfos.add(new SelectInfo("重度皮肤感染", "4"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            skinSelectDialog = new SelectDialog(mActivity, adapter);
            skinSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    skinSelect = genderInfos.get(position);
                    isSkin.set(skinSelect.getName());
                }
            });
        }
        skinSelectDialog.show();
    }

    /**
     * 选择性格特征
     */
    public void selectIsBite() {
        if (biteSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("正常", "0"));
            genderInfos.add(new SelectInfo("温顺", "1"));
            genderInfos.add(new SelectInfo("易激动", "2"));
            genderInfos.add(new SelectInfo("轻度攻击性", "3"));
            genderInfos.add(new SelectInfo("轻度防卫", "4"));
            genderInfos.add(new SelectInfo("敏感攻击性", "5"));
            genderInfos.add(new SelectInfo("劣性", "6"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            biteSelectDialog = new SelectDialog(mActivity, adapter);
            biteSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    biteSelectInfo = genderInfos.get(position);
                    isBite.set(biteSelectInfo.getName());
                }
            });
        }
        biteSelectDialog.show();
    }

    /**
     * 是否疫苗
     *
     * @param v
     */
    public void clickIsYimiaoButton(View v) {
        if (mYimiaoSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("是", "1"));
            genderInfos.add(new SelectInfo("否", "0"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mYimiaoSelectDialog = new SelectDialog(mActivity, adapter);
            mYimiaoSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    if ("1".equals(genderInfo.getType())) {
                        selectYimiaoImg.set(R.mipmap.arrow_down);
                        selectYimiaoPeriodImg.set(R.mipmap.arrow_down);
                        selectYimiao.set(true);
                    } else {
                        selectYimiao.set(false);
                        selectYimiaoImg.set(R.mipmap.arrow_down_grey);
                        selectYimiaoPeriodImg.set(R.mipmap.arrow_down_grey);
                        obAnimalYimiaoDate.set("");
                        obAnimalYimiaoPeriod.set("");
                    }
                    obAnimalIsYimiaoType.set(genderInfo.getType());
                    obAnimalIsYimiao.set(genderInfo.getName());
                }
            });
        }
        mYimiaoSelectDialog.show();
    }

    //是否内驱
    public void clickIsQuchongButton(View v) {
        if (mNeiquIsSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("是", "1"));
            genderInfos.add(new SelectInfo("否", "0"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mNeiquIsSelectDialog = new SelectDialog(mActivity, adapter);
            mNeiquIsSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    if ("1".equals(genderInfo.getType())) {
                        selectQuchong.set(true);
                        selectQuchongImg.set(R.mipmap.arrow_down);
                    } else {
                        selectQuchong.set(false);
                        selectQuchongImg.set(R.mipmap.arrow_down_grey);
                        obAnimalQuchongDate.set("");
                        neiquPeriod.set("");
                    }
                    obAnimalIsQuchongType.set(genderInfo.getType());
                    obAnimalIsQuchong.set(genderInfo.getName());
                }
            });
        }
        mNeiquIsSelectDialog.show();
    }

    //内驱周期设置
    public void selectNeiquPeriod() {
        if (!TextUtils.isEmpty(obAnimalIsQuchongType.get()) && "1".equals(obAnimalIsQuchongType.get())) {
            if (mNeiquPeriodSelectDialog == null) {
                final List<SelectInfo> genderInfos = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    genderInfos.add(new SelectInfo(i + "月", i + ""));
                }
                SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
                mNeiquPeriodSelectDialog = new SelectDialog(mActivity, adapter);
                mNeiquPeriodSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                    @Override
                    public void onSelect(int position) {
                        SelectInfo genderInfo = genderInfos.get(position);
                        neiquPeriod.set(genderInfo.getType());
                    }
                });
            }
            mNeiquPeriodSelectDialog.show();
        } else {
            Toast.makeText(mActivity, "请选择是否内驱", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickYimiaoDateButton(View v) {

        if (!TextUtils.isEmpty(obAnimalIsYimiaoType.get()) && "1".equals(obAnimalIsYimiaoType.get())) {
            if (mTimeYimiaoSelector == null) {
                mTimeYimiaoSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        obAnimalYimiaoDate.set(time);
                    }
                }, "2015-1-1 00:00", "2050-12-31 24:00");
                mTimeYimiaoSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
            }
            mTimeYimiaoSelector.show();
        } else {
            Toast.makeText(mActivity, "请选择是否疫苗", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBirthdaySelect() {
        if (mTimeBirthdaySelector == null) {
            mTimeBirthdaySelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    obAnimalBirthday.set(time);
                }
            }, "1990-01-01 00:00", "2050-12-31 24:00");
            mTimeBirthdaySelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        mTimeBirthdaySelector.show();
    }

    public void clickQuchongDateButton(View v) {
        if (!TextUtils.isEmpty(obAnimalIsQuchongType.get()) && "1".equals(obAnimalIsQuchongType.get())) {
            if (mTimeQuchongSelector == null) {
                mTimeQuchongSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        obAnimalQuchongDate.set(time);
                    }
                }, "2015-1-1 00:00", "2050-12-31 24:00");
                mTimeQuchongSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
            }
            mTimeQuchongSelector.show();
        } else {
            Toast.makeText(mActivity, "请选择是否内驱", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否外驱
     */
    public void selectIsWaiqu() {
        if (mWaiquSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("是", "1"));
            genderInfos.add(new SelectInfo("否", "0"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mWaiquSelectDialog = new SelectDialog(mActivity, adapter);
            mWaiquSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    if ("1".equals(genderInfo.getType())) {
                        selectWaiqu.set(true);
                        selectWaiquImg.set(R.mipmap.arrow_down);
                    } else {
                        selectWaiqu.set(false);
                        selectWaiquImg.set(R.mipmap.arrow_down_grey);
                        obAnimalWaiquTime.set("");
                        waiquPeriod.set("");
                    }
                    obAnimalIsWaiqu.set(genderInfo.getName());
                }
            });
        }
        mWaiquSelectDialog.show();
    }

    /**
     * 选择是否外驱
     */
    public void selectWaiquDate() {
        if (!TextUtils.isEmpty(obAnimalIsWaiqu.get()) && "是".equals(obAnimalIsWaiqu.get())) {
            if (mTimeWaiquSelector == null) {
                mTimeWaiquSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        obAnimalWaiquTime.set(time);
                    }
                }, "2015-1-1 00:00", "2050-12-31 24:00");
                mTimeWaiquSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
            }
            mTimeWaiquSelector.show();
        } else {
            Toast.makeText(mActivity, "请选择是否外驱", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 外驱周期设置
     */
    public void settingWaiquPeriod() {
        if (!TextUtil.isEmpty(obAnimalIsWaiqu.get()) && "是".equals(obAnimalIsWaiqu.get())) {
            if (mWaiquPeriodSelctor == null) {
                final List<SelectInfo> genderInfos = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    genderInfos.add(new SelectInfo(i + "月", i + ""));
                }
                SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
                mWaiquPeriodSelctor = new SelectDialog(mActivity, adapter);
                mWaiquPeriodSelctor.setListenner(new SelectDialog.SelectListenner() {
                    @Override
                    public void onSelect(int position) {
                        SelectInfo genderInfo = genderInfos.get(position);
                        waiquPeriod.set(genderInfo.getType());
                    }
                });
            }
            mWaiquPeriodSelctor.show();
        } else {
            Toast.makeText(mActivity, "请选择是否外驱", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 外驱周期设置
     */
    public void settingYimiaoPeriod() {
        if (!TextUtil.isEmpty(obAnimalIsYimiao.get()) && "是".equals(obAnimalIsYimiao.get())) {
            if (mYiMiaoPeriodSelctor == null) {
                final List<SelectInfo> genderInfos = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    genderInfos.add(new SelectInfo(i + "月", i + ""));
                }
                SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
                mYiMiaoPeriodSelctor = new SelectDialog(mActivity, adapter);
                mYiMiaoPeriodSelctor.setListenner(new SelectDialog.SelectListenner() {
                    @Override
                    public void onSelect(int position) {
                        SelectInfo genderInfo = genderInfos.get(position);
                        obAnimalYimiaoPeriod.set(genderInfo.getType());
                    }
                });
            }
            mYiMiaoPeriodSelctor.show();
        } else {
            Toast.makeText(mActivity, "请选择是否外驱", Toast.LENGTH_SHORT).show();
        }
    }


    public void clickButton(View v) {
        if (obIsEdit.get()) {//编辑
            editAnimalHttp();
        } else {
            addAnimalHttp();
        }
    }

    private void addAnimalHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("comment", petRemark.get());
        params.put("userName", obVipName.get());
        params.put("userPhone", obVipPhone.get());
        params.put("userId", mVipId + "");
        boolean canSave = judgeContent(params);
        if (!canSave) return;

        mActivity.showRDialog();
        mClient.reqApi("insertAnimal", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateAnimal_success);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        mActivity.showToast(e.toString());
                    }
                });

    }

    private void editAnimalHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("comment", petRemark.get());
        params.put("userId", mVipId + "");
        params.put("id", mAnimalId + "");
        params.put("userName", obVipName.get());
        params.put("userPhone", obVipPhone.get());

        boolean canEdit = judgeContent(params);
        if (!canEdit) return;


        mActivity.showRDialog();
        mClient.reqApi("updateAnimal", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateAnimal_success);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        mActivity.showToast(e.toString());
                    }
                });

    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 10) {
                AnimalTypeClassifyInfo info = (AnimalTypeClassifyInfo) data.getExtras().getSerializable("typeInfo");
                obAnimalBreedId.set(info.getType() + "");
                obAnimalBreed.set(info.getName());
            }
        }
    }

    private boolean judgeContent(Map params) {
        if (StringUtils.isEmptyOrWhitespace(obAnimalName.get())) {
            mActivity.showToast("宠物名称不能为空");
            return false;
        }
        params.put("name", obAnimalName.get());

        if (StringUtils.isEmptyOrWhitespace(obAnimalGenderType.get())) {
            mActivity.showToast("宠物性别不能为空");
            return false;
        }
        params.put("sex", obAnimalGenderType.get());


        if (StringUtils.isEmptyOrWhitespace(obAnimalAge.get())) {
            mActivity.showToast("宠物年龄不能为空");
            return false;
        }
        params.put("age", obAnimalAge.get());

        if (!StringUtils.isEmptyOrWhitespace(obAnimalBirthday.get())) {
            params.put("birthday", obAnimalBirthday.get());
        }

        if (StringUtils.isEmptyOrWhitespace(obAnimalWeight.get())) {
            params.put("weight", "0");
        } else {
            params.put("weight", obAnimalWeight.get());
        }

        if (!StringUtils.isEmptyOrWhitespace(obAnimalColor.get())) {
            params.put("colour", obAnimalColor.get());
        }
        if (StringUtils.isEmptyOrWhitespace(obAnimalCategoryId.get())) {
            mActivity.showToast("宠物种类不能为空");
            return false;
        }
        params.put("type", obAnimalCategoryId.get());//种类id

        if (StringUtils.isEmptyOrWhitespace(obAnimalBreedId.get())) {
            mActivity.showToast("宠物品种不能为空");
            return false;
        }
        params.put("varieties", obAnimalBreedId.get());//品种id

        if (StringUtils.isEmptyOrWhitespace(obAnimalBreed.get())) {
            mActivity.showToast("宠物品种名字不能为空");
            return false;
        }
        params.put("varietiesName", obAnimalBreed.get());//品种名字
        if (!StringUtils.isEmptyOrWhitespace(obAnimalIsYimiaoType.get())) {
            params.put("isVaccine", obAnimalIsYimiaoType.get());
            if ("1".equals(obAnimalIsYimiaoType.get())) {
                if (StringUtils.isEmpty(obAnimalYimiaoDate.get())) {
                    showToast(mActivity, "请选择疫苗时间");
                    return false;
                }
                params.put("vaccineTime", obAnimalYimiaoDate.get());
                if (StringUtils.isEmpty(obAnimalYimiaoPeriod.get())) {
                    showToast(mActivity, "请选择疫苗周期");
                    return false;
                }
                params.put("vaccineRemind", obAnimalYimiaoPeriod.get());
            }
        }

        //如果内驱
        if (!StringUtils.isEmptyOrWhitespace(obAnimalIsQuchongType.get())) {
            params.put("isInsect", obAnimalIsQuchongType.get());
            if ("1".equals(obAnimalIsQuchongType.get())) {
                if (StringUtils.isEmpty(obAnimalQuchongDate.get())) {
                    showToast(mActivity, "请选择内驱时间");
                    return false;
                }
                params.put("insectTime", obAnimalQuchongDate.get());
                if (StringUtils.isEmptyOrWhitespace(neiquPeriod.get())) {
                    showToast(mActivity, "请选择内驱提醒周期");
                    return false;
                }
                params.put("insectRemind", neiquPeriod.get());
            }
        }
        //如果外驱
        if (!StringUtils.isEmptyOrWhitespace(obAnimalIsWaiqu.get()) && "是".equals(obAnimalIsWaiqu.get())) {
            params.put("isInsectOut", "1");
            if (StringUtils.isEmptyOrWhitespace(obAnimalWaiquTime.get())) {
                showToast(mActivity, "请选择外驱时间");
                return false;
            }
            params.put("insectOutTime", obAnimalWaiquTime.get());
            if (StringUtils.isEmptyOrWhitespace(waiquPeriod.get())) {
                showToast(mActivity, "请选择外驱提醒周期");
                return false;
            }
            params.put("insectOutRemind", waiquPeriod.get());
        }
        if (skinSelect != null) {
            params.put("isSkin", skinSelect.getType());
        }
        if (biteSelectInfo != null) {
            params.put("isBite", biteSelectInfo.getType());

        }
        return true;

    }


    public void reset() {

        mActivity = null;
        if (mClient != null) {
            mClient.reset();
            mClient = null;
        }
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }

        mSelectDialog = null;
        mYimiaoSelectDialog = null;
        mNeiquPeriodSelectDialog = null;
        mNeiquIsSelectDialog = null;
        mWaiquSelectDialog = null;
        mWaiquPeriodSelctor = null;
        skinSelectDialog = null;
        mYiMiaoPeriodSelctor = null;
        biteSelectDialog = null;
        mTimeQuchongSelector = null;
        mTimeWaiquSelector = null;
        mTimeYimiaoSelector = null;
        mTimeBirthdaySelector = null;
        mAnimalTypeSelectDialog = null;
        mAnimalTypeInfos = null;
    }
}
