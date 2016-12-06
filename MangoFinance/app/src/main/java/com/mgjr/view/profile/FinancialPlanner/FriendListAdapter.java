package com.mgjr.view.profile.FinancialPlanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.model.bean.FriendListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FriendListAdapter extends BaseAdapter {
    private List<FriendListBean.FriendsListBean> friendsList;
    private Context context;

    public FriendListAdapter(List<FriendListBean.FriendsListBean> friendsList, Context context) {
        this.friendsList = friendsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.itemlayout_friendlist_details, null);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        setListData(position, holder);

        return convertView;
    }

    private void setListData(int position, ViewHolder holder) {
        String s = friendsList.get(position).getMobile();
        String mobile = s.substring(0, s.length() - (s.substring(3)).length()) + "****" + s.substring(7);
        holder.tv_username.setText(mobile);
        holder.tv_time.setText(friendsList.get(position).getCtime());
        holder.tv_amount.setText(friendsList.get(position).getJlamount() + "");
    }

    private class ViewHolder {

        TextView tv_username, tv_time, tv_amount;
    }
}
