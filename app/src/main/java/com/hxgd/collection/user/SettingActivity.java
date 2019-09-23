package com.hxgd.collection.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hxgd.collection.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {


    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.rl_username)
    RelativeLayout rlUserName;
    @BindView(R.id.rl_user_phone)
    RelativeLayout rlUserPhone;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tvUserName.setText(UserInfoManager.getInstance().getUserInfo().getUserName());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }
    @OnClick(R.id.iv_back)
    public void onIvBackClick(){
        onBackPressed();
    }

    private void initView() {
        tvUserName.setText(UserInfoManager.getInstance().getUserInfo().getUserName());
        tvDevices.setText( Build.MANUFACTURER + Build.MODEL);
        tvUserPhone.setText(UserInfoManager.getInstance().getUserInfo().getUserPhone());
    }



    @OnClick({R.id.rl_username,R.id.rl_user_phone})
    public void onRlUserInfoClick(){
        UserInfoActivity.start(this);
    }

    @OnClick(R.id.btn_logout)
    public void onBtnLogoutClick(View view){
        //清空一下用户信息
        UserInfoManager.getInstance().logout();

        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
