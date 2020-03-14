package com.example.appmainframe.Fragment;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.example.appmainframe.Bean.User;
import com.google.gson.Gson;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@EFragment(R.layout.fragment_regist)
public class RegistFragment extends Fragment {
    @ViewById(R.id.ev_registUser)
    EditText ev_registUser;
    @ViewById(R.id.tv_registUInfo)
    TextView tv_registUInfo;
    @ViewById(R.id.ev_registPwd)
    EditText ev_registPwd;
    @ViewById(R.id.tv_registPInfo)
    TextView tv_registPInfo;
    @ViewById(R.id.ev_registCPwd)
    EditText ev_registCPwd;
    @ViewById(R.id.tv_registCPInfo)
    TextView tv_registCPInfo;
    @ViewById(R.id.rdoBtn_registMale)
    RadioButton rdoBtn_registMale;
    @ViewById(R.id.rdoBtn_registFemale)
    RadioButton rdoBtn_registFemale;
    @ViewById(R.id.tv_registSInfo)
    TextView tv_registSInfo;
    @ViewById(R.id.rdoBtn_registCommer)
    RadioButton rdoBtn_registCommer;
    @ViewById(R.id.rdoBtn_registService)
    RadioButton rdoBtn_registService;
    @ViewById(R.id.tv_registTInfo)
    TextView tv_registTInfo;

    //处理注册事件
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),"注册成功,欢迎"+msg.obj,Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(),"注册失败"+msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    //注册按钮监听事件
    @Click(R.id.btn_registRegist)
    void registRegistClick(){
        boolean isRegist = true;
        if(ev_registUser.getText().toString().equals("") || ev_registUser.getText().toString().length() < 6){
            tv_registUInfo.setText("用户名需大于6个字符或数字");
            isRegist = false;
        }
        else {
            tv_registUInfo.setText("");
        }
        if(ev_registPwd.getText().toString().equals("") || ev_registPwd.getText().toString().length() < 6){
            tv_registPInfo.setText("密码需大于6个字符或数字");
            isRegist = false;
        }
        else {
            tv_registPInfo.setText("");
        }
        if(!(ev_registCPwd.getText().toString().equals(ev_registCPwd.getText().toString()))){
            tv_registCPInfo.setText("两次输入的密码不同");
            isRegist = false;
        }
        else {
            tv_registCPInfo.setText("");
        }
        if(!(rdoBtn_registMale.isChecked()) && !(rdoBtn_registFemale.isChecked())){
            tv_registSInfo.setText("请选择您的性别");
            isRegist = false;
        }
        else {
            tv_registSInfo.setText("");
        }
        if(!(rdoBtn_registCommer.isChecked()) && !(rdoBtn_registService.isChecked())){
            tv_registTInfo.setText("请选择您的用户类型");
            isRegist = false;
        }
        else {
            tv_registTInfo.setText("");
        }

        if(isRegist) {
            new Thread() {
                @Override
                public void run() {
                    //新建JSON对象进行前后端交互
                    JSONObject json = new JSONObject();
                    User user = new User();
                    //确定后端的url@RequestMapping(value = "/regist")
                    String urlpath = "http://192.168.0.101:8080/user/regist";
                    URL url;
                    //为实体类的对象赋值
                    user.setUserName(ev_registUser.getText().toString());
                    user.setUserPassword(ev_registPwd.getText().toString());
                    user.setUserSex(rdoBtn_registMale.isChecked() ? rdoBtn_registMale.getText().toString() : rdoBtn_registFemale.getText().toString());
                    user.setUserType(rdoBtn_registCommer.isChecked() ? rdoBtn_registCommer.getText().toString() : rdoBtn_registService.getText().toString());
                    try {
                        url = new URL(urlpath);
                        HttpUtils httpUtils = new HttpUtils();
                        Gson gson = new Gson();
                        //将属性写为json格式
                        json.put("userName", user.getUserName());
                        json.put("userPassword", user.getUserPassword());
                        json.put("userSex", user.getUserSex());
                        json.put("userType", user.getUserType());
                        httpUtils.httputils(url,json);
                        //获取http返回值
                        int resultCode = httpUtils.getResultCode();
                        if(resultCode == 200){
                            //获取输入流
                            InputStream inputStream = httpUtils.getInputStream();
                            String js = NetUtils.readString(inputStream);
                            //利用gson进行反序列化
                            User user1 = gson.fromJson(js,User.class);
                            //根据后端给出msg处理数据
                            if(user1.getMsg().equals("注册成功")){
                                Message msg = handler.obtainMessage();
                                msg.what = 1;
                                msg.obj = user1.getUserName();
                                msg.sendToTarget();
                            }
                            else {
                                Message msg = handler.obtainMessage();
                                msg.what = 0;
                                msg.obj = user1.getMsg();
                                msg.sendToTarget();
                            }
                            inputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //结束后返回登录界面
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }.start();
        }
        else {
            Toast.makeText(getContext(),"注册失败",Toast.LENGTH_LONG).show();
        }
    }

    @Click(R.id.btn_registBack)
    void registBackClick(){
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
