package ys.app.pad.activity.manage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;


public class TicketManageActivity extends BaseActivity {
    private ImageView iv_receipt_vip;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_ticket_manage);
        setBackVisiable();
        setTitle("小票管理");
        setBgWhiteStatusBar();
        initView();

    }
    private void initView(){
        linearLayout= (LinearLayout) findViewById(R.id.ll_ticket_setting);
        iv_receipt_vip= (ImageView) findViewById(R.id.iv_setting_receipt_print);
        if (SpUtil.getReceiptPrinterVip()){
            iv_receipt_vip.setImageResource(R.mipmap.setting_switch_on);
        }else{
            iv_receipt_vip.setImageResource(R.mipmap.setting_switch_off);
        }
        iv_receipt_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpUtil.getReceiptPrinterVip()){
                    SpUtil.setReceiptPrinterVip(false);
                    iv_receipt_vip.setImageResource(R.mipmap.setting_switch_off);
                }else{
                    SpUtil.setReceiptPrinterVip(true);
                    iv_receipt_vip.setImageResource(R.mipmap.setting_switch_on);
                }
            }
        });
    }
    public void clickSeeTemplate(View view){
        linearLayout.setVisibility(View.VISIBLE);
    }
}
