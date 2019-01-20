package ys.app.pad.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import ys.app.pad.R;
import ys.app.pad.databinding.DialogCustomerTypeBinding;
import ys.app.pad.viewmodel.CustomerTypeViewModel;

/**
 * Created by aaa on 2017/3/15.
 */

public class CustomerTypeDialog extends Dialog{

    private final Activity mActivity;
    private DialogCustomerTypeBinding binding;
    private OnClickListenner listenner;
    private boolean isGuadan;


    public CustomerTypeDialog(Activity activity,boolean isGuadan) {
        super(activity, R.style.ThemeCustomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_customer_type,null);
        super.setContentView(view);
        binding = DataBindingUtil.bind(view);
        binding.setViewModel(new CustomerTypeViewModel(this,activity,binding,isGuadan));
        this.mActivity = activity;
        this.isGuadan=isGuadan;
        hideSoft();
    }

    private void hideSoft() {
        binding.nameEt.clearFocus();
        binding.phoneEt.clearFocus();
        binding.vipPhoneEt.clearFocus();
        binding.vipCardEt.clearFocus();
        binding.animalEt.clearFocus();
        closeKeybord(binding.nameEt,mActivity);
        closeKeybord(binding.phoneEt,mActivity);
        closeKeybord(binding.vipPhoneEt,mActivity);
        closeKeybord(binding.vipCardEt,mActivity);
        closeKeybord(binding.animalEt,mActivity);
    }


    public void onClickJump() {
        if(listenner!=null){
            listenner.onClickJump();
        }
        dismiss();
    }

    public void onClickButton(int mPosition, String name, String phone, String s) {
        if(listenner!=null){
            listenner.onClickButton(mPosition,name,phone,s);
        }
        //dismiss();
    }

    public interface  OnClickListenner{
        void onClickJump();
        void onClickButton(int mPosition, String name, String phone, String s);
    }

    public void setListenner(OnClickListenner listenner) {
        this.listenner = listenner;
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
