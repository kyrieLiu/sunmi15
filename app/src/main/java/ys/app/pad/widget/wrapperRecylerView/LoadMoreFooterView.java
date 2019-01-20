package ys.app.pad.widget.wrapperRecylerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import ys.app.pad.R;


/**
 * Created by aspsine on 16/3/14.
 */
public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;
    private View mNetworkErrorView;
    private View mEndView;

    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.load_footer, this, true);

        mLoadingView = findViewById(R.id.loading_viewstub);
        mNetworkErrorView = findViewById(R.id.network_error_viewstub);
        mEndView = findViewById(R.id.end_viewstub);

        mNetworkErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(Status.Normal);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
        change();
    }

    public boolean canLoadMore() {
        return mStatus == Status.Normal||mStatus == Status.NetWorkError;
    }

    private void change() {
        switch (mStatus) {
            case Normal:
                mLoadingView.setVisibility(GONE);
                mNetworkErrorView.setVisibility(GONE);
                mEndView.setVisibility(GONE);
                break;

            case Loading:
                mLoadingView.setVisibility(VISIBLE);
                mNetworkErrorView.setVisibility(GONE);
                mEndView.setVisibility(GONE);
                break;

            case NetWorkError:
                mLoadingView.setVisibility(GONE);
                mNetworkErrorView.setVisibility(VISIBLE);
                mEndView.setVisibility(GONE);
                break;

            case TheEnd:
                mLoadingView.setVisibility(GONE);
                mNetworkErrorView.setVisibility(GONE);
                mEndView.setVisibility(VISIBLE);
                break;

        }
    }

    public enum Status {
        Normal/**正常*/, Loading/**加载中..*/, NetWorkError/**网络异常,或者请求失败*/, TheEnd/**加载到最底了*/
    }


    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }

}
