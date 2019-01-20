package ys.app.pad;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ys.app.pad.base.MvvmCommonAdapter;
import ys.app.pad.widget.refresh.NestedRefreshLayout;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;

/**
 * Created by aaa on 2017/4/7.
 */

public abstract class BaseFragmentViewModel extends BaseObservable {

    public ObservableBoolean isNetWorkErrorVisible = new ObservableBoolean(false);
    public ObservableBoolean isNoneDataVisible = new ObservableBoolean(false);
    public ObservableBoolean isLoadingVisible = new ObservableBoolean(false);
    protected boolean firstCome = true;
    protected int startHttp;
    public int limit_15=15;//请求list限制15条

    protected void showToast(Context context, String toastStr) {
        if (context != null) {
            Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
        }
    }

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
                    isLoadingVisible.set(false);
                }
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);//footer为正常状态
            } else {//list长度不为0
                if (firstCome) {//隐藏没有数据img,并且fristCome设置为false
                    isNoneDataVisible.set(false);
                    isLoadingVisible.set(false);
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
    protected int pageSize = 10;
    protected void loadSecMoreSuccess(MvvmCommonAdapter adapter, List data) {
        if (data == null) {
            if (firstCome) {
                isNetWorkErrorVisible.set(true);
            } else {
                if (startHttp == 0) {//如果为刷新
                    if (firstCome) {
                        isNetWorkErrorVisible.set(true);
                    }
                }
            }
            isNoneDataVisible.set(false);
        } else {
            if (startHttp == 0) {
                if (data.isEmpty()) {
                    isNoneDataVisible.set(true);//展示没有数据img
                } else {
                    if (firstCome) {//隐藏没有数据img,并且fristCome设置为false
                        isNoneDataVisible.set(false);
                    }
                    firstCome = false;
                    if (data.size() == pageSize) {
                        adapter.setLoadMoreStatus(MvvmCommonAdapter.load_more_status_normal);
                    } else {
                        adapter.setLoadMoreStatus(MvvmCommonAdapter.load_more_status_load_all);
                    }
                    isNoneDataVisible.set(false);
                }
            } else {
                if (data.size() == pageSize) {
                    adapter.setLoadMoreStatus(MvvmCommonAdapter.load_more_status_normal);
                } else {
                    adapter.setLoadMoreStatus(MvvmCommonAdapter.load_more_status_load_all);
                }
            }
            isNetWorkErrorVisible.set(false);
        }
        startHttp += data.size();
        isLoadingVisible.set(false);
    }

    protected void refreshSuccess(List data) {
        if (data == null || data.isEmpty()) {
            isNoneDataVisible.set(true);//展示没有数据img
        } else {
            if (firstCome) {//隐藏没有数据img,并且fristCome设置为false
                isNoneDataVisible.set(false);
            }
        }
        firstCome = false;
        isNetWorkErrorVisible.set(false);
        isLoadingVisible.set(false);
    }

    protected void refreshNetUnavailable(NestedRefreshLayout nestedRefreshLayout) {
        showToastShort(Constants.network_error_warn);
        if (firstCome) {
            isNoneDataVisible.set(false);
            isLoadingVisible.set(false);
            isNetWorkErrorVisible.set(true);
        }
        nestedRefreshLayout.refreshFinish();
    }
    protected void showToastShort(String toastS) {
        Toast.makeText(AppApplication.getAppContext(), toastS, Toast.LENGTH_SHORT).show();
    }

    protected void refreshFailed(String errorMessage) {
        showToastShort(errorMessage);
        if (firstCome) {
            isNetWorkErrorVisible.set(true);
        }
        isNoneDataVisible.set(false);
        isLoadingVisible.set(false);
    }
    protected void loadHttpNoData() {
        showToastShort(Constants.network_error_warn);
        if (firstCome) {
            isNoneDataVisible.set(false);
            isLoadingVisible.set(false);
            isNetWorkErrorVisible.set(true);
        } else {
            isNoneDataVisible.set(true);
            isLoadingVisible.set(false);
            isNetWorkErrorVisible.set(false);
        }
//        nestedRefreshLayout.refreshFinish();
    }
}
