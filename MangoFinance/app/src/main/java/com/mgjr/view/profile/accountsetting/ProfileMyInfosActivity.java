package com.mgjr.view.profile.accountsetting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.mgjr.R;
import com.mgjr.Utils.BitmapAndFileUtils;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.model.bean.BaseBean;
import com.mgjr.presenter.impl.ProfileMyinfosUploadHeadImagePresenterImpl;
import com.mgjr.presenter.impl.ProfileMyinfosUploadNicknamePresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.InputPayPwdPopw;
import com.mgjr.view.listeners.ViewListener;
import com.mylhyl.superdialog.SuperDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ProfileMyInfosActivity extends ActionbarActivity implements ViewListener<BaseBean>, TakePhoto.TakeResultListener, InvokeListener, View.OnClickListener {

    @InjectView(R.id.iv_user_touxiang)
    ImageView ivUserTouxiang;
    @InjectView(R.id.ib_edit_userinfo)
    ImageButton ibEditUserinfo;
    @InjectView(R.id.tv_user_name)
    TextView tvUserName;
    @InjectView(R.id.rl_nickName)
    RelativeLayout rlNickName;
    @InjectView(R.id.tv_user_nickname)
    TextView tvUserNickname;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CropOptions cropOptions;

    private static int MANGO_BABY_LOGO = 4; //选择芒果宝宝返回data
    private ProfileMyinfosUploadNicknamePresenterImpl profileMyinfosUploadNicknamePresenterImpl;
    private ProfileMyinfosUploadHeadImagePresenterImpl profileMyinfosUploadHeadImagePresenterImpl;
    private File headImg;
    private Bitmap headImageBitmap;
    private CompressConfig compressConfig;
    private String nickName;
    private String headImgUrl;

    private InputPayPwdPopw inputNickNamePopw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_infos, this);
        ButterKnife.inject(this);
        actionbar.setCenterTextView("我的信息");

        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setOutputX(300).setOutputY(300).setWithOwnCrop(false).create();
        compressConfig = new CompressConfig.Builder().setMaxSize(400 * 400).setMaxPixel(10000).enablePixelCompress(true).enableQualityCompress(true).create();
        profileMyinfosUploadNicknamePresenterImpl = new ProfileMyinfosUploadNicknamePresenterImpl(this);
        profileMyinfosUploadHeadImagePresenterImpl = new ProfileMyinfosUploadHeadImagePresenterImpl(this);
        //初始化修改昵称弹窗
        initInputNickNamePopw();
    }

    private void initInputNickNamePopw() {
        inputNickNamePopw = new InputPayPwdPopw();
        inputNickNamePopw.setClickBtnListener(new InputPayPwdPopw.ClickBtnListener() {
            @Override
            public void clickConfirmBtn() {
                //提交数据,请求数据
                nickName = inputNickNamePopw.getEtInputTradePwd().getText().toString();

                if (TextUtils.isEmpty(nickName)) {
                    CommonToastUtils.showToast(ProfileMyInfosActivity.this, "昵称不能为空");
                    inputNickNamePopw.getEtInputTradePwd().requestFocus();
                    return;
                } else if (nickName.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
                    // 包含特殊字符
                    CommonToastUtils.showToast(ProfileMyInfosActivity.this, "昵称不能有特殊字符");
                    inputNickNamePopw.getEtInputTradePwd().requestFocus();
                    return;
                } else {
                    //提交数据,请求数据
                    uploadNickname();
                    KeyBoardUtils.closeKeybord(inputNickNamePopw.getEtInputTradePwd(), ProfileMyInfosActivity.this);
                }

            }

            @Override
            public void clickForgetPwdBtn() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindTouxiangData();
    }

    /**
     * 下面八个方法都是TakePhoto框架的调用方法
     *
     * @param outState
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MANGO_BABY_LOGO) {//得到选中的芒果宝宝
            if (data == null) {//用户点击取消则直接返回
                return;
            }
            int mangoBabyLogoId = data.getIntExtra("mangoBabyLogo", 0);
            if (mangoBabyLogoId == 0) {
                return;
            }
            Bitmap bm = BitmapFactory.decodeResource(getResources(), mangoBabyLogoId);
            headImageBitmap = BitmapAndFileUtils.compressImage(bm);
            saveTempBitmap(headImageBitmap);
            headImg = getTempHeadImageFile();
            uploadHeadImage();
        } else {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
            takePhoto.onEnableCompress(compressConfig, true);
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        Bitmap unCompressedHeadImage = BitmapFactory.decodeFile(result.getImage().getPath());
        headImageBitmap = BitmapAndFileUtils.compressImage(unCompressedHeadImage);
        saveTempBitmap(headImageBitmap);
        headImg = getTempHeadImageFile();
        uploadHeadImage();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        CommonToastUtils.showToast(this, "头像保存失败");
    }

    @Override
    public void takeCancel() {
        CommonToastUtils.showToast(this, "头像上传取消");
    }

    private void bindTouxiangData() {
        //设置头像图片和昵称
        setInitHeadImage();
        setNickName();
    }


    private void setInitHeadImage() {
        headImgUrl = (String) SPUtils.get(this, "LOGIN", "headImgUrl", "");
        if (!TextUtils.isEmpty(headImgUrl)) {
            Picasso.with(this)
                    .load(headImgUrl)
                    .placeholder(R.drawable.mango_baby_4)
                    .error(R.drawable.mango_baby_4)
                    .into(ivUserTouxiang);
        } else {
            Picasso
                    .with(this)
                    .cancelRequest(ivUserTouxiang);
            ivUserTouxiang.setImageResource(R.drawable.mango_baby_4);
        }
    }


    @NonNull
    private File getTempHeadImageFile() {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.mgjr.avater");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File picture = new File(tmpDir, "tempavater.png");
        return picture;
    }


    private void setNickName() {
        String nickname = (String) SPUtils.get(this, "LOGIN", "nickname", "");
        if (!TextUtils.isEmpty(nickname)) {
            tvUserNickname.setText(nickname);
        } else {
//            String userName = (String) SPUtils.get(this, "LOGIN", "name", "");
            tvUserNickname.setText("");
        }
        String userName = (String) SPUtils.get(this, "LOGIN", "name", "");
        tvUserName.setText(userName);
    }


    public void uploadHeadImage() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("suffix", ".png");
        necessaryParams.put("headImg", headImg.getAbsolutePath());
        profileMyinfosUploadHeadImagePresenterImpl.sendRequest(necessaryParams, null);
        CommonToastUtils.showToast(this, "正在上传头像...");
    }

    public void uploadNickname() {
        //请求网络
        Map<String, String> necessaryParams = new HashMap<String, String>();
        necessaryParams.put("mid", "" + SPUtils.get(this, "LOGIN", "id", 0));
        necessaryParams.put("nickname", nickName);
        profileMyinfosUploadNicknamePresenterImpl.sendRequest(necessaryParams, null);
        CommonToastUtils.showToast(this, "正在上传昵称...");
    }


    @OnClick({R.id.ib_edit_userinfo, R.id.iv_user_touxiang, R.id.rl_nickName})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_touxiang:
            case R.id.ib_edit_userinfo:
                showPopupWindow();
                break;
            case R.id.rl_nickName:
                View rootView = LayoutInflater.from(this).inflate(R.layout.activity_profile_infos, null);
                inputNickNamePopw.showInputTradePwdPopw(this, rootView);
                changeInputNickNamePopwStyle();
                break;
        }
    }

    private void changeInputNickNamePopwStyle() {
        inputNickNamePopw.getTv_forgetpaypwd().setVisibility(View.INVISIBLE);
        inputNickNamePopw.getDialog_title().setText("设置昵称");
        inputNickNamePopw.getEtInputTradePwd().setText(tvUserNickname.getText());
        inputNickNamePopw.getEtInputTradePwd().setHint("请输入一个昵称");
        inputNickNamePopw.getEtInputTradePwd().setInputType(InputType.TYPE_CLASS_TEXT);
    }



    private void showPopupWindow() {
        final List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("从相册获取");
        list.add("使用芒果宝宝头像");

        new SuperDialog.Builder(this)
//                .setAlpha(0.5f)
                .setCanceledOnTouchOutside(true)
                .setGravity(Gravity.CENTER)
                .setItems(list, new SuperDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                takePhoto.onEnableCompress(compressConfig, true);
                                takePhoto.onPickFromCaptureWithCrop(Uri.fromFile(getTempHeadImageFile()), cropOptions);
                                break;
                            case 1:
                                takePhoto.onEnableCompress(compressConfig, true);
                                takePhoto.onPickFromGalleryWithCrop(Uri.fromFile(getTempHeadImageFile()), cropOptions);
                                break;
                            case 2:
                                Intent intent = new Intent();
                                MyActivityManager.getInstance().startNextActivityForResult(intent, MANGO_BABY_LOGO, ProfileSelectMangoBabyActivity.class);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .build();
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
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, BaseBean baseBean) {
        dismissLoadingDialog();
        CommonToastUtils.showToast(this, baseBean.getMsg());
//        popActivity();
    }

    @Override
    public void responseData(OnPresenterListener listener, BaseBean baseBean) {
        if (listener instanceof ProfileMyinfosUploadNicknamePresenterImpl) {
            CommonToastUtils.showToast(ProfileMyInfosActivity.this, "昵称修改成功");
            SPUtils.put(this, "LOGIN", "nickname", nickName);
            tvUserNickname.setText(nickName);
            inputNickNamePopw.getInputTradePwdPopw().dismiss();
        } else if (listener instanceof ProfileMyinfosUploadHeadImagePresenterImpl) {
            CommonToastUtils.showToast(ProfileMyInfosActivity.this, "头像修改成功");
            //把头像设置到界面上
            setHeadImage();
            Picasso.with(this).invalidate(headImgUrl);
            int mango_baby_position = (int) SPUtils.get(ProfileMyInfosActivity.this, "LOGIN", "current_mango_baby_position", -1);
            if (mango_baby_position != -1) {
                SPUtils.put(ProfileMyInfosActivity.this, "LOGIN", "mango_baby_position", mango_baby_position);
            }
            //保存
            BitmapAndFileUtils.saveBitmap(headImageBitmap);
        }
    }


    static class ViewHolder {
        @InjectView(R.id.tv_camera)
        TextView tvCamera;
        @InjectView(R.id.tv_gallery)
        TextView tvGallery;
        @InjectView(R.id.tv_mgbaby)
        TextView tvMgbaby;
        @InjectView(R.id.tv_cancle)
        TextView tvCancle;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    private void setHeadImage() {
        ImageView imageView = (ImageView) findViewById(R.id.iv_user_touxiang);
        imageView.setImageBitmap(headImageBitmap);//将图片显示在界面
    }

    //将头像保存到sd卡（返回值是一个file类型的uri）
    public Uri saveTempBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.mgjr.avater");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File picture = new File(tmpDir, "tempavater.png");
        if (picture.exists()) {
            picture.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(picture);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(picture);//返回uri
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
