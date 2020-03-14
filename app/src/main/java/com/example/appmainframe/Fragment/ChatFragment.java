package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appmainframe.Activity.FrameActivity;
import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Activity.MainActivity_;
import com.example.appmainframe.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_chat)
public class ChatFragment extends Fragment {

    @ViewById(R.id.testbutton)
    Button testbutton;

    @Click(R.id.testbutton)
    protected void click(){
        Intent intent = new Intent(getActivity(),FrameActivity_.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

}
