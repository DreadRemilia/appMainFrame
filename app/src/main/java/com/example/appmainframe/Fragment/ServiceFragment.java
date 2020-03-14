package com.example.appmainframe.Fragment;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Activity.FrameActivity_;
import com.example.appmainframe.R;
import com.example.appmainframe.Bean.Service;
import com.example.appmainframe.Adapter.ServiceRecyclerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_service)
public class ServiceFragment extends Fragment {
    List<Service> services = new ArrayList<>();
    @ViewById(R.id.rcy_service)
    RecyclerView rcy_service;
    @AfterViews
    protected void initService() {
        //添加Service的信息
        Service service = new Service("","");
        service.setServiceName("fafafa");
        service.setServiceSex("male");
        services.add(service);
        Service service1 = new Service("fafa","fafa");
        services.add(service1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcy_service.setLayoutManager(layoutManager);
        ServiceRecyclerAdapter adapter = new ServiceRecyclerAdapter(services);
        rcy_service.setAdapter(adapter);
        adapter.setOnItemListener(new ServiceRecyclerAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, ServiceRecyclerAdapter.ViewName viewName, int position) {
                Intent intent = new Intent(getActivity(), FrameActivity_.class);
                intent.putExtra("chatmsg","find"+position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        services.clear();
    }
}
