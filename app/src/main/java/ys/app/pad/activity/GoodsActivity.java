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
import ys.app.pad.databinding.ActivityGoodsBinding;
import ys.app.pad.shangmi.t1miniscan.ScanActivity;
import ys.app.pad.utils.ScannerGunWatcher;
import ys.app.pad.viewmodel.GoodsActivityViewModel;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by admin on 2017/6/7.
 */

public class GoodsActivity extends BaseActivity {

    private ActivityGoodsBinding binding;
    private GoodsActivityViewModel mViewModel;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods);
        setBackVisiable();
        int intnetFrom = getIntent().getIntExtra(Constants.intent_search_from, -1);
        TextView tv_title=(TextView)findViewById(R.id.title_tv);
        ImageView iv_right=(ImageView)findViewById(R.id.add_iv);
        iv_right.setVisibility(View.GONE);
        editText= (EditText) findViewById(R.id.et_tool_ruku_saomiaoqiang);
        if (Constants.intent_form_shangpin_ruku==intnetFrom){
            tv_title.setText("商品入库");

            editText.setVisibility(View.VISIBLE);
            initScannerGun();
        }else if (Constants.intent_form_shangpin_chuku==intnetFrom){
            tv_title.setText("商品出库");

            editText.setVisibility(View.VISIBLE);
            initScannerGun();
        }else if (Constants.intent_form_shangpin_kucunliebiao==intnetFrom){
            tv_title.setText("商品库存");

            editText.setVisibility(View.VISIBLE);
            initScannerGun();
        }else if (Constants.intent_form_shangpin_cuxiaoliebiao==intnetFrom||Constants.intent_form_shangpin_cuxiaoshezhi==intnetFrom){
            tv_title.setText("商品促销");
        }
        TextView searchAllTv = (TextView)findViewById(R.id.search_all_tv);
        searchAllTv.setVisibility(View.VISIBLE);
        searchAllTv.setHint("搜索商品");


        searchAllTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsActivity.this,SearchActivity.class);
                intent.putExtra(Constants.intent_search_type,Constants.intent_search_type_goods);
                intent.putExtra(Constants.intent_search_from,getIntent().getIntExtra(Constants.intent_search_from,-1));
                startActivity(intent);
            }
        });

        mViewModel = new GoodsActivityViewModel(this, binding,intnetFrom);
        binding.setViewModel(mViewModel);
        mViewModel.init();
    }

    private void initScannerGun(){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ScannerGunWatcher watcher=new ScannerGunWatcher(editText);
        watcher.setOnWatcherListener(new ScannerGunWatcher.ScannerGunCallBack() {
            @Override
            public void callBack(String string) {
                if (mViewModel != null) {
                    mViewModel.setScanResult(string);
                }
            }
        });
        editText.addTextChangedListener(watcher);

        //T1mini扫描
        ImageView scan_iv = (ImageView) findViewById(R.id.scan_iv);
        if ("T1mini".equals(Build.MODEL)){
            scan_iv.setVisibility(View.VISIBLE);
        }else {
            scan_iv.setVisibility(View.GONE);
        }
        scan_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(GoodsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(GoodsActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.permission_request_code);
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
        Intent intent = new Intent(GoodsActivity.this, ScanActivity.class);
        startActivityForResult(intent, Constants.scan_result_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.scan_result_code && null != data){
            String info = data.getStringExtra(Constants.scan_result);
            if (!TextUtil.isEmpty(info) && mViewModel != null) {
                mViewModel.setScanResult(info);
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
    protected void onDestroy() {
        mViewModel.clear();
        mViewModel.reset();
        super.onDestroy();
    }
}
