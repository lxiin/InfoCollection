package com.hxgd.collection.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {
    /** 获取屏幕宽度 */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    public static String timeStampToTime(long timeStamp){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(timeStamp));   // 时间戳转换成时间
        return sd;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取当前的 年月日 时分秒
     * 格式yyyy-MM-dd HH:mm:ss
     * @param timeStamp
     * @return
     */
    public static String getCurrentTime(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(timeStamp));
    }

}

