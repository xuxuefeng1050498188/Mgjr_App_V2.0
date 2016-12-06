package com.mgjr.view.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgjr.R;

/**
 * Created by Administrator on 2016/10/11.
 */

public class MsgCenterListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;

    public MsgCenterListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.itemlayout_msgcenter, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*设置数据*/
//        setTextData();
        return convertView;
    }
    class ViewHolder{
        TextView tv_title,tv_msg,tv_time;

    }
}
