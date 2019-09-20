package com.hxgd.collection.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.BuildConfig;
import com.hxgd.collection.R;
import com.hxgd.collection.activity.RecordListActivity;
import com.hxgd.collection.bean.BaseEntity;
import com.hxgd.collection.net.ApiFactory;
import com.hxgd.collection.utils.CountDownButtonHelper;
import com.hxgd.collection.utils.ToastUtil;
import com.hxgd.collection.view.LoadingDialog;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    //测试pull
    CountDownButtonHelper countDownButtonHelper;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, PERMISSION_LIST)) {
            onAllPermissionGranted();
        } else {
            requestPermission();
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG){
            etPhone.setText("12345678901");
            etVerifyCode.setText("1234");
        }
    }
    
    
    @OnClick(R.id.tv_get_code)
    public void onTvGetCodeClick(){
        if (TextUtils.isEmpty(etPhone.getText().toString())){
            ToastUtil.show(this,"请输入手机号");
            return;
         }
        if (etPhone.getText().toString().length() != 11){
            if (TextUtils.isEmpty(etPhone.getText().toString())){
                ToastUtil.show(this,"请输入正确的手机号");
                return;
            }
        }

        countDownButtonHelper = new CountDownButtonHelper(tvGetCode,"重新获取",60,1);
        countDownButtonHelper.start();

    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClick(){

        if (TextUtils.isEmpty(etPhone.getText().toString())){
            ToastUtil.show(this,"请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(etVerifyCode.getText().toString())){
            ToastUtil.show(this,"请输入验证码");
            return;
        }
        if (countDownButtonHelper != null) {
            countDownButtonHelper.cancel();
        }

//        doLogin();


        LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this,"正在登录...");
        loadingDialog.show();
        btnLogin.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("测试一下git");
                loadingDialog.dismiss();
                RecordListActivity.start(LoginActivity.this);
                finish();
            }
        },800);



    }

    private void doLogin(){
        LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this,"正在登录...");
        loadingDialog.show();

        String phone = etPhone.getText().toString().trim();
        String code = etVerifyCode.getText().toString().trim();
        ApiFactory.getApiService().login(code,phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);

                     }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        loadingDialog.dismiss();
                        Logger.d(baseEntity.toString());
                        ToastUtil.show(LoginActivity.this,"登录成功");

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtil.show(LoginActivity.this,"登录失败");


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void requestPermission() {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 0, PERMISSION_LIST)
                        .setRationale("我们需要一些权限来保证app正常运行")
                        .build());
    }

    private void onAllPermissionGranted() {
//        Toast.makeText(this, "全部获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, permissionCallbacks);

    }

    private EasyPermissions.PermissionCallbacks permissionCallbacks = new EasyPermissions.PermissionCallbacks() {


        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            if (EasyPermissions.hasPermissions(LoginActivity.this, PERMISSION_LIST)) {
                onAllPermissionGranted();
            }

        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

            if (EasyPermissions.somePermissionPermanentlyDenied(LoginActivity.this, perms)) {
                new AppSettingsDialog
                        .Builder(LoginActivity.this)
                        .setTitle("权限请求")
                        .setRationale("缺少访问这些权限，app无法正常运行，请在设置页面中开启")
                        .build()
                        .show();
            } else {
                new AlertDialog.Builder(LoginActivity.this)
                        .setCancelable(false)
                        .setTitle("权限请求")
                        .setMessage("缺少访问这些权限，app无法正常运行,请允许")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
