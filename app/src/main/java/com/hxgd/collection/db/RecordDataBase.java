package com.hxgd.collection.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecordEntity.class} , version = 1)
public abstract class RecordDataBase extends RoomDatabase {

    public abstract RecordDao recordDao();

    private static volatile RecordDataBase INSTANCE;

    public static RecordDataBase getInstance(final Context context){
        if (INSTANCE == null){
            synchronized (RecordDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecordDataBase.class,"reocrd_database").build();
                }
            }
        }
        return INSTANCE;
    }


}
