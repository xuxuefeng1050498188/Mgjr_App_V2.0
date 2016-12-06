package com.mgjr.view.profile.activityzone;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.callback.BitmapCallback;
import com.mgjr.R;
import com.mgjr.model.bean.EventsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.Call;

/**
 * Created by xuxuefeng on 2016/8/1.
 */
public class ProfileEventsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<EventsBean.ActivityListBean> list;


    public ProfileEventsListAdapter(List<EventsBean.ActivityListBean> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_profile_events_list, null);
            viewHolder.layout_eventisfinish = (FrameLayout) convertView.findViewById(R.id.layout_eventisfinish);
            viewHolder.img_events_content = (ImageView) convertView.findViewById(R.id.img_events_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String url = list.get(position).getImage_url();
        int status = list.get(position).getStatus();

        if (status == 2) {
            viewHolder.layout_eventisfinish.setVisibility(View.VISIBLE);
        } else {
            viewHolder.layout_eventisfinish.setVisibility(View.GONE);
        }
        //加载图片

        /*HttpClient
                .get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        String s = e.toString();
                    }

                    @Override
                    public void onResponse(Bitmap response) {

                        viewHolder.img_events_content.setImageBitmap(response);

                    }
                });*/

        Picasso.with(parent.getContext())
                .load(url)
                .into(viewHolder.img_events_content);


        return convertView;
    }


    final class ViewHolder {
        FrameLayout layout_eventisfinish;
        ImageView img_events_content;
    }
}
