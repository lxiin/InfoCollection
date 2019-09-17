package com.hxgd.collection.net;

import android.content.Context;

import com.hxgd.collection.App;
import com.hxgd.collection.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetUtil {

    private static OkHttpClient okHttpClient;
    private static OkHttpClient createOkHttpClient(final Context context){
         final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
         if (BuildConfig.DEBUG){
             HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
             loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
             builder.addInterceptor(loggingInterceptor);
         }

        return builder.build();
    }

    public static OkHttpClient getOkHttpClient(){
        if (okHttpClient == null) {
            synchronized (NetUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = createOkHttpClient(App.getAppContext());
                }
            }
        }
        return okHttpClient;
    }

}
