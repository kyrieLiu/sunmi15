package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityNumCardListBinding;
import ys.app.pad.viewmodel.manage.NumCardViewModel;

/**
 * Created by aaa on 2017/3/16.
 */

public class NumCardActivity extends BaseActivity {
    
    private ActivityNumCardListBinding binding;
    private NumCardViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_num_card_list);
        setBackVisiable();
        setTitle("次卡");
        ImageView addTv = (ImageView) findViewById(R.id.add_iv);
        addTv.setVisibility(View.VISIBLE);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToActivity(NumCardDetailActivity.class);
            }
        });
        mViewModel = new NumCardViewModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        setBgWhiteStatusBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
