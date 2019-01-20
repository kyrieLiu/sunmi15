package ys.app.pad.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityCollectMoneyBinding;
import ys.app.pad.shangmi.t1miniscan.ScanActivity;
import ys.app.pad.utils.ScannerGunWatcher;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.viewmodel.CollectMoneyViewModel;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by lyy on 2017/2/20 17:14.
 * email：2898049851@qq.com
 */

public class CollectMoneyActivity extends BaseActivity {

    private ActivityCollectMoneyBinding binding;
    private CollectMoneyViewModel viewModel;
    private EditText et_saomiaoqiang;
    private ImageView scan_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collect_money);
        setBackVisiable(true);
        String title=SpUtil.getShopName();
        TextView tv_title= (TextView) findViewById(R.id.title_tv);
        tv_title.setText(title);
        ImageView blueSearchIv = (ImageView) findViewById(R.id.blue_search_iv);
        viewModel = new CollectMoneyViewModel(this, binding,blueSearchIv);
        binding.setViewModel(viewModel);
        viewModel.initData();
        listenEditText();
        setBgWhiteStatusBar();
    }


    private void listenEditText(){
        et_saomiaoqiang= (EditText) findViewById(R.id.et_tool_saomiaoqiang);
        et_saomiaoqiang.setFocusable(true);
        et_saomiaoqiang.setFocusableInTouchMode(true);
        et_saomiaoqiang.requestFocus();
        ScannerGunWatcher watcher=new ScannerGunWatcher(et_saomiaoqiang);
        watcher.setOnWatcherListener(new ScannerGunWatcher.ScannerGunCallBack() {
            @Override
            public void callBack(String string) {
                if (viewModel != null) {
                    viewModel.setScanResult(string);
                }
            }
        });
        et_saomiaoqiang.addTextChangedListener(watcher);



        //T1mini扫描
        scan_iv = (ImageView) findViewById(R.id.scan_iv);
        if ("T1mini".equals(Build.MODEL)){
            scan_iv.setVisibility(View.VISIBLE);
        }else {
            scan_iv.setVisibility(View.GONE);
        }
        scan_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(CollectMoneyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CollectMoneyActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.permission_request_code);
                    } else {
                        toScanActivity();
                    }
                } else {
                    toScanActivity();
                }

            }
        });
    }

    private void toScanActivity() {
        Intent intent = new Intent(CollectMoneyActivity.this, ScanActivity.class);
        startActivityForResult(intent, Constants.scan_result_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.scan_result_code && null != data){
            String info = data.getStringExtra(Constants.scan_result);
            if (!TextUtil.isEmpty(info) && viewModel != null) {
                viewModel.setScanResult(info);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.permission_request_code && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            toScanActivity();
        }
    }

    @Override
    public void onBackPressed() {
        viewModel.clearAllOrder();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null){
            viewModel.reset();
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        String name=  event.getDevice().getName();
//        Log.i("info","设备名称"+name);
//        //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
//        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction()) {
//            Log.i("info","按回车键");
//            //处理事件
//            return true;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }
}
