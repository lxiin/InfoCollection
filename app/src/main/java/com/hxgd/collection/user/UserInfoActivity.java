package com.hxgd.collection.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_user_icon)
    CircleImageView ivUserIcon;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.tv_phone)
    TextView tvPhone;


    public static void start(Context context) {
        Intent starter = new Intent(context, UserInfoActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        tvUserName.setText("姓名：张三");
        tvDevice.setText("采集设备:"+ Build.MANUFACTURER + " " + Build.MODEL);
        tvPhone.setText("电话：1234567890");
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClick(){
        finish();
    }
}
