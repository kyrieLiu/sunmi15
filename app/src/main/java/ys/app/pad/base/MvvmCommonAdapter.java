package ys.app.pad.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ys.app.pad.BR;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.callback.OnLoadMoreListenner;

/**
 * Created by admin on 2017/7/15.
 */

public abstract class MvvmCommonAdapter<T extends Observable> extends RecyclerView.Adapter<MvvmCommonAdapter.CommonHolder> {

    private final Class<? extends MvvmCommonItemViewModel> mClazz;
    protected Context mContext;
    protected List<T> mDatas;//所有 item 的数据集合
    protected int mLayoutId; //item 布局文件 id
    private int mVariableId;// mvvm绑定的viewModel引用
    protected LayoutInflater mInflater;
    private OnItemClickListener listener;

    private OnLoadMoreListenner mLoadMoreListener;
    private boolean mIsCanLoadMore;
    private static final int TYPE_NORMAL_VIEW = 1;
    private static final int TYPE_FOOTER_VIEW = 2;
    private Integer footer_status = new Integer(load_more_status_normal);
    private boolean isAutoLoadMore = true;
    public static final int load_more_status_normal = 300;//上拉加载
    public static final int load_more_status_loading = 301;//加载中
    public static final int load_more_status_failed = 302;//加载失败
    public static final int load_more_status_load_all = 303;//加载完毕

    //构造方法
    public MvvmCommonAdapter(int variableId, Context context, int layoutId, Class<? extends MvvmCommonItemViewModel> clazz) {
        mContext = context;
        mDatas = new ArrayList<T>();
        mLayoutId = layoutId;
        mVariableId = variableId;
        this.mClazz = clazz;
        mInflater = LayoutInflater.from(mContext);
    }

    //构造方法
    public MvvmCommonAdapter(int variableId, Context context, int layoutId, Class<? extends MvvmCommonItemViewModel> clazz, boolean isCanLoadMore) {
        mContext = context;
        mDatas = new ArrayList<T>();
        mLayoutId = layoutId;
        mVariableId = variableId;
        mIsCanLoadMore = isCanLoadMore;
        this.mClazz = clazz;
        mInflater = LayoutInflater.from(mContext);
    }


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLoadMoreListener(OnLoadMoreListenner mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public List getList() {
        return mDatas;
    }

    public void setList(List mDatas) {
        if (mIsCanLoadMore) {
            if (mDatas != null && !mDatas.isEmpty()) {
                this.mDatas.addAll(mDatas);
                notifyItemRangeInserted(this.mDatas.size() - mDatas.size(), mDatas.size());
                if (mDatas.size() == 10) {
                    setLoadMoreStatus(load_more_status_normal);
                } else {
                    setLoadMoreStatus(load_more_status_load_all);
                }
            }
        } else {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }
    }

    public void setList(int startHttp, List mDatas) {
        if (0 == startHttp) {
            this.mDatas.clear();
        }
        if (mIsCanLoadMore) {
            if (mDatas != null && !mDatas.isEmpty()) {
                this.mDatas.addAll(mDatas);
                if (0 == startHttp) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeInserted(this.mDatas.size() - mDatas.size(), mDatas.size());
                }
                if (mDatas.size() == 10) {
                    setLoadMoreStatus(load_more_status_normal);
                } else {
                    setLoadMoreStatus(load_more_status_load_all);
                }
            }
        } else {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }

    }

    public void setList(int startHttp, List mDatas, int pageSize) {
        if (0 == startHttp) {
            this.mDatas.clear();
        }
        if (mIsCanLoadMore) {
            if (mDatas != null && !mDatas.isEmpty()) {
                this.mDatas.addAll(mDatas);
                if (0 == startHttp) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeInserted(this.mDatas.size() - mDatas.size(), mDatas.size());
                }
                if (mDatas.size() == pageSize) {
                    setLoadMoreStatus(load_more_status_normal);
                } else {
                    setLoadMoreStatus(load_more_status_load_all);
                }
            }
        } else {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }
    }

    public void setLoadMoreStatus(int i) {
        footer_status = i;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonHolder holder = null;
        if (TYPE_FOOTER_VIEW == viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(mInflater, R.layout.load_foot_view, parent, false);
            holder = new CommonHolder(binding);
        } else {
            ViewDataBinding footerBinding = DataBindingUtil.inflate(mInflater, mLayoutId, parent, false);
            holder = new CommonHolder(footerBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        if (!isFooterView(position)) {
            T t = mDatas.get(position);
            holder.bind(t, mVariableId, position, listener, mClazz);
        } else {
            holder.bindFoot(mLoadMoreListener, footer_status);
        }

    }


    @Override
    public int getItemCount() {
        if (mIsCanLoadMore) {
            return null == mDatas ? 1 : mDatas.size() + 1;
        } else {
            return null == mDatas ? 0 : mDatas.size();
        }

    }


    public boolean isFooterView(int position) {
        return mIsCanLoadMore && position >= getItemCount() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        return TYPE_NORMAL_VIEW;
    }

    @Override
    public void onViewAttachedToWindow(CommonHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        startLoadMore(recyclerView, layoutManager);
    }

    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!mIsCanLoadMore || mLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isAutoLoadMore && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    scrollLoadMore();
                } else if (isAutoLoadMore) {
                    isAutoLoadMore = false;
                }
            }
        });
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return findMax(lastVisibleItemPositions);
        }
        return -1;
    }

    public int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    private void scrollLoadMore() {
        if (load_more_status_failed == footer_status || load_more_status_load_all == footer_status || mLoadMoreListener == null)
            return;
        mLoadMoreListener.onLoadMore();
    }

    public class CommonHolder<T> extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        public CommonHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(T itemModle, int variableId, final int position, final OnItemClickListener listener, Class clazz) {
            MvvmCommonItemViewModel<T> mItemViewModel = null;
            try {
                mItemViewModel = (MvvmCommonItemViewModel<T>) clazz.newInstance();
                binding.setVariable(variableId, mItemViewModel);
                binding.executePendingBindings();

                //添加于2017/10/17 调拨记录item需要context
                mItemViewModel.setContext(mContext);

                mItemViewModel.setModel(itemModle);

                if (binding != null) {
                    final View root = binding.getRoot();
                    root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onItemClick(root, position);
                            }
                        }
                    });
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        public void bindFoot(final OnLoadMoreListenner listener, Integer footer_status) {
            if (binding == null) return;
            binding.setVariable(BR.footType, footer_status);
            binding.executePendingBindings();
            if (binding != null) {
                if (load_more_status_failed == footer_status) {
                    final View root = binding.getRoot();
                    root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onLoadMore();
                            }
                        }
                    });
                } else {
                    final View root = binding.getRoot();
                    root.setOnClickListener(null);
                }

            }
        }
    }
}
