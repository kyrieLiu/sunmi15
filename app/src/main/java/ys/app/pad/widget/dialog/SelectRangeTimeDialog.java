package ys.app.pad.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ys.app.pad.R;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.timeselector.TimeSelector;

/**
 * Created by liuyin on 2018/3/28.
 */

public class SelectRangeTimeDialog extends Dialog implements View.OnClickListener{
    private Activity mActivity;
    private String mStartTime;
    private String mEndTime;
    private String startTime,endTime;
    private TextView tv_start;
    private TextView tv_end;

    public SelectRangeTimeDialog( Activity context) {
        super(context,R.style.ThemeCustomDialog);
        this.mActivity=context;
        setContentView(R.layout.dialog_select_achivement_time);
        setCanceledOnTouchOutside(false);
        initView();
    }
    private void initView(){
        tv_start = (TextView) findViewById(R.id.tv_dialog_rangeTime_start);
        tv_end = (TextView) findViewById(R.id.tv_dialog_rangeTime_end);
        Button bt_query= (Button) findViewById(R.id.bt_dialog_rangeTime_query);
       tv_start.setOnClickListener(this);
       tv_end.setOnClickListener(this);
       bt_query.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_rangeTime_start:
                mStartTime = DateUtil.getPreviousSixMonth();
                mEndTime = DateUtil.longFormatDate2(System.currentTimeMillis());
                TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                    startTime =time;
                    tv_start.setText(startTime);
                    }
                }, "2017-01-01 00:00:00", mEndTime);
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
                break;
            case R.id.tv_dialog_rangeTime_end:
                mStartTime = DateUtil.getPreviousSixMonth();
                mEndTime = DateUtil.longFormatDate2(System.currentTimeMillis());
                TimeSelector timeSelectorEnd = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        endTime=time;
                        tv_end.setText(endTime);
                    }
                }, "2017-01-01 00:00:00", mEndTime);
                timeSelectorEnd.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelectorEnd.show();
                break;
            case R.id.bt_dialog_rangeTime_query:
                sendData();
                break;
        }
    }
    private void sendData(){
        if (StringUtils.isEmpty(startTime)){
            Toast.makeText(mActivity,"请选择开始时间",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(endTime)){
            Toast.makeText(mActivity,"请选择结束时间",Toast.LENGTH_SHORT).show();
            return;
        }
        if (listener!=null)listener.query(startTime,endTime);

    }
    private QueryClickListener listener;
    public void setOnQueryClickListener(QueryClickListener listener){
        this.listener=listener;
    }

    public interface QueryClickListener{
        void query(String startTime,String endTime);
    }

}
