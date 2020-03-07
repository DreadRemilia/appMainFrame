package com.example.appmainframe.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Bean.Service;
import com.example.appmainframe.R;

import java.util.List;
public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder> {
    private List<Service> mService;
    class ServiceViewHolder extends RecyclerView.ViewHolder{
        TextView serviceName;
        TextView serviceSex;
        public ServiceViewHolder(View view){
            super(view);
            serviceName = view.findViewById(R.id.serviceName);
            serviceSex = view.findViewById(R.id.serviceSex);
        }
    }
    public ServiceRecyclerAdapter(List<Service> services) {
        mService = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_item,parent,false);
        ServiceViewHolder holder = new ServiceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = mService.get(position);
        holder.serviceName.setText(service.getServiceName());
        holder.serviceSex.setText(service.getServiceSex());
    }

    @Override
    public int getItemCount() {
        return mService.size();
    }
}
