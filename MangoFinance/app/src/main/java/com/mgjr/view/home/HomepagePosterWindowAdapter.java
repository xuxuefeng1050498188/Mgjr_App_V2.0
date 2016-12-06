package com.mgjr.view.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mgjr.Utils.MyActivityManager;
import com.mgjr.model.bean.HomepageRecommendProjectsBean;
import com.mgjr.share.CommonWebBrowserActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class HomepagePosterWindowAdapter extends PagerAdapter {
    private Context context;
    private List<String> urlList;
    private List<HomepageRecommendProjectsBean.Posters> postersList;
    private int[] posterBg;

    public HomepagePosterWindowAdapter(Context context, int[] posterBg) {
        this.context = context;
        this.posterBg = posterBg;
    }

    public HomepagePosterWindowAdapter(Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setData(Context context, List<String> urlList, List<HomepageRecommendProjectsBean.Posters> postersList) {
        this.context = context;
        this.urlList = urlList;
        this.postersList = postersList;
    }

    @Override
    public int getCount() {
        if (urlList == null || urlList.size() == 0) {
            return 0;
        }
        return urlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams imgParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(imgParams);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        imageView.setBackgroundResource(posterBg[position]);
        ViewGroup parent = (ViewGroup) imageView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }


        if (urlList != null && urlList.size() != 0) {


            String url = urlList.get(position);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.with(context).load(url).into(imageView);
//            Picasso.with(context)
//                    .load(url)
//                    .placeholder(posterBg[position])
//                    .error(posterBg[position])
//                    .into(imageView);
        }
//


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String to_url = postersList.get(position).getTo_url();
                HomepageRecommendProjectsBean.Posters poster = postersList.get(position);
                String title = poster.getTitle();
                String shareContent = poster.getShareContent();
                String shareUrl = poster.getShareUrl();
                EventBus.getDefault().postSticky(poster);
                MyActivityManager.getInstance().startNextActivity(CommonWebBrowserActivity.class, title, shareContent, shareUrl);

            }
        });


        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
