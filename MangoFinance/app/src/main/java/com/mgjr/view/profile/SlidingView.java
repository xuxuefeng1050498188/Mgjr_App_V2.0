package com.mgjr.view.profile;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.mgjr.R;
import com.mgjr.Utils.ScreenUtils;

/**
 * Created by wim on 16/7/13.
 */
public class SlidingView extends ViewGroup {

    private Scroller mScroller;

    private int mScreenHeight;
    private int mScreenWidth;

    private float startY;

    private int maxTop;
    private int footerHeight;
    private int mTopPadding;

    private int avatorWidth;
    private int avatorHeight;
    private int maxAvatorWidth;

    private int nickOriginTop;
    private float nickEndTop;

    private View whiteView;
    private View headerView;
    private View contentView;
    private View avatorView;
    private View nickTextView;

    private boolean isExpanded;

    public boolean expanded;

    private boolean isFrist;

    private int contentDy;

    private getContentViewTop getTop;

    public SlidingView(Context context) {
        this(context, null, 0);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mScreenWidth = ScreenUtils.getScreenWidth(context);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingView);
        mTopPadding = (int) typeArray.getDimension(R.styleable.SlidingView_mTopPadding, 0);
        maxAvatorWidth = (int) typeArray.getDimension(R.styleable.SlidingView_maxAvatorWidth, 0);

        isFrist = true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFrist) {
        }
        isFrist = false;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (i == 0) {
                maxTop = child.getMeasuredHeight();
                headerView = child;
                headerView.setAlpha(1.0f);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            } else if (i == 1) {
                whiteView = child;
                int bottm = maxTop + child.getMeasuredHeight();
                child.layout(0, maxTop, child.getMeasuredWidth(), bottm);

            } else if (i == 2) {
                footerHeight = child.getMeasuredHeight();
                contentView = child;
                int bottm = maxTop + child.getMeasuredHeight();
                if (expanded) {
                    child.layout(0, maxTop, child.getMeasuredWidth(), bottm);
                } else {
                    child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
                }

            } else if (i == 3) {
                int maxAvatorTop = (maxTop - child.getMeasuredWidth() / 2);
                int left = (mScreenWidth - child.getMeasuredWidth()) / 2;
                child.layout(left, maxAvatorTop, left + child.getMeasuredWidth(), child.getMeasuredHeight() + maxAvatorTop);
                avatorView = child;

                avatorWidth = child.getMeasuredWidth();
                avatorHeight = child.getMeasuredHeight();
            } else if (i == 4) {
                int top = avatorView.getBottom() + 20;
                nickOriginTop = top;

                nickEndTop = (float) (mTopPadding + maxAvatorWidth + 20.0);

                int left = (mScreenWidth - child.getMeasuredWidth()) / 2;
                child.layout(left, top, left + child.getMeasuredWidth(), child.getMeasuredHeight() + top);
                nickTextView = child;
                nickTextView.setAlpha(1);
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(ev.getY() - startY) > 5) {
                return true;
            } else {
                return false;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (Math.abs(ev.getY() - startY) > 5) {
                return true;
            } else {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getRawY();
                contentDy = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getRawY() - startY);
                int left = contentView.getLeft();
                int right = contentView.getRight();
                int top = contentView.getTop() + dy;
                int bottom = contentView.getBottom() + dy;
                if (top < 0) {
                    top = 0;
                    bottom = contentView.getHeight();
                } else if (top > maxTop) {
                    top = maxTop;
                    bottom = maxTop + footerHeight;
                }

                contentView.layout(left, top, right, bottom);

                animateWhiteView(top);

                animateAvatorView(top);
                animateNickLabl();
                animateHeadView();

                startY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (contentView.getTop() < maxTop / 2) {
                    moveView(contentView.getTop(), 0, 300);
                } else {
                    moveView(contentView.getTop(), maxTop, 300);
                }
                getTop.getTop(contentView.getTop());
                break;
        }
        return true;
    }


    public void moveView(int fromValue, int toValue, int time) {
        ObjectAnimator animator = ObjectAnimator.ofInt(contentView, "top", fromValue, toValue);
        animator.setDuration(time);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (int) animation.getAnimatedValue();
                int bottom = contentView.getHeight() + top;
                contentView.layout(contentView.getLeft(), top, contentView.getRight(), bottom);

                int maxAvatorTop = (maxTop - avatorWidth / 2 - mTopPadding);
                int avatorTop = maxAvatorTop * top / maxTop + mTopPadding;
                int avatorwidth = maxAvatorWidth + ((avatorWidth - maxAvatorWidth) * maxAvatorTop * top / maxTop) / maxAvatorTop;
                int avatorLeft = (mScreenWidth - avatorwidth) / 2;
                int avatorRight = avatorLeft + avatorwidth;
                int avatorBottom = avatorTop + avatorwidth;
                avatorView.layout(avatorLeft, avatorTop, avatorRight, avatorBottom);

                animateHeadView();
                animateNickLabl();

                animateWhiteView(top);

            }
        });
        animator.start();

    }

    public void animateWhiteView(int top) {
        int dy = maxTop - top;

        int marginTop = maxTop - whiteView.getHeight();

        int whiteTop = maxTop - dy * marginTop / maxTop;
//        int whiteTop = whiteDy + whiteView.getTop();
        if (whiteTop <= maxTop && whiteTop >= marginTop) {

        }
        whiteView.layout(whiteView.getLeft(), whiteTop, whiteView.getRight(), whiteView.getHeight() + whiteTop);

    }


    public void animateAvatorView(int top) {
        int maxAvatorTop = (maxTop - maxAvatorWidth / 2 - mTopPadding);
        int avatorTop = maxAvatorTop * top / maxTop + mTopPadding;
        int avatorwidth = maxAvatorWidth + ((avatorWidth - maxAvatorWidth) * maxAvatorTop * top / maxTop) / maxAvatorTop;
        int avatorheight = avatorwidth;
        int avatorLeft = (mScreenWidth - avatorwidth) / 2;
        int avatorRight = avatorLeft + avatorwidth;
        int avatorBottom = avatorTop + avatorheight;

        if (avatorTop < mTopPadding) {
            avatorTop = mTopPadding;
            avatorBottom = avatorTop + avatorheight;
        } else if (avatorTop > maxTop - avatorheight / 2) {
            avatorTop = maxTop - avatorheight / 2;
            avatorBottom = maxTop - avatorheight / 2 + avatorheight;
        }

        avatorView.layout(avatorLeft, avatorTop, avatorRight, avatorBottom);
    }

    public void animateNickLabl() {
        int nicktop = avatorView.getBottom() + 20;
        nickTextView.layout(nickTextView.getLeft(), nicktop, nickTextView.getRight(), nickTextView.getHeight() + nicktop);
        float alpha = 1 - (nickOriginTop - nicktop) / (nickOriginTop - nickEndTop);

        nickTextView.setAlpha(alpha);
    }

    public void animateHeadView() {
        float delat = (float) 0.5 / maxTop;
        float alpha = delat * (float) contentView.getTop() + 0.5f;
        headerView.setAlpha(alpha);
    }

    public void expandView(boolean isExpanded) {
        if (isExpanded) {
            moveView(maxTop, 0, 300);
        } else {
            moveView(0, maxTop, 300);
        }
    }

//    public int getContentViewTop() {
//        if (contentView != null) {
//            int ctop = contentView.getTop();
//            return ctop;
//        }else {
//            return -1;
//        }
//    }

    public interface getContentViewTop {
        void getTop(int top);
    }


    public void setGetTop(getContentViewTop getTop) {
        this.getTop = getTop;
    }
}
