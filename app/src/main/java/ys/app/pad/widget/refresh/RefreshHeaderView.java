package ys.app.pad.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ys.app.pad.R;
import ys.app.pad.utils.Logger;


/**
 * @author Gemini Y
 * @Description:
 * @email xuyangyang@ebrun.com
 * @create 2017/2/16 09:16
 */

public class RefreshHeaderView extends FrameLayout implements IPullRefreshView {

    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;

    private ProgressBar progressBar;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    private int mHeight;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.refresh_view, this);
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);

        ivArrow = (ImageView) findViewById(R.id.ivArrow);

        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);

        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }


    @Override
    public void onRefresh(String reFreshChannleId) {
        Logger.w("onRefresh");
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("正在刷新 . . . ");

    }

    @Override
    public void onPullHided() {
        Logger.w("onPullHided");
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText("下拉可以刷新");
    }

    @Override
    public void onPullRefresh() {
        Logger.w("onPullRefresh");
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("正在刷新 . . . ");

    }

    @Override
    public void onPullFreeHand() {
        Logger.w("onPullFreeHand");
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(GONE);
        tvRefresh.setText("松开立刻刷新");
        if (!rotated) {
            ivArrow.clearAnimation();
            ivArrow.startAnimation(rotateUp);
            rotated = true;
        }

    }

    @Override
    public void onPullDowning() {
        Logger.w("onPullDowning");
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(GONE);
        if (rotated) {
            ivArrow.clearAnimation();
            ivArrow.startAnimation(rotateDown);
            rotated = false;
        }

        tvRefresh.setText("下拉可以刷新");

    }

    @Override
    public void onPullFinished() {
        Logger.w("onPullFinished");
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText("正在刷新 . . . ");
    }

    @Override
    public void onPullProgress(float pullDistance, float pullProgress) {

    }
}
