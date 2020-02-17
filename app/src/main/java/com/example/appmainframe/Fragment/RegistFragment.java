package com.example.appmainframe.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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

        if(isRegist){
            Toast.makeText(getContext(),"注册成功",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
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
