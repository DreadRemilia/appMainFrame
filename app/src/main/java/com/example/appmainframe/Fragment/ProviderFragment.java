package com.example.appmainframe.Fragment;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmainframe.Activity.MainActivity;
import com.example.appmainframe.Bean.EventMessage;
import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.R;
import com.example.appmainframe.Utils.HttpUtils;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.feezu.liuli.timeselector.TimeSelector;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_provider)
public class ProviderFragment extends Fragment {

    String userNameParam;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                tv_providerInfo.setText("发布成功,可至个人设置内查看");
                ev_providerServiceName.setText("");
                tv_providerCity.setText("点击按钮选择城市");
                tv_providerEnd.setText("点击选择结束时间");
                tv_providerStart.setText("点击选择开始时间");
                ev_providerPrice.setText("");
                rdoBtn_providerService.setChecked(false);
                rdoBtn_providerOther.setChecked(false);
            }
        }
    };

    @ViewById(R.id.ev_providerServiceName)
    EditText ev_providerServiceName;
    @ViewById(R.id.tv_providerCity)
    TextView tv_providerCity;
    @ViewById(R.id.btn_providerSelectCity)
    Button btn_providerSelectCity;
    @ViewById(R.id.tv_providerStart)
    TextView tv_providerStart;
    @ViewById(R.id.tv_providerEnd)
    TextView tv_providerEnd;
    @ViewById(R.id.rdoBtn_providerService)
    RadioButton rdoBtn_providerService;
    @ViewById(R.id.rdoBtn_providerOther)
    RadioButton rdoBtn_providerOther;
    @ViewById(R.id.tv_providerInfo)
    TextView tv_providerInfo;
    @ViewById(R.id.btn_providerPublish)
    Button btn_providerPublish;
    @ViewById(R.id.btn_providerClean)
    Button btn_providerClean;
    @ViewById(R.id.ev_providerPrice)
    EditText ev_providerPrice;

    List<HotCity> hotCities = new ArrayList<>();

    @Click(R.id.tv_providerStart)
    protected void providerStartClick(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format1 = format.format(date);
        TimeSelector timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_providerStart.setText(time);
            }
        },format1,"2050-1-1 24:00");
        timeSelector.show();
    }

    @Click(R.id.tv_providerEnd)
    protected void providerEndClick(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format1 = format.format(date);
        TimeSelector timeSelector = new TimeSelector(getContext(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_providerEnd.setText(time);
            }
        },format1,"2050-1-1 24:00");
        timeSelector.show();
    }

    @Click(R.id.btn_providerClean)
    protected void providerCleanClick(){
        ev_providerServiceName.setText("");
        tv_providerCity.setText("点击按钮选择城市");
        tv_providerEnd.setText("点击选择结束时间");
        tv_providerStart.setText("点击选择开始时间");
        tv_providerInfo.setText("");
        ev_providerPrice.setText("");
        rdoBtn_providerService.setChecked(false);
        rdoBtn_providerOther.setChecked(false);
    }

    @Click(R.id.btn_providerPublish)
    protected void providerPublishClick() {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date beginTime = sf.parse(tv_providerStart.getText().toString());
            Date endTime = sf.parse(tv_providerEnd.getText().toString());
            if (ev_providerServiceName.getText().toString().equals("")) {
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            } else if (tv_providerCity.getText().toString().equals("点击按钮选择城市")) {
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            } else if (tv_providerStart.getText().toString().equals("点击选择开始时间")) {
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            } else if (tv_providerEnd.getText().toString().equals("点击选择结束时间")) {
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            } else if (!(rdoBtn_providerOther.isChecked() || rdoBtn_providerService.isChecked())) {
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            } else if (judgeCalendar(beginTime, endTime)) {
                tv_providerInfo.setText("时间设置错误，请重新设置！");
            } else if (Integer.valueOf(ev_providerPrice.getText().toString()) < 0){
                tv_providerInfo.setText("上述配置设置有误，请重新输入！");
            }
              else {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject();
                            ServiceOrder serviceOrder = new ServiceOrder();
                            String urlpath = "http://192.168.0.102:8080/main/publish";
                            URL url;
                            serviceOrder.setServiceName(ev_providerServiceName.getText().

                                    toString());
                            serviceOrder.setServiceCity(tv_providerCity.getText().

                                    toString());
                            serviceOrder.setServiceStart(tv_providerStart.getText().

                                    toString());
                            serviceOrder.setServiceEnd(tv_providerEnd.getText().

                                    toString());
                            serviceOrder.setServiceType(rdoBtn_providerService.isChecked() ? rdoBtn_providerService.getText().

                                    toString()
                                    : rdoBtn_providerOther.getText().

                                    toString());
                            serviceOrder.setServicePrice(ev_providerPrice.getText().toString());
                            serviceOrder.setServiceProvider(userNameParam);
                            url = new

                                    URL(urlpath);

                            HttpUtils httpUtils = new HttpUtils();
                            json.put("serviceName", serviceOrder.getServiceName());
                            json.put("serviceCity", serviceOrder.getServiceCity());
                            json.put("serviceStart", serviceOrder.getServiceStart());
                            json.put("serviceEnd", serviceOrder.getServiceEnd());
                            json.put("serviceType", serviceOrder.getServiceType());
                            json.put("serviceProvider", serviceOrder.getServiceProvider());
                            json.put("servicePrice",serviceOrder.getServicePrice());
                            httpUtils.httputils(url, json);
                            int resultCode = httpUtils.getResultCode();
                            if(resultCode == 200){
                                Message msg = handler.obtainMessage();
                                msg.what = 1;
                                msg.sendToTarget();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
                }catch(Exception e){
                    tv_providerInfo.setText("上述配置设置有误，请重新输入！");
                }
        }
    @AfterViews
    protected void initView(){
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
    }

    @Click(R.id.btn_providerSelectCity)
    public void ProviderSelectCityClick(){
        CityPicker.from(ProviderFragment.this)
                .enableAnimation(true)
                .setAnimationStyle(R.style.DefaultCityPickerAnimation)
                .setLocatedCity(null)
                .setHotCities(hotCities)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        tv_providerCity.setText(String.format("%s", data.getName()));
                    }

                    @Override
                    public void onLocate() {
                        Toast.makeText(getContext(),"取消选择",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CityPicker.from(ProviderFragment.this).locateComplete(new LocatedCity("上海", "上海", "101020100"), LocateState.SUCCESS);
                            }
                        },100);
                    }
                }).show();
    }

    public static boolean judgeCalendar(Date beginTime,Date endTime){
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if(end.before(begin) || end.equals(begin)){
            return true;
        }else {
            return false;
        }
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
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    public void onReceiveUserName(EventMessage eventMessage){
        userNameParam = eventMessage.getMessage();
    }

}
