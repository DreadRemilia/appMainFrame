package com.example.appmainframe.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.R;

import java.util.List;
public class CurSerRecyclerAdapter extends RecyclerView.Adapter<CurSerRecyclerAdapter.CurSerViewHolder> implements View.OnClickListener {
    public List<ServiceOrder> serviceOrders;

    public class CurSerViewHolder extends RecyclerView.ViewHolder{
        TextView tv_curSerName,tv_curSerProvider,tv_curSerPrice,tv_curSerType,tv_curSerCity,tv_curSerStart,tv_curSerEnd,tv_curSerState;
        Button btn_curSerCancel,btn_curSerAddress;

         public CurSerViewHolder(View view){
             super(view);
             tv_curSerCity = view.findViewById(R.id.tv_curSerCity);
             tv_curSerEnd = view.findViewById(R.id.tv_curSerEnd);
             tv_curSerName = view.findViewById(R.id.tv_curSerName);
             tv_curSerPrice = view.findViewById(R.id.tv_curSerPrice);
             tv_curSerProvider = view.findViewById(R.id.tv_curSerProvider);
             tv_curSerStart = view.findViewById(R.id.tv_curSerStart);
             tv_curSerState = view.findViewById(R.id.tv_curSerState);
             tv_curSerType = view.findViewById(R.id.tv_curSerType);
             btn_curSerCancel = view.findViewById(R.id.btn_curSerCancel);
             btn_curSerAddress = view.findViewById(R.id.btn_curSerAddress);
             view.setOnClickListener(CurSerRecyclerAdapter.this);
             btn_curSerCancel.setOnClickListener(CurSerRecyclerAdapter.this);
             btn_curSerAddress.setOnClickListener(CurSerRecyclerAdapter.this);
         }
    }

    public CurSerRecyclerAdapter(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    public CurSerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cue_ser_item,parent,false);
        CurSerViewHolder holder = new CurSerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurSerViewHolder holder, int position) {
        ServiceOrder serviceOrder = serviceOrders.get(position);
        holder.tv_curSerType.setText(serviceOrder.getServiceType());
        holder.tv_curSerState.setText(serviceOrder.getServiceState());
        holder.tv_curSerStart.setText(serviceOrder.getServiceStart());
        holder.tv_curSerProvider.setText(serviceOrder.getServiceProvider());
        holder.tv_curSerPrice.setText(serviceOrder.getServicePrice());
        holder.tv_curSerName.setText(serviceOrder.getServiceName());
        holder.tv_curSerEnd.setText(serviceOrder.getServiceEnd());
        holder.tv_curSerCity.setText(serviceOrder.getServiceCity());
        holder.itemView.setTag(position);
        holder.btn_curSerCancel.setTag(position);
        holder.btn_curSerAddress.setTag(position);
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
        void onItemClick(View v,ViewName viewName, int position);
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
                case R.id.rcy_curCstService:
                    mOnItemListener.onItemClick(v, ViewName.PRACTISE,position);
                    break;
                default:
                    mOnItemListener.onItemClick(v, ViewName.ITEM,position);
                    break;
            }
        }
    }
}
