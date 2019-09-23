package com.hxgd.collection.user;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class LoginActivity extends AppCompatActivity {


    public static final int RESULT_CODE = 1001;
    public static final int REQUEST_CODE = 999;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_clearaccount)
    ImageView mClearIv;


    private String mAccount;
    private String mAuthcode;


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

        /**
         * SP中存有用户名的就不需要再登录
         * 反正登录 也是没有啥意义
         */
        if (!TextUtils.isEmpty(UserInfoManager.getInstance().getUserInfo().getUserPhone())){
            skip2RecordListAct();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG){
            etPhone.setText("13520663178");
            etVerifyCode.setText("");
        }



    }
    
    
    @OnClick(R.id.tv_get_code)
    public void onTvGetCodeClick(){
        String phoneNumber = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)){
            ToastUtil.show(this,"请输入手机号");
            return;
         }
        if (phoneNumber.length() != 11){
            if (TextUtils.isEmpty(etPhone.getText().toString())){
                ToastUtil.show(this,"请输入正确的手机号");
                return;
            }
        }

        getVerifyCode(phoneNumber);


    }

    private void getVerifyCode(String phone) {
        countDownButtonHelper = new CountDownButtonHelper(tvGetCode,"重新获取",60,1);
        countDownButtonHelper.start();
        LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this,"正在获取验证码...");
        loadingDialog.show();
        ApiFactory.getApiService().getVerifyCode(phone)
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
                        if (baseEntity.isSuccessful()){
                            ToastUtil.show(LoginActivity.this,"获取验证码成功");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtil.show(LoginActivity.this,"获取验证码失败");

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

        if (BuildConfig.DEBUG){
            //测试版本直接登录
            UserInfoManager.getInstance().setUserPhone(etPhone.getText().toString());
            if (UserInfoManager.getInstance().writeUserInfo()){
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,UserInfoActivity.class);
                intent.putExtra("extra","aaa");
                startActivityForResult(intent,REQUEST_CODE);
            }else{
                skip2RecordListAct();
            }
        }else{
            doLogin();
        }

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
                        /**
                         * 他这个接口只返回了code码 msg迟早是空的
                         * 在这我只判断200  其余的全部认为是登录失败
                         * 什么原因失败的 我也不知道
                         */
                        if (baseEntity.isSuccessful()){
                            ToastUtil.show(LoginActivity.this,"登录成功,需要补充用户信息");

                            UserInfoManager.getInstance().setUserPhone(phone);
                            if (UserInfoManager.getInstance().writeUserInfo()){
                                 Intent intent = new Intent();
                                 intent.setClass(LoginActivity.this,UserInfoActivity.class);
                                 intent.putExtra("extra","aaa");
                                 startActivityForResult(intent,REQUEST_CODE);
                            }else{
                                ToastUtil.show(LoginActivity.this,"登录成功");

                                skip2RecordListAct();
                            }
                        }else{
                            ToastUtil.show(LoginActivity.this,"登录失败");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        Logger.e("登录失败---->"+e.getLocalizedMessage());
                        ToastUtil.show(LoginActivity.this,"登录失败");


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void skip2RecordListAct() {
        RecordListActivity.start(LoginActivity.this);
        finish();

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_CODE){
                skip2RecordListAct();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.show(this, "再按一次,退出应用");
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }
    @OnClick(R.id.iv_clearaccount)
    void clearAccount() {
        etPhone.setText("");
    }
    private void checkInfo() {
        if (mAccount.length() == 11 && !isEmpty(mAuthcode)) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onAccountChange(CharSequence sequence, int start, int before, int count) {
        mAccount = String.valueOf(sequence);
        if (!isEmpty(mAccount)) {
            mClearIv.setVisibility(View.VISIBLE);
        } else {
            mClearIv.setVisibility(View.INVISIBLE);
        }
        checkInfo();
    }

    @OnTextChanged(value = R.id.et_verify_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onAuthcodeChange(CharSequence sequence, int start, int before, int count) {
        mAuthcode = String.valueOf(sequence);
        checkInfo();
    }
    public static boolean isEmpty(CharSequence str) {
        return (str == null) || (str.length() == 0);
    }
}
