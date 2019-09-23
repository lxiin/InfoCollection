package com.hxgd.collection.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hxgd.collection.R;
import com.hxgd.collection.utils.Constant;
import com.hxgd.collection.utils.SP;

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
    @BindView(R.id.btn_logout)
    Button btnLogout;

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
        tvPhone.setText("电话:"+ SP.get().getString(Constant.SP_USER_NAME));
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClick(){
        finish();
    }

    @OnClick(R.id.btn_logout)
    public void onBtnLogoutClick(View view){
        SP.get().putString(Constant.SP_USER_NAME,"");
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
