package com.mgjr.view.profile.accountsetting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.httpclient.HttpClient;
import com.mgjr.httpclient.request.RequestCall;

/**
 * Created by wim on 16/10/24.
 */

public class UpgradeDialog extends Dialog implements View.OnClickListener{

    private Context mContext;
    private ClickListenerInterface clickListenerInterface;

    public Button cancelButton;
    public Button sureButton;

    public TextView contentTextView;
    public TextView titleTextView;
    public View line;


    public UpgradeDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        setup();
    }

    public UpgradeDialog(Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        setup();
    }

    protected UpgradeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        setup();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setup();
    }

    public void setContent(String title, String content){

        titleTextView.setText(title);
        contentTextView.setText(content);
    }

    public void setup(){
        setCanceledOnTouchOutside(false);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.upgrade_layout, null);
        setContentView(dialogView);

        line = (View)findViewById(R.id.line);

        titleTextView = (TextView)dialogView.findViewById(R.id.title_textview);
        contentTextView = (TextView)dialogView.findViewById(R.id.content_textview);

        cancelButton = (Button)dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        sureButton = (Button)dialogView.findViewById(R.id.sure_button);
        sureButton.setOnClickListener(this);


    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    @Override
    public void onClick(View v) {
        if (v == cancelButton){
            clickListenerInterface.onCancel();
        }
        else if (v == sureButton){
            clickListenerInterface.onSure();
        }
    }

    public void setForceupdate(boolean force){
        if(force){
            cancelButton.setVisibility(View.GONE);

        }
        else {
            cancelButton.setVisibility(View.VISIBLE);
        }
    }


    public interface ClickListenerInterface {
        void onSure();
        void onCancel();
    }
}
