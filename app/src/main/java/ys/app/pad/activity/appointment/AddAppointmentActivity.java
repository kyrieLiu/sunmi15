package ys.app.pad.activity.appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.adapter.FragmentPageAdapter;
import ys.app.pad.fragment.appointment.NormalAppointmentFragment;
import ys.app.pad.fragment.appointment.VipAppointmentFragment;


/**
 * Created by liuyin on 2017/9/13.
 */

public class AddAppointmentActivity extends BaseActivity {
    private android.support.design.widget.TabLayout tableLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        setBackVisiable();
        setTitle("添加预约");

        setBgWhiteStatusBar();
        tableLayout = (android.support.design.widget.TabLayout) findViewById(R.id.tl_vp_with_tab);
        viewPager = (ViewPager) findViewById(R.id.vp_vp_with_tab);
        initView();

    }

    private void initView() {
        String date=getIntent().getStringExtra("date");
        List<Fragment> fragmentList = new ArrayList<>();
        VipAppointmentFragment vipAppointmentFragment=new VipAppointmentFragment();
        NormalAppointmentFragment normalAppointmentFragment=new NormalAppointmentFragment();
        Bundle bundle=new Bundle();
        bundle.putString("date",date);
        vipAppointmentFragment.setArguments(bundle);
        normalAppointmentFragment.setArguments(bundle);
        fragmentList.add(vipAppointmentFragment);
        fragmentList.add(normalAppointmentFragment);

        String[] arrTitle = {"会员", "非会员"};
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), fragmentList, arrTitle);
        viewPager.setAdapter(pageAdapter); //添加适配器
        tableLayout.setupWithViewPager(viewPager);   //设置tablayout
        tableLayout.setTabsFromPagerAdapter(pageAdapter);
    }
}
