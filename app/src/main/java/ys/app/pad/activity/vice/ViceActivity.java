package ys.app.pad.activity.vice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ys.app.pad.R;
import ys.app.pad.shangmi.screen.utils.ViceConnectUtils;
import ys.app.pad.utils.SpUtil;

public class ViceActivity extends AppCompatActivity {
    private final String TAG = ViceActivity.class.getSimpleName();

    private TextView tv_main;
    private ViceConnectUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice);
        tv_main = (TextView) findViewById(R.id.tv_vice_main);
        tv_main.setText("欢迎光临"+ SpUtil.getShopName());
        utils = ViceConnectUtils.getInstance().setContext(this);
    }

}
