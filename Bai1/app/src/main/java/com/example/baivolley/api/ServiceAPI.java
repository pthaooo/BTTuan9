package com.example.baivolley.api;

import com.example.baivolley.model.Message1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceAPI {
    String BASE_URL = "http://app.iotstar.vn/appfoods/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm: ss")
                                 .create();

    ServiceAPI servieapi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceAPI.class);

    @Multipart
    @POST("upload.php")
    Call<List<ImageUpload>> upload(
            @Part(Const.MY_USERNAME) RequestBody username,
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("updateimages.php")
    Call<Message1> upload2(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part images
    );

    @Multipart
    @POST("upload.php")
    Call<Message1> upload1(
            @Part(Const.MY_USERNAME) RequestBody username,
            @Part MultipartBody.Part avatar
    );

}
