package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAddServiceBinding;
import ys.app.pad.viewmodel.AddServiceActivityViewModel;
import ys.app.pad.widget.imagepicker.ImagePicker;

/**
 * Created by admin on 2017/6/7.
 */

public class AddServiceActivity extends BaseActivity {

    private ActivityAddServiceBinding binding;
    private AddServiceActivityViewModel mViewModel;
    private ImagePicker mImagePicker = new ImagePicker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_service);
        setBackVisiable();
        setTitle("新增服务");
        mViewModel = new AddServiceActivityViewModel(this, binding,mImagePicker);
        binding.setViewModel(mViewModel);
        setBgWhiteStatusBar();
        mViewModel.getTypeHttp();
        // 设置标题
        mImagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        mImagePicker.setCropImage(true);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null){
            mViewModel.reset();
        }
    }
}
