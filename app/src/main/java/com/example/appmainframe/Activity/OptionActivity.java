 package com.example.appmainframe.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmainframe.Bean.User;
import com.example.appmainframe.Fragment.CurServiceFragment;
import com.example.appmainframe.Fragment.CurServiceFragment_;
import com.example.appmainframe.Fragment.HisSerFragment;
import com.example.appmainframe.Fragment.HisSerFragment_;
import com.example.appmainframe.Fragment.OptChangeFragment;
import com.example.appmainframe.Fragment.OptChangeFragment_;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

 @EActivity(R.layout.activity_option)
 public class OptionActivity extends BaseActivity {

     @ViewById(R.id.tv_optInfo)
     TextView tv_optInfo;

     @AfterViews
     protected void initView(){
         Intent intent = getIntent();
         String getOptMsg = intent.getStringExtra("optmsg");
         String getTypeMsg = intent.getStringExtra("userType");
         if (getOptMsg.equals("his")) {
             tv_optInfo.setText("全部服务");
             getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.frg_opt,new HisSerFragment_())
                     .addToBackStack(null)
                     .commit();
         }
         else if (getOptMsg.equals("cur")) {
             tv_optInfo.setText("当前服务");
             getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.frg_opt,new CurServiceFragment_())
                     .addToBackStack(null)
                     .commit();
         }
         else if (getOptMsg.equals("chg")) {
             tv_optInfo.setText("更改设置");
             getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.frg_opt, new OptChangeFragment_())
                     .addToBackStack(null)
                     .commit();
         }
     }

     /*
     @ViewById(R.id.btn_test)
     Button btn_test;

     @Click(R.id.btn_test)
     public void click(){

         new Thread() {
             @Override
             public void run() {
                 User user = new User();
                 JSONObject json = new JSONObject();
                 Intent intent1 = getIntent();
                 user.setUserName(intent1.getStringExtra("userNameParam"));
                 user.setUserSex("男");
                 String urlpath = "http://192.168.0.101:8080/main/changeoption";
                 URL url;
                 try{
                     url = new URL(urlpath);
                     json.put("userName",user.getUserName());
                     json.put("userSex",user.getUserSex());
                     HttpUtils httpUtils = new HttpUtils();
                     httpUtils.httputils(url,json);
                     int resultcode = httpUtils.getResultCode();
                     if(resultcode == 200){
                         Intent intent = new Intent(OptionActivity.this,FrameActivity_.class);
                         startActivity(intent);
                     }
                     else {
                         Log.i("change","failed");
                     }
                 }catch (Exception e){
                     e.printStackTrace();
                 }
             }
         }.start();
     }
     */
}
