package com.mgjr.view.profile.servicecenter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PhoneUtils;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.OptionCollectBean;
import com.mgjr.presenter.impl.OptionCollectPresenteImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.APIBuilder;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.SubmitButton;
import com.mgjr.view.listeners.ViewListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFeedBackActivity extends ActionbarActivity implements View.OnClickListener, ViewListener<OptionCollectBean>, TextWatcher {
    //输入框
    private EditText et_feedback_content;
    //字数提示
    private TextView tv_textaccount;
    //提交按钮
    private SubmitButton btn_commit;

    private OptionCollectPresenteImpl optionCollectPresente;

    private CharSequence temp;
    private int selectionStart;
    private int selectionEnd;
    private int num = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_feed_back, this);
        actionbar.setCenterTextView("意见反馈");
        initViews();
        setEditStatus();
    }


    private void initViews() {
        optionCollectPresente = new OptionCollectPresenteImpl(this);
        et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
        tv_textaccount = (TextView) findViewById(R.id.tv_textaccount);
        btn_commit = (SubmitButton) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
    }

    private void setEditStatus() {
        et_feedback_content.addTextChangedListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btn_commit) {
            String describe = et_feedback_content.getText().toString();
            if (TextUtils.isEmpty(describe)) {
                CommonToastUtils.showToast(this, "反馈意见不能为空");
                return;
            }
            if (describe.length() < 8) {
                CommonToastUtils.showToast(this, "反馈意见不能字数不能小于8个字");
                return;
            }
            requestNetworkData();
            btn_commit.submit();
            btn_commit.setClickable(false);
        }
    }

    private void requestNetworkData() {
        String title = "Android用户意见";
        String describe = et_feedback_content.getText().toString();
        String client_type = APIBuilder.getDevice();
        String mobile_device = android.os.Build.MODEL;  //设备型号
        String mobile_sys_version = android.os.Build.VERSION.RELEASE; //系统版本
        String version = PhoneUtils.getVersion();
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("describe", describe);
        params.put("client_type", client_type);
        params.put("mobile_device", mobile_device);
        params.put("mobile_sys_version", mobile_sys_version);
        params.put("version", version);
        if (mid.equalsIgnoreCase("0")) {
            mid = "";
            params.put("mid", mid);
        } else {
            params.put("mid", mid);
        }

//        Map<String, String> unnecessaryParams = new HashMap<>();
//        unnecessaryParams.put("mid", mid);

        optionCollectPresente.sendRequest(params, null);

    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
        dismissLoadingDialog();
        btn_commit.finish("提交");
        btn_commit.setClickable(true);

    }

    @Override
    public void showError(OnPresenterListener listener, OptionCollectBean optionCollectBean) {
        dismissLoadingDialog();
        btn_commit.finish("提交");
        btn_commit.setClickable(true);
    }

    @Override
    public void responseData(OnPresenterListener listener, OptionCollectBean optionCollectBean) {
        btn_commit.finish("提交");
        btn_commit.setClickable(true);
        CommonToastUtils.showSuccessToast("提交成功");
        MyActivityManager.getInstance().popCurrentActivity();
    }

    /*监听输入字数*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        int number = num - s.length();
        tv_textaccount.setText("" + number + "/" + num);
        selectionStart = et_feedback_content.getSelectionStart();
        selectionEnd = et_feedback_content.getSelectionEnd();
        //System.out.println("start="+selectionStart+",end="+selectionEnd);
        if (temp.length() > num) {
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionStart;
            et_feedback_content.setText(s);
            et_feedback_content.setSelection(tempSelection);//设置光标在最后
        }
    }
}
