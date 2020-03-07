package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FrameActivity_.class);
                startActivity(intent);
            }
            else {
                loginLoginInfo.setText(msg.obj.toString());
            }
        }
    };

    @ViewById(R.id.loginLoginInfo)
    TextView loginLoginInfo;
    @ViewById(R.id.loginLoginButton)
    Button loginButton;
    @ViewById(R.id.loginUserEdit)
    EditText loginUserEdit;
    @ViewById(R.id.loginPwEdit)
    EditText loginPwEdit;
    @Click(R.id.loginLoginButton)
    void loginClick(){
        new Thread() {
            @Override
            public void run() {
                //利用json登录交互
                JSONObject json = new JSONObject();
                User user = new User();
                user.setUserName(loginUserEdit.getText().

                        toString());
                user.setUserPassword(loginPwEdit.getText().

                        toString());
                //与后台对应url@RequestMapping(value = "/login")
                String urlpath = "http://192.168.0.101:8080/fa/login";
                URL url;
                try {
                    url = new URL(urlpath);
                    //封装json数据
                    json.put("userName", user.getUserName());
                    json.put("userPassword", user.getUserPassword());
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.httputils(url,json);
                    //获取http返回码
                    int resultCode = httpUtils.getResultCode();
                    if (resultCode == 200) {
                        //获取输入流
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        //利用gson反序列化
                        Gson gson = new Gson();
                        User user1 = gson.fromJson(js, User.class);
                        //根据后端给出msg处理数据
                        if (user1.getMsg().equals("登录成功")) {
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.obj = user1.getMsg();
                            msg.sendToTarget();
                        } else {
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.obj = user1.getMsg();
                            msg.sendToTarget();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @ViewById(R.id.loginRegistButton)
    Button registButton;
    @Click(R.id.loginRegistButton)
    void registClick(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.loginFragment,new RegistFragment_())
                .addToBackStack(null)
                .commit();
    }
    @ViewById
    TextView temperatureText;
    @ViewById
    TextView weatherText;
    @AfterViews
    protected void weather(){
        HeWeather.getWeatherNow(getContext(),"CN101020100", Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            public static final String TAG="he_feng_now";

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ",e);
                System.out.println("Weather Now Error:"+new Gson());
            }

            @Override
            public void onSuccess(Now dataObject) {
                Log.i(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                String jsonData = new Gson().toJson(dataObject);
                String tianqi = null,wendu = null, tianqicode = null;
                if (dataObject.getStatus().equals("ok")){
                    String JsonNow = new Gson().toJson(dataObject.getNow());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(JsonNow);
                        tianqi = jsonObject.getString("cond_txt");
                        wendu = jsonObject.getString("tmp");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    return;
                }
                String wendu2 = wendu +"℃";
                weatherText.setText("当前天气:"+tianqi);
                temperatureText.setText(wendu2);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HeConfig.init("HE2002131605151686","c1694753cd35487dac6654109620dee3");
        HeConfig.switchToFreeServerNode();
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        weather();
    }
}
