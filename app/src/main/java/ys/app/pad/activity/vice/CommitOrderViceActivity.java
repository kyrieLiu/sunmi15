package ys.app.pad.activity.vice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.adapter.ShoppingCarViceAdapter;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;


public class CommitOrderViceActivity extends AppCompatActivity {
    private final String TAG = CommitOrderViceActivity.class.getSimpleName();
    private String orderData;
    private ImageView iv;
    private RecyclerView recyclerView;
    private TextView tv_total,tv_favorable,tv_number,tv_receivable,tv_shopName;
    private ShoppingCarViceAdapter mGoodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ------------>");
        setContentView(R.layout.activity_order_list_vice);

        recyclerView= (RecyclerView) findViewById(R.id.rv_commit_order_vice);
        tv_total= (TextView) findViewById(R.id.tv_order_list_vice_total);
        tv_favorable= (TextView) findViewById(R.id.tv_order_list_vice_favorable);
        tv_number= (TextView) findViewById(R.id.tv_order_list_vice_number);
        tv_receivable= (TextView) findViewById(R.id.tv_order_list_vice_receivable);
        tv_shopName= (TextView) findViewById(R.id.tv_orderMenu_shopName);
        mGoodsAdapter = new ShoppingCarViceAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mGoodsAdapter);
        orderData = getIntent().getStringExtra("orderData");
        initView();

    }

    private void initView(){
        tv_shopName.setText(SpUtil.getShopName());
        Gson gson=new Gson();
        List<SummitOrderInfo> orderInfoList=gson.fromJson(orderData, new TypeToken<List<SummitOrderInfo>>() {}.getType());
        mGoodsAdapter.setList(orderInfoList);
        BigDecimal total=new BigDecimal(0);
        BigDecimal receivable=new BigDecimal(0);
        int number=0;
        for (SummitOrderInfo info:orderInfoList){
            total = add(total, info.getCountMoney());
            receivable=add(receivable,info.getRealMoney());
            number+=info.getCount();
        }
        double totalMoney=total.doubleValue();
        double receiveMoney=receivable.doubleValue();
        double favorable=subtract(totalMoney,receiveMoney);
        tv_total.setText("总计: "+AppUtil.formatStandardMoney(totalMoney)+"元");
        tv_favorable.setText("优惠: "+AppUtil.formatStandardMoney(favorable)+"元");
        tv_number.setText("数量: "+number);
        tv_receivable.setText("应收: "+ AppUtil.formatStandardMoney(receiveMoney)+"元");
    }

    private BigDecimal add(BigDecimal b1, double v2) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    private double subtract(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ---------------->");
        orderData = intent.getStringExtra("orderData");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ------------->");
    }

}
