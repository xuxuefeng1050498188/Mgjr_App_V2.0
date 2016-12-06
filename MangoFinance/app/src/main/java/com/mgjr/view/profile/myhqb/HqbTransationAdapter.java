package com.mgjr.view.profile.myhqb;

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
import com.mgjr.model.bean.HqbTransactionBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mgjr.R.id.tv_childlist_action;
import static com.mgjr.R.id.tv_childlist_date;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HqbTransationAdapter extends BaseAdapter {

    private Context mContext;
    private ListView listView;
    public List billlist;
    private LayoutInflater inflater;
    private TextView layout_listtitle;
    final int VIEW_TYPE = 2;
    private Map<String, String> monthMap = null;
    private HqbTransactionBean.TransactionDetailListBean beanMap = null;
    private double lastY;
    int TYPE_1 = 0;
    int TYPE_2 = 1;

    public HqbTransationAdapter(Context mContext, ListView listView, TextView layout_listtitle) {
        this.listView = listView;
        this.mContext = mContext;
        this.layout_listtitle = layout_listtitle;
        inflater = LayoutInflater.from(mContext);
    }

    public void setListData(List billlist) {
        this.billlist = billlist;
    }

    @Override
    public int getCount() {
        if (null == billlist) {
            return 0;
        }
        return billlist.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (getItem(position) instanceof Map) {
//            int TYPE_1 = 0;
//            return TYPE_1;
//        } else if (getItem(position) instanceof HqbTransactionBean.TransactionDetailListBean) {
//            int TYPE_2 = 1;
//            return TYPE_2;
//        } else
//            return 0;
        if (getItem(position) instanceof Map) {
            return TYPE_1;
        } else if (getItem(position) instanceof HqbTransactionBean.TransactionDetailListBean) {
            return TYPE_2;
        } else return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public Object getItem(int position) {
        if (billlist != null && billlist.size() == 0) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        if (position < 0 || billlist == null || position >= billlist.size()) {
            return convertView;
        }

        if (getItem(position) instanceof Map) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listitem_title, null);
                holder1 = new ViewHolder1();
                holder1.month_title = (TextView) convertView.findViewById(R.id.tv_capitaldetails_listitem_title);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
            monthMap = (Map<String, String>) getItem(position);
            setMonthData(holder1);


        } else if (getItem(position) instanceof HqbTransactionBean.TransactionDetailListBean) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_profile_property_exlist_child, null);
                holder2 = new ViewHolder2();
                holder2.imgline_top = (ImageView) convertView.findViewById(R.id.imgline_top);
                holder2.imgline_bot = (ImageView) convertView.findViewById(R.id.imgline_bot);
                holder2.imgicon_dot = (ImageView) convertView.findViewById(R.id.imgicon_dot);
                holder2.tv_childlist_date = (TextView) convertView.findViewById(tv_childlist_date);
                holder2.tv_childlist_action = (TextView) convertView.findViewById(tv_childlist_action);
                holder2.tv_childlist_time = (TextView) convertView.findViewById(R.id.tv_childlist_time);
                holder2.tv_childlist_project = (TextView) convertView.findViewById(R.id.tv_childlist_project);
                holder2.tv_childlist_amount = (TextView) convertView.findViewById(R.id.tv_childlist_amount);
                holder2.line_view = (ImageView) convertView.findViewById(R.id.line_view);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }

            if (position == billlist.size() - 1) {
                holder2.line_view.setVisibility(View.GONE);
            } else {
                holder2.line_view.setVisibility(View.VISIBLE);
            }

            beanMap = (HqbTransactionBean.TransactionDetailListBean) getItem(position);
            try {
                initLine(position, holder2);
                setBeanData(holder2, position);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return convertView;
    }

//    private void setList() {
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Object item = getItem(firstVisibleItem);
//                if (item instanceof Map) {
//                    layout_listtitle.setVisibility(View.VISIBLE);
//                    String date = (String) ((Map) item).get("date");
//                    listView.setPadding(0, 0, 0, 0);
//                    Date cDate = new Date();
//                    String cYear = new SimpleDateFormat("yyyy").format(cDate);
//                    String cMonth = new SimpleDateFormat("MM").format(cDate);
//                    String year = date.substring(0, 4);
//                    String month = date.substring(5, 7);
//                    if (year.equalsIgnoreCase(cYear)) {
//                        if (month.equalsIgnoreCase(cMonth)) {
//                            layout_listtitle.setText("本月");
//
//                        } else {
//                            layout_listtitle.setText(month + "月");
//                        }
//                    } else {
//                        layout_listtitle.setText(year + "年" + month + "月");
//                    }
//                } else {
//                    for (int i = firstVisibleItem; i > 0 && i < totalItemCount; i--) {
//                        Object monthitem = billlist.get(i);
//                        if (monthitem instanceof Map) {
//
//                            String date = (String) ((Map) monthitem).get("date");
//                            Date cDate = new Date();
//                            String cYear = new SimpleDateFormat("yyyy").format(cDate);
//                            String cMonth = new SimpleDateFormat("MM").format(cDate);
//                            String year = date.substring(0, 4);
//                            String month = date.substring(5, 7);
//                            if (year.equalsIgnoreCase(cYear)) {
//                                if (month.equalsIgnoreCase(cMonth)) {
//                                    layout_listtitle.setText("本月");
//
//                                } else {
//                                    layout_listtitle.setText(month + "月");
//                                }
//                            } else {
//                                layout_listtitle.setText(year + "年" + month + "月");
//                            }
//
//                            return;
//                        }
//
//                    }
//                    layout_listtitle.setVisibility(View.VISIBLE);
//                    listView.setPadding(0, layout_listtitle.getMeasuredHeight(), 0, 0);
//
//                }
//            }
//        });
//    }

    private void initLine(int position, ViewHolder2 holder2) {
    /*设置指示线*/
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

    private void setMonthData(ViewHolder1 holder1) {

        String date = monthMap.get("date");
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
                holder1.month_title.setText("本月");

            } else {
                holder1.month_title.setText(month + "月");
            }
        } else {
            holder1.month_title.setText(year + "年" + month + "月");
        }

    }

    private void setBeanData(ViewHolder2 holder2, int position) throws ParseException {
        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(beanMap.getTime());
        /*date*/
        String time = s.substring(5, 10);
        boolean isToday = DateUtils.IsToday(s);
        boolean isYesterday = DateUtils.IsYesterday(s);
        holder2.tv_childlist_date.setVisibility(View.VISIBLE);
        if (isToday) {
            holder2.tv_childlist_date.setText("今天");
        } else if (isYesterday) {
            holder2.tv_childlist_date.setText("昨天");
        } else {
            holder2.tv_childlist_date.setText(FormatDisplayTime.formatDisplayTime(time, "MM-dd"));
        }
        if (position >= 1 && position < billlist.size()) {
            HqbTransactionBean.TransactionDetailListBean preBean = null;
            HqbTransactionBean.TransactionDetailListBean bean = null;
            HqbTransactionBean.TransactionDetailListBean lastBean = null;

            String preTime = null;
            String curTime = null;
            String lastTime = null;
            if (getItem(position - 1) instanceof HqbTransactionBean.TransactionDetailListBean) {
                preBean = (HqbTransactionBean.TransactionDetailListBean) getItem(position - 1);
                preTime = new SimpleDateFormat("yyyy-MM-dd").format(preBean.getTime());
            }

            if (getItem(position) instanceof HqbTransactionBean.TransactionDetailListBean) {
                bean = (HqbTransactionBean.TransactionDetailListBean) getItem(position);
                curTime = new SimpleDateFormat("yyyy-MM-dd").format(bean.getTime());
            }


            if (position != billlist.size() - 1) {
                if (getItem(position + 1) instanceof HqbTransactionBean.TransactionDetailListBean) {
                    lastBean = (HqbTransactionBean.TransactionDetailListBean) getItem(position + 1);
                    lastTime = new SimpleDateFormat("yyyy-MM-dd").format(lastBean.getTime());
                }
            }

            if (curTime.equalsIgnoreCase(preTime) && position != 1) {

                holder2.tv_childlist_date.setVisibility(View.INVISIBLE);
                holder2.imgicon_dot.setVisibility(View.INVISIBLE);
            } else {
                holder2.tv_childlist_date.setVisibility(View.VISIBLE);
                holder2.imgicon_dot.setVisibility(View.VISIBLE);
            }

            if (position == billlist.size() - 1) {
                holder2.imgicon_dot.setVisibility(View.VISIBLE);
                holder2.imgicon_dot.setBackgroundResource(R.drawable.bluedot_fill);
                holder2.imgline_bot.setVisibility(View.INVISIBLE);
            }
        }

//        if (position >= 1 && position < billlist.size() - 1) {
//            if (getItem(position) instanceof HqbTransactionBean.TransactionDetailListBean && getItem(position - 1) instanceof HqbTransactionBean.TransactionDetailListBean) {
//                HqbTransactionBean.TransactionDetailListBean beanList1 = (HqbTransactionBean.TransactionDetailListBean) getItem(position);
//                HqbTransactionBean.TransactionDetailListBean beanList2 = (HqbTransactionBean.TransactionDetailListBean) getItem(position - 1);
//                boolean isLastItem = false;
//                if ((getItem(position + 1) instanceof Map)) {
//                    isLastItem = true;
//                }
//                String time1 = new SimpleDateFormat("yyyy-MM-dd").format(beanList1.getTime());
//                String time2 = new SimpleDateFormat("yyyy-MM-dd").format(beanList2.getTime());
//                if (time1.equalsIgnoreCase(time2) && !isLastItem) {
//                    holder2.tv_childlist_date.setVisibility(View.INVISIBLE);
//                    holder2.imgicon_dot.setVisibility(View.INVISIBLE);
//                } else {
//                    holder2.tv_childlist_date.setVisibility(View.VISIBLE);
//                    holder2.imgicon_dot.setVisibility(View.VISIBLE);
//                }
//            }
//        } else {
//            holder2.tv_childlist_date.setVisibility(View.VISIBLE);
//            holder2.imgicon_dot.setVisibility(View.VISIBLE);
//        }
        /*time*/
        String cTime = s.substring(11, 16);
        holder2.tv_childlist_time.setText(cTime);
        /*type*/
        String ctype = beanMap.getType();
        if (ctype.equalsIgnoreCase("转入")) {
            holder2.tv_childlist_action.setText("转入");
        } else if (ctype.equalsIgnoreCase("赎回")) {
            holder2.tv_childlist_action.setText("赎回");
        } else if (ctype.equalsIgnoreCase("收益")) {
            holder2.tv_childlist_action.setText("收益");
        } else if (ctype.equalsIgnoreCase("投资")) {
            holder2.tv_childlist_action.setText("投资");
        } else if (ctype.equalsIgnoreCase("还本")) {
            holder2.tv_childlist_action.setText("还本");
        }

        /*project*/
        String remark = beanMap.getTitle();
        holder2.tv_childlist_project.setText(remark);
         /*amount*/
        if (ctype.equalsIgnoreCase("转入")) {
            holder2.tv_childlist_amount.setText("+" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#f15b1c"));
        } else if (ctype.equalsIgnoreCase("赎回")) {
            holder2.tv_childlist_amount.setText("-" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#16b500"));
        } else if (ctype.equalsIgnoreCase("收益")) {
            holder2.tv_childlist_amount.setText("+" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#f15b1c"));
        } else if (ctype.equalsIgnoreCase("还本")) {
            holder2.tv_childlist_amount.setText("-" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#16b500"));
        } else if (ctype.equalsIgnoreCase("投资")) {
            holder2.tv_childlist_amount.setText("+" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#f15b1c"));
        }
    }


    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Object item = getItem(firstVisibleItem);
        if (item instanceof Map) {

            int y = listView.getBottom();
            if (lastY <= y && firstVisibleItem == 0) {
                layout_listtitle.setVisibility(View.GONE);
            } else {
                if (billlist != null) {
                    layout_listtitle.setVisibility(View.VISIBLE);
                }

            }
            lastY = y;

            String date = (String) ((Map) item).get("date");
            listView.setPadding(0, 0, 0, 0);
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
            if (billlist != null) {
                layout_listtitle.setVisibility(View.VISIBLE);
            }

        }
        if (billlist.size() == 0) {
            layout_listtitle.setVisibility(View.GONE);
        }
    }
}

class ViewHolder1 {
    TextView month_title;
}

class ViewHolder2 {
    ImageView imgline_top;
    ImageView imgline_bot;
    ImageView imgicon_dot;
    TextView tv_childlist_date;
    TextView tv_childlist_action;
    TextView tv_childlist_time;
    TextView tv_childlist_project;
    TextView tv_childlist_amount;
    ImageView line_view;
}
