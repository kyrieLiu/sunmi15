package ys.app.pad.activity.inventory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.adapter.FragmentPageAdapter;
import ys.app.pad.fragment.OutPutInventoryFragment;


/**
 * Created by liuyin on 2017/11/10.
 */

public class OutPutInventoryActivity extends BaseActivity {
    private android.support.design.widget.TabLayout tableLayout;
    private ViewPager viewPager;
    private String date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        setBackVisiable();
        setTitle("出入库记录");
        setBgWhiteStatusBar();
        tableLayout= (android.support.design.widget.TabLayout) findViewById(R.id.tl_vp_with_tab);
        viewPager= (ViewPager) findViewById(R.id.vp_vp_with_tab);

        initView();

    }
    private void initView(){
        //1出库,2入库
        List<Fragment> fragmentList = new ArrayList<>();

        OutPutInventoryFragment outFragment=getFragment(1);
        OutPutInventoryFragment putFragment=getFragment(2);

        fragmentList.add(outFragment);
        fragmentList.add(putFragment);

        String []arrTitle={"入库","出库"};
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), fragmentList,arrTitle);
        viewPager.setAdapter(pageAdapter); //添加适配器
        tableLayout.setupWithViewPager(viewPager);   //设置tablayout
        tableLayout.setTabsFromPagerAdapter(pageAdapter);

    }
    private OutPutInventoryFragment getFragment(int state){
        OutPutInventoryFragment fragment=new OutPutInventoryFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(Constants.intent_search_from,state);
        bundle.putString(Constants.intent_flag,date);
        fragment.setArguments(bundle);
        return fragment;
    }
}
