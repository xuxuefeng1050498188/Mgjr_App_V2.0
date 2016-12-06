package com.mgjr.Utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mgjr.view.invester.InvestProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class SimpleViewPagerAdapter extends PagerAdapter {

    private List<View> views;
    private List<String> codes;
    private List<String> types;


    public SimpleViewPagerAdapter(List<View> views) {
        this.views = views;
        types = new ArrayList<>();
        types.add("0");
        types.add("1");
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = views.get(position);
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
            v.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (types != null && views.size() > 0 && codes != null && types != null){
                                MyActivityManager.getInstance().startNextActivity(InvestProductDetailActivity.class, codes.get(position), types.get(position));
                            }
                        }
                    }
            );
        container.addView(views.get(position));

        return views.get(position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
