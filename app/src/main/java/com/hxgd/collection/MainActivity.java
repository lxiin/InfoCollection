package com.hxgd.collection;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.activity.AudioActivity;
import com.hxgd.collection.activity.RecordVideoActivity;
import com.hxgd.collection.user.LoginActivity;
import com.hxgd.collection.user.SettingActivity;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, PERMISSION_LIST)) {
            onAllPermissionGranted();
        } else {
            requestPermission();
        }

    }


    public void AudioClick(View view){
        AudioActivity.start(this);
    }

    public void toUserInfoClick(View view){
        SettingActivity.start(this);
    }

    public void VideoClick(View view){


        RecordVideoActivity.start(MainActivity.this);

    }

    public void toLoginClick(View view){
        LoginActivity.start(this);
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
            if (EasyPermissions.hasPermissions(MainActivity.this, PERMISSION_LIST)) {
                onAllPermissionGranted();
            }

        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

            if (EasyPermissions.somePermissionPermanentlyDenied(MainActivity.this, perms)) {
                new AppSettingsDialog
                        .Builder(MainActivity.this)
                        .setTitle("权限请求")
                        .setRationale("缺少访问这些权限，app无法正常运行，请在设置页面中开启")
                        .build()
                        .show();
            } else {
                new AlertDialog.Builder(MainActivity.this)
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
}
