package com.example.appmainframe.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmainframe.Bean.ServiceOrder;
import com.example.appmainframe.R;

import java.util.List;

public class HisRecyclerAdapter extends RecyclerView.Adapter<HisRecyclerAdapter.HisViewHolder> {
    private List<ServiceOrder> serviceOrders;

    public class HisViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hisName,tv_hisProvider,tv_hisPrice,tv_hisType,tv_hisCity,tv_hisStart,tv_hisEnd,tv_hisState;
        public HisViewHolder(@NonNull View itemView) {
            super(itemView);
            //绑定ID
            tv_hisName = itemView.findViewById(R.id.tv_hisName);
            tv_hisProvider = itemView.findViewById(R.id.tv_hisProvider);
            tv_hisPrice = itemView.findViewById(R.id.tv_hisPrice);
            tv_hisType = itemView.findViewById(R.id.tv_hisType);
            tv_hisCity = itemView.findViewById(R.id.tv_hisCity);
            tv_hisStart = itemView.findViewById(R.id.tv_hisStart);
            tv_hisEnd = itemView.findViewById(R.id.tv_hisEnd);
            tv_hisState = itemView.findViewById(R.id.tv_hisState);
        }
    }

    public HisRecyclerAdapter(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    @NonNull
    @Override
    public HisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.his_item,parent,false);
        HisViewHolder holder = new HisViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HisViewHolder holder, int position) {
        ServiceOrder serviceOrder = serviceOrders.get(position);
        holder.tv_hisCity.setText(serviceOrder.getServiceCity());
        holder.tv_hisEnd.setText(serviceOrder.getServiceEnd());
        holder.tv_hisName.setText(serviceOrder.getServiceName());
        holder.tv_hisPrice.setText(serviceOrder.getServicePrice());
        holder.tv_hisProvider.setText(serviceOrder.getServiceProvider());
        holder.tv_hisStart.setText(serviceOrder.getServiceStart());
        holder.tv_hisState.setText(serviceOrder.getServiceState());
        holder.tv_hisType.setText(serviceOrder.getServiceType());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return serviceOrders.size();
    }

}
