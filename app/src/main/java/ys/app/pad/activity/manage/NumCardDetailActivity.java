package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityNumCardBinding;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.viewmodel.manage.NumCardDetailViewModel;

public class NumCardDetailActivity extends BaseActivity {

    private ActivityNumCardBinding binding;
    private NumCardDetailViewModel viewModel;
    private EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_num_card_detail);

        viewModel = new NumCardDetailViewModel(this,binding);
        binding.setViewModel(viewModel);
        initPageView();
        Serializable serializable= getIntent().getSerializableExtra(Constants.intent_info);

        if (serializable==null){
            setTitle("新增次卡");
        }else{
            NumCardListInfo info= (NumCardListInfo)serializable;
            setTitle("编辑次卡");
            viewModel.setNumCardInformation(info);
        }
        //listenEditText();
    }
    private void initPageView(){
        setBackVisiable(true);

        Button saveBtn = (Button)findViewById(R.id.blue_save_btn);
        saveBtn.setText("新增");
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.commitData();
            }
        });
//        TextView tv_search= (TextView) findViewById(R.id.search_all_tv);
//        ImageView imageView= (ImageView) findViewById(R.id.add_iv);
//        tv_search.setVisibility(View.GONE);
//        imageView.setVisibility(View.GONE);


//        LinearLayout ll_search= (LinearLayout) findViewById(R.id.search_view);
//        ll_search.setVisibility(View.GONE);
//        et_input = (EditText) findViewById(R.id.input_et);
//        et_input.setVisibility(View.VISIBLE);
//        ImageView iv_delete= (ImageView) findViewById(R.id.delete_text_iv);
//        iv_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_input.setText("");
//            }
//        });
    }

    private void listenEditText(){
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) return;
                String s = editable.toString();
                viewModel.getSearchServiceHttp(s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
