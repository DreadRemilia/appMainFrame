package com.example.appmainframe.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.Adapter.CurCstRecyclerAdapter;
import com.example.appmainframe.Adapter.CurSerRecyclerAdapter;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.Bean.User;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@EFragment(R.layout.fragment_cur)
public class CurServiceFragment extends Fragment {

    String userNameParam;
    String userTypeParam;
    ArrayList<ServiceOrder> serviceOrders = new ArrayList<>();
    CurCstRecyclerAdapter ccstadapter = new CurCstRecyclerAdapter(serviceOrders);
    CurSerRecyclerAdapter cseradapter = new CurSerRecyclerAdapter(serviceOrders);

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcy_curCstService.setLayoutManager(layoutManager);
                rcy_curCstService.setAdapter(ccstadapter);
            }
        }
    };

    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcy_curCstService.setLayoutManager(layoutManager);
                rcy_curCstService.setAdapter(cseradapter);
            }
        }
    };

    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),"订单取消成功！",Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frg_opt,new CurServiceFragment_())
                        .addToBackStack(null)
                        .commit();
            }
            else {
                Toast.makeText(getContext(),"订单取消失败，订单状态有误！",Toast.LENGTH_LONG).show();

            }
        }
    };

    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("用户联系方式");
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_address,null);
                builder.setView(view);
                final TextView tv_addressUser,tv_addressMethod,tv_addressNumber;
                tv_addressUser = view.findViewById(R.id.tv_addressUser);
                tv_addressMethod = view.findViewById(R.id.tv_addressMethod);
                tv_addressNumber = view.findViewById(R.id.tv_addressNumber);
                Gson gson = new Gson();
                String js = msg.obj.toString();
                ServiceOrder serviceOrder = gson.fromJson(js,ServiceOrder.class);
                if(serviceOrder.getServiceAddress().length() == 8){
                    tv_addressMethod.setText("固话：");
                }else {
                    tv_addressMethod.setText("手机：");
                }
                tv_addressUser.setText(serviceOrder.getServiceCustomer());
                tv_addressNumber.setText(serviceOrder.getServiceAddress());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }
    };

    Handler handler4 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                Toast.makeText(getContext(),"服务确认完成成功！",Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frg_opt,new CurServiceFragment_())
                        .addToBackStack(null)
                        .commit();
            }
        }
    };

    @ViewById(R.id.rcy_curCstService)
    RecyclerView rcy_curCstService;
    @ViewById(R.id.btn_curCstBack)
    Button btn_curCstBack;


    @Click(R.id.btn_curCstBack)
    protected void curBackAction(){
        Intent intent = new Intent(getActivity(), FrameActivity_.class);
        intent.putExtra("orderMsg","option");
        startActivity(intent);
        getActivity().finish();
    }

    @AfterViews
    protected void initView(){
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    Gson gson = new Gson();
                    URL url;
                    String urlpath = "http://192.168.0.102:8080/option/type";
                    url = new URL(urlpath);
                    json.put("userName", userNameParam);
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.httputils(url, json);
                    int resultCode = httpUtils.getResultCode();
                    if (resultCode == 200) {
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        User user = gson.fromJson(js, User.class);
                        userTypeParam = user.getUserType();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userTypeParam.equals("一般用户")) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject();
                                ServiceOrder serviceOrder = new ServiceOrder();
                                URL url;
                                String urlpath = "http://192.168.0.102:8080/option/curcst";
                                url = new URL(urlpath);
                                Gson gson = new Gson();
                                json.put("userName", userNameParam);
                                HttpUtils httpUtils = new HttpUtils();
                                httpUtils.httputils(url, json);
                                int resultCode = httpUtils.getResultCode();
                                if (resultCode == 200) {
                                    InputStream inputStream = httpUtils.getInputStream();
                                    String js = NetUtils.readString(inputStream);
                                    JsonParser parser = new JsonParser();
                                    JsonArray array = parser.parse(js).getAsJsonArray();
                                    for (JsonElement obj : array) {
                                        ServiceOrder so = gson.fromJson(obj, ServiceOrder.class);
                                        serviceOrders.add(so);
                                    }
                                    Message msg = handler.obtainMessage();
                                    msg.what = 1;
                                    msg.sendToTarget();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else if (userTypeParam.equals("服务人员")) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject();
                                ServiceOrder serviceOrder = new ServiceOrder();
                                URL url;
                                String urlpath = "http://192.168.0.102:8080/option/curser";
                                url = new URL(urlpath);
                                Gson gson = new Gson();
                                json.put("userName", userNameParam);
                                HttpUtils httpUtils = new HttpUtils();
                                httpUtils.httputils(url, json);
                                int resultCode = httpUtils.getResultCode();
                                if (resultCode == 200) {
                                    InputStream inputStream = httpUtils.getInputStream();
                                    String js = NetUtils.readString(inputStream);
                                    JsonParser parser = new JsonParser();
                                    JsonArray array = parser.parse(js).getAsJsonArray();
                                    for (JsonElement obj : array) {
                                        ServiceOrder so = gson.fromJson(obj, ServiceOrder.class);
                                        serviceOrders.add(so);
                                    }
                                    Message msg = handler1.obtainMessage();
                                    msg.what = 1;
                                    msg.sendToTarget();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        }.start();
        cseradapter.setOnItemListener(new CurSerRecyclerAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, CurSerRecyclerAdapter.ViewName viewName, final int position) {
                try {
                    final ServiceOrder serviceOrder = serviceOrders.get(position);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date now = new Date();
                    Date start = sf.parse(serviceOrder.getServiceStart());
                    switch (v.getId()) {
                        case (R.id.btn_curSerCancel):
                            if (now.before(start)) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject();
                                            URL url;
                                            String urlpath = "http://192.168.0.102:8080/option/sercancel";
                                            url = new URL(urlpath);
                                            HttpUtils httpUtils = new HttpUtils();
                                            json.put("serviceNo", serviceOrder.getServiceNo());
                                            httpUtils.httputils(url, json);
                                            int resultCode = httpUtils.getResultCode();
                                            if (resultCode == 200) {
                                                InputStream inputStream = httpUtils.getInputStream();
                                                String js = NetUtils.readString(inputStream);
                                                Gson gson = new Gson();
                                                ServiceOrder serviceOrder1 = gson.fromJson(js, ServiceOrder.class);
                                                if ("error".equals(serviceOrder1.getMsg())) {
                                                    Message msg = handler2.obtainMessage();
                                                    msg.what = 0;
                                                    msg.sendToTarget();
                                                } else {
                                                    Message msg = handler2.obtainMessage();
                                                    msg.what = 1;
                                                    msg.sendToTarget();
                                                }
                                                inputStream.close();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            } else {
                                Toast.makeText(getContext(), "取消失败，无法取消订单！", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case (R.id.btn_curSerAddress):
                            if (serviceOrder.getServiceState().equals("发布")) {
                                Toast.makeText(getContext(), "还未有用户接受订单，请等待！", Toast.LENGTH_LONG).show();
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject json = new JSONObject();
                                            URL url;
                                            String urlpath = "http://192.168.0.102:8080/option/seraddress";
                                            url = new URL(urlpath);
                                            HttpUtils httpUtils = new HttpUtils();
                                            json.put("serviceNo",serviceOrder.getServiceNo());
                                            httpUtils.httputils(url,json);
                                            int resultCode = httpUtils.getResultCode();
                                            if(resultCode == 200){
                                                InputStream inputStream = httpUtils.getInputStream();
                                                String js = NetUtils.readString(inputStream);
                                                Message msg = handler3.obtainMessage();
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
                            }
                            break;
                        }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ccstadapter.setOnItemListener(new CurCstRecyclerAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, CurCstRecyclerAdapter.ViewName viewName,final int position) {
                try {
                    final ServiceOrder serviceOrder = serviceOrders.get(position);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date now = new Date();
                    Date start = sf.parse(serviceOrder.getServiceStart());
                    Date end = sf.parse(serviceOrder.getServiceEnd());
                    switch (v.getId()) {
                        case R.id.btn_curCstCancel:
                            if (now.before(start)) {
                                new Thread(){
                                    @Override
                                    public void run() {
                                        try{
                                            JSONObject json = new JSONObject();
                                            URL url;
                                            String urlpath = "http://192.168.0.102:8080/option/cstcancel";
                                            HttpUtils httpUtils = new HttpUtils();
                                            url = new URL(urlpath);
                                            json.put("serviceNo",serviceOrder.getServiceNo());
                                            httpUtils.httputils(url,json);
                                            int resultCode = httpUtils.getResultCode();
                                            if(resultCode == 200){
                                                    Message msg = handler2.obtainMessage();
                                                    msg.what = 1;
                                                    msg.sendToTarget();
                                                }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            } else{
                                Toast.makeText(getContext(),"取消失败，无法取消订单！",Toast.LENGTH_LONG).show();
                            }
                                break;
                        case R.id.btn_curCstCon:
                            if(now.after(end) && serviceOrder.getServiceState().equals("待完成")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("确认并评价");
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_marks,null);
                                builder.setView(view);
                                final RadioButton rdoBtn_mark1,rdoBtn_mark2,rdoBtn_mark3,rdoBtn_mark4,rdoBtn_mark5;
                                rdoBtn_mark1 = view.findViewById(R.id.rdoBtn_mark1);
                                rdoBtn_mark2 = view.findViewById(R.id.rdoBtn_mark2);
                                rdoBtn_mark3 = view.findViewById(R.id.rdoBtn_mark3);
                                rdoBtn_mark4 = view.findViewById(R.id.rdoBtn_mark4);
                                rdoBtn_mark5 = view.findViewById(R.id.rdoBtn_mark5);
                                builder.setPositiveButton("确认并提交", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(rdoBtn_mark1.isChecked() || rdoBtn_mark2.isChecked() || rdoBtn_mark3.isChecked()
                                                || rdoBtn_mark4.isChecked() || rdoBtn_mark5.isChecked()) {
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String serviceMark = "5";
                                                        if(rdoBtn_mark1.isChecked()){
                                                            serviceMark = "1";
                                                        }else if (rdoBtn_mark2.isChecked()){
                                                            serviceMark = "2";
                                                        }else if (rdoBtn_mark3.isChecked()){
                                                            serviceMark = "3";
                                                        }else if (rdoBtn_mark4.isChecked()){
                                                            serviceMark = "4";
                                                        }else if (rdoBtn_mark5.isChecked()){
                                                            serviceMark = "5";
                                                        }
                                                        JSONObject json = new JSONObject();
                                                        URL url;
                                                        String urlpath = "http://192.168.0.102:8080/option/csrcon";
                                                        HttpUtils httpUtils = new HttpUtils();
                                                        url = new URL(urlpath);
                                                        json.put("serviceNo", serviceOrder.getServiceNo());
                                                        json.put("serviceMarks",serviceMark);
                                                        httpUtils.httputils(url, json);
                                                        int resultCode = httpUtils.getResultCode();
                                                        if (resultCode == 200) {
                                                            Message msg = handler4.obtainMessage();
                                                            msg.what = 1;
                                                            msg.sendToTarget();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        }else {
                                            Toast.makeText(getContext(),"请选择评分并重新提交",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                builder.show();
                            }else {
                                Toast.makeText(getContext(),"确认失败，请等服务结束后确认！",Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        /*
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    Gson gson = new Gson();
                    URL url;
                    String urlpath = "http://192.168.0.102:8080/option/type";
                    url = new URL(urlpath);
                    json.put("userName",userNameParam);
                    HttpUtils httpUtils = new HttpUtils();
                    httpUtils.httputils(url,json);
                    int resultCode  = httpUtils.getResultCode();
                    if(resultCode == 200){
                        InputStream inputStream = httpUtils.getInputStream();
                        String js = NetUtils.readString(inputStream);
                        User user = gson.fromJson(js,User.class);
                        userNameParam = user.getUserType();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
        */
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
