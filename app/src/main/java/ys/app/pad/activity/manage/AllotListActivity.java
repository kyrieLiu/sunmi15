package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.AllotListActivityBinding;
import ys.app.pad.viewmodel.AllotShopListModel;

/**
 * Created by aaa on 2017/6/5.
 */

public class AllotListActivity extends BaseActivity {

    private AllotListActivityBinding binding;
    private AllotShopListModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_allot_shop_list);
        setBackVisiable();
        setTitle("调拨");

        mViewModel = new AllotShopListModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.initView();
        setBgWhiteStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.reset();
    }
}
