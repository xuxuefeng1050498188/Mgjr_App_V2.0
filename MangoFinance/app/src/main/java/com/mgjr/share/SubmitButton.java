package com.mgjr.share;

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
import android.widget.Button;

/**
 * Created by xuxuefeng on 2016/10/27.
 */

public class SubmitButton extends Button {

    private Paint paint = null;
    private Path topPath = null;
    private Path bottomPath = null;

    private RectF rectF;
    private int width;
    private int height;

    private Point centerPoint;

    private float curAngle;

    private AnimatorSet animatorSet;

    private State mState;
    private ValueAnimator startAnimaton;
    private ValueAnimator stopAnimation;

    private enum State {
        SUBMITING, IDLE, COMPLETE, ERROR
    }

    public SubmitButton(Context context) {
        super(context);
        setup();
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        bottomPath = new Path();
        topPath = new Path();

        mState = State.IDLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == State.SUBMITING) {
            drawSubmitingAnim(canvas);
        }
    }

    public void submit() {
        mState = State.SUBMITING;
        this.setClickable(false);
        this.setText("");
        startAnimation();
        invalidate();
    }


    public void finish(String text) {
        mState = State.IDLE;
        stopAnimation();
        this.setText(text);
        this.setClickable(true);

    }

    public void finish() {
        mState = State.IDLE;
        stopAnimation();
        this.setClickable(true);

    }

    private void drawSubmitingAnim(Canvas canvas) {
        topPath.reset();
        bottomPath.reset();

        width = getWidth() - 5;
        height = getHeight() - 5;
        rectF = new RectF((width - height) / 2, 5, (width + height) / 2, height - 5);

        centerPoint = new Point(width / 2, height / 2);

        topPath.moveTo(centerPoint.x, centerPoint.y);
        topPath.addArc(rectF, 180, curAngle);

        bottomPath.moveTo(centerPoint.x, centerPoint.y);
        bottomPath.addArc(rectF, 0, curAngle);

        canvas.drawPath(topPath, paint);
        canvas.drawPath(bottomPath, paint);
    }


    public void startAnimation() {

        startAnimaton = ValueAnimator.ofFloat(0.0f, 200.0f);
        startAnimaton.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        startAnimaton.setRepeatCount(1000000);
        startAnimaton.setDuration(500);

        /*ValueAnimator rotationAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                setRotation(angle);

            }
        });
        rotationAnimator.setRepeatCount(1000000);
        rotationAnimator.setDuration(1000);

        animatorSet = new AnimatorSet();
        animatorSet.play(pathAnimation).with(rotationAnimator);
        animatorSet.start();*/
        startAnimaton.start();
    }

    public void stopAnimation() {
        if (startAnimaton != null) {
            startAnimaton.cancel();
        }
//        animatorSet.cancel();
        /*ValueAnimator rotationAnimator = ValueAnimator.ofFloat(getRotation(), 0.0f);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                setRotation(angle);
            }
        });*/

        stopAnimation = ValueAnimator.ofFloat(200.0f, 0.0f);
        stopAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        stopAnimation.setDuration(1000);
        stopAnimation.start();
       /* AnimatorSet stopAnimatorSet = new AnimatorSet();
        stopAnimatorSet.playTogether(pathAnimation, rotationAnimator);
        stopAnimatorSet.setDuration(600);
        stopAnimatorSet.start();*/


    }
}
