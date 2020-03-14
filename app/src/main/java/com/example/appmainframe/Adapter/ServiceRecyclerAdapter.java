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
import com.example.appmainframe.R;

import java.util.List;
public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder> implements View.OnClickListener {
    private List<Service> mService;

    public class ServiceViewHolder extends RecyclerView.ViewHolder{
        TextView serviceSex;
        Button testbutton;
        public ServiceViewHolder(View view){
            super(view);
            serviceSex = view.findViewById(R.id.serviceSex);
            testbutton = view.findViewById(R.id.testbutton);
            view.setOnClickListener(ServiceRecyclerAdapter.this);
            testbutton.setOnClickListener(ServiceRecyclerAdapter.this);
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
        holder.serviceSex.setText(service.getServiceSex());
        holder.itemView.setTag(position);
        holder.testbutton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mService.size();
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
