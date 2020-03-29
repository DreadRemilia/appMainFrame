package com.example.appmainframe.Adapter;

import android.text.Layout;
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

public class CurCstRecyclerAdapter extends RecyclerView.Adapter<CurCstRecyclerAdapter.CurCstViewHolder> implements View.OnClickListener {
    public List<ServiceOrder> serviceOrders;

    public class CurCstViewHolder extends RecyclerView.ViewHolder{
        TextView tv_curCstName,tv_curCstProvider,tv_curCstPrice,tv_curCstType,tv_curCstCity,tv_curCstStart,tv_curCstEnd,tv_curCstState;
        Button btn_curCstCancel,btn_curCstCon;
        public CurCstViewHolder(View view){
            super(view);
            tv_curCstName = view.findViewById(R.id.tv_curCstName);
            tv_curCstProvider = view.findViewById(R.id.tv_curCstProvider);
            tv_curCstPrice = view.findViewById(R.id.tv_curCstPrice);
            tv_curCstType = view.findViewById(R.id.tv_curCstType);
            tv_curCstCity = view.findViewById(R.id.tv_curCstCity);
            tv_curCstStart = view.findViewById(R.id.tv_curCstStart);
            tv_curCstEnd = view.findViewById(R.id.tv_curCstEnd);
            tv_curCstState = view.findViewById(R.id.tv_curCstState);
            btn_curCstCancel = view.findViewById(R.id.btn_curCstCancel);
            btn_curCstCon = view.findViewById(R.id.btn_curCstCon);
            view.setOnClickListener(CurCstRecyclerAdapter.this);
            btn_curCstCancel.setOnClickListener(CurCstRecyclerAdapter.this);
            btn_curCstCon.setOnClickListener(CurCstRecyclerAdapter.this);
        }
    }

    public CurCstRecyclerAdapter(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    public CurCstViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cue_cst_item,parent,false);
        CurCstViewHolder holder = new CurCstViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurCstViewHolder holder, int position) {
        ServiceOrder serviceOrder = serviceOrders.get(position);
        holder.tv_curCstCity.setText(serviceOrder.getServiceCity());
        holder.tv_curCstEnd.setText(serviceOrder.getServiceEnd());
        holder.tv_curCstName.setText(serviceOrder.getServiceName());
        holder.tv_curCstPrice.setText(serviceOrder.getServicePrice());
        holder.tv_curCstProvider.setText(serviceOrder.getServiceProvider());
        holder.tv_curCstStart.setText(serviceOrder.getServiceStart());
        holder.tv_curCstState.setText(serviceOrder.getServiceState());
        holder.tv_curCstType.setText(serviceOrder.getServiceType());
        holder.itemView.setTag(position);
        holder.btn_curCstCancel.setTag(position);
        holder.btn_curCstCon.setTag(position);
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
                    mOnItemListener.onItemClick(v,ViewName.PRACTISE,position);
                    break;
                default:
                    mOnItemListener.onItemClick(v,ViewName.ITEM,position);
                    break;
            }
        }
    }

}

