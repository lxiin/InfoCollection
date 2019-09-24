package com.hxgd.collection.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.addresswheel_master.R2;
import com.hxgd.collection.R;
import com.hxgd.collection.audio.PlaybackFragment;
import com.hxgd.collection.db.DBHelper;
import com.hxgd.collection.event.RecordEvent;
import com.hxgd.collection.user.UserInfoManager;
import com.hxgd.collection.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xzh.com.addresswheel_master.model.AddressDtailsEntity;
import xzh.com.addresswheel_master.model.AddressModel;
import xzh.com.addresswheel_master.utils.JsonUtil;
import xzh.com.addresswheel_master.utils.Utils;
import xzh.com.addresswheel_master.view.ChooseAddressWheel;
import xzh.com.addresswheel_master.view.listener.OnAddressChangeListener;

public class RecordAddActivity extends AppCompatActivity {

    @BindView(R.id.btn_audio)
    Button btnAudio;
    @BindView(R.id.btn_video)
    Button btnVideo;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_type)
    EditText etType;

    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    private RecordEvent recordEvent = null;
    private ChooseAddressWheel chooseAddressWheel = null;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordAddActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_add);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initCityPicker();
    }

    private void initCityPicker() {
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(new OnAddressChangeListener() {
            @Override
            public void onAddressChange(String province, String city, String district) {
                tvPlace.setText(province + "-" + city + "-" + district);
            }
        });

        String address = Utils.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
             if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }

    @OnClick(R.id.btn_audio)
    public void onBtnAudioClick(){
        RecordingActivity.start(this);
    }

    @SuppressLint("StaticFieldLeak")
    @OnClick(R.id.tv_save)
    public void onTvSaveClick(){

        if (recordEvent == null){
            ToastUtil.show(this,"没有记录 无法保存");
            return;
        }

        Utils.hideKeyBoard(this);

        String itemName = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();

        String place = tvPlace.getText().toString();
        if (TextUtils.isEmpty(itemName)){
            ToastUtil.show(this,"请输入名称");
            return;
        }
        if (TextUtils.isEmpty(type)){
            ToastUtil.show(this,"请输入语言类别");
            return;
        }

        if (place.equals("点击开始选择地区")){
            ToastUtil.show(this,"请选择地区");
            return;
        }

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                DBHelper.getInstance(RecordAddActivity.this).addRecording(
                        recordEvent.getFileName(),recordEvent.getFilePath(),recordEvent.getTime(),
                        recordEvent.getType(),itemName,type, UserInfoManager.getInstance().getCurrentUserInfo().getUserName()
                        ,UserInfoManager.getInstance().getCurrentUserInfo().getUserBirthDay(),place
                );
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ToastUtil.show(RecordAddActivity.this,"保存成功");
                finish();
            }
        }.execute();

    }

    @OnClick(R.id.tv_place)
    public void onTvPlaceClick(View view){
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(view);
    }


    @OnClick(R.id.iv_play)
    public void onIvPlayClick(){
        if (recordEvent == null){
            return;
        }

        if (recordEvent.getType() == 1) {
            //音频播放
            PlaybackFragment.newInstance(recordEvent.getFileName(),recordEvent.getFilePath(),recordEvent.getTime()).show(getSupportFragmentManager(), "play_audio");
        } else {
            //视频播放
            VideoPlayActivity.start(RecordAddActivity.this,recordEvent.getFilePath());
        }
    }

    @OnClick(R.id.btn_video)
    public void onBtnVideoClick(){
        RecordVideoActivity.start(this);
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClick(){
        onBackPressed();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordEvent(RecordEvent event){
        recordEvent = event;
        Logger.e("EventBus接收消息----->"+System.currentTimeMillis());

        llBottom.setVisibility(View.VISIBLE);
        llTop.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (recordEvent == null){
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setMessage("这条记录还没有保存是否要退出?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            File file = new File(recordEvent.getFilePath());
                            if (file.exists()) file.delete();
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
