package com.example.appmainframe.Activity;

import android.widget.Button;
import android.widget.Toast;
import com.example.appmainframe.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

//用注解方式绑定Activity
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //用注解方式注册按钮
    @ViewById(R.id.button)
    Button button;
    //用注解方式注册监听事件
    @Click(R.id.button)
    void myClick(){
        Toast.makeText(this,"coded by androidannotations",Toast.LENGTH_LONG).show();
    }
}
