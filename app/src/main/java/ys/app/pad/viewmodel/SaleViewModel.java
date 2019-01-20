package ys.app.pad.viewmodel;

import android.content.Intent;
import android.view.View;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.GoodsActivity;
import ys.app.pad.activity.SaleActivity;
import ys.app.pad.activity.ServiceActivity;
import ys.app.pad.databinding.ActivitySaleBinding;

/**
 * Created by aaa on 2017/6/9.
 */
public class SaleViewModel extends BaseActivityViewModel {
    private final SaleActivity mActivity;
    private final ActivitySaleBinding mBinding;

    public SaleViewModel(SaleActivity activity, ActivitySaleBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        init();
    }

    private void init() {
//        Intent startIntent = new Intent(mActivity, InitDataService.class);
//        mActivity.startService(startIntent);

    }

    /**
     * 商品促销
     *
     * @param v
     */
    public void clickSPCX(View v) {//GoodsActivity
        Intent intent = new Intent(mActivity, GoodsActivity.class);
        intent.putExtra(Constants.intent_search_from, Constants.intent_form_shangpin_cuxiaoliebiao);
        mActivity.startActivity(intent);
    }

    /**
     * 服务促销
     *
     * @param v
     */
    public void clickFWCX(View v) {

        Intent intent = new Intent(mActivity, ServiceActivity.class);
        intent.putExtra(Constants.intent_search_from, Constants.intent_form_fuwu_cuxiaoliebiao);
        mActivity.startActivity(intent);
    }

    /**
     * 商品促销设置
     *
     * @param v
     */
    public void clickSPCXSZ(View v) {
//        Intent intent = new Intent(mActivity,GoodsActivity.class);
//        intent.putExtra(Constants.type_promotion_setting,true);
//        mActivity.startActivity(intent);

        Intent intent = new Intent(mActivity, GoodsActivity.class);
        intent.putExtra(Constants.intent_search_from, Constants.intent_form_shangpin_cuxiaoshezhi);
        mActivity.startActivity(intent);
    }

    /**
     * 服务促销设置
     *
     * @param v
     */
    public void clickFWCXSZ(View v) {

        Intent intent = new Intent(mActivity, ServiceActivity.class);
        intent.putExtra(Constants.intent_search_from, Constants.intent_form_fuwu_cuxiaoshezhi);
        mActivity.startActivity(intent);
    }
}
