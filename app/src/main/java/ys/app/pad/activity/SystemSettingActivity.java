package ys.app.pad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.activity.manage.TicketManageActivity;

public class SystemSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        setTitle("系统设置");
        setBackVisiable();
        setBgWhiteStatusBar();

    }
    public void clickReceiptPrinterVip(View view){
        Intent intent=new Intent(this, TicketManageActivity.class);
        startActivity(intent);
    }
}
