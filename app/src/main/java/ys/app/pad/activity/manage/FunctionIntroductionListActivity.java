package ys.app.pad.activity.manage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.adapter.manage.FunctionInstroductionAdapter;

/**
 * Created by lyy on 2017/7/3.
 */

public class FunctionIntroductionListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_function_introduction_list);
        setBackVisiable();
        setTitle("功能介绍");
        setBgWhiteStatusBar();
        initView();
    }
    private void initView(){
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.rv_function_introduction);
        FunctionInstroductionAdapter adapter=new FunctionInstroductionAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
