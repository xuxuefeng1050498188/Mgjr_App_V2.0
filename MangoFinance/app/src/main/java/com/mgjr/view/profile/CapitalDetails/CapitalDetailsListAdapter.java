package com.mgjr.view.profile.CapitalDetails;

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

import com.google.gson.GsonBuilder;
import com.mgjr.R;
import com.mgjr.Utils.DateUtils;
import com.mgjr.Utils.FormatDisplayTime;
import com.mgjr.model.bean.CapitalDetailsBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.mgjr.R.id.tv_childlist_action;
import static com.mgjr.R.id.tv_childlist_date;

/**
 * Created by Administrator on 2016/9/5.
 */
public class CapitalDetailsListAdapter extends BaseAdapter {
    private Context mContext;
    private ListView lv_capitaldetails;
    public List billlist;
    private LayoutInflater inflater;
    private TextView layout_listtitle;
    final int VIEW_TYPE = 2;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private Map<String, String> monthMap = null;
    private CapitalDetailsBean.BillDetailListBean beanMap = null;
    private boolean isPull;

    private int lastY;

    public CapitalDetailsListAdapter(Context mContext, ListView lv_capitaldetails, TextView layout_listtitle) {
        this.mContext = mContext;
        this.layout_listtitle = layout_listtitle;
        this.lv_capitaldetails = lv_capitaldetails;


        inflater = LayoutInflater.from(mContext);
    }

    public CapitalDetailsListAdapter(Context mContext, List billlist) {
        this.mContext = mContext;
        this.billlist = billlist;
        String s = new GsonBuilder().create().toJson(billlist);
        inflater = LayoutInflater.from(mContext);


    }

    public void setListData(List billlist) {
        this.billlist = billlist;
//        setList();
    }

    @Override
    public int getCount() {
        if (null == billlist) {
            return 0;
        }
        return billlist.size();
    }

    //    每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Map) {
            return TYPE_1;
        } else if (getItem(position) instanceof CapitalDetailsBean.BillDetailListBean) {
            return TYPE_2;
        } else return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder1 holder1 = null;
        viewHolder2 holder2 = null;


        if (position < 0 || billlist == null || position >= billlist.size()) {
            return convertView;
        }

        if (getItem(position) instanceof Map) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listitem_title, null);
                holder1 = new viewHolder1();
                holder1.month_title = (TextView) convertView.findViewById(R.id.tv_capitaldetails_listitem_title);
                convertView.setTag(holder1);
            } else {
                holder1 = (viewHolder1) convertView.getTag();
            }
            monthMap = (Map<String, String>) getItem(position);
            setMonthData(holder1);


        } else if (getItem(position) instanceof CapitalDetailsBean.BillDetailListBean) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_profile_property_exlist_child, null);
                holder2 = new viewHolder2();
                holder2.imgline_top = (ImageView) convertView.findViewById(R.id.imgline_top);
                holder2.imgline_bot = (ImageView) convertView.findViewById(R.id.imgline_bot);
                holder2.imgicon_dot = (ImageView) convertView.findViewById(R.id.imgicon_dot);
                holder2.tv_childlist_date = (TextView) convertView.findViewById(tv_childlist_date);
                holder2.tv_childlist_action = (TextView) convertView.findViewById(tv_childlist_action);
                holder2.tv_childlist_time = (TextView) convertView.findViewById(R.id.tv_childlist_time);
                holder2.tv_childlist_project = (TextView) convertView.findViewById(R.id.tv_childlist_project);
                holder2.tv_childlist_amount = (TextView) convertView.findViewById(R.id.tv_childlist_amount);
                convertView.setTag(holder2);
            } else {
                holder2 = (viewHolder2) convertView.getTag();
            }
            beanMap = (CapitalDetailsBean.BillDetailListBean) getItem(position);
            try {
                initLine(position, holder2);
                setBeanData(holder2, position);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return convertView;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Object item = getItem(firstVisibleItem);
        if (item instanceof Map) {
            int y = lv_capitaldetails.getBottom();
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


    private void initLine(int position, viewHolder2 holder2) {
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
//                }
//            }
        }
    }

    private void setMonthData(viewHolder1 holder1) {

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
                holder1.month_title.setText("本月");

            } else {
                holder1.month_title.setText(month + "月");
            }
        } else {
            holder1.month_title.setText(year + "年" + month + "月");
        }

    }

    private void setBeanData(viewHolder2 holder2, int position) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String s = sf.format(beanMap.getCtime());
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

        if (isPull) {

        }

        if (position >= 1 && position < billlist.size()) {
            CapitalDetailsBean.BillDetailListBean preBean = null;
            CapitalDetailsBean.BillDetailListBean bean = null;
            CapitalDetailsBean.BillDetailListBean lastBean = null;

            String preTime = null;
            String curTime = null;
            String lastTime = null;
            if (getItem(position - 1) instanceof CapitalDetailsBean.BillDetailListBean) {
                preBean = (CapitalDetailsBean.BillDetailListBean) getItem(position - 1);
                preTime = new SimpleDateFormat("yyyy-MM-dd").format(preBean.getCtime());
            }

            if (getItem(position) instanceof CapitalDetailsBean.BillDetailListBean) {
                bean = (CapitalDetailsBean.BillDetailListBean) getItem(position);
                curTime = new SimpleDateFormat("yyyy-MM-dd").format(bean.getCtime());
            }


            if (position != billlist.size() - 1) {
                if (getItem(position + 1) instanceof CapitalDetailsBean.BillDetailListBean) {
                    lastBean = (CapitalDetailsBean.BillDetailListBean) getItem(position + 1);
                    lastTime = new SimpleDateFormat("yyyy-MM-dd").format(lastBean.getCtime());
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
//            if (getItem(position) instanceof CapitalDetailsBean.BillDetailListBean && getItem(position - 1) instanceof CapitalDetailsBean.BillDetailListBean) {
//                CapitalDetailsBean.BillDetailListBean beanList1 = (CapitalDetailsBean.BillDetailListBean) getItem(position);
//                CapitalDetailsBean.BillDetailListBean beanList2 = (CapitalDetailsBean.BillDetailListBean) getItem(position - 1);
//                boolean isLastItem = false;
//                if ((getItem(position + 1) instanceof Map)) {
//                    isLastItem = true;
//                }
//                String time1 = new SimpleDateFormat("yyyy-MM-dd").format(beanList1.getCtime());
//                String time2 = new SimpleDateFormat("yyyy-MM-dd").format(beanList2.getCtime());
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
        int ctype = beanMap.getCtype();
        if (ctype == 1) {
            holder2.tv_childlist_action.setText("收入");
        } else if (ctype == 2) {
            holder2.tv_childlist_action.setText("支出");
        } else if (ctype == 3) {
            holder2.tv_childlist_action.setText("冻结");
        } else if (ctype == 4) {
            holder2.tv_childlist_action.setText("解冻");
        }

        /*project*/
        Map<String, String> remark = beanMap.getRemark();
        holder2.tv_childlist_project.setText(remark.get("message"));
         /*amount*/
        if (ctype == 1) {
            holder2.tv_childlist_amount.setText("+" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#f15b1c"));
        } else if (ctype == 2) {
            holder2.tv_childlist_amount.setText("-" + new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#16b500"));
        } else if (ctype == 3) {
            holder2.tv_childlist_amount.setText(new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#cccccc"));
        } else if (ctype == 4) {
            holder2.tv_childlist_amount.setText(new DecimalFormat("###,###,##0.00").format(beanMap.getAmount()) + "");
            holder2.tv_childlist_amount.setTextColor(Color.parseColor("#333333"));
        }
    }

}

//各个布局的控件资源
class viewHolder1 {
    TextView month_title;
}

class viewHolder2 {
    ImageView imgline_top;
    ImageView imgline_bot;
    ImageView imgicon_dot;
    TextView tv_childlist_date;
    TextView tv_childlist_action;
    TextView tv_childlist_time;
    TextView tv_childlist_project;
    TextView tv_childlist_amount;
}

