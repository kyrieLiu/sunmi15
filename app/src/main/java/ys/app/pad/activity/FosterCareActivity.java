package ys.app.pad.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greendao.LoginInfoDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.adapter.FosterCareAdapter;
import ys.app.pad.adapter.HairdressingOrgansAdapter;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.animalkind.AminalKindActivity;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.model.AnimalTypeClassifyInfo;
import ys.app.pad.model.AnimalTypeInfo;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

public class FosterCareActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText  owner_name, owner_phone, partyB_name, partyB_phone, pet_name, pet_kind,pet_varieties, pet_age, pet_weight,
            pet_coatColor, pet_remark, startTime, endTime, allTime,date_tv;
    private TextView close_tv, unitPrice, serverDays, allMoney;
    private TextView tv_part_clothes, tv_part_lifes;
    private Button bt_commit;
    private String[] clothes = {"衣服", "饭盒", "项圈", "牵引绳", "肩背带"};
    private String[] lifes = {"吃饭次数", "遛弯散步次数"};

    private HairdressingOrgansAdapter organ_clothes_adapter;
    private FosterCareAdapter foster_lifes_adapter;
    private RecyclerView rv_clothes_clip, rv_lifes_clip;
    private boolean havaClothes, havaLifes;
    private ImageView iv_clothes_visible, iv_lifes_visible;

    private RelativeLayout date_lay;
    private SelectDialog mAnimalTypeSelectDialog;
    private TimeSelector hairdressTimeSelect;//日期选择
    private TimeSelector hairdressStartTimeSelect;//日期选择
    private TimeSelector hairdressEndTimeSelect;//日期选择
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private List<SelectInfo> genderInfos = new ArrayList<>();
    private java.util.List<AnimalTypeInfo> mAnimalTypeInfos;
    private TextView  tv_order;
    private String pet_kind_id;//宠物选择的种类id
    private String pet_varieties_id;//宠物选择的品种id
    private SelectDialog ageSelectDialog;//年龄病选择
    private String showAge, showWeight, showColor;//宠物选择的品种id
    private CustomDialog mBackDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foster_care);

        owner_name = (EditText) findViewById(R.id.et_foster_care_owner_name);
        owner_phone = (EditText) findViewById(R.id.et_foster_care_owner_phone);
        partyB_name = (EditText) findViewById(R.id.et_foster_care_partyB_name);
        partyB_phone = (EditText) findViewById(R.id.et_foster_care_partyB_phone);
        pet_name = (EditText) findViewById(R.id.et_foster_care_pet_name);
        pet_kind = (EditText) findViewById(R.id.et_foster_care_pet_kind);
        pet_varieties = (EditText) findViewById(R.id.et_foster_pet_varieties);
        pet_age = (EditText) findViewById(R.id.et_foster_care_pet_age);
        pet_weight = (EditText) findViewById(R.id.et_foster_care_pet_weight);
        pet_coatColor = (EditText) findViewById(R.id.et_foster_care_pet_coatColor);
        pet_remark = (EditText) findViewById(R.id.et_foster_care_pet_remark);
        startTime = (EditText) findViewById(R.id.et_foster_care_pet_startTime);
        endTime = (EditText) findViewById(R.id.et_foster_care_pet_endTime);
        allTime = (EditText) findViewById(R.id.et_foster_care_pet_allTime);
        unitPrice = (TextView) findViewById(R.id.et_foster_care_unitPrice);
        serverDays = (TextView) findViewById(R.id.et_foster_care_serDays);
        allMoney = (TextView) findViewById(R.id.et_foster_care_pet_serverMoney);
        tv_part_clothes = (TextView) findViewById(R.id.tv_hairdressing_part_clothes);
        rv_clothes_clip = (RecyclerView) findViewById(R.id.rv_clothes_clip);
        iv_clothes_visible = (ImageView) findViewById(R.id.iv_hairdressing_clothes_arrow);
        tv_part_lifes = (TextView) findViewById(R.id.tv_hairdressing_part_lifes);
        rv_lifes_clip = (RecyclerView) findViewById(R.id.rv_lifes_clip);
        iv_lifes_visible = (ImageView) findViewById(R.id.iv_hairdressing_lifes_arrow);
        tv_order = (TextView) findViewById(R.id.tv_order);
        date_lay = (RelativeLayout) findViewById(R.id.date_lay);
        date_tv = (EditText) findViewById(R.id.date_tv);
        bt_commit = (Button) findViewById(R.id.bt_foster_commit);

        initData();
        getAnimalTypeFromDb();
        setListener();
        close_tv = (TextView) findViewById(R.id.close_tv);
        close_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListener() {
        tv_part_clothes.setOnClickListener(this);
        iv_clothes_visible.setOnClickListener(this);
        tv_part_lifes.setOnClickListener(this);
        iv_lifes_visible.setOnClickListener(this);
        pet_kind.setOnClickListener(this);
        pet_varieties.setOnClickListener(this);
        date_lay.setOnClickListener(this);
        date_tv.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        pet_age.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
    }


    private void initData() {
        SummitOrderInfo orderInfo ;
        Bundle bundle=getIntent().getExtras();
        LoginInfoDao loginInfoDao = GreenDaoUtils.getmDaoSession().getLoginInfoDao();
        if (bundle != null) {
            orderInfo=(SummitOrderInfo) bundle.getSerializable(Constants.intent_info);
            owner_name.setText(orderInfo.getVipUserName());
            owner_phone.setText(orderInfo.getVipUserPhone());
            tv_order.setText("单号：" + orderInfo.getOrderId());
            unitPrice.setText(orderInfo.getVipPrice() + "");
            serverDays.setText(orderInfo.getCount() + "");
            allMoney.setText(orderInfo.getRealMoney() + "");
        }
        LoginInfo info = loginInfoDao.loadAll().get(0);
        if (info != null) {
            partyB_name.setText(info.getShopName());
            partyB_phone.setText(info.getPhone());

        }



        organ_clothes_adapter = new HairdressingOrgansAdapter(this, clothes);
        rv_clothes_clip.setLayoutManager(new GridLayoutManager(this, 3));
        rv_clothes_clip.setAdapter(organ_clothes_adapter);

        foster_lifes_adapter = new FosterCareAdapter(this, lifes);
        rv_lifes_clip.setLayoutManager(new GridLayoutManager(this, 1));
        rv_lifes_clip.setAdapter(foster_lifes_adapter);

        String today = format.format(new Date());
        date_tv.setText(today);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hairdressing_part_clothes:
                if (organ_clothes_adapter.filterList.size() == 0) {
                    Toast.makeText(this, "请先选择自带物品", Toast.LENGTH_SHORT).show();
                    return;
                }
                havaClothes = changeSelectType(tv_part_clothes, havaClothes);
                break;
            case R.id.iv_hairdressing_clothes_arrow:
                if (rv_clothes_clip.getVisibility() == View.VISIBLE) {
                    rv_clothes_clip.setVisibility(View.GONE);
                    iv_clothes_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_clothes_clip.setVisibility(View.VISIBLE);
                    iv_clothes_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
                break;
            case R.id.tv_hairdressing_part_lifes:
                boolean isEmpty = true;
                for (int i = 0; i < foster_lifes_adapter.filterList.size(); i++) {
                    if (foster_lifes_adapter.filterList.get(i) > 0) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    Toast.makeText(this, "请先选择生活", Toast.LENGTH_SHORT).show();
                    return;
                }
                havaLifes = changeSelectType(tv_part_lifes, havaLifes);
                break;
            case R.id.iv_hairdressing_lifes_arrow:
                if (rv_lifes_clip.getVisibility() == View.VISIBLE) {
                    rv_lifes_clip.setVisibility(View.GONE);
                    iv_lifes_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_lifes_clip.setVisibility(View.VISIBLE);
                    iv_lifes_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
                break;
            case R.id.et_foster_care_pet_kind:
                //种类
                if (mAnimalTypeSelectDialog != null) {
                    mAnimalTypeSelectDialog.show();
                }
                break;
            case R.id.et_foster_pet_varieties:
                //品种
                if (!TextUtil.isEmpty(pet_kind.getText().toString().trim())) {
                    clickBreedButton();
                } else {
                    Toast.makeText(this, "请选择宠物种类", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date_lay:
            case R.id.date_tv:
                selectAppointmentTime();
                break;
            case R.id.et_foster_care_pet_startTime:
                //开始时间
                selectAppointmentStartTime();
                break;
            case R.id.et_foster_care_pet_endTime:
                //结束时间
                selectAppointmentEndTime();
                break;
            case R.id.et_foster_care_pet_age:
                //年龄
                selectAge();
                break;
            case R.id.bt_foster_commit:
                bt_commit.setClickable(false);
                clickConfirm();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (mBackDialog!=null && !mBackDialog.isShowing() && !bt_commit.isClickable()){
                                bt_commit.setClickable(true);
                                break;
                            }
                        }
                    }
                }).start();
                break;
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
            }, today + " 00:00", "2050-12-31 24:00");
            hairdressTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        hairdressTimeSelect.show();
    }

    /**
     * 时间开始选择
     */
    public void selectAppointmentStartTime() {
        if (hairdressStartTimeSelect == null) {
            String today = format.format(new Date());
            hairdressStartTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    startTime.setText(time.substring(0,10));
                }
            }, today + " 00:00", "2050-12-31 24:00");
            hairdressStartTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        hairdressStartTimeSelect.show();
    }

    /**
     * 时间结束选择
     */
    public void selectAppointmentEndTime() {
        if (hairdressEndTimeSelect == null) {
            String today = format.format(new Date());
            hairdressEndTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    endTime.setText(time.substring(0,10));
                }
            }, today + " 00:00", "2050-12-31 24:00");
            hairdressEndTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        hairdressEndTimeSelect.show();
    }

    /**
     * 选择年龄1-30
     */
    public void selectAge() {
        if (ageSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                genderInfos.add(new SelectInfo(i + "", "0"));
            }
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(this, genderInfos);
            ageSelectDialog = new SelectDialog(this, adapter);
            ageSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo ageSelect = genderInfos.get(position);
                    pet_age.setText(ageSelect.getName());
                }
            });
        }
        ageSelectDialog.show();
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
     * 初始化宠物种类
     */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 10) {
                AnimalTypeClassifyInfo info = (AnimalTypeClassifyInfo) data.getExtras().getSerializable("typeInfo");
                pet_varieties_id = info.getType() + "";
                pet_varieties.setText(info.getName());
            }
        }
    }


    private boolean changeSelectType(TextView textView, boolean b) {
        if (b) {
            textView.setText("添加");
                textView.setBackground(getResources().getDrawable(R.drawable.selector_press_button_empty));
            b = false;
        } else {
            textView.setText("移除");
            textView.setBackground(getResources().getDrawable(R.drawable.selector_blue_btn_bg));
            b = true;
        }
        return b;
    }

    public void clickConfirm() {
        showAge = TextUtils.isEmpty(getText(pet_age)) ? " " : getText(pet_age) + "岁";
        showWeight = TextUtils.isEmpty(getText(pet_weight)) ? " " : getText(pet_weight) + "kg";
        showColor = TextUtils.isEmpty(getText(pet_coatColor)) ? " " : getText(pet_coatColor) + "色";
        if (TextUtils.isEmpty(startTime.getText().toString().trim())) {
            startTime.setText("      ");
        }
        if (TextUtils.isEmpty(endTime.getText().toString().trim())) {
            endTime.setText("      ");
        }
        if (TextUtils.isEmpty(allTime.getText().toString().trim())) {
            allTime.setText("    ");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(tv_order.getText().toString().trim() +
                "\n客户: " + getText(owner_name) +
                "\n联系方式: " + getText(owner_phone) +
                "\n服务方: " + getText(partyB_name) +
                "\n联系方式: " + getText(partyB_phone)
        );

        StringBuilder builderClothes = new StringBuilder();
            for (String str : organ_clothes_adapter.filterList) {
                builderClothes.append(str + "\t");
            }
        StringBuilder builderLifes = new StringBuilder();
            for (int i = 0; i < foster_lifes_adapter.filterList.size(); i++) {
                if (foster_lifes_adapter.filterList.get(i) > 0) {
                    builderLifes.append(lifes[i] + ":" + foster_lifes_adapter.filterList.get(i) + "次\t");
                }
            }

        String content = getResources().getString(R.string.foster_care_content);
        StringBuilder builder1 = new StringBuilder();
        builder1.append("超过约定的时间客户按天数向商户方补交费用，超过约定时间，如果客户没有电话或其他联系方式告知商户方要求继续寄养，商户方有权将寄养宠物按无主宠物处理" +
                "\n二、寄养服务费：" + unitPrice.getText().toString() + "元*" + serverDays.getText().toString() + "日=" + allMoney.getText().toString() + "元" +
                "\n" + content
        );

        StringBuilder builder2 = new StringBuilder();
        builder2.append(getResources().getString(R.string.foster_care_content_little));
        StringBuilder builder3 = new StringBuilder();
        builder3.append(getResources().getString(R.string.foster_care_content_end));

        StringBuilder builderTime = new StringBuilder();
        builderTime.append(
                "\n日期：" + date_tv.getText().toString().trim()+
                        "\n\n客户签字:\n\n" +
                        "\n服务方代理人签字:\n\n\n\n "
        );

        printContent(builder, builder1, builder2, builder3, builderTime, builderClothes, builderLifes,"寄养协议(客户存根)");
        printContent(builder, builder1, builder2, builder3, builderTime, builderClothes, builderLifes,"寄养协议(商户存根)");

        if (mBackDialog == null) {
            mBackDialog = new CustomDialog(this);
        }
        mBackDialog.setContent("打印完成");
        mBackDialog.setLeftButton("挂单");
        mBackDialog.setRightButton("结账");
        mBackDialog.setCanceledOnTouchOutside(false);
        mBackDialog.setCancelVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10);
                Toast.makeText(FosterCareActivity.this,"已挂单",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mBackDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBackDialog.show();

    }

    private void printContent(StringBuilder builder, StringBuilder builder1, StringBuilder builder2, StringBuilder builder3, StringBuilder builderTime, StringBuilder builderClothes, StringBuilder builderLifes,String title) {
        if("D1".equals(Build.MODEL)){
            BlueToothPrintUtil mAidlUtil = BlueToothPrintUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n宠物信息:", 6, false, true);
            mAidlUtil.printPartTwo("姓名: " + getText(pet_name), "\t种类: " + getText(pet_kind), 24, false);
            mAidlUtil.printPartTwo("品种: " + getText(pet_varieties), "\t年龄: " + showAge, 24, false);
            mAidlUtil.printPartTwo("体重: " + showWeight, "\t毛色: " + showColor, 24, false);

            mAidlUtil.printAgreement("\n自带衣物:", 6, false, true);
            if (!TextUtil.isEmpty(builderClothes.toString().trim())) {
                mAidlUtil.printAgreement("\n" + builderClothes.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement("\n生活:", 6, false, true);
            if (!TextUtil.isEmpty(builderLifes.toString().trim())) {
                mAidlUtil.printAgreement("\n" + builderLifes.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement("\n临时寄养协议: ", 28, false, true);
            mAidlUtil.printAgreement("\n一、商户方为客户提供有偿的临时宠物寄养服务，客户将委托寄养宠物交由商户方临时寄养，寄养时间为", 24, false, false);
            mAidlUtil.printAgreement(getText(startTime)+" 至 "+getText(endTime)+"，预计"+getText(allTime)+"天", 24, true, false);
            mAidlUtil.printAgreement(builder1.toString(), 24, false, false);
            mAidlUtil.printAgreement(builder2.toString(), 24, false, false);
            mAidlUtil.printAgreement(builder3.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n备注: " + getText(pet_remark), 24, false, false);
            mAidlUtil.printAgreement(builderTime.toString()+"\n\n  ", 24, false, false);
        }else{
            AidlUtil mAidlUtil = AidlUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n宠物信息", 28, false, true);
            mAidlUtil.printPartTwo("\n姓名: " + getText(pet_name), "\t种类: " + getText(pet_kind), 24, false);
            mAidlUtil.printPartTwo("\n品种: " + getText(pet_varieties), "\t年龄: " + showAge, 24, false);
            mAidlUtil.printPartTwo("\n体重: " + showWeight, "\t毛色: " + showColor, 24, false);

            mAidlUtil.printAgreement("\n自带衣物:", 28, false, true);
            if (!TextUtil.isEmpty(builderClothes.toString().trim())) {
                mAidlUtil.printAgreement("\n" + builderClothes.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement("\n生活:", 28, false, true);
            if (!TextUtil.isEmpty(builderLifes.toString().trim())) {
                mAidlUtil.printAgreement("\n" + builderLifes.toString(), 24, false, false);
            }

            mAidlUtil.printAgreement("\n临时寄养协议: ", 28, false, true);
            mAidlUtil.printAgreement("\n一、商户方为客户提供有偿的临时宠物寄养服务，客户将委托寄养宠物交由商户方临时寄养，寄养时间为", 24, false, false);
            mAidlUtil.printAgreement(getText(startTime), 24, true, false);
            mAidlUtil.printAgreement("至", 24, false, false);
            mAidlUtil.printAgreement(getText(endTime), 24, true, false);
            mAidlUtil.printAgreement("，预计", 24, false, false);
            mAidlUtil.printAgreement(getText(allTime), 24, true, false);
            mAidlUtil.printAgreement("天。", 24, false, false);
            mAidlUtil.printAgreement(builder1.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n"+builder2.toString(), 20, false, false);
            mAidlUtil.printAgreement(builder3.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n备注: " + getText(pet_remark), 24, false, false);
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
        return editText.getText().toString();
    }
}
