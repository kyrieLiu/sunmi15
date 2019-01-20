package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityCheckBinding;
import ys.app.pad.utils.ScannerGunWatcher;
import ys.app.pad.viewmodel.CheckViewModel;

/**
 * Created by aaa on 2017/3/7.
 */

public class CheckActivity extends BaseActivity {

    private ActivityCheckBinding binding;
    private CheckViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check);
        setBackVisiable();
        setTitle("盘点");
        ImageView iv_scan= (ImageView) findViewById(R.id.add_iv);
//        iv_scan.setVisibility(View.VISIBLE);
//        iv_scan.setImageResource(R.mipmap.lansesaomiao);

        mViewModel = new CheckViewModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.init();


        initScannerGun();
        setBgWhiteStatusBar();

    }


    private void initScannerGun(){
       EditText et_saomiaoqiang= (EditText) findViewById(R.id.et_tool_bar_saomiaoqiang);
       et_saomiaoqiang.setVisibility(View.VISIBLE);
        ScannerGunWatcher watcher=new ScannerGunWatcher(et_saomiaoqiang);
        watcher.setOnWatcherListener(new ScannerGunWatcher.ScannerGunCallBack() {
            @Override
            public void callBack(String string) {
                if (mViewModel != null) {
                    mViewModel.setScanResult(string);
                }
            }
        });
        et_saomiaoqiang.addTextChangedListener(watcher);
    }

}
