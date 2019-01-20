package ys.app.pad.activity.vip;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.SearchActivity;
import ys.app.pad.adapter.FragmentPageAdapter;
import ys.app.pad.adapter.vip.VipPetAdapter;
import ys.app.pad.fragment.VipCardFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.SpUtil;


public class VipActivity extends BaseActivity {
    private ViewPager viewPager;
    private int flag=0;
    private int classification;
    private ApiClient<BaseListResult<VipInfo>> apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_card_manager);
        setBackVisiable();
        String title= SpUtil.getShopName();
        setTitle(title);

        setBgWhiteStatusBar();
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        classification =getIntent().getIntExtra(Constants.intent_type,0);
        checkWarnVip();
        initTopView();
        initViewPager();

    }
    private void initViewPager(){

        final TextView vipRechargeTv = (TextView) findViewById(R.id.vip_recharge_tv);
        final TextView vipConsumeTv = (TextView) findViewById(R.id.vip_consume_tv);

        vipRechargeTv.setSelected(true);
        vipConsumeTv.setSelected(false);

        vipRechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        vipConsumeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              viewPager.setCurrentItem(1);
            }
        });

        List<Fragment> fragmentList = new ArrayList<>();

        VipCardFragment vipFragment=getFragment(0);
        VipCardFragment numFragment=getFragment(1);

        fragmentList.add(vipFragment);
        fragmentList.add(numFragment);

        String []arrTitle={"会员卡","次卡"};
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), fragmentList,arrTitle);
        viewPager.setAdapter(pageAdapter); //添加适配器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    setFlag(0);
                }else {
                   setFlag(1);
                }
                vipRechargeTv.setSelected(0 == position ? true : false);
                vipConsumeTv.setSelected(1 == position ? true : false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int type=getIntent().getIntExtra(Constants.intent_type,0);
        if (type==1){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }

    }

    private void initTopView() {
        ImageView mAddTv = (ImageView) findViewById(R.id.add_iv);
        mAddTv.setVisibility(View.VISIBLE);
        mAddTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VipActivity.this, AddVipActivity.class);
                intent.putExtra(Constants.intent_flag, flag);
                startActivity(intent);
            }
        });

        TextView searchAllTv = (TextView) findViewById(R.id.search_all_tv);
        searchAllTv.setVisibility(View.VISIBLE);
        searchAllTv.setHint("搜索会员");
        searchAllTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VipActivity.this, SearchActivity.class);
                intent.putExtra(Constants.intent_search_type, Constants.intent_search_type_vip);
                intent.putExtra(Constants.intent_flag,flag);
                startActivity(intent);
            }
        });
//        if ("T1mini".equals(Build.MODEL)){
//            ImageView iv_card= (ImageView) findViewById(R.id.iv_search_card);
//            iv_card.setVisibility(View.VISIBLE);
//            iv_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(VipActivity.this, NFCActivity.class);
//                    intent.putExtra(Constants.intent_flag, "huiyuan");
//                    intent.putExtra(Constants.intent_vip_name, flag);
//                    startActivity(intent);
//                }
//            });
//        }
    }

    private VipCardFragment getFragment(int flag){
        VipCardFragment vipCardFragment=new VipCardFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.intent_flag,flag);
        int  cardNO = getIntent().getIntExtra(Constants.intent_vip_card_no,-1);
        bundle.putSerializable(Constants.intent_vip_card_no,cardNO);
        bundle.putInt(Constants.intent_type,classification);
        vipCardFragment.setArguments(bundle);
        return vipCardFragment;
    }


    /**
     * 设置fragment的类型  0：折扣会员，1：次卡会员
     * @param flag
     */
    public void setFlag(int flag){
        this.flag = flag;
    }

    private void checkWarnVip(){
        final Dialog dialog=new Dialog(this,R.style.ThemeCustomDialog);

        dialog.setContentView(R.layout.dialog_vip_pet_warn);
        TextView tv_close= (TextView) dialog.findViewById(R.id.tv_dialog_pet_warn_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView recyclerView= (RecyclerView) dialog.findViewById(R.id.recyclerView);
        final VipPetAdapter adapter=new VipPetAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (apiClient==null)apiClient=new ApiClient<>();
        Map params=new HashMap();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("limit", "1000");
        apiClient.reqApi("selectVipUserMessage", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<VipInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipInfo> result) {
                        if (result.isSuccess()) {
                            List<VipInfo> data = result.getData();
                            if (data.size()>0) {
                                adapter.setList(data);
                                dialog.show();
                            }
                        } else {
                            showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
        }



}
