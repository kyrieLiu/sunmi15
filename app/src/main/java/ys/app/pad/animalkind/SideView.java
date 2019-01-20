package ys.app.pad.animalkind;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.R;


public class SideView extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int choose = 0;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;
    private float width, height;
    private Paint rectPaint = new Paint();

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getWidth();
        height = getHeight();
    }

    /**
     * 描绘侧边字母列表
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变颜色
        float singleWidth = width / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(23);
            float xPos = singleWidth * i;
            float textSize = paint.measureText(b[i]) / 2;
            // 选中的状态
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.white));
                paint.setFakeBoldText(true);
                rectPaint.setColor(getResources().getColor(R.color.blue));
                RectF r2 = new RectF();                           //RectF对象
                r2.left = xPos;                                 //左边
                r2.top = 0;                                 //上边
                r2.right = xPos + singleWidth;                                   //右边
                r2.bottom = height;                              //下边
                canvas.drawRoundRect(r2, 5, 5, rectPaint);        //绘制圆角矩形


            }
            // x坐标等于中间减字符串宽度的一般.

            float yPos = height / 2 + textSize;
            canvas.drawText(b[i], xPos + singleWidth / 2 - textSize, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        float x = event.getX();
        int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        int c = (int) (x / getWidth() * b.length);
        switch (action) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }

                        choose = c;
                    }
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
//					if (mTextDialog != null) {
//						mTextDialog.setText(b[c]);
//
//						mTextDialog.setVisibility(View.VISIBLE);
//					}

                        choose = c;
                        invalidate();
                    }
                }
                break;

//		default:
//			setBackgroundResource(R.color.input_grey);
//			if (oldChoose != c) {
//				if (c >= 0 && c < b.length) {
//					if (listener != null) {
//						listener.onTouchingLetterChanged(b[c]);
//					}
//					if (mTextDialog != null) {
//						mTextDialog.setText(b[c]);
//
//						mTextDialog.setVisibility(View.VISIBLE);
//					}
//
//					choose = c;
//					invalidate();
//				}
//			}
//			break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}