package ys.app.pad.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.utils.AppUtil;

/**
 * Created by aaa on 2017/3/16.
 */
public class RefundMoneyResultActivity extends BaseActivity {

    private TextView tv_money;
    private TextView tv_type;
    private TextView tv_no;
    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_refound_money_result);
        setBackVisiable();
        setTitle("退款结果");

        tv_money= (TextView) findViewById(R.id.tv_refound_money);
        tv_type= (TextView) findViewById(R.id.tv_refound_type);
        tv_no= (TextView) findViewById(R.id.tv_refound_no);

        Intent intent = getIntent();
        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.intent_info);

        setData();

    }
    private void setData(){
        tv_money.setText(orderInfo.getRealAmt() + "");
        tv_type.setText(AppUtil.getPayType(orderInfo.getPayedMethod()));
        tv_no.setText(orderInfo.getOrderNo());

        if ("D1".equals(Build.MODEL)){
            BlueToothPrintUtil.getInstance().printRefoundInformation(orderInfo);
        }else{
            AidlUtil.getInstance().printRefoundInformation(orderInfo);
        }
    }
    public void confirmClick(View view){

        finish();
    }
    public void printAgain(View view){
        if ("D1".equals(Build.MODEL)){
            BlueToothPrintUtil.getInstance().printRefoundInformation(orderInfo);
        }else{
            AidlUtil.getInstance().printRefoundInformation(orderInfo);
        }

    }
}
