package com.hxgd.collection.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(RecordEntity recordEntity);

    @Query("SELECT * FROM record_table ORDER BY id DESC")
    List<RecordEntity> getAllRecord();

    @Query("UPDATE record_table SET sync = 1 WHERE id =:id")
    void makeSync(int id);

    @Query("DELETE FROM record_table WHERE id=:id")
    void deleteRecord(int id);




}
