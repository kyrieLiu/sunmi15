package ys.app.pad.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseFragmentViewModel;
import ys.app.pad.Constants;
import ys.app.pad.databinding.FragmentAchievementStaticBinding;
import ys.app.pad.fragment.AchievementStaticFragment;
import ys.app.pad.fragment.AchievementStatisticChildFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AchieveStatisInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class AchievementStaticFragmentViewModel extends BaseFragmentViewModel {
    private AchievementStaticFragment mFragment;
    private FragmentAchievementStaticBinding binding;
    private ApiClient<BaseListResult<AchieveStatisInfo>> apiClient;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<AchievementStatisticChildFragment> itemFragments = new ArrayList<>();
    private AchievementStatisticChildFragment itemFragment;
    private int type;

    public AchievementStaticFragmentViewModel(AchievementStaticFragment mFragment, FragmentAchievementStaticBinding binding) {
        this.mFragment = mFragment;
        this.binding = binding;
        this.viewPager = binding.viewpager;
        this.tabLayout = binding.tabLayout;
        this.apiClient = new ApiClient<>();
        init();
        loadHttpData();
    }

    public void onClickNetWorkError() {
        isNetWorkErrorVisible.set(false);
        isLoadingVisible.set(true);
        loadHttpData();
    }

    private void init() {
        type = mFragment.getArguments().getInt(Constants.TYPE);

//        ColorStateList colorStateList;
//        if (SpUtil.isDaySkin()) {
//             colorStateList = mFragment.getActivity().getResources().getColorStateList(R.color.text_color_day);
//        }else {
//            colorStateList = mFragment.getActivity().getResources().getColorStateList(R.color.text_color_night);
//        }
//        tabLayout.setTabTextColors(colorStateList);
//        tabLayout.setSelectedTabIndicatorColor(mFragment.getActivity().getResources().getColor(R.color.text_red));
//        tabLayout.setTabTextColors(ColorUtils.getInstance().getTextColor(), ColorUtils.getInstance().getTextRedColor());
//        if (SpUtil.isDaySkin()) {
//            tabLayout.setSelectedTabIndicatorColor(mFragment.getActivity().getResources().getColor(R.color.text_grey_color));
//            tabLayout.setTabTextColors(ColorUtils.getInstance().getTextGreyColor(),ColorUtils.getInstance().getTextColor());
//        }else {
//            tabLayout.setSelectedTabIndicatorColor(mFragment.getActivity().getResources().getColor(R.color.text_deep_grey_color));
//            tabLayout.setTabTextColors( ColorUtils.getInstance().getTextGreyColor(),ColorUtils.getInstance().getTextColor());
//        }
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mFragment.getActivity())) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataHttp();
        } else {//无网显示断网连接
            loadHttpNoData();
        }
    }

    private void getDataHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("type",type+"");
        apiClient.reqApi("selectUserListNoPage",params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AchieveStatisInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AchieveStatisInfo> result) {
                        if (result.isSuccess()){
                            List<AchieveStatisInfo> list = result.getData();
                            if (null != list && list.size()>0){
                                refreshSuccess(list);
                                toBindChildFragment(list);
                            }else {
                                refreshSuccess(list);
                            }
                        }else {
                            loadHttpNoData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loadHttpNoData();
                    }
                });
    }

    /**
     * title请求完成，进行下一步操作
     * @param list
     */
    private void toBindChildFragment(List<AchieveStatisInfo> list) {
        for (int i = 0;i<list.size();i++){
            AchieveStatisInfo info = list.get(i);
            itemFragment = new AchievementStatisticChildFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.fragment_args,info.getId());
            bundle.putInt(Constants.TYPE,type);
            itemFragment.setArguments(bundle);
            itemFragments.add(itemFragment);
        }

        if (list == null || list.size() == 0) {
            loadHttpNoData();
            return;
        }

        //关联tablayout
        initTabLayout(list);
    }

    /**
     * 关联tablayout
     */
    private void initTabLayout(List<AchieveStatisInfo> titleList) {
        AchievStatisFragmentAdapter adapter = new AchievStatisFragmentAdapter(mFragment.getFragmentManager(),
                mFragment.getActivity(),titleList,itemFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public class AchievStatisFragmentAdapter extends FragmentStatePagerAdapter {
        private Context context;
        private List<AchieveStatisInfo> titleList;
        private List<AchievementStatisticChildFragment> itemFragments;


        public AchievStatisFragmentAdapter(FragmentManager fm, Context context,
                                           List<AchieveStatisInfo> titleList,
                                           List<AchievementStatisticChildFragment> itemFragments
        ) {
            super(fm);
            this.context = context;
            this.itemFragments = itemFragments;
            this.titleList = titleList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position).getName();
        }

        @Override
        public Fragment getItem(int position) {
            return itemFragments.get(position);
        }

        @Override
        public int getCount() {
            return itemFragments.size();
        }
    }
}
