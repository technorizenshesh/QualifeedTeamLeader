package com.qualifeed.teamleader;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.qualifeed.teamleader.databinding.ActivityMsgDetailsBinding;
import com.qualifeed.teamleader.model.SentModel;

public class SentMsgDetails extends AppCompatActivity {
    ActivityMsgDetailsBinding binding;
    SentModel.Result result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_msg_details);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            result = (SentModel.Result) getIntent().getSerializableExtra("details");
            binding.tvTitle.setText("Subject : " +result.getSubject());
            binding.tvProductId.setText("TO : "+result.getFromEmail());
            binding.tvDate.setText("Date : "+result.getCreatedDate());
            binding.tvTitle.setText(result.getMessage());

            Glide.with(SentMsgDetails.this)
                    .load(result.getImage())
                    .override(150,150)
                    .error(R.drawable.dummy)
                    .into(binding.ivImg);


        }



    }


}
