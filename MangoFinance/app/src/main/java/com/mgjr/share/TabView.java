package com.mgjr.share;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by wim on 16/5/23.
 */
public class TabView extends LinearLayout implements View.OnClickListener{

    public static final String NORMAL_IMAGE = "NORMAL_IMAGE";
    public static final String SELECTED_IMAGE = "SELECTED_IMAGE";
    public static final String TITLE = "TITLE";

    private Context mContext;
    private List<Map<String, Object>> list;

    private LinearLayout rootLinearLayout;
    private LinearLayout.LayoutParams rootParams;

    private TabViewListener listener;

    private TabCell selectedTabCell;

    public TabView(Context context) {
        super(context);
        this.mContext = context;
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;


    }

    public void setup(List<Map<String, Object>> list){
        this.setBackgroundColor(Color.parseColor("#ffffff"));

        this.list = list;

        this.setOrientation(VERTICAL);
        LayoutParams lp = (LayoutParams) this.getLayoutParams();
        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,mContext.getResources().getDisplayMetrics());
        this.setLayoutParams(lp);

        //tab边框线
        ImageView line = new ImageView(mContext);
        line.setBackgroundColor(Color.parseColor("#E3E3E3"));
        LinearLayout.LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT,1);
        line.setLayoutParams(lineParams);

        rootLinearLayout = new LinearLayout(mContext);
        rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLinearLayout.setGravity(Gravity.CENTER);
        rootParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        this.addView(line,lineParams);
        this.addView(rootLinearLayout,rootParams);

        for (int i = 0; i < list.size(); i++){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;

            TabCell tabCell = new TabCell(mContext);
            tabCell.setOnClickListener(this);
            tabCell.setTag(i);
            rootLinearLayout.addView(tabCell,layoutParams);

            Map<String, Object> map = list.get(i);
            int normal = Integer.parseInt(map.get(NORMAL_IMAGE).toString());
            int selected = Integer.parseInt(map.get(SELECTED_IMAGE).toString());
            String title = String.valueOf(map.get(TITLE));

            tabCell.setContent(normal,selected,title);
         }

        this.selectedAtIndex(0);
    }

    @Override
    public void onClick(View v) {
        TabCell curTabCell = (TabCell)v;

        if (selectedTabCell == curTabCell){
            return;
        }

        int count = rootLinearLayout.getChildCount();
        for (int i = 0; i < count; i++){
            Map<String, Object> map = list.get(i);
            TabCell tabCell = (TabCell)rootLinearLayout.getChildAt(i);
            if (tabCell == curTabCell){
                tabCell.setTabSelected(true);
            }
            else {
                tabCell.setTabSelected(false);
            }
        }

        selectedTabCell = curTabCell;
        listener.onTabClick(v);
    }

    public void selectedAtIndex(int index){
        TabCell curTabCell = (TabCell)rootLinearLayout.getChildAt(index);
        onClick(curTabCell);
    }

    public void setListener(TabViewListener listener){
        this.listener = listener;
    }

    public interface TabViewListener{
        public void onTabClick(View v);
    }
}
