package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemDefectBinding;
import com.qualifeed.teamleader.databinding.ItemScrapBinding;
import com.qualifeed.teamleader.model.ScrapModel;

import java.util.ArrayList;

public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.MyViewHolder> {
    Context context;
    ArrayList<ScrapModel.Result> arrayList;

    public ScrapAdapter(Context context,ArrayList<ScrapModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScrapBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scrap, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvId.setText("Defect ID :" + arrayList.get(position).id);
        holder.binding.tvWorkerId.setText("Worker ID :" + arrayList.get(position).workerId);
        holder.binding.tvProductId.setText("ProductRef  :" + arrayList.get(position).productId);
        holder.binding.tvDate.setText("Date :" + arrayList.get(position).dateTime);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemScrapBinding binding;
        public MyViewHolder(@NonNull ItemScrapBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
