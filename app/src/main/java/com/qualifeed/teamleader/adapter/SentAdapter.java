package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.SentMsgDetails;
import com.qualifeed.teamleader.databinding.ItemInboxBinding;
import com.qualifeed.teamleader.model.SentModel;

import java.util.ArrayList;

public class SentAdapter  extends RecyclerView.Adapter<SentAdapter.MyViewHolder> {
    Context context;
    ArrayList<SentModel.Result> arrayList;
    public SentAdapter(Context context, ArrayList<SentModel.Result> arrayList) {
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
        holder.binding.tvSubject.setText("Subject :" + arrayList.get(position).getSubject());
        holder.binding.tvTo.setText("TO :" + arrayList.get(position).getToEmail());
        holder.binding.tvDate.setText("Date :" + arrayList.get(position).getCreatedDate());
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

            binding.layoutMain.setOnClickListener(v -> {
                context.startActivity(new Intent(context, SentMsgDetails.class)
                .putExtra("details",arrayList.get(getAdapterPosition())));
            });
        }
    }
}
