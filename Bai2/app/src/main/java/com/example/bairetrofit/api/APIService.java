package com.example.bairetrofit.api;

import com.example.bairetrofit.model.Category;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @GET("categories.php")
    Call<List<Category>> getCategoryAll();

    @FormUrlEncoded
    @POST("getcategory.php")
    Call<ResponseBody> getSubCategory(
            @Field("idcategory") int idcategory
    );
}
