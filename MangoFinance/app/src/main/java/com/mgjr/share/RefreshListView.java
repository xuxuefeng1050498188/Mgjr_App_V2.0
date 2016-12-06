package com.mgjr.share;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.share.addresswheel_master.utils.Utils;

/**
 * Created by wim on 16/5/25.
 */
public class RefreshListView extends ListView implements OnScrollListener {

    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private RelativeLayout headerView;
    private LinearLayout footerView;
    private TextView tv_load;

    private ImageView goldImageView;
    private ImageView shadowImageView;
    private OnScrollViewListener onScrollViewListener;

    //    private RefreshBgView bg_view;
//    private AnimationDrawable bg_viewAnimation;
//    private RefreshAnimView refreshAnimView;
//    private RefreshLoadingView refreshLoadingView;
//    private AnimationDrawable loadAnimation;
    private int headviewHeight;
    private int footviewHeight;
    private LayoutInflater mInflater;

    private int totalcount;
    private boolean isScrollFirst;
    private boolean isScrollLast;

    private boolean isRefreshable;
    private int refreshstate;
    private int loadstate;
    public boolean isLoadable;

    private static final float REFRESH_RATIO = 2.0f;
    private static final float LOAD_RATIO = 3;

    private static final int REFRESH_DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;

    private static final int LOAD_DONE = 4;
    private static final int PULL_TO_LOAD = 5;
    private static final int RELEASE_TO_LOAD = 6;
    private static final int LOADING = 7;

    AnimatorSet set;
    ObjectAnimator move;
    ObjectAnimator scale;

    private float startY,
            offsetY;



    public RefreshListView(Context context) {

        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        mInflater = LayoutInflater.from(context);
//        headerView=(RelativeLayout) View.inflate(context, R.layout.refresh_footer_layout,null);
//        footerView=(LinearLayout)View.inflate(context,R.layout.refresh_footer_layout,null);
        headerView = (RelativeLayout)mInflater.inflate(R.layout.refresh_header_layout,null);
        footerView = (LinearLayout)mInflater.inflate(R.layout.refresh_footer_layout,null);

        tv_load = (TextView) footerView.findViewById(R.id.tv_load);

        goldImageView = (ImageView) headerView.findViewById(R.id.gold_logo);
        shadowImageView = (ImageView) headerView.findViewById(R.id.shadow_logo);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR2){
            meaSureView(headerView);
            meaSureView(footerView);

            addHeaderView(headerView);
            addFooterView(footerView);

            headviewHeight = headerView.getMeasuredHeight();
            footviewHeight = footerView.getMeasuredHeight();
        }
        else {
            addHeaderView(headerView);
            addFooterView(footerView);

            headviewHeight = Utils.dipToPx(context,100);
            footviewHeight = Utils.dipToPx(context,50);
        }


        headerView.setPadding(0,-headviewHeight,0,0);
        footerView.setPadding(0,0,0,-footviewHeight);

        refreshstate = REFRESH_DONE;//开始下拉完成
        loadstate = LOAD_DONE;//上拉完成

        isRefreshable = true; //可以下拉
        isLoadable = true; //可以上拉

        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setOnScrollListener(this);

    }



    /**
     * 计算控件宽高
     *
     * @param child
     */

    private void meaSureView(final View child){
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        totalcount=totalItemCount;

        if (null != onScrollViewListener){
            onScrollViewListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }


        //判断是不是滑到最顶端或者最低端
        if(firstVisibleItem==0){
            isScrollFirst=true;
        }else {

            isScrollFirst=false;
        }
        if(firstVisibleItem+visibleItemCount==totalcount){
            isScrollLast=true;

        }else {

            isScrollLast=false;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY=ev.getY()-startY;

                /**
                 * 下拉刷新
                 */
                if(isRefreshable && refreshstate!=REFRESHING && isScrollFirst && offsetY>0 && loadstate==LOAD_DONE){
                    float headViewShowHeigt=offsetY/REFRESH_RATIO;//headview露出的高度
                    float currentProgress = headViewShowHeigt /headviewHeight;//根据此比例，在滑动时改变动图大小
                    if(currentProgress>=1){

                        currentProgress=1;
                    }

                    switch (refreshstate){
                        case REFRESH_DONE:
                            refreshstate = PULL_TO_REFRESH;
                            break;
                        case PULL_TO_REFRESH:
                            setSelection(0);
                            //当state=PULL_TO_REFRESH时，如果headerViewShowHeight超过了headerViewHeight，那么此时已经达到可刷新状态了，
                            //意思是准备刷新中，如果此时用户松手，则执行刷新操作
                            if(headViewShowHeigt-headviewHeight>0) {
                                refreshstate = RELEASE_TO_REFRESH;
                                changeHeaderByState(refreshstate);
                            }
                            break;
                        case RELEASE_TO_REFRESH:
                            setSelection(0);
                            if(headViewShowHeigt-headviewHeight<0){

                                refreshstate=PULL_TO_REFRESH;
                                changeHeaderByState(refreshstate);
                            }
                            break;
                    }

                    if(refreshstate==RELEASE_TO_REFRESH||refreshstate==PULL_TO_REFRESH){
                        //设置离父view的距离
                        headerView.setPadding(0,(int)(headViewShowHeigt-headviewHeight),0,0);
//                        refreshAnimView.setCurrentProgress(currentProgress);//绘制headview中的动画
//                        refreshAnimView.postInvalidate();
                    }

                }

                if(isLoadable && isScrollLast && offsetY<0 && loadstate!=LOADING && refreshstate==REFRESH_DONE){
                    float footershowViewHieght=-offsetY/LOAD_RATIO;
                    switch (loadstate){
                        case  LOAD_DONE:
                            loadstate=PULL_TO_LOAD;
                            break;
                        case PULL_TO_LOAD:
                            setSelection(totalcount);
                            if(footershowViewHieght-footviewHeight>0) {
                                loadstate = RELEASE_TO_LOAD;
                                changeFooterByState(loadstate);
                            }
                            break;
                        case RELEASE_TO_LOAD:
                            setSelection(totalcount);
                            if(footershowViewHieght-footviewHeight<0){
                                loadstate=PULL_TO_REFRESH;
                                changeFooterByState(loadstate);
                            }
                            break;
                    }
                    if(loadstate==PULL_TO_LOAD||loadstate==RELEASE_TO_LOAD){
                        footerView.setPadding(0,0,0,(int)(footershowViewHieght-footviewHeight));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 下拉刷新
                 */
                if(isRefreshable) {
                    if (refreshstate == PULL_TO_REFRESH) {

                        refreshstate = REFRESH_DONE;
                        changeHeaderByState(refreshstate);
                    }
                    if (refreshstate == RELEASE_TO_REFRESH) {
                        refreshstate = REFRESHING;
                        changeHeaderByState(refreshstate);
                        startAnimation();
                        mOnRefreshListener.onRefresh();
                    }
                }

                /**
                 * 上拉加载
                 */
                if (isLoadable) {//只有当启用上拉加载时触发
                    if (loadstate == PULL_TO_LOAD) {
                        loadstate = LOAD_DONE;
                        changeFooterByState(loadstate);
                    }
                    else if (loadstate == RELEASE_TO_LOAD) {
                        loadstate = LOADING;
                        changeFooterByState(loadstate);
                        mOnLoadMoreListener.OnLoadMore();
                    }
                    else {
                        loadstate = LOAD_DONE;
                        changeFooterByState(loadstate);

                    }

                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setmOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setmOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }
    /**
     * 下拉刷新完成
     */
    public void setOnRefreshComplete() {
        refreshstate = REFRESH_DONE;

//        set.cancel();

        if (null != set){
            set.cancel();
        }


        changeHeaderByState(refreshstate);
    }

    /**
     * 加载更多完成
     */
    public void setOnLoadMoreComplete() {
        loadstate = LOAD_DONE;
        changeFooterByState(loadstate);
    }

    private void changeHeaderByState(int state) {
        switch (state) {
            case REFRESH_DONE:
                headerView.setPadding(0, -headviewHeight, 0, 0);
//                refreshAnimView.setVisibility(View.VISIBLE);
//                refreshLoadingView.setVisibility(View.GONE);
//                loadAnimation.stop();
                break;
            case RELEASE_TO_REFRESH:
                Log.e("jj", "RELEASE_TO_REFRESH");
                break;
            case PULL_TO_REFRESH:
                Log.e("jj", "PULL_TO_REFRESH");
                break;
            case REFRESHING:
//                refreshAnimView.setVisibility(View.GONE);
//                refreshLoadingView.setVisibility(View.VISIBLE);
//                loadAnimation.start();
                headerView.setPadding(0, 0, 0, 0);
                break;
            default:
                break;
        }
    }

    private void changeFooterByState(int loadstate){

        switch (loadstate){
            case LOAD_DONE:
                footerView.setPadding(0,0,0,-footviewHeight);
                tv_load.setText("上拉加载更多");
                break;
            case PULL_TO_LOAD:
                tv_load.setText("上拉加载更多");
                break;
            case RELEASE_TO_LOAD:
                tv_load.setText("松开加载更多");
                break;
            case LOADING:
                tv_load.setText("正在加载...");
                footerView.setPadding(0, 0, 0, 0);
                break;
            default:
                break;
        }

    }

    private void startAnimation() {
        move = ObjectAnimator.ofFloat(goldImageView, "y", 0f, shadowImageView.getY() - goldImageView.getHeight(), 0f);
        move.setDuration(1000);
        move.setRepeatCount(100);
        scale = ObjectAnimator.ofFloat(shadowImageView, "scaleX", 1.0f, 0.3f, 1.0f);
        scale.setDuration(1000);
        scale.setRepeatCount(100);
        set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(move, scale);
        set.start();
    }

    //定义刷新监听器
    public interface OnRefreshListener {

        void onRefresh();
    }

    //设置下载更多监听器
    public interface OnLoadMoreListener {


        void OnLoadMore();
    }

    public interface OnScrollViewListener{
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.onScrollViewListener = onScrollViewListener;
    }
}
