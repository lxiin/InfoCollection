package com.hxgd.collection.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;
import com.hxgd.collection.event.RecordEvent;
import com.hxgd.collection.utils.Constant;
import com.hxgd.collection.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;
    @BindView(R.id.btnStartStop)
    Button mBtnStartStop;
    @BindView(R.id.btnPlayVideo)
    Button mBtnPlay;
    @BindView(R.id.text)
    TextView textView;


    private boolean mStartedFlg = false; //是否正在录音
    private boolean mIsPlay = false; //是否正在播放录像
    private MediaRecorder mRecoder;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private MediaPlayer mediaPlayer;
    private String filePath;
    private int text = 0;
    private String fileName;
    private long mStartingTimeMillis = 0;



    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            text++;
            textView.setText(String.valueOf(text));
            handler.postDelayed(this,1000);
        }
    };


    public static void start(Context context) {
        Intent starter = new Intent(context, RecordVideoActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        ButterKnife.bind(this);



        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @OnClick(R.id.btnStartStop)
    public void onBtnStartStopClick(){
        if (mIsPlay){
            stopPlay();
        }
        if (!mStartedFlg){
            startRecordVideo();
        }else{
            if (mStartedFlg){
                stopRecordVideo();
            }
        }
    }

    private void stopRecordVideo() {
        try {
            mStartedFlg = false;
            handler.removeCallbacks(runnable);
            mRecoder.stop();
            mRecoder.reset();
            mRecoder.release();
            mRecoder = null;
            mBtnStartStop.setText("开始");
            if (camera != null){
                camera.release();
                camera = null;
            }


        } catch (Exception e) {
            Log.e("录视频关闭失败---->",e.getMessage());
        }

//        try {
//            mDatabase.addRecording(fileName, filePath,System.currentTimeMillis() - mStartingTimeMillis,2);
//        } catch (Exception e) {
//            Log.e("录视频关闭存数据库失败---->",e.getMessage());
//        }
        RecordEvent event = new RecordEvent(fileName,filePath,System.currentTimeMillis()-mStartingTimeMillis,2);
        EventBus.getDefault().post(event);
        finish();

    }

    private void startRecordVideo() {
        handler.postDelayed(runnable,1000);
         if (mRecoder == null){
            mRecoder = new MediaRecorder();
        }

        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        if (camera != null){
            camera.setDisplayOrientation(90);
            camera.unlock();
            mRecoder.setCamera(camera);
        }

        try {
            mRecoder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mRecoder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecoder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecoder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            mRecoder.setVideoSize(640,480);
            mRecoder.setVideoFrameRate(30);
            mRecoder.setVideoEncodingBitRate(3 * 1024 * 1024);
            mRecoder.setOrientationHint(90);
            mRecoder.setMaxDuration(30 * 1000);
            mRecoder.setPreviewDisplay(mSurfaceHolder.getSurface());

            filePath = getSDPath();
            if (filePath != null){
                File dir = new File(filePath + Constant.VIDEO_DIR);
                if (!dir.exists()){
                    dir.mkdirs();
                }
                fileName = "Video_"+System.currentTimeMillis()+".mp4";
                filePath = dir + "/"+ fileName;
                Log.e("sdasad---->","视频保存的路径--->"+ filePath);
                mRecoder.setOutputFile(filePath);
                mRecoder.prepare();
                mRecoder.start();

                mStartingTimeMillis = System.currentTimeMillis();


                mStartedFlg = true;
                mBtnStartStop.setText("结束录制");
            }
        }catch (Exception e){
            Log.e("录视频初始化失败---->",e.getMessage());
        }
    }

    private void stopPlay() {
        if (mediaPlayer != null){
            mIsPlay = false;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    @OnClick(R.id.btnPlayVideo)
    public void onBtnPlayVideoClick(){
        mIsPlay = true;
         if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        Uri uri = Uri.parse(filePath);
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDisplay(mSurfaceHolder);
        try{
            mediaPlayer.prepare();
        }catch (Exception e){
         }
        mediaPlayer.start();

    }


    private String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "Video_" + year + (month + 1) + day + hour + minute + second;
        return date;
    }


    private String getSDPath() {
        return  Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;

        if (camera == null) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mRecoder == null) {
            mRecoder = new MediaRecorder();
        }
        if (camera != null) {
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                Camera.Parameters parameters = camera.getParameters();
                //实现Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                camera.setParameters(parameters);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceView = null;
        mSurfaceHolder = null;
        handler.removeCallbacks(runnable);
        if (mRecoder != null){
            mRecoder.release();
            mRecoder =null;
        }
        if (camera != null) {
            camera.release();
            camera = null;
        }
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStartedFlg){
            ToastUtil.show(this,"请先停止录制 再退出");
        }
    }
}
