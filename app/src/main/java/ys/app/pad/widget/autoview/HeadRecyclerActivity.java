package ys.app.pad.widget.autoview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;


public class HeadRecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Bean> list;
    EditText insertET;
   HeaderRecyclerAdapter adapter;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String result = s.toString().trim();
            if (TextUtils.isEmpty(result) || 0 ==  Integer.valueOf(result) ){
                return;
            }
            //TODO 插入操作
            Bean bean = new Bean();
            bean.setHeader((int)(Math.random()*10) + "+head");
            List<String> lic = new ArrayList<>();
            for (int i = 0; i<4; i++){
                lic.add(bean.getHeader()+"==" + i);
            }
            bean.setDataList(lic);
//            adapter.setList(bean);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        insertET = (EditText)findViewById(R.id.insert_et);
        insertET.addTextChangedListener(watcher);

        list = new ArrayList<>();
        for (int i = 0; i <5; i++) {
            Bean bean = new Bean();
            List<String> lis = new ArrayList<>();
            for (int j = 0; j <5; j++) {
                lis.add("==" + j);
            }
            bean.setDataList(lis);
            bean.setHeader(i + "+head");
            bean.setFooter(i + "footer");
            list.add(bean);
        }


//       adapter = new HeaderRecyclerAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                int SpanSize = 0;
                if (adapter.getItemViewType(position) == adapter.TYPE_HEAD){
                    return SpanSize = 2;                                            //通过判断即可知道哪个跨行
                }else {
                    return SpanSize = 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
        adapter.setListener(new HeaderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int parentPosition, int childPosition, int realPosition) {
                adapter.removeData(parentPosition,childPosition,realPosition);
            }
        });
    }

    public class Bean {
        private String header;
        private String footer;
        private List<String> lis;

        public List<String> getLis() {
            return lis;
        }

        public String getHeader() {
            return header;
        }

        public String getFooter() {
            return footer;
        }

        public void setDataList(List<String> lis) {
            this.lis = lis;
        }

        public void setHeader(String s) {
            this.header = s;
        }

        public void setFooter(String s) {
            this.footer = s;
        }
    }
}
