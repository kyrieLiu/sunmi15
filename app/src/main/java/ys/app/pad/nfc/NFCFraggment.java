package ys.app.pad.nfc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ys.app.pad.Constants;
import ys.app.pad.R;

/**
 * Created by liuyin on 2017/9/30.
 */

public class NFCFraggment extends Fragment {

    private TextView stapInfoTv;
    private NfcAdapter nfcAdapter;

    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    private IsoDep na;
    private View view;
    private String intentFlag;
    private VipCardModel vipCardModel;
    private int vipFlag;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nfc,null);
        Bundle bundle=getArguments();
        intentFlag = bundle.getString(Constants.intent_flag);
        vipFlag=bundle.getInt(Constants.intent_vip_name);
        vipCardModel = new VipCardModel(this,intentFlag,vipFlag);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stapInfoTv = (TextView)view.findViewById(R.id.test_content_stap_info);
        setTestRange();

        init();
    }

    @SuppressLint("NewApi")
    public void init() {
        if (Build.VERSION.SDK_INT < 10) {
            return;
        }
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        } catch (Exception e1) {
            e1.printStackTrace();
            nfcAdapter = null;
            showMsgDialog(e1.getMessage());
            return;
        }
        // 判断2
        if (nfcAdapter == null) {
            // 如果手机不支持NFC，或者NFC没有打开就直接返回
            Log.d(this.getClass().getName(), "手机不支持NFC功能！");
            showMsgDialog("设备不支持NFC！");
            return;
        }

        // 三种Activity NDEF_DISCOVERED ,TECH_DISCOVERED,TAG_DISCOVERED
        // 指明的先后顺序非常重要， 当Android设备检测到有NFC Tag靠近时，会根据Action申明的顺序给对应的Activity
        // 发送含NFC消息的 Intent.
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] { ndef, tech, tag };
        mTechLists = new String[][] { new String[] { Ndef.class.getName(), MifareClassic.class.getName(),
                NfcA.class.getName(),NfcB.class.getName(),NfcV.class.getName(),NfcF.class.getName() } };

        if (!nfcAdapter.isEnabled()) {
            Log.d(this.getClass().getName(), "手机NFC功能没有打开！");
            enableDialog(getActivity());
            return;
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            mPendingIntent = PendingIntent.getActivity(getActivity(), 0,
                    new Intent(getActivity(), getActivity().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            nfcAdapter.enableForegroundDispatch(getActivity(), mPendingIntent, mFilters, mTechLists);
        }
    }

    /*
     *
     * @see android.app.Activity#onPause()
     */
    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(getActivity());
        }
    }

    public void onNewIntent(Intent intent) {
        if (isEnabled()) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            try {
                na = IsoDep.get(tag);
                //na.connect();
                //在这里可以进行nfc交互处理
                updateLogInfo(HEX.bytesToHex(tag.getId()));
            }catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"查不到相关数据",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(),"NFC不可用",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEnabled() {
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }

    private void enableDialog(final Context context) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle("提醒");
        ab.setMessage("手机NFC开关未打开，是否现在去打开？");
        ab.setNeutralButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.setNegativeButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                dialog.dismiss();
            }
        });
        ab.create().show();
    }

    private void showMsgDialog(String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("提醒");
        ab.setMessage(msg);
        ab.setNeutralButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ab.create().show();
    }

    private void updateLogInfo(String msg) {
        if ("huiyuan".equals(intentFlag)){
            vipCardModel.getVip(msg);
        }else{
            Intent intent=new Intent();
            intent.putExtra("cardNo",msg);
            getActivity().setResult(10,intent);
            getActivity().finish();
        }
    }

    /**
     * 设置当前检测进度
     */
    private void setTestRange() {
        StringBuilder sb = new StringBuilder();
        sb.append("NFC读卡:\n");
        sb.append("1、NFC卡放在设备头部带有NFC标志处。\n");
        sb.append("2、观察是否能够获取NFC卡号信息。\n");

        stapInfoTv.setText(sb.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vipCardModel.clear();
    }
}
