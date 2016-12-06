package com.mgjr.share;

/**
 * Created by wim on 16/9/18.
 */
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wim on 16/9/14.
 */
public class LoadingView extends View{

    private Paint paint = null;
    private Path topPath = null;
    private Path bottomPath = null;

    private RectF rectF;
    private int width;
    private int height;

    private Point centerPoint;

    private float curAngle;

    private AnimatorSet animatorSet;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup(){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        bottomPath = new Path();
        topPath = new Path();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int width = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        topPath.reset();
        bottomPath.reset();

        width = getWidth() - 5;
        height = getHeight() - 5;
        rectF = new RectF(5,5,width,height);

        centerPoint = new Point(width/2,height/2);

        topPath.moveTo(centerPoint.x,centerPoint.y);
        topPath.addArc(rectF,180,curAngle);

        bottomPath.moveTo(centerPoint.x,centerPoint.y);
        bottomPath.addArc(rectF,0,curAngle);

        canvas.drawPath(topPath,paint);
        canvas.drawPath(bottomPath,paint);
    }

    public void startAnimation(){

        ValueAnimator pathAnimation = ValueAnimator.ofFloat(0.0f, 170.0f,0.0f);
        pathAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        pathAnimation.setRepeatCount(1000000);
        pathAnimation.setDuration(1500);

        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(0.0f,360.0f);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float)animation.getAnimatedValue();
                setRotation(angle);

            }
        });
        rotationAnimator.setRepeatCount(1000000);
        rotationAnimator.setDuration(1000);

        animatorSet = new AnimatorSet();
        animatorSet.play(pathAnimation).with(rotationAnimator);
        animatorSet.start();
    }

    public void stopAnimation(){
        animatorSet.cancel();
        ValueAnimator rotationAnimator = ValueAnimator.ofFloat(getRotation(),0.0f);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float)animation.getAnimatedValue();
                setRotation(angle);
            }
        });

        ValueAnimator pathAnimation = ValueAnimator.ofFloat(170.0f, 0.0f);
        pathAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = (float)animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet stopAnimatorSet = new AnimatorSet();
        stopAnimatorSet.playTogether(pathAnimation,rotationAnimator);
        stopAnimatorSet.setDuration(600);
        stopAnimatorSet.start();



    }
}