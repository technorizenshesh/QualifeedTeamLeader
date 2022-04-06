package com.qualifeed.teamleader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.qualifeed.teamleader.R;
import com.qualifeed.teamleader.databinding.FragmentDefectBinding;

public class DefectFragment extends Fragment {
   FragmentDefectBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_defect, container, false);
        return binding.getRoot();
    }
}