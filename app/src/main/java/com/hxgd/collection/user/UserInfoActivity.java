package com.hxgd.collection.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;
import com.hxgd.collection.utils.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity {

    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_username)
    TextView tvUserName;
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
    @BindView(R.id.rl_username)
    RelativeLayout rlUserName;
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
    @BindView(R.id.rl_mother_lauguage)
    RelativeLayout rlMotherLauage;
    @BindView(R.id.rl_mother_nation)
    RelativeLayout rlMotherNation;
    @BindView(R.id.rl_marital_status)
    RelativeLayout rlMaritalStatus;
    @BindView(R.id.rl_spousenation)
    RelativeLayout rlSpousenation;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_save)
    TextView tvSave;


    private String extra;

    private final int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;


    public static void start(Context context) {
        Intent starter = new Intent(context, UserInfoActivity.class);
        context.startActivity(starter);
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extra = getIntent().getStringExtra("extra");
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);

        initUserInfo();

    }

    private void initUserInfo() {
        UserInfo userInfo = UserInfoManager.getInstance().getCurrentUserInfo();
        tvUserPhone.setText(userInfo.getUserPhone());
        tvUserName.setText(userInfo.getUserName());
        tvGender.setText(userInfo.getGender());
        tvEducation.setText(userInfo.getEducation());
        tvBirthday.setText(userInfo.getUserBirthDay());
        tvFatherLaguage.setText(userInfo.getFatherLauages());
        tvFatherNation.setText(userInfo.getFatherNation());
        tvMotherLaguage.setText(userInfo.getMotherLauages());
        tvMotherNation.setText(userInfo.getMotherNation());
        tvMarital_status.setText(userInfo.getMaritalStatus());
        tvSpousenation.setText(userInfo.getSpouseNation());
    }

    @OnClick(R.id.iv_back)
    public void IvBackClick(View view){
        onBackPressed();
    }

    @OnClick(R.id.tv_save)
    public void onTvSaveClick(){

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(tvUserName.getText().toString().trim());
        userInfo.setUserPhone(tvUserPhone.getText().toString());
        userInfo.setGender(tvGender.getText().toString());
        userInfo.setEducation(tvEducation.getText().toString());
        userInfo.setUserBirthDay(tvBirthday.getText().toString());
        userInfo.setFatherLauages(tvFatherLaguage.getText().toString());
        userInfo.setFatherNation(tvFatherNation.getText().toString());
        userInfo.setMotherLauages(tvMotherLaguage.getText().toString());
        userInfo.setMotherNation(tvMotherNation.getText().toString());
        userInfo.setMaritalStatus(tvMarital_status.getText().toString());
        userInfo.setSpouseNation(tvSpousenation.getText().toString());

        UserInfoManager.getInstance().changeCurrentUserInfo(userInfo);

        onBackPressed();

    }

    @OnClick(R.id.rl_username)
    public void onRlUserNameClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
        builder.setTitle("修改用户名")
                .setPlaceholder("在此输入您的户名")
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
                            tvUserName.setText(text);
                             dialog.dismiss();
                             tvSave.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtil.show(UserInfoActivity.this,"请输入用户名");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();


    }

    @OnClick(R.id.rl_gender)
    public void onRlGenderClick(){
        final String[] items = new String[]{"男", "女"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(UserInfoActivity.this)
                .setCheckedIndex(checkedIndex)
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

    @OnClick(R.id.rl_education)
    public void onEducationCLick(){
        final String[] items = new String[]{"初中", "高中","专科","大学","研究生","硕士","博士","博士后"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(UserInfoActivity.this)
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
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();

    }

    @OnClick(R.id.rl_father_nation)
    public void onRlFatherNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_mother_lauguage)
    public void onRlMotherLaguageClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_mother_nation)
    public void onMotherNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_marital_status)
    public void onMaritalStatusClick(){
        final String[] items = new String[]{"已婚", "未婚"};
        final int checkedIndex = 0;
        new QMUIDialog.CheckableDialogBuilder(UserInfoActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1){
                            rlSpousenation.setVisibility(View.GONE);
                        }else{
                            rlSpousenation.setVisibility(View.VISIBLE);
                        }
                        tvMarital_status.setText(items[which]);
                        dialog.dismiss();
                        tvSave.setVisibility(View.VISIBLE);

                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @OnClick(R.id.rl_spousenation)
    public void onRlSpouseNationClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }


    @OnClick(R.id.rl_birthday)
    public void onRlBirthDayClick(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(UserInfoActivity.this);
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
                            ToastUtil.show(UserInfoActivity.this,"请输入内容再保存");
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    @Override
    public void onBackPressed() {

        if (TextUtils.isEmpty(tvUserName.getText().toString()) && TextUtils.isEmpty(tvGender.getText().toString())
                && TextUtils.isEmpty(tvEducation.getText()) && TextUtils.isEmpty(tvBirthday.getText().toString())){
            ToastUtil.show(this,"请补全信息~");
            return;
        }

        if (!TextUtils.isEmpty(extra)){
            setResult(LoginActivity.RESULT_CODE);
        }
        finish();
    }
}
