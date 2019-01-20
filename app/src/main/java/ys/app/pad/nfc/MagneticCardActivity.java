package ys.app.pad.nfc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;

/**
 * 读取magnetic磁条卡信息
 * 
 * @author TIANHUI
 * 
 */
public class MagneticCardActivity extends BaseActivity  {

	private String intentFlag;
	private int vipFlag;//区别次卡和会员卡
	private LinearLayout ll_magnetic;
	private ImageView iv_magnetic;
	private LinearLayout ll_nfc;
	private ImageView iv_nfc;
	private MagneticFragment magneticFragment;
	private NFCFraggment nfcFraggment;
	private FragmentTransaction ft;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private boolean isNFC=false;
	private TextView tv_card_magnetic,tv_card_nfc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_normal);
		setBackVisiable();
		ll_magnetic= (LinearLayout) findViewById(R.id.ll_card_magnetic);
		iv_magnetic= (ImageView) findViewById(R.id.iv_card_magnetic);
		iv_nfc= (ImageView) findViewById(R.id.iv_card_nfc);
		ll_nfc= (LinearLayout) findViewById(R.id.ll_card_nfc);

		tv_card_magnetic = (TextView) findViewById(R.id.tv_card_magnetic);
		tv_card_nfc = (TextView) findViewById(R.id.tv_card_nfc);

		intentFlag = getIntent().getStringExtra(Constants.intent_flag);
		vipFlag=getIntent().getIntExtra(Constants.intent_vip_name,0);
		if (vipFlag==0){
			setTitle("会员卡");
		}else{
			setTitle("次卡");
		}
		iv_magnetic.setSelected(true);
		iv_nfc.setSelected(false);
		ll_magnetic.setEnabled(false);
		ll_nfc.setEnabled(true);

		tv_card_magnetic.setSelected(true);
		tv_card_magnetic.setTextColor(getResources().getColor(R.color.white));
		tv_card_nfc.setSelected(false);
		tv_card_nfc.setTextColor(getResources().getColor(R.color.blue));


		initView();
		listenView();//监听状态
	}
	private void initView(){
		Bundle bundle=new Bundle();
		bundle.putString(Constants.intent_flag,intentFlag);
		bundle.putInt(Constants.intent_vip_name,vipFlag);
		magneticFragment = new MagneticFragment();
		magneticFragment.setArguments(bundle);
		nfcFraggment = new NFCFraggment();
		nfcFraggment.setArguments(bundle);
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		transaction.replace(R.id.fl_nfc_container, magneticFragment);
		transaction.commit();
	}

	private void listenView(){
		ll_magnetic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_magnetic.setSelected(true);
				iv_nfc.setSelected(false);
				ll_magnetic.setEnabled(false);
				ll_nfc.setEnabled(true);

				tv_card_magnetic.setSelected(true);
				tv_card_magnetic.setTextColor(getResources().getColor(R.color.white));
				tv_card_nfc.setSelected(false);
				tv_card_nfc.setTextColor(getResources().getColor(R.color.blue));

				replaceFragment(magneticFragment);
				isNFC=false;
			}
		});
		ll_nfc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iv_magnetic.setSelected(false);
				iv_nfc.setSelected(true);
				ll_magnetic.setEnabled(true);
				ll_nfc.setEnabled(false);

				tv_card_magnetic.setSelected(false);
				tv_card_magnetic.setTextColor(getResources().getColor(R.color.blue));
				tv_card_nfc.setSelected(true);
				tv_card_nfc.setTextColor(getResources().getColor(R.color.white));

				replaceFragment(nfcFraggment);
				isNFC=true;
			}
		});
	}
	private void replaceFragment(Fragment fragment){
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fl_nfc_container,fragment);
		transaction.commit();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onNewIntent(Intent intent) {
		if (isNFC){
			nfcFraggment.onNewIntent(intent);
		}

		super.onNewIntent(intent);
	}

}
