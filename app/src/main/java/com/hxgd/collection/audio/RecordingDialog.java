//package com.hxgd.collection.audio;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.SystemClock;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Chronometer;
//import android.widget.TextView;
//
//import com.hxgd.collection.R;
//import com.hxgd.collection.utils.Constant;
//
//import java.io.File;
//
//public class RecordingDialog extends Dialog {
//
//    private static final String TAG = "RecordingDialog";
//
//    Chronometer chronometer;
//    TextView tvClose;
//
//
//
//    public RecordingDialog(Context context) {
//        super(context, R.style.customDialog);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setCancelable(false);
//        setCanceledOnTouchOutside(false);
//
//        setContentView(R.layout.dialog_recording);
//
//
//        chronometer = findViewById(R.id.time_display);
//        tvClose =  findViewById(R.id.tv_close);
//
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        chronometer.setFormat("%S");
//        chronometer.start();
//
//        tvClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//    }
//    public void showDialog(){
//        show();
//
//        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
//        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//
//        startRecord();
//    }
//
//
//
//    @Override
//    public void dismiss() {
//        stopRecord();
//
//        super.dismiss();
//
//    }
//
//
//    /**
//     * 开始录音
//     */
//    private void startRecord() {
//        File folder = new File(Environment.getExternalStorageDirectory()+ Constant.AUDIO_DIR);
//        if (!folder.exists()){
//            folder.mkdirs();
//        }
//        getContext().startService(new Intent(getContext(), RecordingService.class));
//        //keep screen on while recording
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }
//
//    /**
//     * 停止录音
//     */
//    private void stopRecord() {
//        getContext().stopService(new Intent(getContext(), RecordingService.class));
//    }
//
//
//}
