package com.mgjr.view.invester;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Administrator on 2016/7/13.
 */
public class InvestItemBubbleView extends View {

    /**
     * 泡泡当前的半径,最大半径,最小半径
     */
    public float mFirstCurrentRadius = 16f;
    public float mSecondCurrentRadius = 16f;
    public float mThirdCurrentRadius = 16f;
    public float mMaxRadius = 16f;
    public float mMinRadius = 9f;

    private Handler mHandler;
    /**
     * 当前圆心点,依据该点来画圆
     */
    private Point firstPoint;
    private Point secondPoint;
    private Point thirdPoint;

    private Paint mFirstPointPaint;
    private Paint mSecondPointPaint;
    private Paint mThirdPointPaint;
    private boolean isCanDrawSecondPoint;
    private boolean isCanDrawThirdPoint;
    private boolean isCanDrawFirstPoint;

    public InvestItemBubbleView(Context context) {
        this(context, null);
    }

    public InvestItemBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InvestItemBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler();

        mFirstPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstPointPaint.setColor(0xFFD9B1);//灰白色
        mFirstPointPaint.setAlpha(255);//设置不透明度：透明为0，完全不透明为255

        mSecondPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondPointPaint.setColor(0xFFD9B1);//灰白色
        mSecondPointPaint.setAlpha(255);//设置不透明度：透明为0，完全不透明为255

        mThirdPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThirdPointPaint.setColor(0xFFD9B1);//灰白色
        mThirdPointPaint.setAlpha(255);//设置不透明度：透明为0，完全不透明为255
        initBubbles();
    }

    public void initBubbles() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firstPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
                isCanDrawFirstPoint = true;
                startFirstPointAnimation();
            }
        }, 1000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
                isCanDrawSecondPoint = true;
                startSecondPointPointAnimation();
            }
        }, 2000);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                thirdPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
                isCanDrawThirdPoint = true;
                startThirdPointAnimation();
            }
        }, 3000);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (isCanDrawFirstPoint) {
            drawCircle(canvas, firstPoint, mFirstCurrentRadius, mFirstPointPaint);
        }
        if (isCanDrawSecondPoint) {
            drawCircle(canvas, secondPoint, mSecondCurrentRadius, mSecondPointPaint);
        }
        if (isCanDrawThirdPoint) {
            drawCircle(canvas, thirdPoint, mThirdCurrentRadius, mThirdPointPaint);
        }
    }

    private void drawCircle(Canvas canvas, Point point, float mCurrentRadius, Paint mPaint) {
        float x = point.getX();
        float y = point.getY();
        canvas.drawCircle(x, y, mCurrentRadius, mPaint);
    }

    private void startFirstPointAnimation() {
        Point startPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
        Point endPoint = new Point(getWidth() / 2, -mMaxRadius);
        //泡泡上升轨迹动画
        ValueAnimator bubbleUpAnim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        bubbleUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                firstPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        bubbleUpAnim.setDuration(5000);
        bubbleUpAnim.setRepeatCount(Animation.INFINITE);
        bubbleUpAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleUpAnim.start();

        //半径变小的动画
        ValueAnimator bubbleRadiusDecAnim = ValueAnimator.ofFloat(mMaxRadius, mMinRadius);
        bubbleRadiusDecAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentRadius = (float) animation.getAnimatedValue();
                if (currentRadius != mMinRadius) {
                    mFirstCurrentRadius = currentRadius;
                } else {
                    mFirstCurrentRadius = 0;
                }
            }
        });
        bubbleRadiusDecAnim.setDuration(5000);
        bubbleRadiusDecAnim.setRepeatCount(Animation.INFINITE);
        bubbleRadiusDecAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleRadiusDecAnim.start();

    }


    private void startSecondPointPointAnimation() {
        Point startPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
        Point endPoint = new Point(getWidth() / 2, -mMaxRadius);
        //泡泡上升轨迹动画
        ValueAnimator bubbleUpAnim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        bubbleUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                secondPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        bubbleUpAnim.setDuration(5000);
        bubbleUpAnim.setRepeatCount(Animation.INFINITE);
        bubbleUpAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleUpAnim.start();

        //半径变小的动画
        ValueAnimator bubbleRadiusDecAnim = ValueAnimator.ofFloat(mMaxRadius, mMinRadius);
        bubbleRadiusDecAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentRadius = (float) animation.getAnimatedValue();
                if (currentRadius != mMinRadius) {
                    mSecondCurrentRadius = currentRadius;
                } else {
                    mSecondCurrentRadius = 0;
                }
            }
        });
        bubbleRadiusDecAnim.setDuration(5000);
        bubbleRadiusDecAnim.setRepeatCount(Animation.INFINITE);
        bubbleRadiusDecAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleRadiusDecAnim.start();

    }


    private void startThirdPointAnimation() {
        Point startPoint = new Point(getWidth() / 2, getHeight() + mMaxRadius);
        Point endPoint = new Point(getWidth() / 2, -mMaxRadius);
        //泡泡上升轨迹动画
        ValueAnimator bubbleUpAnim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        bubbleUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                thirdPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        bubbleUpAnim.setDuration(5000);
        bubbleUpAnim.setRepeatCount(Animation.INFINITE);
        bubbleUpAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleUpAnim.start();

        //半径变小的动画
        ValueAnimator bubbleRadiusDecAnim = ValueAnimator.ofFloat(mMaxRadius, mMinRadius);
        bubbleRadiusDecAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentRadius = (float) animation.getAnimatedValue();
                if (currentRadius != mMinRadius) {
                    mThirdCurrentRadius = currentRadius;
                } else {
                    mThirdCurrentRadius = 0;
                }
            }
        });
        bubbleRadiusDecAnim.setDuration(5000);
        bubbleRadiusDecAnim.setRepeatCount(Animation.INFINITE);
        bubbleRadiusDecAnim.setRepeatMode(ValueAnimator.RESTART);
        bubbleRadiusDecAnim.start();

    }

    /**
     * 用于表示点的内部类
     */
    public class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    /**
     * 用于计算平滑过渡的值
     */
    public class PointEvaluator implements TypeEvaluator {

        /* @Override
         public Object evaluate(float fraction, Object startValue, Object endValue) {
             Point startPoint = (Point) startValue;
             Point endPoint = (Point) endValue;
             float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
             float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
             Point point = new Point(x, y);
             return point;
         }*/


        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            final float t = fraction;
            float oneMinusT = 1.0f - t;
            Point point = new Point(0, 0);

            Point point0 = (Point) startValue;

//            Point point1 = new Point(getWidth(), 0);
            Point point1 = new Point(0, getHeight() / 2);

            Point point2 = new Point(getWidth(), getHeight() / 2);

            Point point3 = (Point) endValue;

            point.x = oneMinusT * oneMinusT * oneMinusT * (point0.x)
                    + 3 * oneMinusT * oneMinusT * t * (point1.x)
                    + 3 * oneMinusT * t * t * (point2.x)
                    + t * t * t * (point3.x);

            point.y = oneMinusT * oneMinusT * oneMinusT * (point0.y)
                    + 3 * oneMinusT * oneMinusT * t * (point1.y)
                    + 3 * oneMinusT * t * t * (point2.y)
                    + t * t * t * (point3.y);
            return point;
        }
    }
}
