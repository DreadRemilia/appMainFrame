package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmainframe.Activity.FrameActivity;
import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.R;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {
    @ViewById(R.id.loginLoginButton)
    Button loginButton;
    @Click(R.id.loginLoginButton)
    void loginClick(){
        //登录点击事件
        Intent intent = new Intent(getActivity(), FrameActivity_.class);
        startActivity(intent);
    }
    @ViewById(R.id.loginRegistButton)
    Button registButton;
    @Click(R.id.loginRegistButton)
    void registClick(){
        //跳转RegistFragment
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
