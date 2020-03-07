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
    @ViewById(R.id.registUserEdit)
    EditText registUserEdit;
    @ViewById(R.id.registUserInfo)
    TextView registUserInfo;
    @ViewById(R.id.registPwEdit)
    EditText registPwEdit;
    @ViewById(R.id.registPwInfo)
    TextView registPwInfo;
    @ViewById(R.id.registCpwEdit)
    EditText registCpwEdit;
    @ViewById(R.id.registCpwInfo)
    TextView registCpwInfo;
    @ViewById(R.id.registSexMaleButton)
    RadioButton registSexMaleButton;
    @ViewById(R.id.registSexFemaleButton)
    RadioButton registSexFemaleButton;
    @ViewById(R.id.registSexInfo)
    TextView registSexInfo;
    @ViewById(R.id.registTypeComButton)
    RadioButton registTypeComButton;
    @ViewById(R.id.registTypeSerButton)
    RadioButton registTypeSerButton;
    @ViewById(R.id.registTypeInfo)
    TextView registTypeInfo;

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
    @Click(R.id.registLoginButton)
    void registLoginClick(){
        boolean isRegist = true;
        if(registUserEdit.getText().toString().equals("") || registUserEdit.getText().toString().length() < 6){
            registUserInfo.setText("用户名需大于6个字符或数字");
            isRegist = false;
        }
        else {
            registUserInfo.setText("");
        }
        if(registPwEdit.getText().toString().equals("") || registPwEdit.getText().toString().length() < 6){
            registPwInfo.setText("密码需大于6个字符或数字");
            isRegist = false;
        }
        else {
             registPwInfo.setText("");
        }
        if(!(registCpwEdit.getText().toString().equals(registPwEdit.getText().toString()))){
            registCpwInfo.setText("两次输入的密码不同");
            isRegist = false;
        }
        else {
            registCpwInfo.setText("");
        }
        if(!(registSexMaleButton.isChecked()) && !(registSexFemaleButton.isChecked())){
            registSexInfo.setText("请选择您的性别");
            isRegist = false;
        }
        else {
            registSexInfo.setText("");
        }
        if(!(registTypeComButton.isChecked()) && !(registTypeSerButton.isChecked())){
            registTypeInfo.setText("请选择您的用户类型");
            isRegist = false;
        }
        else {
            registTypeInfo.setText("");
        }

        if(isRegist) {
            new Thread() {
                @Override
                public void run() {
                    //新建JSON对象进行前后端交互
                    JSONObject json = new JSONObject();
                    User user = new User();
                    //为实体类的对象赋值
                    user.setUserName(registUserEdit.getText().toString());
                    user.setUserPassword(registPwEdit.getText().toString());
                    user.setUserSex(registSexMaleButton.isChecked() ? registSexMaleButton.getText().toString() : registSexFemaleButton.getText().toString());
                    user.setUserType(registTypeComButton.isChecked() ? registTypeComButton.getText().toString() : registTypeSerButton.getText().toString());
                    //确定后端的url@RequestMapping(value = "/regist")
                    String urlpath = "http://192.168.0.101:8080/fa/regist";
                    URL url;
                    try {
                        url = new URL(urlpath);
                        //将属性写为json格式
                        json.put("userName", user.getUserName());
                        json.put("userPassword", user.getUserPassword());
                        json.put("userSex", user.getUserSex());
                        json.put("userType", user.getUserType());
                        HttpUtils httpUtils = new HttpUtils();
                        httpUtils.httputils(url,json);
                        //获取http返回值
                        int resultCode = httpUtils.getResultCode();
                        if(resultCode == 200){
                            //获取输入流
                            InputStream inputStream = httpUtils.getInputStream();
                            String js = NetUtils.readString(inputStream);
                            //利用gson进行反序列化
                            Gson gson = new Gson();
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
    @Click(R.id.registBackButton)
    void registBackClick(){
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
