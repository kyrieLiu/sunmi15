package ys.app.pad.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import ys.app.pad.R;

/**
 * 作者：lv
 * 时间：2017/3/4 10:41
 */

public class CircleTickView extends View {


    private PathMeasure tickPathMeasure;
    /**
     * 打钩百分比
     */
    float tickPercent = 0;

    private Path path;
    //初始化打钩路径
    private Path tickPath;


    // 圆圈的大小,半径
    private int circleStrokeWidth;
    private RectF rec;
    private Paint tickPaint;


    public CircleTickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CircleTickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTickView(Context context) {
        super(context);
        init(context, null);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTickView);
        circleStrokeWidth = mTypedArray.getInteger(R.styleable.CircleTickView_circleStrokeWidth, 15);
        mTypedArray.recycle();

        tickPaint = new Paint();
        tickPaint.setColor(Color.parseColor("#FFFFFF"));
        tickPaint.setAntiAlias(true);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);
        tickPaint.setStrokeWidth(circleStrokeWidth);
        tickPaint.setDither(true);

        tickPathMeasure = new PathMeasure();
        rec = new RectF();
        path = new Path();
        tickPath = new Path();

        //打钩动画
        ValueAnimator mTickAnimation;
        mTickAnimation = ValueAnimator.ofFloat(0f, 1f);
        mTickAnimation.setStartDelay(500);
        mTickAnimation.setDuration(500);
        mTickAnimation.setInterpolator(new AccelerateInterpolator());
        mTickAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tickPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mTickAnimation.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {



        int width = canvas.getWidth();
        int height = canvas.getHeight();


        // 设置第一条直线的相关参数
        int firStartX = width/13;
        int firStartY = height*10/23;

        int firEndX = width*10/26;
        int firEndY = height*10/14;


        int secEndX = width*10/11;
        int secEndY = height/4;


        rec.set(0, 0, width, height);
        tickPath.moveTo(firStartX, firStartY);
        tickPath.lineTo(firEndX, firEndY);
        tickPath.lineTo(secEndX, secEndY);
        tickPathMeasure.setPath(tickPath, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        tickPathMeasure.getSegment(0, tickPercent * tickPathMeasure.getLength(), path, true);
        path.rLineTo(0, 0);
        tickPaint.setPathEffect(new CornerPathEffect(10));//连接处圆滑效果
        canvas.drawPath(path, tickPaint);
//        canvas.drawArc(rec, 0, 360, false, tickPaint);
    }


}