package com.mgjr.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.ScreenUtils;
import com.mgjr.mangofinance.MainApplication;

/**
 * Created by Administrator on 2016/7/21.
 */
public class CustomCommonDialog extends Dialog {

    private Context context;
    private TextView dialogTitle, dialogContent, confirmBtn, cancelBtn, singleBtn;
    private LinearLayout layout_btn;
    private String title, content, confirmText, cancleText;

    private boolean noTitle = true;
    private boolean isSingleBtn = false;

    private ClickListenerInterface clickListenerInterface;
    private ClickListenerInterfaceSingle clickListenerInterfaceSingle;

    public CustomCommonDialog(boolean isSingleBtn, Context context, String title, String content, String cancleText, boolean noTitle) {
        super(context);
        this.cancleText = cancleText;
        this.content = content;
        this.title = title;
        this.isSingleBtn = isSingleBtn;
        this.noTitle = noTitle;
    }

    public CustomCommonDialog(Context context, String title, String content, boolean noTitle) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.noTitle = noTitle;
    }

    public CustomCommonDialog(Context context, String title, String content, String confirmText, String cancleText, boolean noTitle) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.noTitle = noTitle;
        this.confirmText = confirmText;
        this.cancleText = cancleText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.layout_errodialog, null);
        setContentView(dialogView);
        layout_btn = (LinearLayout) dialogView.findViewById(R.id.layout_btn);
        dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        dialogContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        cancelBtn = (TextView) dialogView.findViewById(R.id.dialog_canclebtn);
        confirmBtn = (TextView) dialogView.findViewById(R.id.dialog_confirmbtn);
        singleBtn = (TextView) dialogView.findViewById(R.id.tv_singlebtn);
        //绑定从构造方法传递过来的数据
        dialogTitle.setText(title);
        dialogContent.setText(content);
        if (confirmText != null) {
            confirmBtn.setText(confirmText);
        }
        if (cancleText != null) {
            cancelBtn.setText(cancleText);
        }

        if (noTitle) {
            dialogTitle.setVisibility(View.GONE);
            RelativeLayout.LayoutParams sp_params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            sp_params.addRule(RelativeLayout.ALIGN_LEFT);
            sp_params.topMargin = ScreenUtils.dipToPx(this.getContext(), 30);
            sp_params.leftMargin = ScreenUtils.dipToPx(this.getContext(), 20);
            sp_params.rightMargin = ScreenUtils.dipToPx(this.getContext(), 20);
            dialogContent.setLayoutParams(sp_params);
            dialogContent.setText(content);
        }
        if (isSingleBtn == true) {
            singleBtn.setVisibility(View.VISIBLE);
            layout_btn.setVisibility(View.GONE);
        } else {
            layout_btn.setVisibility(View.VISIBLE);
            singleBtn.setVisibility(View.GONE);
        }

        singleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterfaceSingle.doSingleBtn();
                ;
            }
        });

        //为确定,取消设置点击事件
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doConfirm();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doCancel();
            }
        });

    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    public interface ClickListenerInterface {

        void doConfirm();

        void doCancel();

    }

    public void setClicklistener(ClickListenerInterfaceSingle clickListenerInterfaceSingle) {
        this.clickListenerInterfaceSingle = clickListenerInterfaceSingle;
    }

    public interface ClickListenerInterfaceSingle {
        void doSingleBtn();
    }
}
