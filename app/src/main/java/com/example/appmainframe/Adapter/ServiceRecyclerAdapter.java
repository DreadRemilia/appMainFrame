package com.example.appmainframe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Bean.Service;
import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.R;

import java.util.List;
public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder> implements View.OnClickListener {
    private List<ServiceOrder> serviceOrders;

    public class ServiceViewHolder extends RecyclerView.ViewHolder{
        TextView tv_serviceName,tv_serviceProvider,tv_servicePrice,tv_serviceType,tv_serviceCity,tv_serviceStart,tv_serviceEnd;
        Button btn_serviceOrder,btn_serviceShowProvider;
        public ServiceViewHolder(View view){
            super(view);
            tv_serviceName = view.findViewById(R.id.tv_serviceName);
            tv_serviceProvider = view.findViewById(R.id.tv_serviceProvider);
            tv_servicePrice = view.findViewById(R.id.tv_servicePrice);
            tv_serviceType = view.findViewById(R.id.tv_serviceType);
            tv_serviceCity = view.findViewById(R.id.tv_serviceCity);
            tv_serviceStart = view.findViewById(R.id.tv_serviceStart);
            tv_serviceEnd = view.findViewById(R.id.tv_serviceEnd);
            btn_serviceOrder = view.findViewById(R.id.btn_serviceOrder);
            btn_serviceShowProvider = view.findViewById(R.id.btn_serviceShowProvider);
            view.setOnClickListener(ServiceRecyclerAdapter.this);
            btn_serviceOrder.setOnClickListener(ServiceRecyclerAdapter.this);
            btn_serviceShowProvider.setOnClickListener(ServiceRecyclerAdapter.this);
        }
    }
    public ServiceRecyclerAdapter(List<ServiceOrder> mserviceorder) {
        serviceOrders = mserviceorder;
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
        ServiceOrder serviceOrder = serviceOrders.get(position);
        holder.tv_serviceName.setText(serviceOrder.getServiceName());
        holder.tv_serviceCity.setText(serviceOrder.getServiceCity());
        holder.tv_servicePrice.setText(serviceOrder.getServicePrice());
        holder.tv_serviceType.setText(serviceOrder.getServiceType());
        holder.tv_serviceProvider.setText(serviceOrder.getServiceProvider());
        holder.tv_serviceStart.setText(serviceOrder.getServiceStart());
        holder.tv_serviceEnd.setText(serviceOrder.getServiceEnd());
        holder.itemView.setTag(position);
        holder.btn_serviceShowProvider.setTag(position);
        holder.btn_serviceOrder.setTag(position);
    }

    @Override
    public int getItemCount() {
        return serviceOrders.size();
    }

    public enum ViewName{
        ITEM,
        PRACTISE
    }

    public interface OnItemListener{
        void onItemClick(View v,ViewName viewName,int position);
    }

    private OnItemListener mOnItemListener;

    public void setOnItemListener(OnItemListener listener){
        this.mOnItemListener = listener;
    }

    @Override
    public void onClick(View v){
        int position = (int)v.getTag();
        if(mOnItemListener != null){
            switch (v.getId()){
                case R.id.rcy_service:
                    mOnItemListener.onItemClick(v,ViewName.PRACTISE,position);
                    break;
                default:
                    mOnItemListener.onItemClick(v,ViewName.ITEM,position);
                    break;
            }
        }
    }

}
