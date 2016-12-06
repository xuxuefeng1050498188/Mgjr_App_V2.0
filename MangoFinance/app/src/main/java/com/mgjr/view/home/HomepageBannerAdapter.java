package com.mgjr.view.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.share.CommonWebBrowserActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class HomepageBannerAdapter extends PagerAdapter {
    private Context context;
    private List<String> urlList;
    private List<HomepageRecommendProjectsBean.AppBannersBean> appbannersList;
    private int[] shadows;

    public HomepageBannerAdapter(Context context, int[] shadows) {
        this.context = context;
        this.shadows = shadows;
    }

    public HomepageBannerAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setData(Context context, List<String> urlList, List<HomepageRecommendProjectsBean.AppBannersBean> appbannersList) {
        this.context = context;
        this.urlList = urlList;
        this.appbannersList = appbannersList;


    }

//    public void setShadowImg(int[] shadows) {
//        this.shadows = shadows;
//    }


    /**
     * 返回有多少页
     */
    @Override
    public int getCount() {
        if (urlList == null) {
            return shadows.length;
        } else {
            return urlList.size();
        }
    }

    /**
     * 用于判断ViewPager是否可以复用
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;  // 固定写法
    }

    /**
     * 跟ListView中的Adpater中的getView方法类似，用于创建一个Item
     *
     * @param container ViewPager
     * @param position  要生成item的位置
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams imgParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setBackgroundResource(shadows[position]);

        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = imageView.getParent();
        if (vp != null) {
            ViewGroup vg = (ViewGroup) vp;
            vg.removeView(imageView);
        }
        if (urlList.size() != 0) {
            position = position % urlList.size();
        }
        final int finalPosition = position;
        String url = "";
        if (urlList.size() != 0) {
            url = urlList.get(position);

            Bitmap bitmap = null;

//            Picasso.with(context).load(url).into(imageView);
            Picasso.with(context)
                    .load(url)
                    .placeholder(shadows[position])
                    .error(shadows[position])
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                String to_url = appbannersList.get(finalPosition).getTo_url();
                    HomepageRecommendProjectsBean.AppBannersBean appBannersBean = appbannersList.get(finalPosition);
                    String title = appBannersBean.getTitle();
                    String shareContent = appBannersBean.getShareContent();
                    String shareUrl = null;
                    if (TextUtils.isEmpty(appBannersBean.getShareUrl())) {
                        shareUrl = appBannersBean.getTo_url();
                    } else {
                        shareUrl = appBannersBean.getShareUrl();
                    }
                    EventBus.getDefault().postSticky(appBannersBean);
                    MyActivityManager.getInstance().startNextActivity(CommonWebBrowserActivity.class, title, shareContent, shareUrl);
                }
            });
        }
//        if (urlList == null) {
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setBackgroundResource(shadows[position]);
////            Picasso.with(context).load(shadows[position]).into(imageView);
//        }
        container.addView(imageView);
        return imageView;


    }

    /**
     * 销毁一个Item
     *
     * @param container ViewPager
     * @param position  要销毁item的位置
     * @param object    instantiateItem方法的返回值
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
