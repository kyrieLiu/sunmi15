package ys.app.pad.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.adapter.GoodTypeInVipAdapter;
import ys.app.pad.adapter.OrderChildAdapter;
import ys.app.pad.adapter.ServiceTypeInVipAdapter;
import ys.app.pad.adapter.inventory.InventoryRecordsDetailAdapter;
import ys.app.pad.adapter.inventory.InventoryRecordsItemAdapter;
import ys.app.pad.adapter.manage.NumCardChildAdapter;
import ys.app.pad.adapter.vip.VipPetChildAdapter;
import ys.app.pad.base.MvvmCommonAdapter;
import ys.app.pad.itemmodel.CardNumberList2Bean;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.InventoryRecordsInfo;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.model.ServiceTypeInfo;

/**
 * Created by aaa on 2017/3/24.
 */

public class PetBindingUtil {

    @BindingAdapter("OnCheckedChangeListener")
    public static void setOnCheckedChangeListener(CheckBox checkBox, CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("imgSrc")
    public static void setImgSrc(ImageView imgSrc, int res) {
        imgSrc.setImageResource(res);
    }


    @BindingAdapter("imgIsSelect")
    public static void setImgIsSelect(ImageView imgSrc, boolean isSelect) {
        if (isSelect) {
            imgSrc.setImageResource(R.mipmap.square_select);
        } else {
            imgSrc.setImageResource(R.mipmap.square_no_select);
        }
    }

    @BindingAdapter("drawableRightSrc")
    public static void setDrawableRight(TextView textView, int res) {
        Drawable drawable = textView.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    @BindingAdapter("drawableTopSrc")
    public static void setDrawableTop(TextView textView, int res) {
        Drawable drawable = textView.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }


    @BindingAdapter("onTextChangedListener")
    public static void setTextChangedListener(EditText editText, TextWatcher listener) {
        editText.addTextChangedListener(listener);
    }


    @BindingAdapter("setHttpImgByGlide")
    public static void setHttpImgByGlide(ImageView imageView, String path) {
        GlideUtil.getInstance().loadImageView(Constants.base_url + path, R.mipmap.load_img_normal, imageView, imageView.getContext());
    }

    @BindingAdapter("setHttpImgByGlideCircle")
    public static void setHttpImgByGlideCircle(ImageView imageView, String path) {
        GlideUtil.getInstance().loadCircleImageView(Constants.base_url + path, R.mipmap.load_img_circle_normal, imageView, imageView.getContext());
    }

    @BindingAdapter("imgUri")
    public static void setImgUri(ImageView imageView, Uri imgUri) {
        imageView.setImageURI(imgUri);
    }

    @BindingAdapter("imgData")
    public static void setImgData(ImageView imageView, byte[] data) {
        if (data == null) return;
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    @BindingAdapter("adapterData")
    public static void setChildAdapterData(RecyclerView recyclerView, List<OrderInfo.OrderDetailedListBean> list) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        OrderChildAdapter adapter = new OrderChildAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter(value = {"goodsTypeAdapterData","isInput","isAddVipCard"},requireAll = false)
    public static void setGoodsTypeAdapterData(RecyclerView recyclerView, List<GoodTypeInfo> list,boolean isInput,boolean isAddVipCard) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        GoodTypeInVipAdapter adapter = new GoodTypeInVipAdapter(context,isInput,isAddVipCard);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter(value = {"goodsListTypeAdapterData","isInput","isAddVipCard"},requireAll = false)
    public static void setGoodsListTypeAdapterData(RecyclerView recyclerView, List<GoodTypeInfo> list,boolean isInput,boolean isAddVipCard) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        GoodTypeInVipAdapter adapter = new GoodTypeInVipAdapter(context,isInput,isAddVipCard);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }
    @BindingAdapter(value ={"serviceTypeAdapterData","isInput","isAddVipCard"},requireAll = false)
    public static void setServiceTypeAdapterData(RecyclerView recyclerView, List<ServiceTypeInfo> list, boolean isInput,boolean isAddVipCard) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context,1));
        ServiceTypeInVipAdapter adapter = new ServiceTypeInVipAdapter(context,isInput,isAddVipCard);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }
    @BindingAdapter(value ={"serviceListTypeAdapterData","isInput","isAddVipCard"},requireAll = false)
    public static void setServiceListTypeAdapterData(RecyclerView recyclerView, List<ServiceTypeInfo> list, boolean isInput,boolean isAddVipCard) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        ServiceTypeInVipAdapter adapter = new ServiceTypeInVipAdapter(context,isInput,isAddVipCard);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter(value ={"numCardAdapterData"},requireAll = false)
    public static void setNumCardAdapterData(RecyclerView recyclerView, List<CardNumberList2Bean> list) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context,4));
        NumCardChildAdapter adapter = new NumCardChildAdapter(context);

        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter(value ={"vipPetAdapterData"},requireAll = false)
    public static void setVipPetAdapterData(RecyclerView recyclerView, List<AnimalInfo> list) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        VipPetChildAdapter adapter = new VipPetChildAdapter(context);

        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter("inventoryRecordsAdapterData")
    public static void setInventoryRecordsAdapterData(RecyclerView recyclerView, List<InventoryRecordsInfo.InventoryList2Bean> list) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        InventoryRecordsItemAdapter adapter = new InventoryRecordsItemAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }

    @BindingAdapter("inventoryRecordsDetail")
    public static void setInventoryRecordsDetailData(RecyclerView recyclerView, List<InventoryRecordsInfo.InventoryListBean> list) {
        if (recyclerView == null || list == null) return;
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        InventoryRecordsDetailAdapter adapter = new InventoryRecordsDetailAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
    }



    @BindingAdapter({"isVis", "isClick", "count"})
    public static void setIsVis(final View view, boolean isVis, int isClick, int count) {
        if (isClick == -1) {
            view.setVisibility(View.GONE);
            return;
        }
        if (isVis && isClick == 0) {
            openAnim(view, count);
        } else if (isClick == 1) {
            closeAnim(view);
        }
    }

    @BindingAdapter({"isVisible", "isClicked", "viewCount"})
    public static void setIsVisFull(final View view, boolean isVisible, int isClicked, int viewCount) {
        if (isClicked == -1) {
            view.setVisibility(View.GONE);
            return;
        }
        if (isVisible && isClicked == 0) {
            openAnimFull(view, viewCount);
        } else if (isClicked == 1) {
            closeAnim(view);
        }
    }


    @BindingAdapter({"isClassification", "isPromotion", "isFold", "vipPrice"})
    public static void setFristPrice(final TextView view, int isClassification, int isPromotion, int isFold, double vipPrice) {
        if (isClassification == 0||isClassification==1||isClassification==3) {//会员卡
            if (1 == isPromotion) {
                if (1 == isFold) {
                    view.setText("折上折价：￥" + AppUtil.formatStandardMoney(vipPrice));
                } else {
                    view.setText("vip价：￥" + AppUtil.formatStandardMoney(vipPrice));
                }
            } else {
                view.setText("vip价：￥" + AppUtil.formatStandardMoney(vipPrice));
            }
        }else if (isClassification==2){//纯折扣卡
            view.setText("折扣价：￥" + AppUtil.formatStandardMoney(vipPrice));
        } else {
            if (1 == isPromotion) {
                view.setText("促销价：￥" + AppUtil.formatStandardMoney(vipPrice));
            } else {
                view.setText("￥" + AppUtil.formatStandardMoney(vipPrice));
            }
        }
    }

    @BindingAdapter({"isClassification", "isPromotion"})
    public static void setPrice(final View view, int isClassification, int isPromotion) {

        if(-1!= isClassification||1 == isPromotion){
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.GONE);
        }

    }

    @BindingAdapter({"vipUserId", "isPromotion", "isFold"})
    public static void setCostAndCost(final TextView view, int vipUserId, int isPromotion, int isFold) {
        if (vipUserId > 0&&1 == isPromotion) {//会员卡
            view.setVisibility(View.VISIBLE);
            if(1 == isFold){
                view.setText("参与折上折");
            }else{
                view.setText("不参与折上折");
            }
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({ "realMoney"})
    public static void setTotalMoney(final TextView view, double realMoney) {
        if (realMoney<0){
            double realAmt=0-realMoney;
            view.setText("总价：-￥" + AppUtil.formatStandardMoney(realAmt));
        }else{
            view.setText("总价：￥" + AppUtil.formatStandardMoney(realMoney));
        }
    }
    private static void openAnim(View view, int count) {
        view.setVisibility(View.VISIBLE);
        float mDensity = view.getResources().getDisplayMetrics().density;
        int mHiddenViewMeasuredHeight = (int) (mDensity * (56 + 105 * count) + 0.5);//计算child总高度,根据布局算的总高度
        ValueAnimator animator = createDropAnimator(view, 0, mHiddenViewMeasuredHeight);
        animator.start();
    }

    private static void openAnimFull(View view, int count) {
        view.setVisibility(View.VISIBLE);
        float mDensity = view.getResources().getDisplayMetrics().density;
        int mHiddenViewMeasuredHeight = (int) (mDensity * 460);//计算child总高度,根据布局算的总高度

        int size[] = new int[2];
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
//        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

//        Log.i("orderInfo","mDensity的高度为"+mDensity+"mHiddenViewMeasureHeight=="+mHiddenViewMeasuredHeight);
        ValueAnimator animator;
        if (measuredHeight > mHiddenViewMeasuredHeight){
            animator = createDropAnimator(view, 0, mHiddenViewMeasuredHeight);
        }else {
            animator = createDropAnimator(view, 0, measuredHeight);
        }
        animator.start();
    }

    private static void closeAnim(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }


    private static ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

    /**
     * 设置EditText是否可编辑
     * @author com.tiantian
     * @param editText 要设置的EditText
     * @param value 可编辑:true 不可编辑:false
     */
    @SuppressLint("NewApi")
    @BindingAdapter(value = {"editTextEditable","background"},requireAll = false)
    public static void setEditTextEditable(EditText editText, boolean value,boolean background){
        if(value){
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }else{
            editText.setFocusableInTouchMode(false);
            editText.clearFocus();
        }

        if (!background){
            editText.setBackground(null);
        }
    }
    @BindingAdapter("loadMoreStatusStr")
    public static void setLoadMoreStatusStr(TextView textView, int loadMoreStatus) {
        if (MvvmCommonAdapter.load_more_status_normal == loadMoreStatus) {
            textView.setText("上拉加载");
        } else if (MvvmCommonAdapter.load_more_status_loading == loadMoreStatus) {
            textView.setText("加载中");
        } else if (MvvmCommonAdapter.load_more_status_failed == loadMoreStatus) {
            textView.setText("加载失败，点击后重试");
        } else if (MvvmCommonAdapter.load_more_status_load_all == loadMoreStatus) {
            textView.setText("加载完毕");
        } else {
            textView.setText("上拉加载");
        }
    }
}
