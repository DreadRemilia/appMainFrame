package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Adapter.HisRecyclerAdapter;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

@EFragment(R.layout.fragment_his)
public class HisSerFragment extends Fragment {

    String userNameParam;
    ArrayList<ServiceOrder> serviceOrders = new ArrayList<>();
    HisRecyclerAdapter adapter = new HisRecyclerAdapter(serviceOrders);

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcy_hisService.setLayoutManager(layoutManager);
                rcy_hisService.setAdapter(adapter);
            }
        }
    };

    @ViewById(R.id.rcy_hisService)
    RecyclerView rcy_hisService;
    @ViewById(R.id.btn_hisBack)
    Button btn_hisBack;

    @AfterViews
    protected void initView(){
        new Thread(){
            @Override
            public void run() {
                try{
                    JSONObject json = new JSONObject();
                    ServiceOrder serviceOrder = new ServiceOrder();
                    URL url;
                    Gson gson = new Gson();
                    String urlpath = "http://192.168.0.102:8080/option/his";
                    url = new URL(urlpath);
                    json.put("userName",userNameParam);
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.httputils(url,json);
                    int resultcode = httpUtils.getResultCode();
                    if(resultcode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        JsonParser parser = new JsonParser();
                        JsonArray array = parser.parse(js).getAsJsonArray();
                        for(JsonElement obj:array){
                            ServiceOrder so = gson.fromJson(obj,ServiceOrder.class);
                            serviceOrders.add(so);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.sendToTarget();
                        inputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Click(R.id.btn_hisBack)
    protected void hisBackAction(){
        Intent intent = new Intent(getActivity(), FrameActivity_.class);
        intent.putExtra("orderMsg","option");
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }
}
