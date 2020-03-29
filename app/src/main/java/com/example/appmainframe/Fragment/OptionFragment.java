package com.example.appmainframe.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmainframe.Activity.MainActivity;
import com.example.appmainframe.Activity.MainActivity_;
import com.example.appmainframe.Activity.OptionActivity;
import com.example.appmainframe.Activity.OptionActivity_;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.Base64ToBitmap;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.android.material.badge.BadgeUtils;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

@EFragment(R.layout.fragment_option)
public class OptionFragment extends Fragment {

    String userNameParam;
    String userTypeParam;
    @ViewById(R.id.tv_optionUser)
    TextView tv_optionUser;
    @ViewById(R.id.tv_optionSex)
    TextView tv_optionSex;
    @ViewById(R.id.tv_optionType)
    TextView tv_optionType;
    @ViewById(R.id.tv_optionTotal)
    TextView tv_optionTotal;
    @ViewById(R.id.btn_optionExit)
    Button btn_optionExit;
    @ViewById(R.id.btn_optionCurSer)
    Button btn_optionCurSer;
    @ViewById(R.id.btn_optionHisSer)
    Button btn_optionHisSer;
    @ViewById(R.id.btn_optionChangeSetting)
    Button btn_optionChangeSetting;
    @ViewById(R.id.riv_optHead)
    RoundedImageView riv_optHead;

    //接收msg显示事件
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Gson gson = new Gson();
                User user1 = gson.fromJson(msg.obj.toString(),User.class);
                userTypeParam = user1.getUserType();
                tv_optionSex.setText(user1.getUserSex());
                tv_optionType.setText(user1.getUserType());
                tv_optionTotal.setText(user1.getUserCount());
                if(user1.getUserHead() != null){
                    Bitmap newMap = Base64ToBitmap.base64ToBitmap(user1.getUserHead());
                    riv_optHead.setImageBitmap(newMap);
                }
            }
        }
    };

    @Click(R.id.btn_optionExit)
    protected void optionLogoutAction(){
        Intent intent = new Intent(getActivity(), MainActivity_.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Click(R.id.btn_optionCurSer)
    public void optionCurSerAction(){
        //当前服务事件
        Intent intent = new Intent(getActivity(), OptionActivity_.class);
        intent.putExtra("optmsg","cur");
        intent.putExtra("userType",userTypeParam);
        startActivity(intent);
    }

    @Click(R.id.btn_optionHisSer)
    public void optionHisSerAction(){
        //历史服务事件
        Intent intent = new Intent(getActivity(),OptionActivity_.class);
        intent.putExtra("optmsg","his");
        startActivity(intent);
    }

    @Click(R.id.btn_optionChangeSetting)
    public void optionChangeSettingAction(){
        //更改设置事件
        Intent intent = new Intent(getActivity(),OptionActivity_.class);
        //intent.putExtra("userNameParam",userNameParam);
        intent.putExtra("optmsg","chg");
        startActivity(intent);
        getActivity().finish();
    }

    //接收基本信息
    @AfterViews
    protected void initView(){
        tv_optionUser.setText(userNameParam);
        new Thread(){
            @Override
            public void run() {
                JSONObject json = new JSONObject();
                User user = new User();
                //确定后端的url@RequestMapping(value = "/showoption")
                String urlpath = "http://192.168.0.102:8080/main/showoption";
                URL url;
                user.setUserName(userNameParam);

                try{
                    url = new URL(urlpath);
                    HttpUtils httpUtils = new HttpUtils();
                    json.put("userName",user.getUserName());
                    httpUtils.httputils(url,json);
                    int resultcode = httpUtils.getResultCode();
                    if(resultcode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        //Gson gson = new Gson();
                        //User user1 = gson.fromJson(js,User.class);
                        if(resultcode == 200){
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = js;
                            msg.sendToTarget();
                        }else {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.obj = "错误";
                            msg.sendToTarget();
                        }
                        inputStream.close();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //EventBus注册
        EventBus.getDefault().register(this);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus注销
        EventBus.getDefault().unregister(this);
    }

    //EventBus接收变量
    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }
}
