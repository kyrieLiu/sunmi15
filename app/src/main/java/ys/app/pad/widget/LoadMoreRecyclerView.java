package ys.app.pad.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import ys.app.pad.R;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;

/**
 * Created by lyy on 2017/2/15 15:37.
 * email：2898049851@qq.com
 */

public class LoadMoreRecyclerView extends IRecyclerView {



    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final View loadMoreFooter = LayoutInflater.from(getContext()).inflate(R.layout.load_footer, null, false);
        addHeaderView(loadMoreFooter);
    }

}
