package com.mgjr.view.profile.accountsetting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jph.takephoto.permission.PermissionManager;
import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.share.ActionbarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileSelectMangoBabyActivity extends ActionbarActivity {

    private static final int MY_PERMISSIONS_REQUEST = 0;
    private GridView mGridView;
    private int[] babyLogos = {R.drawable.mango_baby_0, R.drawable.mango_baby_1, R.drawable.mango_baby_2, R.drawable.mango_baby_3, R.drawable.mango_baby_4, R.drawable.mango_baby_5, R.drawable.mango_baby_6, R.drawable.mango_baby_7, R.drawable.mango_baby_8,};
    private MyMangoBabyGridViewAdapter adapter;
    private static int MANGO_BABY_LOGO = 4; //选择芒果宝宝返回data
    private int selectPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_select_mango_baby, this);
        selectPosition = (int) SPUtils.get(ProfileSelectMangoBabyActivity.this, "LOGIN", "mango_baby_position", -1);
        actionbar.setCenterTextView("芒果宝宝头像");
        mGridView = (GridView) findViewById(R.id.gridView_mangoBaby);
        adapter = new MyMangoBabyGridViewAdapter();
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //请求动态权限
                    ActivityCompat.requestPermissions(ProfileSelectMangoBabyActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
                    Intent data = new Intent();
                    data.putExtra("mangoBabyLogo", babyLogos[position]);
                    setResult(MANGO_BABY_LOGO, data);
                    SPUtils.put(ProfileSelectMangoBabyActivity.this, "LOGIN", "current_mango_baby_position", position);
                    adapter.notifyDataSetChanged();
//                popActivity();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MyActivityManager.getInstance().popCurrentActivity();
                        }
                    }, 500);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                //检查权限是否被拒绝
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    CommonToastUtils.showToast(ProfileSelectMangoBabyActivity.this, "取消上传头像");
                    return;
                }
            }
            Intent data = new Intent();
            data.putExtra("mangoBabyLogo", babyLogos[selectPosition]);
            setResult(MANGO_BABY_LOGO, data);
            SPUtils.put(ProfileSelectMangoBabyActivity.this, "LOGIN", "current_mango_baby_position", selectPosition);
            adapter.notifyDataSetChanged();
//                popActivity();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyActivityManager.getInstance().popCurrentActivity();
                }
            }, 500);
        }
    }



    class MyMangoBabyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return babyLogos.length;
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.layout_item_baby_logo_gridview, null);
                holder = new ViewHolder(convertView);
                holder.ivMangoBaby.setImageResource(babyLogos[position]);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == selectPosition) {
                holder.ivSelectedMangoBaby.setVisibility(View.VISIBLE);
            } else {
                holder.ivSelectedMangoBaby.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.iv_mango_baby)
            ImageView ivMangoBaby;
            @InjectView(R.id.iv_selected_mango_baby)
            ImageView ivSelectedMangoBaby;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
