package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityServiceBinding;
import ys.app.pad.viewmodel.ServiceActivityViewModel;

/**
 * Created by admin on 2017/6/7.
 */

public class ServiceActivity extends BaseActivity {
    private ActivityServiceBinding binding;
    private ServiceActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service);
        setBackVisiable();
        final int searchFormIntent = getIntent().getIntExtra(Constants.intent_search_from, -1);
        TextView tv_title= (TextView) findViewById(R.id.title_tv);
        if (Constants.intent_form_fuwu_kucunliebiao==searchFormIntent){
            tv_title.setText("服务");
        }else if (Constants.intent_form_fuwu_cuxiaoliebiao==searchFormIntent){
            tv_title.setText("服务促销");
        }else if (Constants.intent_form_fuwu_cuxiaoshezhi==searchFormIntent){
            tv_title.setText("服务促销设置");
        }

        TextView searchAllTv = (TextView) findViewById(R.id.search_all_tv);
        searchAllTv.setHint("搜索服务");

        searchAllTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, SearchActivity.class);
                intent.putExtra(Constants.intent_search_type, Constants.intent_search_type_service);
                intent.putExtra(Constants.intent_search_from, searchFormIntent);
                startActivity(intent);
            }
        });
        ImageView iv_add= (ImageView) findViewById(R.id.add_iv);
        iv_add.setVisibility(View.GONE);
        mViewModel = new ServiceActivityViewModel(this, binding, searchFormIntent);
        binding.setViewModel(mViewModel);
    }

    @Override
    protected void onDestroy() {
        mViewModel.clear();
        super.onDestroy();
    }
}
