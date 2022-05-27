package com.qualifeed.teamleader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.qualifeed.teamleader.DashboardAct;
import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.ItemDefectListBinding;
import com.qualifeed.teamleader.model.DefectListModel;

import java.util.ArrayList;

public class DefectProductAdapter extends RecyclerView.Adapter<DefectProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<DefectListModel.Result>arrayList;

    public DefectProductAdapter(Context context, ArrayList<DefectListModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDefectListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_defect_list,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         setChar(String.valueOf(arrayList.get(position).getDefectsCount()),holder,position);
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDefectListBinding binding;
        public MyViewHolder(@NonNull ItemDefectListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


    private void setChar(String d1/*,ItemDefectListBinding binding*/,MyViewHolder holder,int position ) {
        BarData data = new BarData(getDataSet(d1));
        holder.binding.chart.setData(data);
        holder.binding.chart.animateXY(2000, 2000);
        holder.binding.chart.invalidate();
        holder.binding.chart.getAxisRight().setDrawGridLines(false);
        holder.binding.chart.getAxisLeft().setDrawGridLines(false);
        holder.binding.chart.getXAxis().setEnabled(false);
        holder.binding.chart.getXAxis().setDrawAxisLine(false);
        holder.binding.chart.getDescription().setEnabled(false);
        holder.binding.chart.getAxisLeft().setDrawLabels(false);
        holder.binding.chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);

        holder.binding.chart.getLegend().setEnabled(false);


        //remove left border
        holder.binding.chart.getAxisLeft().setDrawAxisLine(false);

        //remove right border
        holder.binding.chart.getAxisRight().setDrawAxisLine(false);

        holder.binding.tvname.setText(arrayList.get(position).getName());
        holder.binding.tvCount.setText(arrayList.get(position).getDefectsCount()+"");

    }


    private BarDataSet getDataSet(String d1) {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(0f, Float.parseFloat(d1)));
      //  entries.add(new BarEntry(2f, Float.parseFloat(d2)));
     //   entries.add(new BarEntry(4f, Float.parseFloat(d1)));
        //   entries.add(new BarEntry(12f, 3));
        //    entries.add(new BarEntry(18f, 4));
        //     entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries,"hi");
      if (Integer.parseInt(d1)> 50)
        dataset.setColors(new int[]{R.color.dark_gray} , context);
       else         dataset.setColors(new int[]{R.color.gray_light} , context);

        return dataset;
    }

}
