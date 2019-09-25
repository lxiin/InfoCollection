package com.hxgd.collection.db;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import com.hxgd.collection.R;
import com.orhanobut.logger.Logger;

import java.util.List;

public class TestRoom extends AppCompatActivity {

    EditText etId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_room);
        etId = findViewById(R.id.et_id);
    }

    public void add(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RecordEntity recordEntity = new RecordEntity("aaa","aa_fileName","内存卡", 456,"lx","男","1995-12-1",
                        "研究生","汉语","汉族","汉语","汉语",
                        "未婚","没有配偶","地区","方言","老家土话",
                        "土话",3200000,"河南焦作","",1,0);
                RecordDataBase.getInstance(TestRoom.this).recordDao().insert(recordEntity);
            }
        }).start();
    }

    public void queryAllClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecordEntity> list = RecordDataBase.getInstance(TestRoom.this).recordDao().getAllRecord();
                Logger.e("这个时候数据库里的数据是---->"+list.size());

                Logger.e(list.toString());
            }
        }).start();
    }

    public void delClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String id = etId.getText().toString();
                if (!TextUtils.isEmpty(id)){
                    RecordDataBase.getInstance(TestRoom.this).recordDao().deleteRecord(Integer.parseInt(id));

                }
            }
        }).start();
    }


    public void makeSyncClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String id = etId.getText().toString();
                if (!TextUtils.isEmpty(id)){
                    RecordDataBase.getInstance(TestRoom.this).recordDao().makeSync(Integer.parseInt(id));

                }
            }
        }).start();
    }

}
