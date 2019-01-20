package ys.app.pad.activity.appointment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAppointmentMainBinding;
import ys.app.pad.viewmodel.appointment.AppointmentMainViewModel;


public class AppointmentMainActivity extends BaseActivity {
    private ActivityAppointmentMainBinding binding;
    private AppointmentMainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_appointment_main);

        setBackVisiable();
        setTitle("预约");

        TextView addTV = (TextView) findViewById(R.id.sreach_tv);
        addTV.setVisibility(View.VISIBLE);
        addTV.setText("添加");
        addTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format=new SimpleDateFormat("MM月dd日");
                String today=format.format(new Date());
                Intent intent=new Intent(AppointmentMainActivity.this,AddAppointmentActivity.class);
                intent.putExtra("date",today);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            }
        });
        viewModel = new AppointmentMainViewModel(binding,this);
        binding.setViewModel(viewModel);
        setBgWhiteStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
