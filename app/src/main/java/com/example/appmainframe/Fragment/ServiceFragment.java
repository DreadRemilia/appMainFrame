package com.example.appmainframe.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Activity.FrameActivity;
import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.Bean.User;
import com.example.appmainframe.R;
import com.example.appmainframe.Bean.Service;
import com.example.appmainframe.Adapter.ServiceRecyclerAdapter;
import com.example.appmainframe.Utils.Base64ToBitmap;
import com.example.appmainframe.Utils.HttpUtils;
import com.example.appmainframe.Utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.makeramen.roundedimageview.RoundedImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@EFragment(R.layout.fragment_service)
public class ServiceFragment extends Fragment {

    String userNameParam;
    ArrayList<ServiceOrder> serviceOrders = new ArrayList<>();
    ServiceRecyclerAdapter adapter = new ServiceRecyclerAdapter(serviceOrders);


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcy_service.setLayoutManager(layoutManager);
                rcy_service.setAdapter(adapter);
            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),"预定成功！请至个人设置内查看",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), FrameActivity_.class);
                startActivity(intent);
            }else {
                Toast.makeText(getContext(),"预定失败！余额不足，请充值！",Toast.LENGTH_LONG).show();
            }
        }
    };

    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("用户明细");
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_showprovider,null);
            builder.setView(view);
            final TextView tv_dlg_providerName,tv_dlg_sex,tv_dlg_marks,tv_dlg_total;
            final RoundedImageView riv_showHead;
            tv_dlg_providerName = view.findViewById(R.id.tv_dlg_providerName);
            tv_dlg_sex = view.findViewById(R.id.tv_dlg_sex);
            tv_dlg_marks = view.findViewById(R.id.tv_dlg_marks);
            tv_dlg_total = view.findViewById(R.id.tv_dlg_total);
            riv_showHead = view.findViewById(R.id.riv_showHead);
            Gson gson = new Gson();
            String js = msg.obj.toString();
            User userParam = gson.fromJson(js,User.class);
            tv_dlg_sex.setText(userParam.getUserSex());
            tv_dlg_providerName.setText(userParam.getUserName());
            tv_dlg_total.setText(userParam.getUserCount());
            tv_dlg_marks.setText(userParam.getUserMarks());
            if(userParam.getUserHead() != null){
                Bitmap newMap = Base64ToBitmap.base64ToBitmap(userParam.getUserHead());
                riv_showHead.setImageBitmap(newMap);}
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    };

    @ViewById(R.id.rcy_service)
    RecyclerView rcy_service;

    @AfterViews
    protected void initService() {

        new Thread(){
            @Override
            public void run() {
                try{
                    JSONObject json = new JSONObject();
                    String urlpath = "http://192.168.0.102:8080/main/showorder";
                    URL url;
                    Gson gson = new Gson();
                    url = new URL(urlpath);
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
                        /*
                        Iterator<ServiceOrder> iterator = serviceOrders.iterator();
                        while(iterator.hasNext()){
                            Log.e("fafa","fa" + iterator.next().toString());
                        }
                        */
                        //inputStream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        /*
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcy_service.setLayoutManager(layoutManager);
        ServiceRecyclerAdapter adapter = new ServiceRecyclerAdapter(serviceOrders);
        rcy_service.setAdapter(adapter);
        */

        adapter.setOnItemListener(new ServiceRecyclerAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, ServiceRecyclerAdapter.ViewName viewName, final int position) {
                switch (v.getId()){
                    case R.id.btn_serviceOrder:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("请选择填写联系方式");
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_order,null);
                        builder.setView(view);
                        final RadioButton rdoBtn_dialog_phone,rdoBtn_dialog_mobile;
                        final EditText ev_dialog_number;
                        rdoBtn_dialog_phone = view.findViewById(R.id.rdoBtn_dialog_phone);
                        rdoBtn_dialog_mobile = view.findViewById(R.id.rdoBtn_dialog_mobile);
                        ev_dialog_number = view.findViewById(R.id.ev_dialog_number);
                        builder.setPositiveButton("预定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(rdoBtn_dialog_phone.isChecked() || rdoBtn_dialog_mobile.isChecked()){
                                    if(rdoBtn_dialog_mobile.isChecked()){
                                        if(ev_dialog_number.getText().toString().length() == 11 && Long.valueOf(ev_dialog_number.getText().toString()) > 0){
                                            //发送
                                            new Thread(){
                                                @Override
                                                public void run() {
                                                    try{
                                                        String serviceAddress = ev_dialog_number.getText().toString();
                                                        JSONObject json = new JSONObject();
                                                        ServiceOrder serviceOrder = new ServiceOrder();
                                                        serviceOrder = serviceOrders.get(position);
                                                        String urlpath = "http://192.168.0.102:8080/main/order";
                                                        URL url;
                                                        serviceOrder.setServiceCustomer(userNameParam);
                                                        url = new URL(urlpath);
                                                        HttpUtils httpUtils = new HttpUtils();
                                                        Gson gson = new Gson();
                                                        json.put("serviceCustomer",serviceOrder.getServiceCustomer());
                                                        json.put("serviceNo",serviceOrder.getServiceNo());
                                                        json.put("serviceAddress",serviceAddress);
                                                        httpUtils.httputils(url,json);
                                                        int resultCode = httpUtils.getResultCode();
                                                        if(resultCode == 200){
                                                            InputStream inputStream = httpUtils.getInputStream();
                                                            String js = NetUtils.readString(inputStream);
                                                            ServiceOrder serviceOrder1 = gson.fromJson(js,ServiceOrder.class);
                                                            if(!"error".equals(serviceOrder1.getMsg())) {
                                                                Message msg = handler1.obtainMessage();
                                                                msg.what = 1;
                                                                msg.sendToTarget();
                                                            }else {
                                                                Message msg = handler1.obtainMessage();
                                                                msg.what = 0;
                                                                msg.sendToTarget();
                                                            }
                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"预定失败，请填写正确手机号",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        if(ev_dialog_number.getText().toString().length() == 8 && Long.valueOf(ev_dialog_number.getText().toString()) > 0){
                                            //发送
                                            new Thread(){
                                                @Override
                                                public void run() {
                                                    try{
                                                        String serviceAddress = ev_dialog_number.getText().toString();
                                                        JSONObject json = new JSONObject();
                                                        ServiceOrder serviceOrder = new ServiceOrder();
                                                        serviceOrder = serviceOrders.get(position);
                                                        String urlpath = "http://192.168.0.102:8080/main/order";
                                                        URL url;
                                                        serviceOrder.setServiceCustomer(userNameParam);
                                                        url = new URL(urlpath);
                                                        HttpUtils httpUtils = new HttpUtils();
                                                        Gson gson = new Gson();
                                                        json.put("serviceCustomer",serviceOrder.getServiceCustomer());
                                                        json.put("serviceNo",serviceOrder.getServiceNo());
                                                        json.put("serviceAddress",serviceAddress);
                                                        httpUtils.httputils(url,json);
                                                        int resultCode = httpUtils.getResultCode();
                                                        if(resultCode == 200){
                                                            InputStream inputStream = httpUtils.getInputStream();
                                                            String js = NetUtils.readString(inputStream);
                                                            ServiceOrder serviceOrder1 = gson.fromJson(js,ServiceOrder.class);
                                                            if(!"error".equals(serviceOrder1.getMsg())) {
                                                                Message msg = handler1.obtainMessage();
                                                                msg.what = 1;
                                                                msg.sendToTarget();
                                                            }else {
                                                                Message msg = handler1.obtainMessage();
                                                                msg.what = 0;
                                                                msg.sendToTarget();
                                                            }
                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"预定失败，请填写正确电话号码",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }else {
                                    Toast.makeText(getContext(),"预定失败，请选择联系方式",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        /*
                        new Thread(){
                            @Override
                            public void run() {
                                try{
                                    JSONObject json = new JSONObject();
                                    ServiceOrder serviceOrder = new ServiceOrder();
                                    serviceOrder = serviceOrders.get(position);
                                    String urlpath = "http://192.168.0.102:8080/main/order";
                                    URL url;
                                    serviceOrder.setServiceCustomer(userNameParam);
                                    url = new URL(urlpath);
                                    HttpUtils httpUtils = new HttpUtils();
                                    Gson gson = new Gson();
                                    json.put("serviceCustomer",serviceOrder.getServiceCustomer());
                                    json.put("serviceNo",serviceOrder.getServiceNo());
                                    httpUtils.httputils(url,json);
                                    int resultCode = httpUtils.getResultCode();
                                    if(resultCode == 200){
                                        InputStream inputStream = httpUtils.getInputStream();
                                        String js = NetUtils.readString(inputStream);
                                        ServiceOrder serviceOrder1 = gson.fromJson(js,ServiceOrder.class);
                                        if(!"error".equals(serviceOrder1.getMsg())) {
                                            Message msg = handler1.obtainMessage();
                                            msg.what = 1;
                                            msg.sendToTarget();
                                        }else {
                                            Message msg = handler1.obtainMessage();
                                            msg.what = 0;
                                            msg.sendToTarget();
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        */
                        break;
                    case R.id.btn_serviceShowProvider:
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject json = new JSONObject();
                                    ServiceOrder serviceOrder = new ServiceOrder();
                                    serviceOrder = serviceOrders.get(position);
                                    String urlpath = "http://192.168.0.102:8080/main/showprovider";
                                    URL url;
                                    url = new URL(urlpath);
                                    HttpUtils httpUtils = new HttpUtils();
                                    json.put("serviceProvider",serviceOrder.getServiceProvider());
                                    json.put("serviceNo",serviceOrder.getServiceNo());
                                    httpUtils.httputils(url,json);
                                    int resultCode = httpUtils.getResultCode();
                                    if(resultCode == 200){
                                        InputStream inputStream = httpUtils.getInputStream();
                                        String js = NetUtils.readString(inputStream);
                                        Message msg = handler2.obtainMessage();
                                        msg.what = 1;
                                        msg.obj = js;
                                        msg.sendToTarget();
                                        inputStream.close();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("用户明细");
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_showprovider,null);
                        builder.setView(view);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                        */
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        serviceOrders.clear();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }
}
