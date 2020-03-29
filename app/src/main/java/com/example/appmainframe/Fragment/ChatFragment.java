package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.Activity.FrameActivity;
import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Activity.MainActivity_;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.GenericSignatureFormatError;
import java.net.URL;

@EFragment(R.layout.fragment_chat)
public class ChatFragment extends Fragment {

    String userNameParam;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Gson gson = new Gson();
                User user = new User();
                String js = msg.obj.toString();
                user = gson.fromJson(js,User.class);
                tv_moneyRest.setText(user.getUserMoney());
                Toast.makeText(getContext(),"充值成功！",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(),"充值失败，请联系系统管理员",Toast.LENGTH_LONG).show();

            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Gson gson = new Gson();
                User user = new User();
                String js = msg.obj.toString();
                user = gson.fromJson(js,User.class);
                tv_moneyRest.setText(user.getUserMoney());
            }
        }
    };

    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Gson gson = new Gson();
                User user = new User();
                String js = msg.obj.toString();
                user = gson.fromJson(js,User.class);
                tv_moneyRest.setText(user.getUserMoney());
            }else {
                Toast.makeText(getContext(),"提现失败，请联系系统管理员！",Toast.LENGTH_LONG).show();
            }
        }
    };

    @ViewById(R.id.tv_moneyName)
    TextView tv_moneyName;
    @ViewById(R.id.tv_moneyRest)
    TextView tv_moneyRest;
    @ViewById(R.id.btn_moneyIn)
    Button btn_moneyIn;
    @ViewById(R.id.btn_moneyOut)
    Button btn_moneyOut;
    @ViewById(R.id.rdoBtn_in_10)
    RadioButton rdoBtn_in_10;
    @ViewById(R.id.rdoBtn_in_50)
    RadioButton rdoBtn_in_50;
    @ViewById(R.id.rdoBtn_in_100)
    RadioButton rdoBtn_in_100;
    @ViewById(R.id.rdoBtn_out_10)
    RadioButton rdoBtn_out_10;
    @ViewById(R.id.rdoBtn_out_50)
    RadioButton rdoBtn_out_50;
    @ViewById(R.id.rdoBtn_out_100)
    RadioButton rdoBtn_out_100;

    @AfterViews
    protected void initView(){
        tv_moneyName.setText(userNameParam);
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    URL url;
                    String urlPath = "http://192.168.0.102:8080/main/money";
                    url = new URL(urlPath);
                    json.put("userName",userNameParam);
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.httputils(url,json);
                    int resultCode = httpUtils.getResultCode();
                    if(resultCode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        Message msg = handler1.obtainMessage();
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

    @Click(R.id.btn_moneyIn)
    protected void moneyInAction(){
        if(rdoBtn_in_10.isChecked() || rdoBtn_in_50.isChecked() || rdoBtn_in_100.isChecked()) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        int moneyIn = 0;
                        if(rdoBtn_in_10.isChecked()){
                            moneyIn = 10;
                        }
                        else if (rdoBtn_in_50.isChecked()){
                            moneyIn = 50;
                        }
                        else if (rdoBtn_in_100.isChecked()){
                            moneyIn = 100;
                        }
                        JSONObject json = new JSONObject();
                        URL url;
                        User user = new User();
                        String urlpath = "http://192.168.0.102:8080/main/moneyin";
                        url = new URL(urlpath);
                        HttpUtils httpUtils = new HttpUtils();
                        json.put("userName", userNameParam);
                        json.put("userMoneyIn",moneyIn);
                        httpUtils.httputils(url, json);
                        int resultCode = httpUtils.getResultCode();
                        if (resultCode == 200) {
                            InputStream inputStream = httpUtils.getInputStream();
                            String js = NetUtils.readString(inputStream);
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = js;
                            msg.sendToTarget();
                            inputStream.close();
                        }else {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.sendToTarget();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else {
            Toast.makeText(getContext(),"充值失败，请选择需要充值的金额！",Toast.LENGTH_LONG).show();
        }
    }

    @Click(R.id.btn_moneyOut)
    protected void moneyOutAction(){
        int moneyOut = 0;
        if(rdoBtn_out_10.isChecked() || rdoBtn_out_50.isChecked() || rdoBtn_out_100.isChecked()){
            if(rdoBtn_out_10.isChecked()){
                moneyOut = 10;
            }else if (rdoBtn_out_50.isChecked()){
                moneyOut = 50;
            }else if (rdoBtn_out_100.isChecked()){
                moneyOut = 100;
            }
            if(moneyOut <= Integer.valueOf(tv_moneyRest.getText().toString())){
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            int moneyDec = 0;
                            if(rdoBtn_out_10.isChecked()){
                                moneyDec = 10;
                            }
                            else if (rdoBtn_out_50.isChecked()){
                                moneyDec = 50;
                            }
                            else if (rdoBtn_out_100.isChecked()){
                                moneyDec = 100;
                            }
                            JSONObject json = new JSONObject();
                            URL url;
                            String urlpath = "http://192.168.0.102:8080/main/moneyout";
                            HttpUtils httpUtils = new HttpUtils();
                            url = new URL(urlpath);
                            json.put("userName",userNameParam);
                            json.put("userMoneyOut",moneyDec);
                            httpUtils.httputils(url,json);
                            int resultCode = httpUtils.getResultCode();
                            if(resultCode == 200){
                                InputStream inputStream = httpUtils.getInputStream();
                                String js = NetUtils.readString(inputStream);
                                Message msg = handler2.obtainMessage();
                                msg.what = 1;
                                msg.obj = js;
                                msg.sendToTarget();
                                inputStream.close();
                            }else {
                                Message msg = handler2.obtainMessage();
                                msg.what = 0;
                                msg.sendToTarget();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }else {
                Toast.makeText(getContext(),"提现失败，余额不足！",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(),"提现失败，请选择你需要提现的金额！",Toast.LENGTH_LONG).show();
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
