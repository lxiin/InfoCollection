package com.hxgd.collection.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static Retrofit.Builder getRxRetrofitBuilder(){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(io.reactivex.schedulers.Schedulers.io()))
                .client(NetUtil.getOkHttpClient());
     }

    public static ApiService getApiService(){
        return getRxRetrofitBuilder()
                .baseUrl(ApiService.BASE_URL)
                .build()
                .create(ApiService.class);
    }

}
