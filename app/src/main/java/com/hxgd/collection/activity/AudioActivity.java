package com.hxgd.collection.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hxgd.collection.db.DBHelper;
import com.hxgd.collection.R;
import com.hxgd.collection.audio.AudioAdapter;
import com.hxgd.collection.audio.AudioRecordItem;
import com.hxgd.collection.audio.PlaybackFragment;
import com.hxgd.collection.utils.ToastUtil;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Deprecated
public class AudioActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvBack;
    TextView tvAdd;


    private AudioAdapter adapter = new AudioAdapter();

    public static void start(Context context) {
        Intent starter = new Intent(context, AudioActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        initView();
        getData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getData() {
      new AsyncTask<Void, Void, List<AudioRecordItem>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<AudioRecordItem> doInBackground(Void... voids) {
                List<AudioRecordItem> list = DBHelper.getInstance(AudioActivity.this).getRecordList();
                Collections.sort(list, new Comparator<AudioRecordItem>() {
                    @Override
                    public int compare(AudioRecordItem item, AudioRecordItem t1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            return Integer.compare(t1.getId(),item.getId());
                        }else{
                            return 0;
                        }
                    }

                });
                return list;
            }

            @Override
            protected void onPostExecute(List<AudioRecordItem> audioRecordItems) {
                super.onPostExecute(audioRecordItems);
//                adapter.setNewData(audioRecordItems);

            }
        }.execute();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(View.inflate(this,R.layout.layout_empty,null));
        tvBack = findViewById(R.id.tv_back);
        tvAdd = findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordingActivity.start(AudioActivity.this);

            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AudioRecordItem item = (AudioRecordItem) adapter.getData().get(position);
                File file = new File(item.getFilePath());
                if (!file.exists()){
                    Toast.makeText(AudioActivity.this, "文件找不到了", Toast.LENGTH_SHORT).show();
                }else{
                    PlaybackFragment.newInstance(item.name,item.filePath,item.length).show(getSupportFragmentManager(),"play_audio");
                }
            }
        });

        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                new AlertDialog.Builder(AudioActivity.this)
                        .setMessage("是否要删除这个条目？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AudioRecordItem item = (AudioRecordItem) adapter.getData().get(position);
                                delAudioItem(item);
                            }
                        })
                        .show();
                return false;
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void delAudioItem(AudioRecordItem audioRecordItem){
         new AsyncTask<Void, Void, Boolean>() {

             @Override
             protected Boolean doInBackground(Void... voids) {
                 String filePath = audioRecordItem.getFilePath();
                 File file = new File(filePath);
                 if (file.exists()){
                     file.delete();
//                     DBHelper.getInstance(AudioActivity.this).delAudio(filePath);
                 }
                 return Boolean.TRUE;
             }

             @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                ToastUtil.show(AudioActivity.this,"删除成功");
                getData();
            }
        }.execute();
    }




}

