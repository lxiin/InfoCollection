package com.hxgd.collection.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;
import com.hxgd.collection.audio.PlaybackFragment;
import com.hxgd.collection.db.RecordDataBase;
import com.hxgd.collection.db.RecordEntity;
import com.hxgd.collection.event.RecordEvent;
import com.hxgd.collection.utils.AppUtils;
import com.hxgd.collection.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

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
    @BindView(R.id.sl_info)
    ScrollView slInfo;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    @BindView(R.id.tv_itemname)
    TextView tvItemName;
    @BindView(R.id.tv_laguage_type)
    TextView tvLaguageType;
    @BindView(R.id.tv_population)
    TextView tvPopulation;
    @BindView(R.id.tv_spoker)
    TextView tvSpoker;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_faher_laguage)
    TextView tvFatherLaguage;
    @BindView(R.id.tv_father_nation)
    TextView tvFatherNation;
    @BindView(R.id.tv_mother_laguage)
    TextView tvMotherLaguage;
    @BindView(R.id.tv_mother_nation)
    TextView tvMotherNation;
    @BindView(R.id.tv_marital_status)
    TextView tvMarital_status;
    @BindView(R.id.tv_spousenation)
    TextView tvSpousenation;
    @BindView(R.id.rl_item_name)
    RelativeLayout rlItemName;
    @BindView(R.id.rl_place)
    RelativeLayout rlPlace;
    @BindView(R.id.rl_spoker)
    RelativeLayout rlSpoker;
    @BindView(R.id.rl_gender)
    RelativeLayout rlGender;
    @BindView(R.id.rl_education)
    RelativeLayout rlEducation;
    @BindView(R.id.rl_birthday)
    RelativeLayout rlBirthday;
    @BindView(R.id.rl_faher_laguage)
    RelativeLayout rlFatherLaguage;
    @BindView(R.id.rl_father_nation)
    RelativeLayout rlFatherNation;
    @BindView(R.id.rl_population)
    RelativeLayout rlPopulation;
    @BindView(R.id.rl_mother_lauguage)
    RelativeLayout rlMotherLauage;
    @BindView(R.id.rl_mother_nation)
    RelativeLayout rlMotherNation;
    @BindView(R.id.rl_marital_status)
    RelativeLayout rlMaritalStatus;
    @BindView(R.id.rl_spousenation)
    RelativeLayout rlSpousenation;



    private RecordEvent recordEvent = null;
    private ChooseAddressWheel chooseAddressWheel = null;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordAddActivity.class);
        context.startActivity(starter);
    }

    private final int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;


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

        if (!checkInfo()){
            ToastUtil.show(this,"请补全信息再保存");
            return;
        }

        String itemName = tvItemName.getText().toString();
        String laguageType = tvLaguageType.getText().toString();
        String population = tvPopulation.getText().toString();
        String place = tvPlace.getText().toString();
        String spoker = tvSpoker.getText().toString();
        String gender = tvGender.getText().toString();
        String education = tvEducation.getText().toString();
        String birthday = tvBirthday.getText().toString();
        String fatherLaguage = tvFatherLaguage.getText().toString();
        String fatherNation = tvFatherNation.getText().toString();
        String motherLaguage = tvMotherLaguage.getText().toString();
        String motherNation = tvMotherNation.getText().toString();
        String maritalStatus = tvMarital_status.getText().toString();
        String spousenation= tvSpousenation.getText().toString();


        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                RecordEntity recordEntity = new RecordEntity(itemName,recordEvent.getFileName(),recordEvent.getFilePath(),recordEvent.getTime(),
                        spoker,gender,birthday,education,fatherLaguage,fatherNation,
                        motherLaguage,motherNation,maritalStatus,spousenation,"","","",laguageType,Integer.parseInt(population),
                        place,AppUtils.timeStampToTime(System.currentTimeMillis()),recordEvent.getType(),0);
                RecordDataBase.getInstance(RecordAddActivity.this).recordDao().insert(recordEntity);
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


    private boolean checkInfo(){
        if (tvItemName.getText().toString().isEmpty()||tvItemName.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvLaguageType.getText().toString().isEmpty()||tvLaguageType.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvPlace.getText().toString().isEmpty()||tvPlace.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvSpoker.getText().toString().isEmpty()||tvSpoker.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvGender.getText().toString().isEmpty()||tvGender.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvEducation.getText().toString().isEmpty()||tvEducation.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvBirthday.getText().toString().isEmpty()||tvBirthday.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvFatherLaguage.getText().toString().isEmpty()||tvFatherLaguage.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvFatherNation.getText().toString().isEmpty()||tvFatherNation.getText().toString().contains("点击输入")){
            return false;
        } if (tvMotherLaguage.getText().toString().isEmpty()||tvMotherLaguage.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvMotherNation.getText().toString().isEmpty()||tvMotherNation.getText().toString().contains("点击输入")){
            return false;
        }
        if (tvMarital_status.getText().toString().isEmpty()||tvMarital_status.getText().toString().contains("点击输入")){
            return false;
        }

       return true;


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

        slInfo.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.rl_spoker)
    public void onRlSpokerClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入发言人")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvSpoker.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();

    }


    @OnClick(R.id.rl_population)
    public void onRlpopulationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入使用人数")
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvPopulation.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }


    @OnClick(R.id.rl_item_name)
    public void onRlItemNameClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvItemName.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();

    }

    @OnClick(R.id.rl_laguage_type)
    public void onRlLaguageTypeClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入语言类别")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvLaguageType.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();

    }

    @OnClick(R.id.rl_gender)
    public void onRlGenderClick(){
        final String[] items = new String[]{"男", "女"};
         new QMUIDialog.CheckableDialogBuilder(RecordAddActivity.this)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvGender.setText(items[which]);
                        dialog.dismiss();
                        tvSave.setVisibility(View.VISIBLE);

                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick({R.id.rl_place})
    public void onRlPlaceClick(View view){
        Utils.hideKeyBoard(this);
        chooseAddressWheel.show(view);
    }

    @OnClick(R.id.rl_education)
    public void onEducationCLick(){
        final String[] items = new String[]{"初中", "高中","专科","大学","研究生","硕士","博士","博士后"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(RecordAddActivity.this)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvEducation.setText(items[which]);
                        dialog.dismiss();
                        tvSave.setVisibility(View.VISIBLE);

                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_faher_laguage)
    public void onRlFatherLaguageClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您父亲的语种")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvFatherLaguage.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();

    }

    @OnClick(R.id.rl_father_nation)
    public void onRlFatherNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您父亲的民族")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvFatherNation.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_mother_lauguage)
    public void onRlMotherLaguageClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您母亲的语种")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvMotherLaguage.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_mother_nation)
    public void onMotherNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您母亲的民族")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvMotherNation.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_marital_status)
    public void onMaritalStatusClick(){
        final String[] items = new String[]{"已婚", "未婚"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(RecordAddActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvMarital_status.setText(items[which]);
                        dialog.dismiss();
                        tvSave.setVisibility(View.VISIBLE);

                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_spousenation)
    public void onRlSpouseNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您配偶的民族")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvSpousenation.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);

                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }


    @OnClick(R.id.rl_birthday)
    public void onRlBirthDayClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(RecordAddActivity.this);
        builder.setPlaceholder("在此输入您的生日")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            tvBirthday.setText(text);
                            dialog.dismiss();
                            tvSave.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtil.show(RecordAddActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
