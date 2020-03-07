package com.example.appmainframe.Activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appmainframe.Fragment.LoginFragment;
import com.example.appmainframe.Fragment.LoginFragment_;
import com.example.appmainframe.Fragment.RegistFragment;
import com.example.appmainframe.Fragment.RegistFragment_;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.AppUtils;
import com.example.appmainframe.Utils.CustomVideoView;
import com.example.appmainframe.Utils.Density;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

//用注解方式绑定Activity
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewById(R.id.loginVideo)
    CustomVideoView loginVideo;
    @AfterViews
    protected void initView(){
        //设置加载路径
        loginVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginvideo));
        //播放
        loginVideo.start();
        //循环播放
        loginVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                loginVideo.start();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将LoginFragment入栈
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.loginFragment,new LoginFragment_())
                .addToBackStack(null)
                .commit();
    }
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        loginVideo.stopPlayback();
        super.onStop();
    }

}
