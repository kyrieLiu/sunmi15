package ys.app.pad.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greendao.AnimalTypeClassifyInfoDao;
import com.greendao.AnimalTypeInfoDao;
import com.greendao.LoginInfoDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.animalkind.AminalKindActivity;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalTypeClassifyInfo;
import ys.app.pad.model.AnimalTypeInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

public class LivingBuyActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText buyer_phone, buyer_address, seller_phone, seller_address, pet_name,
            pet_sex,pet_birthday,pet_age, pet_skin,pet_bring,pet_lifeTime,pet_charactor, pet_kind,pet_varieties, pet_weight,
            pet_coatColor,pet_vaccine,pet_drive, pet_remark, startTime, endTime, allTime,date_tv,et_money,et_outDriveTime,et_blood;
    private TextView close_tv;
    private Button bt_commit;

    private SelectDialog mAnimalTypeSelectDialog;
    private TimeSelector hairdressTimeSelect;//日期选择
    private TimeSelector vaccineTimeSelect;//疫苗时间
    private TimeSelector driveTimeSelect;//内驱时间
    private TimeSelector outDriveTimeSelect;//外驱时间
    private TimeSelector birthdayTimeSelect;//生日
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private List<SelectInfo> genderInfos = new ArrayList<>();
    private List<AnimalTypeInfo> mAnimalTypeInfos;
    private String pet_kind_id;//宠物选择的种类id
    private SelectDialog ageSelectDialog;//年龄病选择

    private ApiClient<BaseListResult<AnimalTypeInfo>> mAnimalTypeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_living_body);

        buyer_phone = (EditText) findViewById(R.id.et_buyer_phone);
        buyer_address = (EditText) findViewById(R.id.et_buyer_address);
        seller_phone = (EditText) findViewById(R.id.et_seller_phone);
        seller_address = (EditText) findViewById(R.id.et_seller_address);
        pet_name = (EditText) findViewById(R.id.et_pet_name);
        pet_kind = (EditText) findViewById(R.id.et_pet_kind);
        pet_varieties = (EditText) findViewById(R.id.et_varieties);
        pet_age = (EditText) findViewById(R.id.et_pet_age);
        pet_weight = (EditText) findViewById(R.id.et_pet_weight);
        pet_coatColor = (EditText) findViewById(R.id.et_pet_coatColor);
        pet_remark = (EditText) findViewById(R.id.et_pet_remark);
        date_tv = (EditText) findViewById(R.id.date_tv);
        bt_commit = (Button) findViewById(R.id.bt_foster_commit);
        et_outDriveTime= (EditText) findViewById(R.id.et_pet_outDriveTime);
        et_blood= (EditText) findViewById(R.id.et_blood);


        pet_sex = (EditText) findViewById(R.id.et_pet_sex);
        pet_birthday = (EditText) findViewById(R.id.et_birthday);
        pet_lifeTime = (EditText) findViewById(R.id.et_pet_lifeTime);
        pet_bring = (EditText) findViewById(R.id.et_pet_bring);
        pet_charactor = (EditText) findViewById(R.id.et_pet_charactor);
        pet_vaccine = (EditText) findViewById(R.id.et_pet_vaccineTime);
        pet_drive = (EditText) findViewById(R.id.et_pet_driveTime);
        et_money = (EditText) findViewById(R.id.et_living_money);

        bt_commit.setOnClickListener(this);
        pet_kind.setOnClickListener(this);
        pet_varieties.setOnClickListener(this);
        pet_vaccine.setOnClickListener(this);
        pet_drive.setOnClickListener(this);
        et_outDriveTime.setOnClickListener(this);
        pet_birthday.setOnClickListener(this);
        date_tv.setOnClickListener(this);
        close_tv = (TextView) findViewById(R.id.close_tv);
        close_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }



    private void initData() {
        SummitOrderInfo orderInfo ;
        Bundle bundle=getIntent().getExtras();
        LoginInfoDao loginInfoDao = GreenDaoUtils.getmDaoSession().getLoginInfoDao();
        if (bundle != null) {
            orderInfo=(SummitOrderInfo) bundle.getSerializable(Constants.intent_info);
            buyer_phone.setText(orderInfo.getVipUserName());
            buyer_address.setText(orderInfo.getVipUserPhone());
            et_money.setText(orderInfo.getRealMoney() + "");
        }
        LoginInfo info = loginInfoDao.loadAll().get(0);
        if (info != null) {
            seller_phone.setText(info.getShopName());
            seller_address.setText(info.getPhone());
        }

        String today = format.format(new Date());
        date_tv.setText(today);
        getAnimalTypeFromDb();
    }

    /**
     * 初始化宠物种类
     */
    private void getAnimalTypeFromDb() {
        mAnimalTypeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao().loadAll();
        if (mAnimalTypeInfos != null && !mAnimalTypeInfos.isEmpty()) {
            SelectInfo genderInfo;
            for (AnimalTypeInfo info : mAnimalTypeInfos) {//添加种类
                genderInfo = new SelectInfo(info.getName(), String.valueOf(info.getId()));
                genderInfos.add(genderInfo);
            }
            initPetTypeDialog();
        }else{
            loadAnimalTypeHttp();
        }
    }

    private void loadAnimalTypeHttp() {
        if (mAnimalTypeClient == null) {
            mAnimalTypeClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();

        mAnimalTypeClient.reqApi("queryAnimalType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<AnimalTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AnimalTypeInfo> result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            AnimalTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao();
                            AnimalTypeClassifyInfoDao infoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeClassifyInfoDao();
                            List<AnimalTypeInfo> list = result.getData();
                            dao.deleteAll();
                            infoDao.deleteAll();
                            if (list != null && !list.isEmpty()) {
                                for (AnimalTypeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                    List<AnimalTypeClassifyInfo> varietiesList = info.getVarietiesList();
                                    if (varietiesList != null && !varietiesList.isEmpty()) {
                                        for (AnimalTypeClassifyInfo info1 : varietiesList) {
                                            info1.setRequestTime(nowTime);
                                        }
                                        infoDao.insertInTx(varietiesList);
                                    }
                                }
                            }
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                }).updateUI(new Callback<BaseListResult<AnimalTypeInfo>>() {
            @Override
            public void onSuccess(BaseListResult<AnimalTypeInfo> animalTypeInfoBaseListResult) {
                List<AnimalTypeInfo> list = animalTypeInfoBaseListResult.getData();

                if (list != null && !list.isEmpty()) {
                    SelectInfo genderInfo;
                    for (AnimalTypeInfo info : list) {//添加种类
                        genderInfo = new SelectInfo(info.getName(), String.valueOf(info.getId()));
                        genderInfos.add(genderInfo);
                    }
                    initPetTypeDialog();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_tv:
                selectAppointmentTime();
                break;
            case R.id.et_pet_kind:
                //种类
                if (mAnimalTypeSelectDialog != null) {
                    mAnimalTypeSelectDialog.show();
                }
                break;
            case R.id.et_varieties:
                //品种
                if (!TextUtil.isEmpty(pet_kind.getText().toString().trim())) {
                    clickBreedButton();
                } else {
                    Toast.makeText(this, "请选择宠物种类", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_foster_commit:
                clickConfirm();
                break;
            case R.id.et_pet_vaccineTime:
                selectVaccineTime();
                break;
            case R.id.et_pet_driveTime:
                selectDriveTime();
                break;
            case R.id.et_pet_outDriveTime:
                selectOutDriveTime();
                break;
            case R.id.et_birthday:
                selectBirthdayTime();
                break;
        }
    }

    //宠物物种选择
    private void initPetTypeDialog() {
        if (mAnimalTypeSelectDialog == null && !genderInfos.isEmpty()) {
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(this, genderInfos);
            mAnimalTypeSelectDialog = new SelectDialog(this, adapter);
            mAnimalTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    if ("1".equals(genderInfo.getType())) {
                    } else {
                    }
                    pet_kind.setText(genderInfo.getName());
                    pet_kind_id = genderInfo.getType();
                }
            });
        }
    }

    /**
     * 宠物品种
     */
    public void clickBreedButton() {
        if (!StringUtils.isEmpty(pet_kind.getText().toString().trim())) {
            Intent intent = new Intent(this, AminalKindActivity.class);
            intent.putExtra("categoryID", pet_kind_id);
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "请选择宠物种类", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 时间选择
     */
    public void selectAppointmentTime() {
        if (hairdressTimeSelect == null) {
            String today = format.format(new Date());
            hairdressTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    date_tv.setText(time.substring(0,10));
                }
            }, "2016-01-01 00:00", "2050-12-31 24:00");
            hairdressTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        hairdressTimeSelect.show();
    }
    /**
     * 时间疫苗时间
     */
    public void selectVaccineTime() {
        if (vaccineTimeSelect == null) {
            String today = format.format(new Date());
            vaccineTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    pet_vaccine.setText(time.substring(0,10));
                }
            }, "2016-01-01 00:00", "2050-12-31 24:00");
            vaccineTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        vaccineTimeSelect.show();
    }
    /**
     * 内驱时间
     */
    public void selectDriveTime() {
        if (driveTimeSelect == null) {
            String today = format.format(new Date());
            driveTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    pet_drive.setText(time.substring(0,10));
                }
            }, "2016-01-01 00:00", "2050-12-31 24:00");
            driveTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        driveTimeSelect.show();
    }

    /**
     * 外驱事件
     */
    public void selectOutDriveTime() {
        if (outDriveTimeSelect == null) {
            String today = format.format(new Date());
            outDriveTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    et_outDriveTime.setText(time.substring(0,10));
                }
            }, "2016-01-01 00:00", "2050-12-31 24:00");
            outDriveTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        outDriveTimeSelect.show();
    }

    /**
     * 选择生日
     */
    public void selectBirthdayTime() {
        if (birthdayTimeSelect == null) {
            String today = format.format(new Date());
            birthdayTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    pet_birthday.setText(time.substring(0,10));
                }
            }, "1992-01-01 00:00", "2050-12-31 24:00");
            birthdayTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        birthdayTimeSelect.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                AnimalTypeClassifyInfo info = (AnimalTypeClassifyInfo) data.getExtras().getSerializable("typeInfo");
                pet_varieties.setText(info.getName());
            }
        }
    }






    public void clickConfirm() {

        StringBuilder builder = new StringBuilder();
        builder.append("甲方（买方） 电话: " + getText(buyer_phone) +
                "\n地址: " + getText(buyer_address) +
                "\n乙方（卖方） 电话: " + getText(seller_phone) +
                "\n地址: " + getText(seller_address)
        );

        StringBuilder builderTime = new StringBuilder();
        builderTime.append(
                "\n日期：" + date_tv.getText().toString().trim()+
                        "\n\n甲方签字:\n\n" +
                        "\n乙方签字:\n\n\n\n "
        );

        printContent(builder,  builderTime,"买卖合同(客户存根)");
        printContent(builder, builderTime,"买卖合同(商户存根)");

//        if (mBackDialog == null) {
//            mBackDialog = new CustomDialog(this);
//        }
//        mBackDialog.setContent("打印完成");
//        mBackDialog.setLeftButton("挂单");
//        mBackDialog.setRightButton("结账");
//        mBackDialog.setCanceledOnTouchOutside(false);
//        mBackDialog.setCancelVisiable(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(10);
//                Toast.makeText(LivingBuyActivity.this,"已挂单",Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//        mBackDialog.setOkVisiable(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mBackDialog.show();

    }

    private void printContent(StringBuilder builder, StringBuilder builderTime, String title) {
        if("D1".equals(Build.MODEL)){
            BlueToothPrintUtil mAidlUtil = BlueToothPrintUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n双方本着保护动物，爱惜 尊重生命的人道主义精神，以诚信，公平，合理之原则，经双方充分的平等协商，特订立本合同，双方共同遵守。: ", 24, false, true);
            mAidlUtil.printAgreement("\n宠物信息:", 6, false, true);
            mAidlUtil.printPartTwo("姓名: " + getText(pet_name), "\t性别: " + getText(pet_sex), 24, false);
            mAidlUtil.printPartTwo("出生日期: " + getText(pet_birthday), "\t年龄: " + getText(pet_age), 24, false);
            mAidlUtil.printPartTwo("初样程度: " + getText(pet_bring), "\t寿命: " + getText(pet_lifeTime), 24, false);
            mAidlUtil.printPartTwo("种类: " + getText(pet_kind), "\t品种: " + getText(pet_varieties), 24, false);
            mAidlUtil.printPartTwo("毛色: " + getText(pet_coatColor), "\t体重: " + getText(pet_weight), 24, false);
            mAidlUtil.printPartTwo("疫苗时间: " + getText(pet_vaccine), "\t内驱时间: " + getText(pet_drive), 24, false);
            mAidlUtil.printPartTwo("外驱时间: " + getText(pet_vaccine), "血统信息:  "+getText(et_blood), 24, false);

            mAidlUtil.printAgreement("\n二 甲乙双方经友好协商，就宠物售出事宜达成以下协议请签名前仔细阅读，签名表示您已理解并接受下述条款：", 24, false, false);
            mAidlUtil.printAgreement("\n"+getResources().getString(R.string.living_buy), 24, false, false);
            mAidlUtil.printAgreement(" \n3 甲方应支付乙方金额为人民币 "+getText(et_money)+"元", 24, true, false);
            mAidlUtil.printAgreement("\n"+getResources().getString(R.string.living_buy_second), 24, false, false);
            mAidlUtil.printAgreement("\n备注: " + getText(pet_remark), 24, false, false);
            mAidlUtil.printAgreement(builderTime.toString()+"\n\n  ", 24, false, false);
        }else{
            AidlUtil mAidlUtil = AidlUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n双方本着保护动物，爱惜 尊重生命的人道主义精神，以诚信，公平，合理之原则，经双方充分的平等协商，特订立本合同，双方共同遵守。: ", 24, false, true);
            mAidlUtil.printAgreement("\n宠物信息:", 6, false, true);
            mAidlUtil.printPartTwo("姓名: " + getText(pet_name), "\t性别: " + getText(pet_sex), 24, false);
            mAidlUtil.printPartTwo("出生日期: " + getText(pet_birthday), "\t年龄: " + getText(pet_age), 24, false);
            mAidlUtil.printPartTwo("初样程度: " + getText(pet_bring), "\t寿命: " + getText(pet_lifeTime), 24, false);
            mAidlUtil.printPartTwo("种类: " + getText(pet_kind), "\t品种: " + getText(pet_varieties), 24, false);
            mAidlUtil.printPartTwo("毛色: " + getText(pet_coatColor), "\t体重: " + getText(pet_weight), 24, false);
            mAidlUtil.printPartTwo("疫苗时间: " + getText(pet_vaccine), "\t内驱时间: " + getText(pet_drive), 24, false);
            mAidlUtil.printPartTwo("外驱时间: " + getText(pet_vaccine), "血统信息:  "+getText(et_blood), 24, false);


            mAidlUtil.printAgreement("\n二 甲乙双方经友好协商，就宠物售出事宜达成以下协议请签名前仔细阅读，签名表示您已理解并接受下述条款：", 24, false, false);
            mAidlUtil.printAgreement("\n"+getResources().getString(R.string.living_buy), 24, false, false);
            mAidlUtil.printAgreement(" \n3 甲方应支付乙方金额为人民币 "+getText(et_money)+"元", 24, true, false);
            mAidlUtil.printAgreement("\n"+getResources().getString(R.string.living_buy_second), 24, false, false);
            mAidlUtil.printAgreement("\n备注: " + getText(pet_remark), 24, false, false);
            mAidlUtil.printAgreement(builderTime.toString()+"\n\n  ", 24, false, false);
            mAidlUtil.printAgreement(builderTime.toString(), 24, false, false);
            if ("T1mini".equals(Build.MODEL)) {
                mAidlUtil.print3Line();
            }
            AidlUtil.getInstance().cutPaper();
        }

    }

    public void clickClose(View view) {
        finish();
    }

    private String getText(EditText editText) {
        String text=editText.getText().toString();
        return text==null?"  ":text;
    }
}
