package com.hxgd.collection.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hxgd.collection.R;

import java.io.IOException;


public class VideoPlayActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    MediaPlayer mMediaPlayer;
    SurfaceView mSurface;
    private int mPosition = 0;
    String filePath = "";
    private ImageView ivBack;

    public static void start(Context context, String filePath) {
        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.setClass(context, VideoPlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        filePath = getIntent().getStringExtra("filePath");
        mSurface = (SurfaceView) findViewById(R.id.surface);
        //mSurface.getHolder().setFixedSize(不要设置宽高，会自动拉伸播放,但不是我们所要的效果);
        mSurface.getHolder().addCallback(this);
        mSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//为了和低版本兼容添加上了这一句
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }
        });
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音乐流的类型
            mMediaPlayer.setDataSource(filePath);//设置路径,这里选用的是一个本地文件，Android支持http协议和rtsp协议。也可以填写这两个协议的链接。
            mMediaPlayer.prepare();//缓冲
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    private void play() {
//        try {
//            mMediaPlayer.reset();//重置为初始状态
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音乐流的类型
//            mMediaPlayer.setDataSource(filePath);//设置路径,这里选用的是一个本地文件，Android支持http协议和rtsp协议。也可以填写这两个协议的链接。
//            mMediaPlayer.prepare();//缓冲
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.play_start:
//                play();
//                break;
//
//            case R.id.play_pause:
//                if (mMediaPlayer.isPlaying()) {
//                    mMediaPlayer.pause();
//                } else {
//                    mMediaPlayer.start();
//                }
//                break;
//
//            case R.id.play_stop:
//                if (mMediaPlayer.isPlaying()) {
//                    mMediaPlayer.stop();
//                }
//                break;
//
//            case R.id.test:
//                //startActivity(new Intent(this, TestActivity.class));
//                break;
//        }
//    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(mSurface.getHolder());//设置video影片以surfaceviewholder播放
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mMediaPlayer.isPlaying()) {
            mPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.stop();
        }
    }
}