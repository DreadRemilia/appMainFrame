package com.example.appmainframe.Fragment;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @ViewById(R.id.serviceRecycle)
    RecyclerView serviceRecycle;
    @AfterViews
    protected void initService() {
        //添加Service的信息
        //Test
        Service ben = new Service("ben","male");
        services.add(ben);
        Service tina = new Service("Tina","female");
        services.add(tina);
        Service lucy = new Service("Lucy","female");
        services.add(lucy);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        serviceRecycle.setLayoutManager(layoutManager);
        ServiceRecyclerAdapter adapter = new ServiceRecyclerAdapter(services);
        serviceRecycle.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        services.clear();
    }
}
