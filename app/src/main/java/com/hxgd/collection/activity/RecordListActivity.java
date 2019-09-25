package com.hxgd.collection.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxgd.collection.R;
import com.hxgd.collection.audio.AudioAdapter;
import com.hxgd.collection.audio.PlaybackFragment;
import com.hxgd.collection.bean.BaseEntity;
import com.hxgd.collection.db.RecordDataBase;
import com.hxgd.collection.db.RecordEntity;
import com.hxgd.collection.net.ApiFactory;
import com.hxgd.collection.user.SettingActivity;
import com.hxgd.collection.user.UserInfo;
import com.hxgd.collection.user.UserInfoManager;
import com.hxgd.collection.utils.Constant;
import com.hxgd.collection.utils.SP;
import com.hxgd.collection.utils.ToastUtil;
import com.hxgd.collection.view.LoadingDialog;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 主页
 */
public class RecordListActivity extends AppCompatActivity {

    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_back_up)
    ImageView ivBackUP;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AudioAdapter adapter = new AudioAdapter();

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordListActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(View.inflate(this, R.layout.layout_empty, null));

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RecordEntity item = (RecordEntity) adapter.getData().get(position);
                File file = new File(item.getFilePath());
                if (!file.exists()) {
                    Toast.makeText(RecordListActivity.this, "文件找不到了", Toast.LENGTH_SHORT).show();
                } else {

                    switch (view.getId()){
                        case R.id.iv_del:
                            new AlertDialog.Builder(RecordListActivity.this)
                                    .setMessage("是否要删除这条记录")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delAudioItem((RecordEntity)adapter.getData().get(position));
                                }
                            }).show();
                            break;
                        case R.id.iv_upload:
                             uploadRecord(adapter, position);
                            break;
                        case R.id.ll_layout:
                        case R.id.iv_type:
                            if (item.getRecordType() == 1) {
                                PlaybackFragment.newInstance(item.getFileName(),item.getFilePath(),item.getRecordTime()).show(getSupportFragmentManager(), "play_audio");
                            } else {
                                VideoPlayActivity.start(RecordListActivity.this, item.getFilePath());
                            }
                            break;
                    }


                }
            }
        });
    }

    private void uploadRecord(BaseQuickAdapter adapter, int position) {
         RecordEntity item = (RecordEntity) adapter.getData().get(position);

        if (item.isHasSync()){
            Logger.e("上传过了 不要再传了");
            return;
        }

        File file = new File(item.getFilePath());
        if (!file.exists()) {
            ToastUtil.show(RecordListActivity.this, "文件找不到了~");
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        builder.addFormDataPart(file.getName(), file.getName(), requestBody);
        if (item.getRecordType() == 1){
            builder.addFormDataPart("audioName", file.getName());
            Logger.e("上传的是音频");
        }else{
            builder.addFormDataPart("videoName", file.getName());
            Logger.e("上传的是视频");
        }
        builder.addFormDataPart("creator", item.getUserName());
        builder.addFormDataPart("informant.birthday", item.getBirthday());
        builder.addFormDataPart("informant.education", item.getEducation());
        builder.addFormDataPart("informant.fatherLanguages", item.getFatherLaguages());
        builder.addFormDataPart("informant.fatherNation", item.getFatherNation());
        builder.addFormDataPart("informant.gender", item.getGender());
        builder.addFormDataPart("informant.maritalStatus", item.getMaritalStatus());
        builder.addFormDataPart("informant.motherLanguages", item.getMotherLaguages());
        builder.addFormDataPart("informant.motherNation", item.getMotherNation());
        builder.addFormDataPart("informant.spouseNation", item.getSpouseNation());
        builder.addFormDataPart("informant.userName", SP.get().getString(Constant.SP_USER_PHONE));
        builder.addFormDataPart("itemName", item.getItemName());
        builder.addFormDataPart("languageInfo.area", item.getArea());
        builder.addFormDataPart("languageInfo.dialect", item.getDialect());
        builder.addFormDataPart("languageInfo.localDialect", item.getLocalDialect());
        builder.addFormDataPart("languageInfo.name", item.getLaguageType());
        builder.addFormDataPart("languageInfo.population", String.valueOf(item.getPopulation()));
        builder.addFormDataPart("recordingDevice", Build.MANUFACTURER + " " + Build.MODEL);
        builder.addFormDataPart("recordingPlace", item.getPlace());
        builder.addFormDataPart("recordingTime",item.getTimeAdd());
        List<MultipartBody.Part> parts = builder.build().parts();

        LoadingDialog loadingDialog = new LoadingDialog(RecordListActivity.this, "正在上传文件...");
        loadingDialog.show();

        ApiFactory.getApiService().uploadRecordFile(parts)
                .flatMap(new Function<BaseEntity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(BaseEntity baseEntity) throws Exception {
                        if (baseEntity.isSuccessful()) {
                            RecordDataBase.getInstance(RecordListActivity.this).recordDao().makeSync(item.getId());
                            return Observable.just(Boolean.TRUE);
                        } else {
                            return Observable.just(Boolean.FALSE);
                        }
                     }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }


                    @Override
                    public void onNext(Boolean b) {
                        loadingDialog.dismiss();
                        if ( b == Boolean.TRUE) {
                            ToastUtil.show(RecordListActivity.this, "上传成功");
                            item.sync = 1;
                            adapter.notifyItemChanged(adapter.getData().indexOf(item));
                         } else {
                            ToastUtil.show(RecordListActivity.this, "上传失败");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtil.show(RecordListActivity.this, "上传失败");
                        Logger.e("上传失败--->" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    @OnClick(R.id.iv_add)
    public void onIvAddClick() {
        RecordAddActivity.start(this);
    }

    @OnClick(R.id.iv_setting)
    public void onIvSettingClick() {
        SettingActivity.start(this);
    }

    @OnClick(R.id.iv_back_up)
    public void onIvBackUpClick() {
        BackUpActivity.start(this);
    }


    @SuppressLint("StaticFieldLeak")
    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog(RecordListActivity.this);
        loadingDialog.show();


        Disposable disposable = Observable.create(new ObservableOnSubscribe<List<RecordEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RecordEntity>> emitter) throws Exception {
                List<RecordEntity> list = RecordDataBase.getInstance(RecordListActivity.this).recordDao().getAllRecord();
                emitter.onNext(list);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecordEntity>>() {
                    @Override
                    public void accept(List<RecordEntity> audioRecordItems) throws Exception {
                        loadingDialog.dismiss();
                        adapter.setNewData(audioRecordItems);
                    }
                });

        compositeDisposable.add(disposable);
    }


    @SuppressLint("StaticFieldLeak")
    public void delAudioItem(RecordEntity recordEntity) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                String filePath = recordEntity.getFilePath();
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                    RecordDataBase.getInstance(RecordListActivity.this).recordDao().deleteRecord(recordEntity.getId());
                }
                return Boolean.TRUE;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                ToastUtil.show(RecordListActivity.this, "删除成功");
                adapter.getData().remove(recordEntity);
                adapter.notifyDataSetChanged();
            }
        }.execute();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
