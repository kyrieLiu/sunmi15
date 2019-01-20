package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greendao.EmployeeInfoDao;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.model.CommitOrderTempInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.dialog.UnitPriceDialog;

/**
 * Created by liuyin on 2017/12/5.
 */

public class CollectShopCarAdapter extends RecyclerView.Adapter<CollectShopCarAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater mInflater;
    private OnShopCarClickListener listener;
    private List<CommitOrderTempInfo> mList ;

    public CollectShopCarAdapter(Context context,List<CommitOrderTempInfo> mList) {
        this.context = context;
        this.mList=mList;
        mInflater = LayoutInflater.from(context);
    }

    public List<CommitOrderTempInfo> getList() {
        return mList;
    }

    public void setListener(OnShopCarClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_collect_shop_car, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, context);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_item_collect_name);
        viewHolder.et_price = (EditText) view.findViewById(R.id.et_item_collect_price);
        viewHolder.et_num = (EditText) view.findViewById(R.id.et_item_collect_num);
        viewHolder.tv_employee = (TextView) view.findViewById(R.id.tv_item_collect_employee);
        viewHolder.bt_gift = (Button) view.findViewById(R.id.bt_item_collect_setGift);
        viewHolder.bt_delete = (Button) view.findViewById(R.id.bt_item_collect_delete);
        viewHolder.iv_remove = (ImageView) view.findViewById(R.id.iv_item_collect_remove);
        viewHolder.iv_add = (ImageView) view.findViewById(R.id.iv_item_collect_add);
        viewHolder.iv_gift = (ImageView) view.findViewById(R.id.iv_item_collect_gift);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.bind(position, mList.get(position), mList, this, listener);

    }

    public void setList(List<CommitOrderTempInfo> list) {
        this.mList=list;
        notifyDataSetChanged();

    }

    public void addShop(CommitOrderTempInfo info) {
        this.mList.add(info);
        int positionStart = mList.size();
        if (positionStart > 0) {
            notifyItemRangeInserted(positionStart, 1);
        } else {
            notifyDataSetChanged();
        }
    }

    public void deleteAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if (registerI != mList.size()) {
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View view) {
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_employee;
        private EditText et_num, et_price;
        private Button bt_gift, bt_delete;
        private CustomDialog customDialog;
        private Context context;
        private DeleteDialog dialog;
        private ImageView iv_remove, iv_add, iv_gift;
        private SelectDialog selectDialog;
        private CommitOrderTempInfo model;
        private static final String TAG = "MyViewHolder";

        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        public void bind(final int position, final CommitOrderTempInfo info, final List<CommitOrderTempInfo> mList,
                         final CollectShopCarAdapter adapter, final OnShopCarClickListener listener) {
            this.model = info;
            tv_employee.setText(model.getUserName());
            et_num.setText(model.getNum() + "");
            if (model.getIsGift() == 1) {
                et_price.setText("0.00");
                iv_gift.setVisibility(View.VISIBLE);
                bt_gift.setVisibility(View.GONE);
                et_price.setEnabled(false);
                tv_name.setText(model.getName());
            } else {
                if (model.getIsPromotion()==1){
                    et_price.setText(AppUtil.formatStandardMoney(model.getPromotionAmt()));
                }else{
                    et_price.setText(AppUtil.formatStandardMoney(model.getPrice()));
                }
                iv_gift.setVisibility(View.GONE);
                if (model.getType() == 3) {
                    bt_gift.setVisibility(View.GONE);
                    et_price.setEnabled(false);
                    tv_name.setText(model.getName() + " (次卡)");
                } else {
                    bt_gift.setVisibility(View.VISIBLE);
                    if (model.getIsPromotion() == 1) {
                        et_price.setEnabled(false);
                        tv_name.setText(model.getName() + "(促销)");
                    } else {
                        et_price.setEnabled(true);
                        tv_name.setText(model.getName());
                    }
                }
            }

            bt_gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (dialog == null) {
                        dialog = new DeleteDialog(context);
                        dialog.setHint("请输入赠送数量");
                        dialog.setOnConfirmListener(new DeleteDialog.OnOkConfirmListener() {
                            @Override
                            public void confirmResult(String text) {
                                dialog.dismiss();
                                if (!StringUtils.isEmptyOrWhitespace(text)) {
                                    int i = Integer.parseInt(text);
                                    if (i > model.getNum()) {
                                        Toast.makeText(context, "最多赠送" + model.getNum() + "个", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    int num = model.getNum() - i;
                                    if (num == 0) {
                                        mList.remove(model);
                                    } else {
                                        et_num.setText(num + "");
                                    }
                                    CommitOrderTempInfo addEntity = new CommitOrderTempInfo( model.getId(), model.getType(),
                                            model.getPrice(), i, model.getUserId(), model.getName(),
                                            model.getUserName(), model.getIsPromotion(), model.getPromotionAmt(),
                                            1, 0, -1,model.getOriginNum(),model.getVipUserId());
                                    mList.add(addEntity);
                                    adapter.notifyDataSetChanged();
                                    listener.shopCarClick();
                                }
                            }
                        });
                    }
                    dialog.show();
                }
            });
            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customDialog == null) {
                        customDialog = new CustomDialog(context);
                        customDialog.setContent("是否删除该商品");
                        customDialog.setCancelVisiable();
                        customDialog.setOkVisiable(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                                    mList.remove(model);
                                    adapter.notifyDataSetChanged();
                                    listener.shopCarClick();
                            }
                        });
                    }
                    customDialog.show();
                }
            });
            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = model.getNum() - 1;
                    model.setNum(num);
                    if (num == 0) {
                        mList.remove(model);
                        adapter.notifyDataSetChanged();
                    } else {
                        et_num.setText(num + "");
                    }
                    listener.shopCarClick();
                }
            });
            iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = model.getNum() + 1;
                        model.setNum(num);
                        et_num.setText(num + "");
                        listener.shopCarClick();

                }
            });
            tv_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectDialog == null) {
                        EmployeeInfoDao employeeInfoDao = GreenDaoUtils.getmDaoSession().getEmployeeInfoDao();
                        List<EmployeeInfo> list = employeeInfoDao.loadAll();
                        final List<SelectInfo> genderInfos = new ArrayList<>();
                        for (EmployeeInfo info : list) {
                            genderInfos.add(new SelectInfo(info.getName(), info.getId() + ""));
                        }
                        SelectSimpleAdapter adapter = new SelectSimpleAdapter(context, genderInfos);
                        selectDialog = new SelectDialog(context, adapter);
                        selectDialog.setListenner(new SelectDialog.SelectListenner() {
                            @Override
                            public void onSelect(int position) {
                                SelectInfo genderInfo = genderInfos.get(position);
                                tv_employee.setText(genderInfo.getName());
                                model.setUserName(genderInfo.getName());
                                model.setUserId(Integer.parseInt(genderInfo.getType()));
                            }
                        });
                    }
                    selectDialog.show();
                }
            });
            et_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if (StringUtils.isEmptyOrWhitespace(text)) {
                        text = "0";
                    }
                    int i = Integer.parseInt(text);
                        model.setNum(i);;
                        listener.shopCarClick();
                }
            });

//            et_price.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    Log.d(TAG, "beforeTextChanged: s="+s+"  start="+start+"  count="+count+"  after="+after);
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    Log.d(TAG, "onTextChanged: s="+s+"  start="+start+"  count="+count);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    String text = s.toString();
//                    Log.d(TAG, "onTextChanged: s="+text);
//                    if (StringUtils.isEmptyOrWhitespace(text)) {
//                        text = "0";
//                    }else{
//                        text=text.replaceAll(",","");
//                    }
//                    double price = Double.parseDouble(text);
//                    qb.where(CommitOrderTempInfoDao.Properties.DbId.eq(model.getDbId()));
//                    CommitOrderTempInfo entity = (CommitOrderTempInfo) qb.unique();
//                    int isPromotion = model.getIsPromotion();
//                    if (Constants.is_promotion == isPromotion) {
//                        entity.setPromotionAmt(price);
//                    }else{
//                        entity.setRealAmt(price);
//                        entity.setPrice(price);
//                    }
//
//                    commitOrderTempInfoDao.update(entity);
//                    listener.shopCarClick();
//                }
//            });

            et_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnitPriceDialog unitPriceDialog = new UnitPriceDialog(context);
                    unitPriceDialog.setOkVisiable(new UnitPriceDialog.UnitPriceListener() {
                        @Override
                        public void completed(String s) {
                            String text = s.toString();
                            Log.d(TAG, "onTextChanged: s="+text);
                            if (StringUtils.isEmptyOrWhitespace(text)) {
                                text = "0";
                            }else{
                                text=text.replaceAll(",","");
                            }
                            double price = Double.parseDouble(text);
                            int isPromotion = model.getIsPromotion();
                            if (Constants.is_promotion == isPromotion) {
                                model.setPromotionAmt(price);
                            }else{
                                model.setPrice(price);
                                model.setPrice(price);
                            }
                            et_price.setText(price+"");
                            listener.shopCarClick();
                        }
                    });
                    unitPriceDialog.show();
                }
            });
        }
    }

    public interface OnShopCarClickListener {
        void shopCarClick();
    }
}
