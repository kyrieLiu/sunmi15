package ys.app.pad.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAddGoodsBinding;
import ys.app.pad.shangmi.t1miniscan.ScanActivity;
import ys.app.pad.viewmodel.AddGoodsActivityViewModel;
import ys.app.pad.widget.imagepicker.ImagePicker;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by admin on 2017/6/7.
 */

public class AddGoodsActivity extends BaseActivity {
    private ActivityAddGoodsBinding binding;
    private AddGoodsActivityViewModel mViewModel;
    private ImagePicker mImagePicker = new ImagePicker();
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_goods);
        setBackVisiable();
        setTitle("新增商品");
        mViewModel = new AddGoodsActivityViewModel(this, binding, mImagePicker);
        binding.setViewModel(mViewModel);
        mViewModel.getTypeHttp();
        mViewModel.getUnitHttp();
        setBgWhiteStatusBar();
        // 设置标题
        mImagePicker.setTitle("设置头像");
        // 设置是否裁剪图片
        mImagePicker.setCropImage(true);
        //设置右上角新增button
        showAddButton();
    }

    private void showAddButton() {
        addButton = (Button) findViewById(R.id.blue_save_btn);
        addButton.setVisibility(View.VISIBLE);
        addButton.setText("新增");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.clickButton(v);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.scan_result_code) {
            if (null != data) {
                String info = data.getStringExtra(Constants.scan_result);
                if (!TextUtil.isEmpty(info) && mViewModel != null) {
                    mViewModel.obGoodsCode.set(info);
                }
            }
        } else {
            mImagePicker.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.permission_request_code) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, Constants.scan_result_code);
            }
        } else {
            mImagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.reset();
        }
    }
}
