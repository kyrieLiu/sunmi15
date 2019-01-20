package ys.app.pad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lyy on 2017/2/9 16:23.
 * email：2898049851@qq.com
 */

public class FragmentPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private String[] arrTitle;
    public FragmentPageAdapter(FragmentManager fm, List<Fragment> list, String []arrTitle) {
        super(fm);
        this.list=list;
        this.arrTitle=arrTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list==null){
            return 0;
        }
        return list.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return arrTitle[position];
    }

}
