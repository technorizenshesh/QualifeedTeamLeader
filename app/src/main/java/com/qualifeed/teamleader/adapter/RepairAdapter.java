package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemDefectBinding;
import com.qualifeed.teamleader.databinding.ItemRepairBinding;
import com.qualifeed.teamleader.model.RepairModel;

import java.util.ArrayList;

public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.MyViewHolder> {
    Context context;
    ArrayList<RepairModel.Result>arrayList;

    public RepairAdapter(Context context, ArrayList<RepairModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRepairBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_repair, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvId.setText("Product ID :" + arrayList.get(position).productId);
        holder.binding.tvWorkerId.setText("Worker ID :" + arrayList.get(position).workerId);
        holder.binding.tvRepairTime.setText("Repair Time :" + arrayList.get(position).timer);
        holder.binding.tvDate.setText("Date :" + arrayList.get(position).dateTime);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRepairBinding binding;
        public MyViewHolder(@NonNull ItemRepairBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}