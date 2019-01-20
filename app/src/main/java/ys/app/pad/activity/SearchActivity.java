package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivitySearchBinding;
import ys.app.pad.viewmodel.SearchViewModel;

/**
 * Created by admin on 2017/6/8.
 */

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setBackVisiable();
        LinearLayout searchLl = (LinearLayout) findViewById(R.id.search_view);
        searchLl.setVisibility(View.VISIBLE);
        TextView typeTv = (TextView) findViewById(R.id.type_tv);
        EditText inputEt = (EditText) findViewById(R.id.input_et);
        ImageView deleteTextIv = (ImageView) findViewById(R.id.delete_text_iv);
        ImageView iv_card= (ImageView) findViewById(R.id.iv_tool_card);
        Intent intent=getIntent();
        int searchTypeExtra = intent.getIntExtra(Constants.intent_search_type, -1);
        int searchFromExtra = intent.getIntExtra(Constants.intent_search_from, -1);
        int vipFlag=intent.getIntExtra(Constants.intent_flag,0);
        int numCardID=intent.getIntExtra(Constants.intent_vip_card_no,-1);
        mViewModel = new SearchViewModel(this, binding,iv_card,typeTv,inputEt,deleteTextIv,searchTypeExtra,searchFromExtra,vipFlag,numCardID);
        binding.setViewModel(mViewModel);
        setBgWhiteStatusBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
