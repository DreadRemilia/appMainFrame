package com.example.appmainframe.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.appmainframe.Fragment.ChatFragment_;
import com.example.appmainframe.Fragment.OptionFragment_;
import com.example.appmainframe.Fragment.ServiceFragment_;
import com.example.appmainframe.R;
import com.example.appmainframe.Adapter.MyFragmentPagerAdapter;
import com.example.appmainframe.Utils.AppUtils;
import com.example.appmainframe.Utils.Density;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_frame)
public class FrameActivity extends BaseActivity {
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
        fFragment = new ArrayList<>(3);
        fFragment.add(new ServiceFragment_());
        fFragment.add(new ChatFragment_());
        fFragment.add(new OptionFragment_());
        //初始化ViewPager
        fAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fFragment);
        framePager.setAdapter(fAdapter);
        //注册监听事件
        framePager.addOnPageChangeListener(onPageChangeListener);
        frameRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
    }

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
    protected void onDestroy() {
        super.onDestroy();
        framePager.removeOnPageChangeListener(onPageChangeListener);
    }
}
