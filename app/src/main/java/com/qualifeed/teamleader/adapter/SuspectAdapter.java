package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemDefectBinding;
import com.qualifeed.teamleader.model.DefectModel;
import com.qualifeed.teamleader.model.RepairModel;

import java.util.ArrayList;

public class SuspectAdapter extends RecyclerView.Adapter<SuspectAdapter.MyViewHolder> {
    Context context;
    ArrayList<DefectModel.Result> arrayList;
    public SuspectAdapter(Context context, ArrayList<DefectModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDefectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_defect, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvId.setText("Defect ID :" + arrayList.get(position).id);
        holder.binding.tvWorkerId.setText("Worker ID :" + arrayList.get(position).workerId);
        holder.binding.tvProductId.setText("Product ID :" + arrayList.get(position).productId);
        holder.binding.tvDate.setText("Date :" + arrayList.get(position).dateTime);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDefectBinding binding;
        public MyViewHolder(@NonNull ItemDefectBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
