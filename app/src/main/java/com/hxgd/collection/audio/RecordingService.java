package com.hxgd.collection.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


import com.hxgd.collection.event.RecordEvent;
import com.hxgd.collection.utils.Constant;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

public class RecordingService extends Service {

    private static final String TAG = "RecordingService";

    private String mFileName;
    private String mFilePath;

    private MediaRecorder mRecorder;

    private long mStartingTimeMillis = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startRecording();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null){
            stopRecording();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void startRecording() {
        setFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //关于格式的设置   https://blog.csdn.net/qq_24349189/article/details/78573477
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //录音文件保存的格式，这里保存为 mp4
        mRecorder.setOutputFile(mFilePath); // 设置录音文件的保存路径
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        // 设置录音文件的清晰度
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

        } catch (IOException e) {
            Log.e("录音初始化失败-->", "prepare() failed---->"+e.getMessage());
        }


    }

    private void setFileNameAndPath() {
        File file;
        do {
            mFileName = "Audio_"+System.currentTimeMillis()+".mp4";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += Constant.AUDIO_DIR +mFileName;
            file = new File(mFilePath);
        }while (file.exists() && !file.isDirectory());
    }


    private void stopRecording() {

        Log.e("sadas----->","录音文件存放的位置---->"+mFilePath);

        try {
            mRecorder.stop();
        } catch (IllegalStateException e) {
            // TODO 如果当前java状态和jni里面的状态不一致，
            //e.printStackTrace();
            mRecorder = null;
            mRecorder = new MediaRecorder();
        }
        mRecorder.release();
        mRecorder = null;

        RecordEvent recordEvent = new RecordEvent(mFileName,mFilePath,System.currentTimeMillis() - mStartingTimeMillis,1);
        EventBus.getDefault().post(recordEvent);
        Logger.e("EventBus发送消息----->"+System.currentTimeMillis());
//
//        try {
//             mDatabase.addRecording(mFileName,mFilePath,System.currentTimeMillis() - mStartingTimeMillis,1);
//        } catch (Exception e) {
//            Log.e(TAG,"数据库插入数据失败---");
//        }

    }
}
