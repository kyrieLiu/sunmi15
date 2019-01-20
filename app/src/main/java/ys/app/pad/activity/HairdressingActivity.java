package ys.app.pad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ys.app.pad.adapter.HairdressingInspectAdapter;
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
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

public class HairdressingActivity extends Activity implements View.OnClickListener {
    private EditText owner_name, owner_phone, partyB_name, partyB_phone, pet_name, pet_kind, pet_age, pet_weight,
            pet_coatColor, pet_remark, pet_varieties;
    private RecyclerView rv_before_inspect, rv_after_inspect, rv_organs_dye, rv_complex_dye, rv_organs_mould, rv_organs_clip, rv_clothes_clip;
    private TextView tv_auditoryMeatus_clear, tv_cut_fingernail, tv_shaved_face, tv_cut_feet, tv_part_clothes,tv_orderID;
    private String[] before_inspects = {"眼部疾病", "毛量", "耳部疾病", "毛质", "鼻子疾病", "发情", "牙齿疾病", "打结", "身体缺毛", "胆小敏感",
            "正常免疫", "刀头过敏", "一周内有疫苗接种"};
    private String[] after_inspects = {"拔除耳毛/清洗", "梳毛及开结", "过敏现象", "剪指甲及磨", "吹水及拉毛", "挤肛门腺", "剪破/剃破处",
            "共同部位", "洗澡", "眼部是否清理干净", "各项检查是否正常"};
    private String[] organs = {"左耳", "右耳", "双耳", "脸部", "背部", "肚子", "前腿", "后腿", "屁股", "尾巴", "全身"};
    private String[] clothes = {"衣服", "饭盒", "项圈", "牵引绳", "肩背带"};
    private String[] patterns = {"日系风格", "韩系风格", "欧式风格", "美式风格", "简约风格", "清新风格", "古典风格"};

    private boolean haveComplexDye, havePartClip, haveAuditoryMeatures, haveCutFingernail, haveShavedFace, haveCutFeet, havePartDye, havaClothes;

    private ImageView iv_clip_visible, iv_dye_visible, iv_pattern_visible, iv_mould_visible, iv_clothes_visible;
    private Button bt_commit;
    private HairdressingOrgansAdapter organ_clip_adapter;
    private HairdressingOrgansAdapter organ_dye_adapter;
    private HairdressingOrgansAdapter organ_clothes_adapter;
    private HairdressingOrgansAdapter complex_dye_adapter;
    private HairdressingOrgansAdapter organ_mould_adapter;
    private HairdressingInspectAdapter before_inspect_adapter;
    private TextView close_tv;

    private RelativeLayout date_lay;
    private TimeSelector hairdressTimeSelect;//日期选择
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private TextView date_tv;
    private SelectDialog mAnimalTypeSelectDialog;
    private SelectDialog ageSelectDialog;//年龄病选择
    private List<SelectInfo> genderInfos = new ArrayList<>();
    private List<AnimalTypeInfo> mAnimalTypeInfos;
    private String pet_kind_id;//宠物选择的种类id
    private String pet_varieties_id;//宠物选择的品种id
    private String orderID;
    CustomDialog mBackDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hairdressing);
        initView();
        loadData();
        loadCheckBox();
        setListener();
        getAnimalTypeFromDb();
    }

    private void initView() {
        rv_before_inspect = (RecyclerView) findViewById(R.id.rv_before_inspect);
        rv_organs_dye = (RecyclerView) findViewById(R.id.rv_organs_dye);
        rv_organs_clip = (RecyclerView) findViewById(R.id.rv_organs_clip);
        rv_clothes_clip = (RecyclerView) findViewById(R.id.rv_clothes_clip);
        rv_complex_dye = (RecyclerView) findViewById(R.id.rv_organs_pattern);
        rv_organs_mould = (RecyclerView) findViewById(R.id.rv_organs_mould);
        iv_clip_visible = (ImageView) findViewById(R.id.iv_hairdressing_clip_arrow);
        iv_dye_visible = (ImageView) findViewById(R.id.iv_hairdressing_dye_arrow);
        iv_pattern_visible = (ImageView) findViewById(R.id.iv_hairdressing_pattern_arrow);
        iv_mould_visible = (ImageView) findViewById(R.id.iv_hairdressing_mould_arrow);
        iv_clothes_visible = (ImageView) findViewById(R.id.iv_hairdressing_clothes_arrow);
        tv_auditoryMeatus_clear = (TextView) findViewById(R.id.tv_hairdressing_auditoryMeatus_clear);
        tv_cut_fingernail = (TextView) findViewById(R.id.tv_hairdressing_cut_fingernail);
        tv_shaved_face = (TextView) findViewById(R.id.tv_hairdressing_shaved_face);
        tv_shaved_face = (TextView) findViewById(R.id.tv_hairdressing_shaved_face);
        tv_cut_feet = (TextView) findViewById(R.id.tv_hairdressing_cut_feet);
        tv_part_clothes = (TextView) findViewById(R.id.tv_hairdressing_part_clothes);
        bt_commit = (Button) findViewById(R.id.bt_hairdressing_commit);
        owner_name = (EditText) findViewById(R.id.et_hairdressing_owner_name);
        owner_phone = (EditText) findViewById(R.id.et_hairdressing_owner_phone);
        partyB_name = (EditText) findViewById(R.id.et_hairdressing_partyB_name);
        partyB_phone = (EditText) findViewById(R.id.et_hairdressing_partyB_phone);
        pet_name = (EditText) findViewById(R.id.et_hairdressing_pet_name);
        pet_kind = (EditText) findViewById(R.id.et_hairdressing_pet_kind);
        pet_age = (EditText) findViewById(R.id.et_hairdressing_pet_age);
        pet_weight = (EditText) findViewById(R.id.et_hairdressing_pet_weight);
        pet_coatColor = (EditText) findViewById(R.id.et_hairdressing_pet_coatColor);
        pet_remark = (EditText) findViewById(R.id.et_hairdressing_pet_notice);
        pet_varieties = (EditText) findViewById(R.id.et_hairdressing_pet_varieties);
        date_lay = (RelativeLayout) findViewById(R.id.date_lay);
        date_tv = (TextView) findViewById(R.id.date_tv);
        tv_orderID = (TextView) findViewById(R.id.tv_foster_care_orderID);

        close_tv = (TextView) findViewById(R.id.close_tv);
        close_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {
        before_inspect_adapter = new HairdressingInspectAdapter(this, before_inspects);
        rv_before_inspect.setLayoutManager(new GridLayoutManager(this, 2));
        rv_before_inspect.setAdapter(before_inspect_adapter);


        LoginInfoDao loginInfoDao = GreenDaoUtils.getmDaoSession().getLoginInfoDao();
        Bundle bundle=getIntent().getExtras();
        if (bundle != null) {
            SummitOrderInfo orderInfo = (SummitOrderInfo) bundle.getSerializable(Constants.intent_info);
            owner_name.setText(orderInfo.getVipUserName());
            owner_phone.setText(orderInfo.getVipUserPhone());
            tv_orderID.setText("单号: "+orderInfo.getOrderId());
            this.orderID=orderInfo.getOrderId();
        }
        LoginInfo info = loginInfoDao.loadAll().get(0);
        if (info != null) {
            partyB_name.setText(info.getShopName());
            partyB_phone.setText(info.getPhone());

        }

        String today = format.format(new Date());
        date_tv.setText(today);
    }

    private void loadCheckBox() {
        organ_dye_adapter = new HairdressingOrgansAdapter(this, organs);
        rv_organs_dye.setLayoutManager(new GridLayoutManager(this, 3));
        rv_organs_dye.setAdapter(organ_dye_adapter);

        complex_dye_adapter = new HairdressingOrgansAdapter(this, patterns);
        rv_complex_dye.setLayoutManager(new GridLayoutManager(this, 3));
        rv_complex_dye.setAdapter(complex_dye_adapter);

        organ_mould_adapter = new HairdressingOrgansAdapter(this, patterns);
        rv_organs_mould.setLayoutManager(new GridLayoutManager(this, 3));
        rv_organs_mould.setAdapter(organ_mould_adapter);

        organ_clip_adapter = new HairdressingOrgansAdapter(this, organs);
        rv_organs_clip.setLayoutManager(new GridLayoutManager(this, 3));
        rv_organs_clip.setAdapter(organ_clip_adapter);

        organ_clothes_adapter = new HairdressingOrgansAdapter(this, clothes);
        rv_clothes_clip.setLayoutManager(new GridLayoutManager(this, 3));
        rv_clothes_clip.setAdapter(organ_clothes_adapter);

    }

    private void setListener() {
        iv_clip_visible.setOnClickListener(this);
        iv_dye_visible.setOnClickListener(this);
        iv_pattern_visible.setOnClickListener(this);
        iv_mould_visible.setOnClickListener(this);
        iv_clothes_visible.setOnClickListener(this);
        rv_organs_dye.setOnClickListener(this);
        rv_organs_clip.setOnClickListener(this);
        rv_clothes_clip.setOnClickListener(this);
        iv_clip_visible.setOnClickListener(this);
        tv_auditoryMeatus_clear.setOnClickListener(this);
        tv_cut_fingernail.setOnClickListener(this);
        tv_shaved_face.setOnClickListener(this);
        tv_shaved_face.setOnClickListener(this);
        tv_cut_feet.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        tv_part_clothes.setOnClickListener(this);
        date_lay.setOnClickListener(this);
        date_tv.setOnClickListener(this);
        pet_kind.setOnClickListener(this);
        pet_varieties.setOnClickListener(this);
        pet_age.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hairdressing_clip_arrow:
                if (rv_organs_clip.getVisibility() == View.VISIBLE) {
                    rv_organs_clip.setVisibility(View.GONE);
                    iv_clip_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_organs_clip.setVisibility(View.VISIBLE);
                    iv_clip_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
                break;
            case R.id.iv_hairdressing_dye_arrow:
                if (rv_organs_dye.getVisibility() == View.VISIBLE) {
                    rv_organs_dye.setVisibility(View.GONE);
                    iv_dye_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_organs_dye.setVisibility(View.VISIBLE);
                    iv_dye_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
                break;
            case R.id.iv_hairdressing_pattern_arrow:
                if (rv_complex_dye.getVisibility() == View.VISIBLE) {
                    rv_complex_dye.setVisibility(View.GONE);
                    iv_pattern_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_complex_dye.setVisibility(View.VISIBLE);
                    iv_pattern_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
                break;
            case R.id.iv_hairdressing_mould_arrow:
                if (rv_organs_mould.getVisibility() == View.VISIBLE) {
                    rv_organs_mould.setVisibility(View.GONE);
                    iv_mould_visible.setImageResource(R.mipmap.arrow_up_grey);
                } else {
                    rv_organs_mould.setVisibility(View.VISIBLE);
                    iv_mould_visible.setImageResource(R.mipmap.arrow_down_grey);
                }
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
            case R.id.tv_hairdressing_auditoryMeatus_clear:
                haveAuditoryMeatures = changeSelectType(tv_auditoryMeatus_clear, haveAuditoryMeatures);
                break;
            case R.id.tv_hairdressing_cut_fingernail:
                haveCutFingernail = changeSelectType(tv_cut_fingernail, haveCutFingernail);
                break;
            case R.id.tv_hairdressing_shaved_face:
                haveShavedFace = changeSelectType(tv_shaved_face, haveShavedFace);
                break;
            case R.id.tv_hairdressing_cut_feet:
                haveCutFeet = changeSelectType(tv_cut_feet, haveCutFeet);
                break;

            case R.id.tv_hairdressing_part_clothes:
                if (organ_clothes_adapter.filterList.size() == 0) {
                    Toast.makeText(this, "请先选择自带物品", Toast.LENGTH_SHORT).show();
                    return;
                }
                havaClothes = changeSelectType(tv_part_clothes, havaClothes);
                break;
            case R.id.bt_hairdressing_commit:
                bt_commit.setClickable(false);
                confirm();
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
            case R.id.date_lay:
            case R.id.date_tv:
                selectAppointmentTime();
                break;
            case R.id.et_hairdressing_pet_kind:
                //种类
                if (mAnimalTypeSelectDialog != null) {
                    mAnimalTypeSelectDialog.show();
                }
                break;
            case R.id.et_hairdressing_pet_varieties:
                //品种
                if (!TextUtil.isEmpty(pet_kind.getText().toString().trim())) {
                    clickBreedButton();
                } else {
                    Toast.makeText(this, "请选择宠物种类", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_hairdressing_pet_age:
                //年龄
                selectAge();
                break;
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
     * 时间选择
     */
    public void selectAppointmentTime() {
        if (hairdressTimeSelect == null) {
            String today = format.format(new Date());
            hairdressTimeSelect = new TimeSelector(this, new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    date_tv.setText(time);
                }
            }, today + " 00:00", "2050-12-31 24:00");
            hairdressTimeSelect.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        }
        hairdressTimeSelect.show();
    }

    private boolean changeSelectType(TextView textView, boolean b) {
        if (b) {
            textView.setText("添加");
            textView.setBackground(getResources().getDrawable(R.drawable.selector_press_button_empty));
            textView.setTextColor(getResources().getColor(R.color.selector_press_button_text_color));
            b = false;
        } else {
            textView.setText("移除");
            textView.setBackground(getResources().getDrawable(R.drawable.selector_blue_btn_bg));
            textView.setTextColor(getResources().getColor(R.color.white));
            b = true;
        }
        return b;
    }

    private void confirm() {
        StringBuilder builder = new StringBuilder();
        String content = getResources().getString(R.string.hairdressing_content);
        builder.append("\n单号: " +orderID+
                "\n客户: " + getText(owner_name) +
                "\n联系方式: " + getText(owner_phone) +
                "\n服务方: " + getText(partyB_name) +
                "\n联系方式: " + getText(partyB_phone)
        );

        StringBuilder builder1 = new StringBuilder();
        for (String str : before_inspect_adapter.filterList) {

            if ("毛量".equals(str)){
                builder1.append("毛量多\t");
            }else if ("毛质".equals(str)){
                builder1.append("毛质软\t");
            }else{
                builder1.append(str + "\t");
            }
        }
        if (!before_inspect_adapter.filterList.contains("毛量")){
            builder1.append("毛量少\t");
        }
        if (!before_inspect_adapter.filterList.contains("毛质")){
            builder1.append("毛质硬\t");
        }

        StringBuilder builder3 = new StringBuilder();

        builder3.append("\n服务内容: ");

        if (haveAuditoryMeatures) builder3.append("\n耳道清理\t");
        if (haveCutFingernail) builder3.append("\n修剪指甲\t");
        if (haveShavedFace) builder3.append("\n剃脸\t");
        if (haveCutFeet) builder3.append("\n剃脚\t");

        StringBuilder builder3_1 = new StringBuilder();
        for (String str : organ_clip_adapter.filterList) {
            builder3_1.append(str + "\t");
        }
        StringBuilder builder3_2 = new StringBuilder();
        for (String str : organ_dye_adapter.filterList) {
            builder3_2.append(str + "\t");
        }
        StringBuilder builder3_3 = new StringBuilder();
        for (String str : organ_clothes_adapter.filterList) {
            builder3_3.append(str + "\t");
        }

        StringBuilder builder4 = new StringBuilder();
        builder4.append(
                content + "\n爱宠备注 :" + getText(pet_remark)+
                        "\n日期: "+date_tv.getText().toString()  );
        builder4.append("\n\n客户签字:\n\n\n" + "服务方代理人签字：\n\n\n\n\n");



        printContent(builder, builder1, builder3, builder3_1, builder3_2, builder3_3, builder4,"美容确认单(客户存根)");
        printContent(builder, builder1, builder3, builder3_1, builder3_2, builder3_3, builder4,"美容确认单(商户存根)");


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
                Toast.makeText(HairdressingActivity.this,"已挂单",Toast.LENGTH_SHORT).show();
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

    private void printContent(StringBuilder builder, StringBuilder builder1, StringBuilder builder3, StringBuilder builder3_1, StringBuilder builder3_2, StringBuilder builder3_3, StringBuilder builder4,String title) {
        if ("D1".equals(Build.MODEL)){
            BlueToothPrintUtil mAidlUtil = BlueToothPrintUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("宠物信息:", 6, false, true);
            mAidlUtil.printPartTwo("姓名: " + getText(pet_name), "\t种类: " + getText(pet_kind), 24, false);
            mAidlUtil.printPartTwo("品种: " + getText(pet_varieties), "\t年龄: " + getText(pet_age) + "岁", 24, false);
            mAidlUtil.printPartTwo("体重: " + getText(pet_weight) + "kg", "\t毛色: " + getText(pet_coatColor) + "色", 24, false);

            if (!TextUtil.isEmpty(builder3_3.toString().trim())) {
                mAidlUtil.printAgreement("\n自带衣物:", 6, false, true);
                mAidlUtil.printAgreement("\n" + builder3_3.toString(), 24, false, false);
            }

            if (!TextUtil.isEmpty(builder1.toString().trim())) {
                mAidlUtil.printAgreement("\n护理前检查:", 6, false, true);
                mAidlUtil.printAgreement( builder1.toString(), 24, false, false);
            }else{
                mAidlUtil.printAgreement("客户确认,检查正常\n" , 24, false, false);
            }
            mAidlUtil.printAgreement( builder3.toString(), 6, false, true);
            if (complex_dye_adapter.filterList.size()>0){
                mAidlUtil.printAgreement("复杂染色图案:", 6, false, true);
                StringBuilder complexDye=new StringBuilder();
                for (String string:complex_dye_adapter.filterList){
                    complexDye.append(string+"\t");
                }
                mAidlUtil.printAgreement( complexDye.toString(), 24, false, false);
            }
            if (organ_mould_adapter.filterList.size()>0){
                mAidlUtil.printAgreement("\n整体造型:", 28, false, true);
                StringBuilder complexDye=new StringBuilder();
                for (String string:organ_mould_adapter.filterList){
                    complexDye.append(string+"\t");
                }
                mAidlUtil.printAgreement("\n" + complexDye.toString(), 24, false, false);
            }
            if (!TextUtil.isEmpty(builder3_1.toString().trim())) {
                mAidlUtil.printAgreement("\n局部修剪:", 28, false, true);
                mAidlUtil.printAgreement("\n" + builder3_1.toString(), 24, false, false);
            }

            if (!TextUtil.isEmpty(builder3_2.toString().trim())) {
                mAidlUtil.printAgreement("\n局部染色:", 28, false, true);
                mAidlUtil.printAgreement("\n" + builder3_2.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement( builder4.toString()+"\n\n\n", 24, false, false);
        }else{
            AidlUtil mAidlUtil = AidlUtil.getInstance();
            mAidlUtil.printTitle(title);
            mAidlUtil.printAgreement(builder.toString(), 24, false, false);
            mAidlUtil.printAgreement("\n宠物信息", 28, false, true);
            mAidlUtil.printPartTwo("\n姓名: " + getText(pet_name), "\t种类: " + getText(pet_kind), 24, false);
            mAidlUtil.printPartTwo("\n品种: " + getText(pet_varieties), "\t年龄: " + getText(pet_age) + "岁", 24, false);
            mAidlUtil.printPartTwo("\n体重: " + getText(pet_weight) + "kg", "\t毛色: " + getText(pet_coatColor) + "色", 24, false);

            if (!TextUtil.isEmpty(builder3_3.toString().trim())) {
                mAidlUtil.printAgreement("\n自带衣物:", 28, false, true);
                mAidlUtil.printAgreement("\n" + builder3_3.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement("\n护理前检查:", 28, false, true);
            if (!TextUtil.isEmpty(builder1.toString().trim())) {
                mAidlUtil.printAgreement("\n" + builder1.toString(), 24, false, false);
            }else{
                mAidlUtil.printAgreement("\n客户确认,检查正常" , 24, false, false);
            }
            mAidlUtil.printAgreement( builder3.toString(), 28, false, true);
            if (complex_dye_adapter.filterList.size()>0){
                mAidlUtil.printAgreement("\n复杂染色图案:", 28, false, true);
                StringBuilder complexDye=new StringBuilder();
                for (String string:complex_dye_adapter.filterList){
                    complexDye.append(string+"\t");
                }
                mAidlUtil.printAgreement("\n" + complexDye.toString(), 24, false, false);
            }
            if (organ_mould_adapter.filterList.size()>0){
                mAidlUtil.printAgreement("\n整体造型:", 28, false, true);
                StringBuilder complexDye=new StringBuilder();
                for (String string:organ_mould_adapter.filterList){
                    complexDye.append(string+"\t");
                }
                mAidlUtil.printAgreement("\n" + complexDye.toString(), 24, false, false);
            }
            if (!TextUtil.isEmpty(builder3_1.toString().trim())) {
                mAidlUtil.printAgreement("\n局部修剪:", 28, false, true);
                mAidlUtil.printAgreement("\n" + builder3_1.toString(), 24, false, false);
            }

            if (!TextUtil.isEmpty(builder3_2.toString().trim())) {
                mAidlUtil.printAgreement("\n局部染色:", 28, false, true);
                mAidlUtil.printAgreement("\n" + builder3_2.toString(), 24, false, false);
            }
            mAidlUtil.printAgreement("\n" + builder4.toString(), 24, false, false);
            if ("T1mini".equals(Build.MODEL)) {
                mAidlUtil.print3Line();
            }
            AidlUtil.getInstance().cutPaper();
        }
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

}
