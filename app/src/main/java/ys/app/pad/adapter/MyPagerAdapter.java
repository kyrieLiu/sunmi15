package ys.app.pad.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lyy on 2017/2/9 16:18.
 * email：2898049851@qq.com
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> list;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
        // super.destroyItem(container, position, object);
    }


}
