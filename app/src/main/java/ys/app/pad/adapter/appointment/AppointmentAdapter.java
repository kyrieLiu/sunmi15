package ys.app.pad.adapter.appointment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemAppointmentBinding;
import ys.app.pad.itemmodel.AppointmentBean;
import ys.app.pad.itemmodel.AppointmentItemModel;

/**
 * Created by liuyin on 2017/9/11.
 */

public class AppointmentAdapter  extends RecyclerView.Adapter<AppointmentAdapter.MyViewHodler>{
    private List<AppointmentBean> mList;
    private Context fragment;
    private boolean appointmentWaiting;
    private boolean appointmentFailer;
    private boolean appointmentAready;
    private OnItemClickListener listener;

    public AppointmentAdapter(Context fragment){
        this.mList=new ArrayList<>();
        this.fragment=fragment;
    }
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public List<AppointmentBean> getList() {
        return mList;
    }

    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_appointment,parent,false);
        return new MyViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        holder.bind(mList.get(position),position,listener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setAppointmentWaiting(boolean appointmentWaiting){
        this.appointmentWaiting=appointmentWaiting;
    }
    public void setAppointmentFailer(boolean appointmentFailer){
        this.appointmentFailer=appointmentFailer;
    }
    public void setAppointmentAready(boolean appointmentAready){
        this.appointmentAready=appointmentAready;
    }

    public void setList(int startHttp, List<AppointmentBean> list) {
        if( 0 == startHttp){
            mList.clear();
        }

        int positionStart = mList.size();
        int itemCount = list.size();
        mList.addAll(list);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }

    }

     class MyViewHodler extends RecyclerView.ViewHolder{
         ItemAppointmentBinding binding;
        public MyViewHodler(ItemAppointmentBinding binding) {
            super(binding.rootView);
            this.binding=binding;
        }
        public void bind(AppointmentBean bean, final int position, final OnItemClickListener mListenner){

            AppointmentItemModel model;
            if (binding.getItemViewModel()==null){
                model=new AppointmentItemModel(bean,fragment,appointmentAready,appointmentWaiting,appointmentFailer);
                model.setModel(bean);
                binding.setItemViewModel(model);
            }else{
                model=binding.getItemViewModel();
                model.setModel(bean);
            }
            if (bean.getVipUserId()>0){
                binding.tvItemAppointmentIsVip.setText("会员");
            }else{
                binding.tvItemAppointmentIsVip.setText("非会员");
            }
            int age=bean.getPetAge();
            binding.tvItemAppointmentAge.setText(age+"岁");
            final View root = binding.getRoot();
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListenner != null) {
                        mListenner.onItemClick(root, position);
                    }
                }
            });
        }
    }
}
