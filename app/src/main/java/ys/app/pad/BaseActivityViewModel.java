package ys.app.pad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;

/**
 * Created by aaa on 2017/4/7.
 */

public class BaseActivityViewModel extends BaseObservable {

    public ObservableBoolean isNetWorkErrorVisible = new ObservableBoolean(false);
    public ObservableBoolean isNoneDataVisible = new ObservableBoolean(false);
    public ObservableBoolean isLoadingVisible = new ObservableBoolean(false);
    protected int startHttp;
    protected boolean firstCome = true;
    public  int limit_15=15;
    public  int limit_20=20;

    public void onClickNetWorkError(View view) {
        isNetWorkErrorVisible.set(false);
        reloadData(view);
    }

    public void reloadData(View view) {
    }


    protected void afterRefreshAndLoadMoreSuccess(List mList, final IRecyclerView recyclerView, LoadMoreFooterView loadMoreFooterView) {

        if (mList == null || recyclerView == null || loadMoreFooterView == null) return;

        if (startHttp == 0) {//如果是刷新
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setRefreshing(false);
                }
            });//刷新停止
            if (mList.size() == 0) {//list长度为0
                if (firstCome) {//第一次进来
                    isNoneDataVisible.set(true);//展示没有数据img
                }
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);//footer为正常状态
            } else {//list长度不为0
                if (firstCome) {//隐藏没有数据img,并且fristCome设置为false
                    isNoneDataVisible.set(false);
                    firstCome = false;
                }
                if (mList.size() == 10) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                } else {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.TheEnd);
                }
            }
        } else {//如果是加载更多
            if (mList.size() == 10) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
            } else {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.TheEnd);
            }
        }
        startHttp += mList.size();
        isLoadingVisible.set(false);
    }
    protected void afterRefreshAndLoadMoreItemNum(List mList, final IRecyclerView recyclerView, LoadMoreFooterView loadMoreFooterView,int num) {

        if (mList == null || recyclerView == null || loadMoreFooterView == null) return;

        if (startHttp == 0) {//如果是刷新
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setRefreshing(false);
                }
            });//刷新停止
            if (mList.size() == 0) {//list长度为0
                if (firstCome) {//第一次进来
                    isNoneDataVisible.set(true);//展示没有数据img
                    isLoadingVisible.set(false);
                }
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);//footer为正常状态
            } else {//list长度不为0
                if (firstCome) {//隐藏没有数据img,并且fristCome设置为false
                    isNoneDataVisible.set(false);
                    isLoadingVisible.set(false);
                    firstCome = false;
                }
                if (mList.size() == num) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                } else {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.TheEnd);
                }
            }
        } else {//如果是加载更多
            if (mList.size() == num) {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
            } else {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.TheEnd);
            }
        }
        startHttp += mList.size();
    }

    protected void afterRefreshAndLoadMoreFailed(final IRecyclerView recyclerView, LoadMoreFooterView loadMoreFooterView) {

        if (recyclerView == null || loadMoreFooterView == null) return;

        if(firstCome){
            isNetWorkErrorVisible.set(true);
        }else {
            if (startHttp == 0) {//如果为刷新
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshing(false);
                    }
                });
                if (firstCome) {
                    isNetWorkErrorVisible.set(true);
                } else {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.NetWorkError);
                }
            } else {//如果为加载更多
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.NetWorkError);
            }
        }
        isLoadingVisible.set(false);

    }


    protected void afterRefreshFailed(final IRecyclerView recyclerView) {
        if (firstCome) {
            isNetWorkErrorVisible.set(true);
            isNoneDataVisible.set(false);
            isLoadingVisible.set(false);
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(false);
            }
        });
    }


    protected void afterRefreshSuccess(final IRecyclerView recyclerView, List data) {
        if (firstCome) {
            isLoadingVisible.set(false);
            if (data.size() == 0) {
                isNoneDataVisible.set(true);
            }else {
                isNoneDataVisible.set(false);
                firstCome = false;
            }
        } else {
            isNoneDataVisible.set(false);
            firstCome = false;
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(false);
            }
        });
    }

    public void turnToActivity(Activity activity,Class<? extends BaseActivity> openActivity) {
        Intent intent = new Intent();
        intent.setClass(activity, openActivity);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    protected void showToast(Context context, String toastStr) {
        if (context != null) {
            Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
        }
    }
    protected void showLongToast(Context context, String toastStr) {
        if (context != null) {
            Toast.makeText(context, toastStr, Toast.LENGTH_LONG).show();
        }
    }
}
