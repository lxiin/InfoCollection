package com.hxgd.collection.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.hxgd.collection.audio.AudioRecordItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

 public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = "InfoCollection.db";

    private static final int DB_VERSION = 1;

    private static DBHelper dbHelper;


    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "InfoRecord";

        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name"; // 文件名字
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path"; //文件路径
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length"; //时长
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";  //添加时间
        public static final String COLUNM_NAME_RECORD_TYPE = "record_type";  //1表示音频  2表示视频
        public static final String COULUMN_NAME_HAS_SYNC = "sync";   // 0表示未同步  1表示同步
        public static final String COULUMN_NAME_ITEM_NAME = "item_name"; //这个记录的名字
        public static final String COULUMN_NAME_LAGUAGE_TYPE = "language_type"; //语言类别
        public static final String COULUMN_NAME_SPOKESMAN = "spokesman"; //发言人
        public static final String COULUMN_NAME_BIRTH = "birth"; //发言人的生日
        public static final String COULUMN_NAME_PLACE = "place"; //地区
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private final String CREATE_AUDIO_TABLE = "CREATE TABLE IF NOT EXISTS " + DBHelperItem.TABLE_NAME + " (" +
            DBHelperItem._ID + " INTEGER PRIMARY KEY "+ COMMA_SEP +
            DBHelperItem.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COULUMN_NAME_ITEM_NAME + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COULUMN_NAME_LAGUAGE_TYPE + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COULUMN_NAME_BIRTH + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COULUMN_NAME_PLACE + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COULUMN_NAME_SPOKESMAN + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_TIME_ADDED + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COLUNM_NAME_RECORD_TYPE + " INTEGER "+ COMMA_SEP+
            DBHelperItem.COULUMN_NAME_HAS_SYNC + " INTEGER"+ ")";


    public static DBHelper getInstance(Context context){
        if (dbHelper == null){
            synchronized (DBHelper.class){
                if (dbHelper == null){
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }


    public DBHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_AUDIO_TABLE);
//        Log.e("sdasd---------->","数据库创建成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 添加一条记录
     * @param recordingName
     * @param filePath
     * @param length
     * @param type
     * @return
     */
    public long addRecording(String recordingName, String filePath, long length,int type,String itemName,String language_type,
                             String spokesman,String birth,String place) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, timeStampToTime(System.currentTimeMillis()));
        cv.put(DBHelperItem.COLUNM_NAME_RECORD_TYPE,type);
        cv.put(DBHelperItem.COULUMN_NAME_ITEM_NAME,itemName);
        cv.put(DBHelperItem.COULUMN_NAME_LAGUAGE_TYPE,language_type);
        cv.put(DBHelperItem.COULUMN_NAME_BIRTH,birth);
        cv.put(DBHelperItem.COULUMN_NAME_SPOKESMAN,spokesman);
        cv.put(DBHelperItem.COULUMN_NAME_PLACE,place);
        cv.put(DBHelperItem.COULUMN_NAME_HAS_SYNC,0);
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);

        return rowId;
    }

    /**
     * 删除这条记录
     * @param recordName
     */
    public void delAudio(String recordName){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBHelperItem.TABLE_NAME,DBHelperItem.COLUMN_NAME_RECORDING_NAME + "= ?",new String[]{recordName});
     }


    public List<AudioRecordItem> getRecordList(){
        List<AudioRecordItem> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelperItem.TABLE_NAME,null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                AudioRecordItem audioRecordItem = new AudioRecordItem();
                audioRecordItem.setId(cursor.getInt(cursor.getColumnIndex(DBHelperItem._ID)));
                audioRecordItem.setName(cursor.getString(cursor.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_NAME)));
                audioRecordItem.setFilePath(cursor.getString(cursor.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH)));
                audioRecordItem.setCreateTime(cursor.getString(cursor.getColumnIndex(DBHelperItem.COLUMN_NAME_TIME_ADDED)));
                audioRecordItem.setLength(cursor.getInt(cursor.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH)));
                audioRecordItem.setType(cursor.getInt(cursor.getColumnIndex(DBHelperItem.COLUNM_NAME_RECORD_TYPE)));
                audioRecordItem.setHasSync(cursor.getInt(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_HAS_SYNC)) != 0);
                audioRecordItem.setItemName(cursor.getString(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_ITEM_NAME)));
                audioRecordItem.setPlace(cursor.getString(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_PLACE)));
                audioRecordItem.setSpokesman(cursor.getString(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_SPOKESMAN)));
                audioRecordItem.setBirth(cursor.getString(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_BIRTH)));
                audioRecordItem.setLanguageType(cursor.getString(cursor.getColumnIndex(DBHelperItem.COULUMN_NAME_LAGUAGE_TYPE)));
                list.add(audioRecordItem);
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 将这条记录更新为 已上传的状态
     * @param recordName
     */
    public void makeRecordSync(String recordName){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = DBHelperItem.COLUMN_NAME_RECORDING_NAME + "=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelperItem.COULUMN_NAME_HAS_SYNC,1);
        String[] whereArgs = {recordName};
        db.update(DBHelperItem.TABLE_NAME,contentValues,whereClause,whereArgs);
    }


    private String timeStampToTime(long timeStamp){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(timeStamp));   // 时间戳转换成时间
        return sd;
    }

}
