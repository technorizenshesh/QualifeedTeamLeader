package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemDefectBinding;
import com.qualifeed.teamleader.databinding.ItemInboxBinding;
import com.qualifeed.teamleader.model.DefectModel;
import com.qualifeed.teamleader.model.InboxModel;

import java.util.ArrayList;

public class InboxAdapter  extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {
    Context context;
    ArrayList<InboxModel.Result> arrayList;
    public InboxAdapter(Context context, ArrayList<InboxModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInboxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_inbox, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvSubject.setText("Subject :" + arrayList.get(position).subject);
        holder.binding.tvTo.setText("From :" + arrayList.get(position).sender);
        holder.binding.tvDate.setText("Date :" + arrayList.get(position).dateTime);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemInboxBinding binding;
        public MyViewHolder(@NonNull ItemInboxBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

