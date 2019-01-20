package ys.app.pad.viewmodel.vip;

import android.view.View;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.vip.VipRecordActivity;
import ys.app.pad.databinding.VipRecordActivityBinding;
import ys.app.pad.event.RxManager;

/**
 * Created by Administrator on 2017/6/8.
 */

public class VipRecordViewModel extends BaseActivityViewModel {
    private final VipRecordActivity mActivity;
    private VipRecordActivityBinding mBinding;
    private RxManager mRxManager;

    public VipRecordViewModel(VipRecordActivity activity, VipRecordActivityBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        registerBus();
    }

    private void registerBus() {
        if (mRxManager == null){
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer registerI) {
                if(Constants.type_addGood_success == registerI||Constants.type_addService_success== registerI){
                    mActivity.finish();
                }
            }
        });
    }

    public void clickButton(View v){
        if (mRxManager == null){
            mRxManager = new RxManager();
        }
        if(1 == mBinding.viewPager.getCurrentItem()){
            mRxManager.post(Constants.bus_type_click_btn,Constants.type_add_service);
        }else {
            mRxManager.post(Constants.bus_type_click_btn,Constants.type_add_good);
        }

    }
}

