package com.hxgd.collection.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackUpActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    public static void start(Context context) {
        Intent starter = new Intent(context, BackUpActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClick(){
        finish();
    }
}
