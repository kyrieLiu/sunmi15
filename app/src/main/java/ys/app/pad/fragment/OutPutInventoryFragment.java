package ys.app.pad.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.fragment.appointment.ViewPagerFragment;
import ys.app.pad.databinding.OutPutRecordListBinding;
import ys.app.pad.viewmodel.inventory.OutPutRecordModel;
import ys.app.pad.widget.dialog.RequestDialog;


/**
 * Created by liuyin on 2017/11/10.
 */

public class OutPutInventoryFragment extends ViewPagerFragment {
    private OutPutRecordListBinding binding;
    private OutPutRecordModel mViewModel;
    private RequestDialog mRDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_out_put_list, container, false);
            rootView = binding.getRoot();
            Bundle bundle=getArguments();
            int type = bundle.getInt(Constants.intent_search_from, 1);
            mViewModel = new OutPutRecordModel(this,binding.recyclerView, type);
            binding.setViewModel(mViewModel);
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();// 缓存的view需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void initData() {
        mViewModel.initView();
    }

    public void showRDialog() {
        if (mRDialog == null) {
            mRDialog = new RequestDialog(getActivity());
        }
        mRDialog.show();
    }

    public void hideRDialog() {
        if (mRDialog != null) {
            mRDialog.hide();
        }
    }

    @Override
    public void onDestroy() {
        if (mRDialog!=null){
            mRDialog.dismiss();
            mRDialog=null;
        }
        if (mViewModel != null){
            mViewModel.clear();
        }
        super.onDestroy();
    }
}
