package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemRepairBinding;
import com.qualifeed.teamleader.databinding.ItemTeamBinding;
import com.qualifeed.teamleader.model.RepairModel;
import com.qualifeed.teamleader.model.TeamModel;
import com.qualifeed.teamleader.retrofit.onPosListener;

import java.util.ArrayList;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {
    Context context;
    ArrayList<TeamModel.Result> arrayList;
    onPosListener listener;

    public TeamAdapter(Context context, ArrayList<TeamModel.Result>arrayList,onPosListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_team, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvWorkName.setText(arrayList.get(position).workerName);
        holder.binding.tvWorkerId.setText("Worker ID :" + arrayList.get(position).workerId);
        holder.binding.tvTeamId.setText("TEAM ID :" + arrayList.get(position).id);
        holder.binding.tvStatus.setText( arrayList.get(position).status);

        if(arrayList.get(position).status.equals("Block")) holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.color_red));
        else holder.binding.tvStatus.setBackgroundColor(context.getColor(R.color.color_green));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTeamBinding binding;
        public MyViewHolder(@NonNull ItemTeamBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.tvStatus.setOnClickListener(v -> {
                if(arrayList.get(getAdapterPosition()).status.equals("Unblock")) listener.onPos(getAdapterPosition(),"Block");
                 else listener.onPos(getAdapterPosition(),"Unblock");

                 notifyDataSetChanged();
            });


            binding.layoutMain.setOnClickListener(v -> {
                listener.onPos(getAdapterPosition(),"Next");
            });



        }
    }
}