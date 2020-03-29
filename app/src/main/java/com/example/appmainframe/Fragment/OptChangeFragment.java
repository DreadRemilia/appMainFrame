package com.example.appmainframe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.GetChars;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.Base64ToBitmap;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;

import okhttp3.internal.http1.Http1ExchangeCodec;

@EFragment(R.layout.fragment_opt_change)
public class OptChangeFragment extends Fragment {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Gson gson = new Gson();
                String js = msg.obj.toString();
                User user = gson.fromJson(js,User.class);
                tv_chgUser.setText(user.getUserName());
                tv_chgPwd.setText(user.getUserPassword());
                tv_chgSex.setText(user.getUserSex());
                tv_chgType.setText(user.getUserType());
                if(user.getUserHead() != null){
                    Bitmap newMap = Base64ToBitmap.base64ToBitmap(user.getUserHead());
                    riv_chgHead.setImageBitmap(newMap);}
            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(),FrameActivity_.class);
                startActivity(intent);
                getActivity().finish();
            }else {
                Toast.makeText(getContext(),"修改失败，请联系管理员",Toast.LENGTH_LONG).show();
            }
        }
    };

    @ViewById(R.id.riv_chgHead)
    RoundedImageView riv_chgHead;
    @ViewById(R.id.btn_chgCamera)
    Button btn_chgCamera;
    @ViewById(R.id.btn_chgGallary)
    Button btn_chgGallary;
    @ViewById(R.id.tv_chgUser)
    TextView tv_chgUser;
    @ViewById(R.id.tv_chgPwd)
    TextView tv_chgPwd;
    @ViewById(R.id.tv_chgSex)
    TextView tv_chgSex;
    @ViewById(R.id.tv_chgType)
    TextView tv_chgType;
    @ViewById(R.id.btn_chgCommit)
    Button btn_chgCommit;
    @ViewById(R.id.btn_chgBack)
    Button btn_chgBack;
    @ViewById(R.id.ev_chgNewPwd)
    EditText ev_chgNewPwd;
    @ViewById(R.id.ev_chgNewCPwd)
    EditText ev_chgNewCPwd;
    @ViewById(R.id.rdoBtn_chg_male)
    RadioButton rdoBtn_chg_male;
    @ViewById(R.id.rdoBtn_chg_female)
    RadioButton rdoBtn_chg_female;

    static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    static final int CODE_GALLERY_REQUEST = 0xa0;
    static final int CODE_CAMERA_REQUEST = 0xa1;
    static final int CODE_RESULT_REQUEST = 0xa2;
    static int output_X = 480;
    static int output_Y = 480;

    Bitmap photo;
    String userNameParam;

    @AfterViews
    protected void initView(){
        new Thread(){
            @Override
            public void run() {
                try{
                    JSONObject json = new JSONObject();
                    URL url;
                    String urlpath = "http://192.168.0.102:8080/option/showuser";
                    url = new URL(urlpath);
                    HttpUtils httpUtils = new HttpUtils();
                    json.put("userName",userNameParam);
                    httpUtils.httputils(url,json);
                    int resultCode = httpUtils.getResultCode();
                    if(resultCode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = js;
                        msg.sendToTarget();
                        inputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Click(R.id.btn_chgCamera)
    protected void chgCameraAction(){
        choseHeadImageFromCameraCapture();
    }
    @Click(R.id.btn_chgGallary)
    protected void chgGallary(){
        choseHeadImageFromGallery();
    }

    @Click(R.id.btn_chgBack)
    protected void chgBackAction(){
        Intent intent = new Intent(getActivity(), FrameActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btn_chgCommit)
    protected void chgCommitAction(){
        if(ev_chgNewPwd.getText().toString().length() < 6){
            Toast.makeText(getContext(),"无法提交修改，用户名需大于6个字符",Toast.LENGTH_LONG).show();
            ev_chgNewPwd.setText("");
            ev_chgNewCPwd.setText("");
        }else if (!ev_chgNewPwd.getText().toString().equals(ev_chgNewCPwd.getText().toString())){
            Toast.makeText(getContext(),"无法提交修改，两次密码输入不一致",Toast.LENGTH_LONG).show();
            ev_chgNewPwd.setText("");
            ev_chgNewCPwd.setText("");
        }else if (!rdoBtn_chg_male.isChecked() && !rdoBtn_chg_female.isChecked()){
            Toast.makeText(getContext(),"无法提交修改，请选择性别",Toast.LENGTH_LONG).show();
        }else {
            new Thread(){
                @Override
                public void run() {
                    try{
                        JSONObject json = new JSONObject();
                        URL url;
                        String urlpath = "http://192.168.0.102:8080/option/chgoption";
                        url = new URL(urlpath);
                        HttpUtils httpUtils = new HttpUtils();
                        json.put("userName",userNameParam);
                        json.put("userType",tv_chgType.getText());
                        json.put("userPassword",ev_chgNewPwd.getText());
                        json.put("userSex",rdoBtn_chg_male.isChecked() ? rdoBtn_chg_male.getText() : rdoBtn_chg_female.getText());
                        json.put("userHead",getBase64(photo));
                        httpUtils.httputils(url,json);
                        int resultCode = httpUtils.getResultCode();
                        if(resultCode == 200){
                            Message msg = handler1.obtainMessage();
                            msg.what = 1;
                            msg.sendToTarget();
                        }else {
                            Message msg = handler1.obtainMessage();
                            msg.what = 0;
                            msg.sendToTarget();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File path = new File(getContext().getFilesDir(),"image");
                    if(!path.exists()){
                        path.mkdir();
                    }
                    File newPath = new File(path,IMAGE_FILE_NAME);
                    Uri contentUri = FileProvider.getUriForFile(getContext(),"com.example.appmainframe.fileProvider",newPath);
                    cropRawPhoto(contentUri);
                } else {
                    Toast.makeText(getContext(),"没有SDCard!",Toast.LENGTH_LONG).show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    protected void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            riv_chgHead.setImageBitmap(photo);
        }
    }

    public String getBase64(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //压缩的质量为60%
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
        //生成base64字符
        String base = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        return base;
    }

    protected void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
        // 设置文件类型
        intentFromGallery.setType("image/*");
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    protected void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent();
        intentFromCapture.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.addCategory(Intent.CATEGORY_DEFAULT);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            File path = new File(getContext().getFilesDir(),"image");
            if(!path.exists()){
                path.mkdir();
            }
            File newPath = new File(path,IMAGE_FILE_NAME);
            Uri contentUri = FileProvider.getUriForFile(getContext(),"com.example.appmainframe.fileProvider",newPath);

            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,contentUri);
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }
}
