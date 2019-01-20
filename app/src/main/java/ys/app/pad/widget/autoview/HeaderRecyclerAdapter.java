package ys.app.pad.widget.autoview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greendao.ServiceTypeInfoDao;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.itemmodel.NumCardEntityInfo;
import ys.app.pad.model.ServiceTypeInfo;


/**
 * Created by WBJ on 2018/3/30 15:07.
 */

public class HeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_CONTENT = 1;
    public static final int TYPE_FOOTER = 2;
    public static final String TAG = HeaderRecyclerAdapter.class.getSimpleName();

    private List<ServiceTypeInfo> typeInfos;

    List<NumCardParamBean> list;
    Context context;

    public HeaderRecyclerAdapter(@Nullable List<NumCardParamBean> list, Context context) {
        if (null == list || list.size() <= 0){
            this.list = new ArrayList<>();
        }else {
            this.list = list;
        }
        this.context = context;
        ServiceTypeInfoDao infoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
        typeInfos =  infoDao.loadAll();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i(TAG,"执行onCreateViewHolder()方法");
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case TYPE_HEAD:
                View view = inflater.inflate(R.layout.item_header,parent,false);
                ViewHeadHolder viewHeadHolder = new ViewHeadHolder(view);
                return viewHeadHolder;
            case TYPE_CONTENT:
                View view1 = inflater.inflate(R.layout.item_content,parent,false);
                ViewContentHolder viewContentHolder = new ViewContentHolder(view1);
                return viewContentHolder;
            case TYPE_FOOTER:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.i(TAG,"执行onBindViewHolder()方法");
        int count = 0;
        for (int parent=0;parent<list.size();parent++){
            NumCardParamBean bean = list.get(parent);
            List<NumCardEntityInfo> lis = bean.getLis();
            if (count == position){
                ViewHeadHolder headHolder = (ViewHeadHolder)holder;
                headHolder.textView_head.setText(bean.getHeader() + context.getString(R.string.maohao));
                headHolder.setParentPosition(parent);
                headHolder.setRealPosition(position);
            }
            count++;
            for (int child=0;child<lis.size();child++){
                if (count == position) {
                    ViewContentHolder contentHolder = (ViewContentHolder) holder;
                    contentHolder.textView_content.setText(lis.get(child).getName()+"  "+lis.get(child).getNum()+"次");
                    //将父节点和子节点存起来
                    contentHolder.setParentPosition(parent);
                    contentHolder.setChildPosition(child);
                    //将展示真实节点存起来
                }
                count++;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//        Log.i(TAG,"执行getItemViewType()方法");
        int count = 0;
        for (int i=0;i<list.size();i++){
            NumCardParamBean bean = list.get(i);
            List<NumCardEntityInfo> lic = bean.getLis();
            if (count == position){
                return TYPE_HEAD;
            }
            count++;
            for (NumCardEntityInfo str :lic){
                if (position == count){
                    return TYPE_CONTENT;
                }
                count++;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
//        Log.i(TAG,"执行getItemCount()方法");
        int count = list.size();
        for (int i =0; i<list.size();i++ ){
            for (int j = 0;j < list.get(i).getLis().size();j++){
                count++;
            }
        }
        return count;
    }

    public  List<NumCardParamBean> getList(){
        return list;
    }

    public void setList(NumCardEntityInfo data) {
        boolean isAdded = false;

        if (list !=null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                NumCardParamBean bean = list.get(i);

                //先判断是否存在item,如果已存在怎执行完毕一定要跳出
                if (data.getTypeName().equals(bean.getHeader())){
                    List<NumCardEntityInfo> entityInfoList = bean.getLis();
                    for (int j = 0; j < entityInfoList.size(); j++) {
                        NumCardEntityInfo entityInfo =  entityInfoList.get(j);
                        if (data.getId() == entityInfo.getId()){
                            entityInfo.setNum(data.getNum());//将数据中的num重新设置即可
                            isAdded = true;
                        }
                    }
                    if (!isAdded) {
                        entityInfoList.add(data);
                        isAdded = true;
                    }
                    break;
                }
            }
        }

        if (!isAdded){
            for (int i=0;i<typeInfos.size();i++) {
                if (typeInfos.get(i).getId() == data.getType()){
                    NumCardParamBean bean = new NumCardParamBean();
                    bean.setHeader(data.getTypeName());
                    List<NumCardEntityInfo> itemList = new ArrayList<>();
                    itemList.add(data);
                    bean.setLis(itemList);
                    list.add(bean);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeData(int parentPosition,int childPosition,int realPosition) {
        //通知数据进行改变
        List<NumCardEntityInfo> lis = list.get(parentPosition).getLis();
        lis.remove(childPosition);
        if (lis == null || lis.size() <= 0){
            list.remove(parentPosition);            //当父布局中的子布局已经不存在，则父布局也没有存在的biyao
        }

        //通知显示进行调整
        notifyItemRemoved(realPosition);
        notifyDataSetChanged();
    }

    public class ViewHeadHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView textView_head;
        private int parentPosition;
        private int realPosition;

        public ViewHeadHolder(View rootView) {
            super(rootView);
            Log.i(TAG,"执行onCreateViewHolder()方法");
            this.rootView = rootView;
            this.textView_head = (TextView) rootView.findViewById(R.id.title_tv);
        }

        public int getParentPosition() {
            return parentPosition;
        }

        public void setParentPosition(int parentPosition) {
            this.parentPosition = parentPosition;
        }

        public int getRealPosition() {
            return realPosition;
        }

        public void setRealPosition(int realPosition) {
            this.realPosition = realPosition;
        }

    }

    public class ViewContentHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView textView_content;
        public ImageView delete_iv;
        private int parentPosition;
        private int childPosition;
        private int realPosition;

        public ViewContentHolder(View rootView) {
            super(rootView);
            Log.i(TAG,"执行onCreateViewHolder()方法");
            this.rootView = rootView;
            this.textView_content = (TextView) rootView.findViewById(R.id.content_tv);
            this.delete_iv = (ImageView)rootView.findViewById(R.id.delete_iv);
            this.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,parentPosition,childPosition,realPosition);
                }
            });
        }

        public int getRealPosition() {
            return realPosition;
        }

        public void setRealPosition(int realPosition) {
            this.realPosition = realPosition;
        }

        public int getParentPosition() {
            return parentPosition;
        }

        public void setParentPosition(int parentPosition) {
            this.parentPosition = parentPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }

        public void setChildPosition(int childPosition) {
            this.childPosition = childPosition;
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(View view, int parentPosition, int childPosition, int realPosition);
    }
}
