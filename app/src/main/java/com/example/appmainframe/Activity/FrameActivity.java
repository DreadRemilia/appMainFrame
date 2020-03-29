package com.example.appmainframe.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.Bean.ViewPagerNum;
import com.example.appmainframe.Fragment.ChatFragment;
import com.example.appmainframe.Fragment.ChatFragment_;
import com.example.appmainframe.Fragment.OptionFragment;
import com.example.appmainframe.Fragment.OptionFragment_;
import com.example.appmainframe.Fragment.ProviderFragment;
import com.example.appmainframe.Fragment.ProviderFragment_;
import com.example.appmainframe.Fragment.ServiceFragment;
import com.example.appmainframe.Fragment.ServiceFragment_;
import com.example.appmainframe.R;
import com.example.appmainframe.Adapter.MyFragmentPagerAdapter;
import com.example.appmainframe.Utils.AppUtils;
import com.example.appmainframe.Utils.Density;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_frame)
public class FrameActivity extends BaseActivity {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                fFragment.add(new ProviderFragment_());
                fFragment.add(new ChatFragment_());
                fFragment.add(new OptionFragment_());
                fAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fFragment);
                framePager.setAdapter(fAdapter);
                //注册监听事件
                framePager.addOnPageChangeListener(onPageChangeListener);
                frameRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
                Intent intent = getIntent();
                String optionMsg = intent.getStringExtra("orderMsg");
                if("option".equals(optionMsg)){
                    //framePager.setCurrentItem(2);
                }
            }
            else if(msg.what == 0){
                fFragment.add(new ServiceFragment_());
                fFragment.add(new ChatFragment_());
                fFragment.add(new OptionFragment_());
                fAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fFragment);
                framePager.setAdapter(fAdapter);
                //注册监听事件
                framePager.addOnPageChangeListener(onPageChangeListener);
                frameRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
                Intent intent = getIntent();
                String optionMsg = intent.getStringExtra("orderMsg");
                if("option".equals(optionMsg)){
                    //framePager.setCurrentItem(2);
                }
            }
            else {
                Log.i("fafa","failed");
            }
        }
    };

    String userNameParam;

    @ViewById(R.id.tv_frmInfo)
    TextView frameInfo;
    @ViewById(R.id.frameRadioGroup)
    RadioGroup frameRadioGroup;
    @ViewById(R.id.frameServiceButton)
    RadioButton frameServiceButton;
    @ViewById(R.id.frameChatButton)
    RadioButton frameChatButton;
    @ViewById(R.id.frameOptionButton)
    RadioButton frameOptionButton;
    @ViewById(R.id.framePager)
    ViewPager framePager;
    List<Fragment> fFragment;
    FragmentPagerAdapter fAdapter;

    @AfterViews
    protected void initView(){
        //初始化Fragment
        fFragment = new ArrayList<>();
        int flag = -1;
        new Thread(){
            @Override
            public void run() {
                try{
                    JSONObject json = new JSONObject();
                    User user = new User();
                    URL url;
                    String urlpath = "http://192.168.0.102:8080/user/type";
                    user.setUserName(userNameParam);
                    url = new URL(urlpath);
                    HttpUtils httpUtils = new HttpUtils();
                    Gson gson = new Gson();
                    json.put("userName",user.getUserName());
                    httpUtils.httputils(url,json);
                    int resultCode = httpUtils.getResultCode();

                    if(resultCode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        User user1 = gson.fromJson(js,User.class);
                        if(user1.getUserType().equals("服务人员")){
                            Message msg = handler.obtainMessage();
                            msg.what = 1;
                            msg.sendToTarget();
                        }else{
                            Message msg = handler.obtainMessage();
                            msg.what = 0;
                            msg.sendToTarget();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        //fFragment.add(new ServiceFragment_());
        //fFragment.add(new ProviderFragment_());
        //fFragment.add(new ChatFragment_());
        //fFragment.add(new OptionFragment_());
        //初始化ViewPager
        //fAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fFragment);
        //framePager.setAdapter(fAdapter);
        //注册监听事件
        //framePager.addOnPageChangeListener(onPageChangeListener);
        //frameRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        //二次跳转指定页面
    }

    //viewpager切换事件监听
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) frameRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //rdoBtn点击事件监听
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for(int i = 0;i < group.getChildCount();i++){
                if(group.getChildAt(i).getId() == checkedId){
                    framePager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        framePager.removeOnPageChangeListener(onPageChangeListener);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }

}
