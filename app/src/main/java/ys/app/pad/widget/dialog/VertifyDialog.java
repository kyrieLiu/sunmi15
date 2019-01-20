package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.adapter.manage.EmployeeAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.model.AllotRecordInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.widget.wrapperRecylerView.DividerItemDecoration;


/**
 * Created by aaa on 2017/3/20.
 */

public class VertifyDialog extends Dialog{

    private Context context;

    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_number;
    private TextView tv_time;
    private TextView tv_user;
    private LinearLayout ll_employee;
    private Button bt_cancel;
    private Button bt_confirm;

    private List<EmployeeInfo> mEmployeeInfos;
    private View mContentView;
    private EmployeeInfo mEmployeeInfo;
    private PopupWindow mPop;

    private OnConfirmClickListener listener;

    private AllotRecordInfo info;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.CENTER); //显示在底部

//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
//        getWindow().setAttributes(p);
    }

    public VertifyDialog(Context context) {
        super(context, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.dialog_check_allot);
        this.context = context;
        initView();
    }
    private void initView(){
        tv_name= (TextView) findViewById(R.id.tv_dialog_allot_name);
        tv_type= (TextView) findViewById(R.id.tv_dialog_allot_type);
        tv_number= (TextView) findViewById(R.id.tv_dialog_allot_number);
        tv_time= (TextView) findViewById(R.id.tv_dialog_allot_time);
        tv_user= (TextView) findViewById(R.id.tv_dialog_allot_user);
        ll_employee= (LinearLayout) findViewById(R.id.ll_check_allot_user);
        bt_cancel= (Button) findViewById(R.id.bt_dialog_allot_cancel);
        bt_confirm= (Button) findViewById(R.id.bt_dialog_allot_confirm);
        getEmployeeListFromDb();
        tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmployeeInfo==null){
                    Toast.makeText(context,"请选择审核人",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.clickCheckConfirm(info,mEmployeeInfo.getId(),mEmployeeInfo.getName());
            }
        });
    }
    public void setModel(AllotRecordInfo info){
        this.info=info;
        tv_name.setText("商品名称 : "+info.getCommodityName());
        tv_type.setText("商品类型 : "+info.getCommodityTypeName());
        tv_number.setText("数量 : "+info.getNum());
        tv_time.setText("时间 : "+ AppUtil.getTimes(info.getDotime()));
    }
    private void getEmployeeListFromDb() {
        mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
        // 一个自定义的布局，作为显示的内容
        mContentView = LayoutInflater.from(context).inflate(R.layout.pop_window, null);
        // 设置按钮的点击事件
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
        EmployeeAdapter adapter = new EmployeeAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division_line));
        recyclerView.setAdapter(adapter);
        adapter.setList(mEmployeeInfos);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mEmployeeInfo = mEmployeeInfos.get(position);
                tv_user.setText(mEmployeeInfo.getName());
                if (mPop != null) {
                    mPop.dismiss();
                }
            }
        });
    }

    private void showPop() {
        mPop = new PopupWindow(mContentView, ll_employee.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_pop_bg);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPop.setBackgroundDrawable(drawable);
        mPop.showAsDropDown(ll_employee);
    }

    public interface OnConfirmClickListener{
        void clickCheckConfirm(AllotRecordInfo info, long userID, String userName);
    }
    public void setListener(OnConfirmClickListener listener){
        this.listener=listener;
    }

}
