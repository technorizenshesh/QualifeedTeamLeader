package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemBlockedBinding;
import com.qualifeed.teamleader.databinding.ItemDefectBinding;
import com.qualifeed.teamleader.model.BlockedModel;
import com.qualifeed.teamleader.model.DefectModel;

import java.util.ArrayList;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.MyViewHolder> {
    Context context;
    ArrayList<BlockedModel.Result> arrayList;
    public BlockedAdapter(Context context, ArrayList<BlockedModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBlockedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_blocked, parent, false);
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
        ItemBlockedBinding binding;
        public MyViewHolder(@NonNull ItemBlockedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}