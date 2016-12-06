package com.mgjr.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;

/**
 * Created by Administrator on 2016/10/24.
 */

public class ImportentNoticeDialog extends Dialog {

    private LinearLayout layout_dialog_type1,layout_dialog_type2;
    private TextView tv_dialog_title;
    private TextView tv_dialog_content;
    private Button btn_dialog;
    private Button btn_time;
    private int type = 1;


    public ImportentNoticeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) MainApplication.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.layout_importentnotice_dialog, null);
        layout_dialog_type1 = (LinearLayout) contentView.findViewById(R.id.layout_dialog_type1);
        layout_dialog_type2 = (LinearLayout) contentView.findViewById(R.id.layout_dialog_type2);
        tv_dialog_title = (TextView) contentView.findViewById(R.id.tv_dialog_title);
        tv_dialog_content = (TextView) contentView.findViewById(R.id.tv_dialog_content);
        btn_time = (Button) contentView.findViewById(R.id.btn_time);
        btn_dialog = (Button) contentView.findViewById(R.id.btn_dialog);

        if(type == 1){
            layout_dialog_type1.setVisibility(View.VISIBLE);
            layout_dialog_type2.setVisibility(View.GONE);
            btn_time.setVisibility(View.VISIBLE);
        }else if(type == 2){
            layout_dialog_type1.setVisibility(View.GONE);
            layout_dialog_type2.setVisibility(View.VISIBLE);
            btn_time.setVisibility(View.GONE);
        }


        setContentView(contentView);
    }
}
