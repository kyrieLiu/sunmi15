package ys.app.pad.activity.vice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;

/**
 * Created by liuyin on 2018/4/18.
 */

public class ViceWelcomeActivity extends Activity{
    private TextView tv_main;
    private static final String TAG = "ViceWelcomeActivity";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice);
        tv_main = (TextView) findViewById(R.id.tv_vice_main);
        Log.d(TAG,"taskID=="+getTaskId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"onNewIntent=="+intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_main.setText("欢迎光临"+ SpUtil.getShopName());
    }
}
