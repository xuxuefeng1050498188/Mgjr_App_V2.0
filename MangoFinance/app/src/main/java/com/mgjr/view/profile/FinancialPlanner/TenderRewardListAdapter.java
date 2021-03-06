package com.mgjr.view.profile.FinancialPlanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.DateUtils;
import com.mgjr.Utils.FormatDisplayTime;
import com.mgjr.model.bean.TenderRewardListBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/10/10.
 */

public class TenderRewardListAdapter extends BaseAdapter {
    private Context context;
    public List billlist;
    private LayoutInflater inflater;
    private TextView layout_listtitle;
    private ListView lv_tenderlist;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private Map<String, String> monthMap = null;
    private TenderRewardListBean.LcsTenderRewardListBean beanMap = null;
    private TenderRewardListBean tenderRewardListBean;
    private String mobile;
    private double lastY;

    TenderRewardListAdapter(Context context, TextView layout_listtitle) {
        this.context = context;
        this.layout_listtitle = layout_listtitle;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == billlist) {
            return 0;
        }
        return billlist.size();
    }

    @Override
    public Object getItem(int position) {
        if (billlist.size() == 0) {
            return null;
        } else {
            return billlist.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Map) {
            return TYPE_1;
        } else if (getItem(position) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
            return TYPE_2;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;

        if (position < 0 || billlist == null || position >= billlist.size()) {
            return convertView;
        }
        if (getItem(position) instanceof Map) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_lcs_monthlist, null);
                holder1 = new ViewHolder1();
                holder1.tv_monthtitle = (TextView) convertView.findViewById(R.id.tv_monthtitle);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
            monthMap = (Map<String, String>) getItem(position);
            setMonthData(holder1);


        } else if (getItem(position) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_lcs_rewardlist, null);
                holder2 = new ViewHolder2();
                holder2.imgline_top = (ImageView) convertView.findViewById(R.id.imgline_top);
                holder2.imgline_bot = (ImageView) convertView.findViewById(R.id.imgline_bot);
                holder2.imgicon_dot = (ImageView) convertView.findViewById(R.id.imgicon_dot);
                holder2.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                holder2.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder2.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder2.tv_planner = (TextView) convertView.findViewById(R.id.tv_planner);
                holder2.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
            beanMap = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position);
            initLine(position, holder2);
            setBeanData(holder2, position);
        }
        setList();
        return convertView;
    }

    private void setList() {
        lv_tenderlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Object item = getItem(firstVisibleItem);
                if (item instanceof Map) {
                    int y = lv_tenderlist.getBottom();
                    if (lastY <= y && firstVisibleItem == 0) {
                        layout_listtitle.setVisibility(View.GONE);
                    } else {
                        if (billlist != null) {
                            layout_listtitle.setVisibility(View.VISIBLE);
                        }
                    }
                    lastY = y;


                    String date = (String) ((Map) item).get("date");
                    Date cDate = new Date();
                    String cYear = new SimpleDateFormat("yyyy").format(cDate);
                    String cMonth = new SimpleDateFormat("MM").format(cDate);
                    String year = date.substring(0, 4);
                    String month = date.substring(5, 7);

                    String m = month.substring(0, 1);
                    if (m.equalsIgnoreCase("0")) {
                        month = date.substring(6, 7);
                    }

                    if (year.equalsIgnoreCase(cYear)) {
                        if (month.equalsIgnoreCase(cMonth)) {
                            layout_listtitle.setText("本月");

                        } else {
                            layout_listtitle.setText(month + "月");
                        }
                    } else {
                        layout_listtitle.setText(year + "年" + month + "月");
                    }
                } else {
                    for (int i = firstVisibleItem; i > 0 && i < totalItemCount; i--) {
                        if (billlist.size() != 0) {
                            Object monthitem = billlist.get(i);
                            if (monthitem instanceof Map) {

                                String date = (String) ((Map) monthitem).get("date");
                                Date cDate = new Date();
                                String cYear = new SimpleDateFormat("yyyy").format(cDate);
                                String cMonth = new SimpleDateFormat("MM").format(cDate);
                                String year = date.substring(0, 4);
                                String month = date.substring(5, 7);
                                String m = month.substring(0, 1);
                                if (m.equalsIgnoreCase("0")) {
                                    month = date.substring(6, 7);
                                }
                                if (year.equalsIgnoreCase(cYear)) {
                                    if (month.equalsIgnoreCase(cMonth)) {
                                        layout_listtitle.setText("本月");

                                    } else {
                                        layout_listtitle.setText(month + "月");
                                    }
                                } else {
                                    layout_listtitle.setText(year + "年" + month + "月");
                                }

                                return;
                            }
                        }
                    }
                    if (billlist != null) {
                        layout_listtitle.setVisibility(View.VISIBLE);
                    }

                }

                if (billlist.size() == 0) {
                    layout_listtitle.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//        Object item = getItem(firstVisibleItem);
//        if (item instanceof Map) {
//            int y = lv_tenderlist.getBottom();
//            if (lastY <= y && firstVisibleItem == 0) {
//                layout_listtitle.setVisibility(View.GONE);
//            } else {
//                if (billlist != null) {
//                    layout_listtitle.setVisibility(View.VISIBLE);
//                }
//            }
//            lastY = y;
//
//
//            String date = (String) ((Map) item).get("date");
//            Date cDate = new Date();
//            String cYear = new SimpleDateFormat("yyyy").format(cDate);
//            String cMonth = new SimpleDateFormat("MM").format(cDate);
//            String year = date.substring(0, 4);
//            String month = date.substring(5, 7);
//
//            String m = month.substring(0, 1);
//            if (m.equalsIgnoreCase("0")) {
//                month = date.substring(6, 7);
//            }
//
//            if (year.equalsIgnoreCase(cYear)) {
//                if (month.equalsIgnoreCase(cMonth)) {
//                    layout_listtitle.setText("本月");
//
//                } else {
//                    layout_listtitle.setText(month + "月");
//                }
//            } else {
//                layout_listtitle.setText(year + "年" + month + "月");
//            }
//        } else {
//            for (int i = firstVisibleItem; i > 0 && i < totalItemCount; i--) {
//                if (billlist.size() != 0) {
//                    Object monthitem = billlist.get(i);
//                    if (monthitem instanceof Map) {
//
//                        String date = (String) ((Map) monthitem).get("date");
//                        Date cDate = new Date();
//                        String cYear = new SimpleDateFormat("yyyy").format(cDate);
//                        String cMonth = new SimpleDateFormat("MM").format(cDate);
//                        String year = date.substring(0, 4);
//                        String month = date.substring(5, 7);
//                        String m = month.substring(0, 1);
//                        if (m.equalsIgnoreCase("0")) {
//                            month = date.substring(6, 7);
//                        }
//                        if (year.equalsIgnoreCase(cYear)) {
//                            if (month.equalsIgnoreCase(cMonth)) {
//                                layout_listtitle.setText("本月");
//
//                            } else {
//                                layout_listtitle.setText(month + "月");
//                            }
//                        } else {
//                            layout_listtitle.setText(year + "年" + month + "月");
//                        }
//
//                        return;
//                    }
//                }
//            }
//            if (billlist != null) {
//                layout_listtitle.setVisibility(View.VISIBLE);
//            }
//
//        }
//
//        if (billlist.size() == 0) {
//            layout_listtitle.setVisibility(View.GONE);
//        }
    }


    private void initLine(int position, ViewHolder2 holder2) {
        if (position > 0 && position < getCount()) {
            if (getItem(position - 1) instanceof Map) {
                if (position + 1 < billlist.size() && getItem(position + 1) instanceof Map) {
                    holder2.imgline_bot.setVisibility(View.INVISIBLE);
                    holder2.imgline_top.setVisibility(View.INVISIBLE);
                    holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_fill);
                } else {
                    holder2.imgline_bot.setVisibility(View.VISIBLE);
                    holder2.imgline_top.setVisibility(View.INVISIBLE);
                    holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_fill);
                }
            } else {

                if (position + 1 < billlist.size() && getItem(position + 1) instanceof Map) {
                    holder2.imgline_bot.setVisibility(View.INVISIBLE);
                    holder2.imgline_top.setVisibility(View.VISIBLE);
                    holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_fill);

                } else {
                    holder2.imgline_bot.setVisibility(View.VISIBLE);
                    holder2.imgline_top.setVisibility(View.VISIBLE);
                    holder2.imgicon_dot.setVisibility(View.VISIBLE);
                    holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_hollow);
                }
            }
        }
    }

    private void setBeanData(ViewHolder2 holder2, int position) {
        String rTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(beanMap.getTime());
        /*date*/
        String time = rTime.substring(5, 10);
        try {
            boolean isToday = DateUtils.IsToday(rTime);
            boolean isYesterday = DateUtils.IsYesterday(rTime);
            if (isToday) {
                holder2.tv_date.setText("今天");
            } else if (isYesterday) {
                holder2.tv_date.setText("昨天");
            } else {
                holder2.tv_date.setText(FormatDisplayTime.formatDisplayTime(time, "MM-dd"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder2.tv_date.setText(FormatDisplayTime.formatDisplayTime(time, "MM-dd"));
         /*time*/
        String cTime = rTime.substring(11, 16);
        holder2.tv_time.setText(cTime);
        /*理财师*/
        String tzrUsername = beanMap.getTzrUsername();
        String mobileNum = mobile.substring(0, mobile.length() - (mobile.substring(3)).length()) + "****" + mobile.substring(7);
        if (mobileNum != null) {

            holder2.tv_planner.setText("理财师(" + tzrUsername + ")");
        }
        /*金额*/
        holder2.tv_amount.setText("+" + beanMap.getJlamount() + "");
        holder2.tv_amount.setTextColor(Color.parseColor("#feaa00"));

//        if (position > 1 && position < billlist.size() - 2) {
//            if (getItem(position) instanceof TenderRewardListBean.LcsTenderRewardListBean && getItem(position - 1) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
//                TenderRewardListBean.LcsTenderRewardListBean beanList1 = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position);
//                TenderRewardListBean.LcsTenderRewardListBean beanList2 = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position - 1);
//                boolean isLastItem = false;
//                if ((getItem(position + 1) instanceof Map)) {
//                    isLastItem = true;
//                }
//                String time1 = new SimpleDateFormat("yyyy-MM-dd").format(beanList1.getTime());
//                String time2 = new SimpleDateFormat("yyyy-MM-dd").format(beanList2.getTime());
//                if (time1.equalsIgnoreCase(time2) && !isLastItem) {
//                    holder2.tv_date.setVisibility(View.INVISIBLE);
//                    holder2.imgicon_dot.setVisibility(View.INVISIBLE);
//                } else {
//                    holder2.tv_date.setVisibility(View.VISIBLE);
//                    holder2.imgicon_dot.setVisibility(View.VISIBLE);
//                }
//            }
//        } else {
//            holder2.tv_date.setVisibility(View.VISIBLE);
//            holder2.imgicon_dot.setVisibility(View.VISIBLE);
//        }
        if (position >= 1 && position < billlist.size()) {
            TenderRewardListBean.LcsTenderRewardListBean preBean = null;
            TenderRewardListBean.LcsTenderRewardListBean bean = null;
            TenderRewardListBean.LcsTenderRewardListBean lastBean = null;

            String preTime = null;
            String curTime = null;
            String lastTime = null;
            if (getItem(position - 1) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
                preBean = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position - 1);
                preTime = new SimpleDateFormat("yyyy-MM-dd").format(preBean.getTime());
            }

            if (getItem(position) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
                bean = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position);
                curTime = new SimpleDateFormat("yyyy-MM-dd").format(bean.getTime());
            }


            if (position != billlist.size() - 1) {
                if (getItem(position + 1) instanceof TenderRewardListBean.LcsTenderRewardListBean) {
                    lastBean = (TenderRewardListBean.LcsTenderRewardListBean) getItem(position + 1);
                    lastTime = new SimpleDateFormat("yyyy-MM-dd").format(lastBean.getTime());
                }
            }

            if (curTime.equalsIgnoreCase(preTime) && position != 1) {

                holder2.tv_date.setVisibility(View.INVISIBLE);
                holder2.imgicon_dot.setVisibility(View.INVISIBLE);
            } else {
                holder2.tv_date.setVisibility(View.VISIBLE);
                holder2.imgicon_dot.setVisibility(View.VISIBLE);
            }

            if (position == billlist.size() - 1) {
                holder2.imgicon_dot.setVisibility(View.VISIBLE);
                holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_fill);
                holder2.imgline_bot.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setMonthData(ViewHolder1 holder1) {
        String date = monthMap.get("date");
        Date cDate = new Date();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM");
        sf1.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        sf2.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String cYear = sf1.format(cDate);
        String cMonth = sf2.format(cDate);
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String m = month.substring(0, 1);
        if (m.equalsIgnoreCase("0")) {
            month = date.substring(6, 7);
        }
        if (year.equalsIgnoreCase(cYear)) {
            if (month.equalsIgnoreCase(cMonth)) {
                holder1.tv_monthtitle.setText("本月");

            } else {
                holder1.tv_monthtitle.setText(month + "月");
            }
        } else {
            holder1.tv_monthtitle.setText(year + "年" + month + "月");
        }
    }

    public void setListData(List itemList) {
        this.billlist = itemList;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setListTitle(ListView lv_tenderlist) {
        this.lv_tenderlist = lv_tenderlist;
    }

    class ViewHolder1 {
        TextView tv_monthtitle;
    }

    class ViewHolder2 {
        ImageView imgline_top;
        ImageView imgline_bot;
        ImageView imgicon_dot;
        TextView tv_date;
        TextView tv_type;
        TextView tv_time;
        TextView tv_planner;
        TextView tv_amount;
    }

}
