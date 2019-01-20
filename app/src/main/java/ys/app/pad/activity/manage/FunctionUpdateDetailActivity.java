package ys.app.pad.activity.manage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.model.FunctionIntroductionBean;

public class FunctionUpdateDetailActivity extends BaseActivity {
    private TextView tv_update;
    private ImageView iv_instroduction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_update_detail);
        setTitle("更新详情");
        setBackVisiable();
        setBgWhiteStatusBar();
        initData();

    }
    private void initData(){
        tv_update= (TextView) findViewById(R.id.tv_function_update_information);
        iv_instroduction= (ImageView) findViewById(R.id.iv_function_update);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            FunctionIntroductionBean bean= (FunctionIntroductionBean) bundle.getSerializable(Constants.intent_info);
            tv_update.setText(bean.getInstroduction());
            if ("智选E派1.1.9主要更新".equals(bean.getTitle())){
                iv_instroduction.setVisibility(View.VISIBLE);
                iv_instroduction.setImageResource(R.mipmap.icon_20180620);
            }
        }
    }
}
