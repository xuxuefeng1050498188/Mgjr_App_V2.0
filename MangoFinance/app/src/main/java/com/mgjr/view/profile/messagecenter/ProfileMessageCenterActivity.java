package com.mgjr.view.profile.messagecenter;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mgjr.Utils.MyActivityManager;
import com.mgjr.R;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.PagerSlidingTabStrip;

public class ProfileMessageCenterActivity extends ActionbarActivity {

    private ViewPager viewpager_profile_message_center;
    private PagerSlidingTabStrip pager_sliding_tabs;
    String[] titles = {"投资信息", "平台公告"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_message_center, this);
        actionbar.setCenterTextView("消息中心");
        actionbar.setRightImageView(R.drawable.profile_message_center_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(ProfileInfomationSettingActivity.class);
            }
        });

        viewpager_profile_message_center = (ViewPager) findViewById(R.id.viewpager_profile_message_center);
        viewpager_profile_message_center.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ListView listView = new ListView(container.getContext());
                if (position == 0) {
                    listView.setAdapter(new ProfileMessageCenterInvestInfosAdapter());
                } else if (position == 1) {
                    listView.setAdapter(new ProfileMessageCenterAnnouncementAdapter());
                }
                container.addView(listView);
                return listView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        pager_sliding_tabs = (PagerSlidingTabStrip) findViewById(R.id.pager_sliding_tabs);
        pager_sliding_tabs.setViewPager(viewpager_profile_message_center);
    }
}
