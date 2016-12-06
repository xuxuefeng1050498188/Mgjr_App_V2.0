package com.mgjr.view.profile.messagecenter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.mgjr.R;

/**
 * Created by xuxuefeng on 2016/8/9.
 */
public class ProfileMessageCenterInvestInfosAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 10;
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
        return View.inflate(parent.getContext(), R.layout.item_profile_infomation_center_invest_info, null);
    }
}
