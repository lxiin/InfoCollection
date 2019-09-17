package com.hxgd.collection.net;

import com.hxgd.collection.bean.BaseEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://223.247.203.213:9055/";


    @Multipart
    @POST("language/upload")
    Observable<BaseEntity> uploadRecordFile(@Part List<MultipartBody.Part> parts);

    @POST("language/login")
    Observable<BaseEntity> login(@Query("code") String code, @Query("phone") String phone);

}
