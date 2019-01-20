package ys.app.pad.pad_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.model.GoodInfo;

public class GoodLoadActivity extends AppCompatActivity {
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_load);
        gridView= (GridView) findViewById(R.id.gridView);
        List<GoodInfo> list=new ArrayList<>();
        for (int i=0;i<100;i++){
            GoodInfo info=new GoodInfo();
            info.setRealAmt(52);
            list.add(info);
        }
        GoodsLoadAdapter adapter=new GoodsLoadAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setList(list);
    }
}
