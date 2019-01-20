package ys.app.pad.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ys.app.pad.model.StatisticsDataInfo;
import ys.app.pad.utils.Logger;

/**
 * Created by LY on 2018/3/27.
 * 用来展示店铺今日营业统计数据
 */
public class PieChart extends View implements GestureDetector.OnGestureListener {


    /**
     * view的宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图的半径、内部空白圆的半径
     */
    private float mRadius, mInnerRadius;
    /**
     * 饼状图的外切
     */
    private RectF mPieRect;
    /**
     * 各种画笔
     */
    private Paint mPiePaint, mBlankPaint, mLinePaint, mTextPaint, mLegendPaint;
    /**
     * 实体类集合
     */
    private List<StatisticsDataInfo.ProductTypeListBean> mElements;
    /**
     * 各个元素的角度
     */
    private List<Float> mAngles = new ArrayList<>();
    /**
     * 元素的颜色
     */
    private String[]mColors=new String[]{"#f9576e","#996699","#3c45f1","#e7c049","#62dc52","#A9A9A9","#FF9900"
            ,"#1ac9de","#993333","#3218b5","#df981d","#666666"};
    /**
     * 元素的占比
     */
    private List<String> mPercents = new ArrayList<>();
    /**
     * 中心文字
     */
    private String mText="日结统计比例图";

    private SparseArray<double[]> angles = new SparseArray<>();

    private GestureDetector mDetector;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDetector = new GestureDetector(getContext(), this);
        mPieRect = new RectF();
        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setColor(Color.RED);

        mBlankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlankPaint.setColor(Color.WHITE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setColor(Color.BLACK);

        mLegendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int width = 0, height = 0;
//        if (widthMode == MeasureSpec.AT_MOST) {
//            width = (int) getSize();
//        } else {
//            width = widthSize;
//        }
//        if (heightMode == MeasureSpec.AT_MOST) {
//            height = (int) getSize();
//        } else {
//            height = heightSize;
//        }
//        int size = Math.min(width, height);
//        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mRadius = (float) (Math.min(mWidth, mHeight) / 2 * 0.6);
        mPieRect.left = -mRadius;
        mPieRect.top = -mRadius;
        mPieRect.right = mRadius;
        mPieRect.bottom = mRadius;

        mInnerRadius = (float) (mRadius * 0.8);
        Logger.d("onSizeChanged   width=="+mWidth+"   height=="+mHeight);
        mTextPaint.setTextSize(mWidth/96);
        mLegendPaint.setTextSize(mWidth/76);

    }

    private Path mPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mElements==null)return;
        canvas.save();
        canvas.translate(mWidth / 4, mHeight / 2);
        //从12点方向开始画
        float sweepedAngle = -90;
        float extendLeng;//第一象限
        int colorLength=mColors.length;
        if (mAngles.size()==mElements.size()){


        for (int i = 0; i < mElements.size(); i++) {
            if (mElements.get(i).getTotalAmt()==0){
                continue;
            }
            //当相邻的两个角度过小时防止被压盖
            if (i%2==1)extendLeng= (float) 1.44;
            else extendLeng= (float) 1.2;
            int indexColor=i%colorLength;
            Logger.d("i===="+i+"   length==="+colorLength+"   i%mColors.length=="+indexColor);

            //设置扇形的颜色
            //if (i>colorLength){
                mPiePaint.setColor(Color.parseColor(mColors[indexColor]));
                mLinePaint.setColor(Color.parseColor(mColors[indexColor]));
//            }else{
//                mPiePaint.setColor(Color.parseColor(mColors[i]));
//                mLinePaint.setColor(Color.parseColor(mColors[i]));
//            }
            //画扇形
            canvas.drawArc(mPieRect, sweepedAngle, mAngles.get(i), true, mPiePaint);
            //扫过的角度++
            double[] ang = new double[2];
            ang[0] = sweepedAngle + 90;
            ang[1] = ang[0] + mAngles.get(i);
            angles.put(i, ang);
            sweepedAngle += mAngles.get(i);
            String percentText = mPercents.get(i) + "%";
            float x = getXCoordinate(mAngles.get(i), sweepedAngle);
            float y = getYCoordinate(mAngles.get(i), sweepedAngle);
            sweepedAngle += 1;
            mPath.reset();
            mPath.moveTo(x, y);
            mPath.lineTo( x * extendLeng, y * extendLeng);
            canvas.drawPath(mPath, mLinePaint);
            mPath.reset();
            mPath.moveTo(x * extendLeng, y * extendLeng);
            //水平线的长度设置为文字长度的1.3倍
            float horizontalLineLength = (float) (getTextWidth(mTextPaint, percentText) * 1.3);
            //当线的起点在第三、四象限时，先把path移动到终点位置，然后向起点画线，使后面画文字时，文字方向是正确的
            if (x < 0) {
                horizontalLineLength = -horizontalLineLength;
                mPath.moveTo(x * extendLeng + horizontalLineLength, y * extendLeng);
                mPath.lineTo(x * extendLeng, y * extendLeng);
            } else {
                mPath.lineTo(x * extendLeng + horizontalLineLength, y * extendLeng);
            }
            mPath.close();
            canvas.drawPath(mPath, mLinePaint);
            //垂直方向的偏移量，画文字时，文字显示在path的下方，为了让文字显示在上方，设置一个文字高度的垂直偏移量
            float offsetV = -getTextHeight(mTextPaint, percentText)/2;
            canvas.drawTextOnPath(percentText, mPath, 5, offsetV, mTextPaint);
        }
        }
        //这里开始画中心空白部分以及文字，空白部分半径设置为整个圆半径的0.6倍

        canvas.drawCircle(0, 0, mInnerRadius, mBlankPaint);
        double index = Math.ceil(mText.length() / 2) + 1;
        if (mText.length() % 2 == 0) index -= 1;

        String longerText = mText.substring(0, (int) index);
        calculateTextPaint(longerText);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(longerText, 0, 0, mTextPaint);
        canvas.drawText(mText.substring((int) (index)), 0, getTextHeight(mTextPaint, mText) + 6, mTextPaint);
        canvas.restore();
        if (mShowLegend) {//展示统计详情
            drawLegend(canvas);
        }
    }

    /**
     * 画图例
     * @param canvas
     */
    private void drawLegend(Canvas canvas) {
        float verticalOffset = 0;
        //canvas.translate(0, mHeight / 9 );
        for (int i = 0; i < mElements.size(); i++) {
            StatisticsDataInfo.ProductTypeListBean ele = mElements.get(i);
            int indexColor=i%mColors.length;
            mLegendPaint.setColor(Color.parseColor(mColors[indexColor]));

            String itemMessage=ele.getName()+"   "+ele.getTotalAmt()+"元";
            mLegendPaint.getTextBounds(itemMessage, 0, itemMessage.length(), rect);
            verticalOffset = rect.height() + (mWidth/64);

            mLegendPaint.setStrokeWidth(8);

            float translateX=mWidth / 2 ;
            float radius=6;
            if (i % 2 == 1) {
                canvas.drawCircle(translateX+(mWidth/8), radius/2, radius, mLegendPaint);
                canvas.drawText(itemMessage, translateX+(float) (mWidth/7), rect.height() / 2, mLegendPaint);
            } else {
                canvas.translate(0, verticalOffset);
                canvas.drawCircle(translateX, radius/2, radius, mLegendPaint);
                canvas.drawText(itemMessage, translateX+15, rect.height() / 2, mLegendPaint);
            }
        }
    }

    private Rect rect = new Rect();


    public void setData(List<StatisticsDataInfo.ProductTypeListBean> elements) {
        mElements = elements;
        setValuesAndColors();
        invalidate();
    }

    /**
     * 设置中心文字
     *
     * @param text
     */
    public void setTitleText(String text) {
        mText = text;
    }

    /**
     * 计算角度值和各个值的占比
     */
    private void setValuesAndColors() {
        float sum = 0;
        int valueNum=0;
        if (mElements != null && mElements.size() > 0) {
            for (StatisticsDataInfo.ProductTypeListBean ele : mElements) {
                sum += ele.getTotalAmt();
                if (ele.getTotalAmt()!=0)valueNum++;
            }
            if (sum>0){
                BigDecimal totleAngel;
                if (valueNum>1){
                    totleAngel = BigDecimal.valueOf(360 - valueNum);
                }else{
                    totleAngel=BigDecimal.valueOf(360);
                }

                for (int i = 0; i < mElements.size(); i++) {
                    StatisticsDataInfo.ProductTypeListBean ele = mElements.get(i);
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(ele.getTotalAmt()));
                    BigDecimal sumBigDecimal = BigDecimal.valueOf(sum);
                    BigDecimal res = bigDecimal.divide(sumBigDecimal, 5, BigDecimal.ROUND_HALF_UP);
                    //计算角度
                    BigDecimal angle = res.multiply(totleAngel);
                    mAngles.add(angle.floatValue());
                    //计算百分比保留两位小数并保存
                    mPercents.add(bigDecimal.multiply(new BigDecimal(100)).divide(sumBigDecimal, 2, BigDecimal.ROUND_HALF_UP).toPlainString());
                }
            }

        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {

        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

//        Log.e("angle", String.valueOf(angle));
        int position = getPosition(motionEvent);
        if (position >= 0 && mListener != null) {
            mListener.onItemClick(position);
        }

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }


    /**
     * 获取点击位置坐标对应的饼状图的区域
     * @param motionEvent
     * @return 数据的position
     */
    private int getPosition(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        //判断点击位置是否在innerRadius内
        if ((Math.pow(x-centerX,2)+ Math.pow(y-centerY,2))<mInnerRadius*mInnerRadius){
            return -1;
        }

        //判断点击位置是否在饼状图以外
        if ((Math.pow(x-centerX,2)+ Math.pow(y-centerY,2))>mRadius*mRadius){
            return -1;
        }

        // 判断象限
        // 第一象限
        double angle = 0;

        if (x > centerX && y < centerY) {
            angle = Math.toDegrees(Math.atan((Math.abs(x - centerX)) / (Math.abs(centerY - y))));

        } else if (x > centerX && y > centerY) {//第二象限
            angle = Math.toDegrees(Math.atan(((y - centerY) / (x - centerX))));
            angle += 90;
        } else if (x < centerX && y > centerY) {//第三象限
            angle = Math.toDegrees(Math.atan(((centerX - x) / (y - centerY))));
            angle += 180;
        } else if (x < centerX && y < centerY) {//第四象限
            angle = Math.toDegrees(Math.atan(((centerY - y) / (centerX - x))));
            angle += 270;
        }

        for (int i = 0; i < angles.size(); i++) {
            double[] angs = angles.get(i);
            if (angle >= angs[0] && angle <= angs[1]) {
                return i;
            }
        }
        return -1;
    }

    private OnItemClickListener mListener;
    //点击数据回调
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private boolean mShowLegend = true;

    /**
     * 图例开关
     * @param enable
     */
    public void enableLegend(boolean enable) {
        mShowLegend = enable;
    }



    /**
     * 把文字分两行，并画在圆内接正方形内，依此计算画笔的textSize
     *
     * @param text
     */
    private void calculateTextPaint(String text) {
        if (!TextUtils.isEmpty(text)) {
            measureText(text, 100);
        }
    }

    /**
     * 递归调用，计算testSize
     *
     * @param text
     * @param textSize
     */
    private void measureText(String text, int textSize) {
        mTextPaint.setTextSize(textSize);
        float width = getTextWidth(mTextPaint, text);
        float height = getTextHeight(mTextPaint, text);
        if (width > mInnerRadius * 1.41421) {
            textSize--;
            measureText(text, textSize);
            return;
        }
        if (height * 2.5 > mInnerRadius * 1.41421) {
            textSize--;
            measureText(text, textSize);
        }
    }

    private float getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * @param paint
     * @param text
     * @return
     */
    private float getTextWidth(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 获取圆弧中点的x轴坐标
     *
     * @param angle        圆弧对应的角度
     * @param sweepedAngle 扫过的角度
     * @return 圆弧中点的x轴坐标
     */
    private float getXCoordinate(float angle, float sweepedAngle) {
        float x = (float) (mRadius * Math.cos(Math.toRadians(sweepedAngle - angle / 2)));
        return x;

    }

    /**
     * 获取圆弧中点的y轴坐标
     *
     * @param angle        圆弧对应的角度
     * @param sweepedAngle 扫过的角度
     * @return 圆弧中点的y轴坐标
     */
    private float getYCoordinate(float angle, float sweepedAngle) {
        float y = (float) (mRadius * Math.sin(Math.toRadians(sweepedAngle - angle / 2)));
        return y;

    }

    private float getSize() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float widht = display.getWidth();
        float height = display.getHeight();
        return Math.min(widht, height);
    }

}
