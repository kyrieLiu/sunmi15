package ys.app.pad.viewmodel;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.AddGoodsActivity;
import ys.app.pad.activity.AddServiceActivity;
import ys.app.pad.activity.CheckActivity;
import ys.app.pad.activity.GoodsActivity;
import ys.app.pad.activity.ServiceActivity;
import ys.app.pad.activity.inventory.InventoryRecordsActivity;
import ys.app.pad.databinding.ActivityMainBinding;
import ys.app.pad.utils.DensityUtil;


/**
 * Created by lyy on 2017/2/14 16:27.
 * email：2898049851@qq.com
 */

public class MainViewModel extends BaseActivityViewModel {

    private Context mContext;
    private ActivityMainBinding binding;
    private Boolean isVisable = false;

    public MainViewModel(Context context,ActivityMainBinding binding) {

        this.mContext = context;
        this.binding = binding;

    }

    /**
     * 点击了商品库存
     *
     * @param v
     */
    public void clickSPKC(View v) {
        Intent intent = new Intent(mContext, GoodsActivity.class);
        intent.putExtra(Constants.intent_search_from,Constants.intent_form_shangpin_kucunliebiao);
        mContext.startActivity(intent);
    }


    /**
     * 点击了服务库存
     *
     * @param v
     */
    public void clickFWKC(View v) {
        Intent intent = new Intent(mContext, ServiceActivity.class);
        intent.putExtra(Constants.intent_search_from,Constants.intent_form_fuwu_kucunliebiao);
        mContext.startActivity(intent);
    }


    /**
     * 点击了新增商品
     *
     * @param v
     */
    public void clickXZSP(View v) {
        turnToActivity((Activity) mContext, AddGoodsActivity.class);
    }


    /**
     * 点击了新增服务
     *
     * @param v
     */
    public void clickXZFW(View v) {
        turnToActivity((Activity) mContext, AddServiceActivity.class);
    }


    /**
     * 点击了商品盘点
     *
     * @param v
     */
    public void clickSPPD(View v) {
        turnToActivity((Activity) mContext, CheckActivity.class);
    }


    /**
     * 点击了商品入库
     *
     * @param v
     */
    public void clickSPRK(View v) {
        Intent intent = new Intent(mContext, GoodsActivity.class);
        intent.putExtra(Constants.intent_search_from,Constants.intent_form_shangpin_ruku);
        mContext.startActivity(intent);

    }


    /**
     * 点击了商品出库
     *
     * @param v
     */
    public void clickSPCK(View v) {
        Intent intent = new Intent(mContext, GoodsActivity.class);
        intent.putExtra(Constants.intent_search_from,Constants.intent_form_shangpin_chuku);
        mContext.startActivity(intent);
    }
    /**
     * 点击了盘点记录
     *
     * @param v
     */
    public void clickPDJL(View v) {
        Intent intent = new Intent(mContext, InventoryRecordsActivity.class);
        mContext.startActivity(intent);
    }




    /**
     * 改变二级菜单显示状态
     */
    public void changeSecMenu(){
        ValueAnimator animator;
        if (!isVisable) {
           animator = ValueAnimator.ofInt(0, DensityUtil.dip2px(mContext, 125));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LinearLayout linearLayout = binding.secRoot;
                    int h = (int) (animation.getAnimatedFraction() * DensityUtil.dip2px(mContext, 125.0f));
                    linearLayout.getLayoutParams().height = h;
                    linearLayout.getParent().requestLayout();
                }
            });
        }else {
            animator = ValueAnimator.ofInt(DensityUtil.dip2px(mContext, 125),0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    LinearLayout linearLayout = binding.secRoot;
                    int h = DensityUtil.dip2px(mContext, 125.0f) - (int) (animation.getAnimatedFraction() * DensityUtil.dip2px(mContext, 125.0f));
                    linearLayout.getLayoutParams().height = h;
                    linearLayout.getParent().requestLayout();
                }
            });
        }
        animator.setDuration(500);
        animator.start();
        isVisable = !isVisable;
    }


    public void destroy() {
        mContext = null;
    }
}
