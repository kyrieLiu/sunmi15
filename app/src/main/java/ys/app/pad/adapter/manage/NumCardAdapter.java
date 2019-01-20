package ys.app.pad.adapter.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greendao.NumCardEntityInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemNumCardBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.itemmodel.NumCardEntityInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.autoview.NumCardParamBean;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class NumCardAdapter extends RecyclerView.Adapter<NumCardAdapter.MyViewHolder> {

    private final Context mContext;

    public List<ServiceInfo> getList() {
        return mList;
    }

    private List<ServiceInfo> mList;

    private OnItemClickListener listener;


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public NumCardAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;


    }

    @Override
    public NumCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNumCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_num_card_service, parent, false);
        return new NumCardAdapter.MyViewHolder(binding,  listener);
    }

    @Override
    public void onBindViewHolder(NumCardAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if (registerI != mList.size()) {
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setList(List<ServiceInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends IViewHolder {

        private ItemNumCardBinding mBinding;
        private final OnItemClickListener mListenner;
        private QueryBuilder qb;
        private NumCardEntityInfoDao numCardEntityInfoDao;
        private RxManager rxManager;
        public MyViewHolder(@NonNull ItemNumCardBinding binding, OnItemClickListener listener) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mListenner = listener;
            numCardEntityInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardEntityInfoDao();
            qb = numCardEntityInfoDao.queryBuilder();
        }

        public void bind(ServiceInfo itemModle, final int position, Context mContext) {

            mBinding.tvItemNumCardName.setText(itemModle.getName());

            listenEditText(itemModle);


            if (mBinding != null) {
                final View root = mBinding.getRoot();
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
        private void listenEditText(final ServiceInfo itemModle){
            mBinding.etItemNumCardNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int num = 0 ;
                    if (!StringUtils.isEmpty(s)){
                        num=Integer.parseInt(s.toString());

                        qb.where(NumCardEntityInfoDao.Properties.Id.eq(itemModle.getId()));
                        NumCardEntityInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardEntityInfoDao();
                        List<NumCardEntityInfo> list = dao.loadAll();
                        if (qb.unique()==null){
                            NumCardEntityInfo unique = new NumCardEntityInfo();
                            unique.setNum(num);
                            unique.setId(itemModle.getId());
                            unique.setName(itemModle.getName());
                            unique.setRealAmt(itemModle.getRealAmt());
                            unique.setType(itemModle.getType());
                            unique.setTypeName(itemModle.getTypeName());
                            numCardEntityInfoDao.insert(unique);
                        }else{
                            NumCardEntityInfo unique = (NumCardEntityInfo) qb.unique();
                            unique.setNum(num);
                            numCardEntityInfoDao.update(unique);
                        }
                    }else{
                        qb.where(NumCardEntityInfoDao.Properties.Id.eq(itemModle.getId()));
                        if (qb.unique()!=null){
                            NumCardEntityInfo entity = (NumCardEntityInfo) qb.unique();
                            numCardEntityInfoDao.delete(entity);
                        }
                    }
                    if (rxManager==null)rxManager=new RxManager();
                    rxManager.post(Constants.type_card,0);

                    //改变展示
                    changeShow(num, itemModle);
                }
            });
        }

        private void changeShow(int num, ServiceInfo itemModle) {
            NumCardEntityInfo entityInfo = new NumCardEntityInfo();
            entityInfo.setNum(num);
            entityInfo.setId(itemModle.getId());
            entityInfo.setName(itemModle.getName());
            entityInfo.setRealAmt(itemModle.getRealAmt());
            entityInfo.setType(itemModle.getType());
            entityInfo.setTypeName(itemModle.getTypeName());
            rxManager.post(Constants.infomations, entityInfo);
        }
    }

}
