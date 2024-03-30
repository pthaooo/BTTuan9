package com.example.bairetrofit.model;

import android.util.Log;
import android.widget.Toast;

import com.example.bairetrofit.MainActivity;
import com.example.bairetrofit.api.APIService;
import com.example.bairetrofit.api.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSubCategories {
    private APIService apiService;
    private int idCategory;
    public static List<SubCategory> subCategories = new ArrayList<>();
    public AllSubCategories(int idCategory) {
        this.idCategory = idCategory;
    }

    public void getAllSubCategories() {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("idcategory", String.valueOf(idCategory))
//                .build();

        apiService = RetrofitClient.getRetrofit()
                .create(APIService.class);

        apiService.getSubCategory(idCategory)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(
                            Call<ResponseBody> call, Response<ResponseBody> response
                    ) {
                        if (response.isSuccessful()) {
                            try {
                                String json = response.body()
                                        .string();

                                Log.e("Response", "onResponse1: " + json + response.body()
                                        .string());

                                parseJson(json);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("Error", "onResponse2: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

    }
    public void parseJson(String json) {
        Type founderListType = new TypeToken<ArrayList<SubCategory>>() {}.getType();
        Gson gson = new Gson();
        subCategories = gson.fromJson(json, founderListType);

        for (SubCategory subCategory : subCategories) {
            subCategory.getId();
        }
    }

}
