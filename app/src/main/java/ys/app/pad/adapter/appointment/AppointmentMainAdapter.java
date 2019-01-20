package ys.app.pad.adapter.appointment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.activity.appointment.AppointmentMainActivity;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.AppointmentMainItemModelBinding;
import ys.app.pad.itemmodel.AppointmentMainItemModel;
import ys.app.pad.model.AppointmentMainBean;


/**
 * Created by liuyin on 2017/9/12.
 */

public class AppointmentMainAdapter extends RecyclerView.Adapter<AppointmentMainAdapter.MyViewHolder>{
    private List<AppointmentMainBean> list;
    private AppointmentMainActivity mActivity;
    private static OnItemClickListener listener;
    public AppointmentMainAdapter(AppointmentMainActivity mActivity){
        this.list=new ArrayList<>();
        this.mActivity=mActivity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppointmentMainItemModelBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_appointment_main,parent,false);
        return new MyViewHolder(binding);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public List<AppointmentMainBean> getList() {
        return list;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bind(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppointmentMainBean> mList){
        this.list.clear();
        list.addAll(mList);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private AppointmentMainItemModelBinding binding;
        public MyViewHolder(AppointmentMainItemModelBinding binding) {
            super(binding.rootView);
            this.binding=binding;
        }
        public void bind(AppointmentMainBean bean, final int position){
            AppointmentMainItemModel itemModel;
            if (binding.getItemViewModel()==null){
                itemModel=new AppointmentMainItemModel(bean);
                binding.setItemViewModel(itemModel);
            }else{
                itemModel=binding.getItemViewModel();
                itemModel.setModel(bean);
            }
            if (binding!=null){
                final View root = binding.getRoot();
                binding.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            listener.onItemClick(root, position);
                        }
                        //mActivity.turnToActivity(AppointmentActivity.class);
                    }
                });
            }

        }
    }
}
