package com.example.bairetrofit;

import static com.example.bairetrofit.model.CategoryAdapter.ans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bairetrofit.api.APIService;
import com.example.bairetrofit.api.RetrofitClient;
import com.example.bairetrofit.model.Category;
import com.example.bairetrofit.model.CategoryAdapter;
import com.example.bairetrofit.model.SubCategory;
import com.example.bairetrofit.model.SubCategoryActivity;
import com.example.bairetrofit.model.SubCategoryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcCate;
    CategoryAdapter categoryAdapter;
    APIService apiService;
    List<Category> categoryList;
    List<SubCategory> subCategoryList;
    SubCategoryAdapter subCategoryAdapter;

    public static int ok = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();
        getCategory();

//        getAllSubCategories(1);

    }

    private void getCategory() {
        apiService = RetrofitClient.getRetrofit()
                                   .create(APIService.class);
        apiService.getCategoryAll()
                  .enqueue(new Callback<List<Category>>() {
                      @SuppressLint("NotifyDataSetChanged")
                      @Override
                      public void onResponse(
                              @NonNull Call<List<Category>> call,
                              @NonNull Response<List<Category>> response
                      ) {
                          if (response.isSuccessful()) {
                              categoryList = response.body();
                              categoryAdapter = new CategoryAdapter(
                                      MainActivity.this,
                                      categoryList
                              );
                              rcCate.setHasFixedSize(true);

                              RecyclerView.LayoutManager layoutManager =
                                      new LinearLayoutManager(
                                              getApplicationContext(),
                                              LinearLayoutManager.VERTICAL,
                                              false
                                      );

                              rcCate.setLayoutManager(layoutManager);
                              rcCate.setAdapter(categoryAdapter);

                              categoryAdapter.notifyDataSetChanged();

                          } else {
                              int statusCode = response.code();
                              Log.e("Error", "onResponse: " + statusCode);
                          }
                      }

                      @Override
                      public void onFailure(
                              @NonNull Call<List<Category>> call,
                              @NonNull Throwable t
                      ) {
                          Log.e("Error", "onFailure: " + t.getMessage());
                      }
                  });
    }

//    public void getAllSubCategories(int idCategory) {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("idcategory", String.valueOf(idCategory))
//                .build();
//
//        apiService = RetrofitClient.getRetrofit()
//                                   .create(APIService.class);
//
//        apiService.getSubCategory(idCategory)
//                  .enqueue(new Callback<ResponseBody>() {
//                      @Override
//                      public void onResponse(
//                              Call<ResponseBody> call, Response<ResponseBody> response
//                      ) {
//                          if (response.isSuccessful()) {
//                              try {
//                                  String json = response.body()
//                                                        .string();
//                                  Log.e("Response", "onResponse: " + response.body()
//                                                                             .string());
//
//
////                                  parseJson(json);
//                              } catch (Exception e) {
//                                  e.printStackTrace();
//                              }
//                          } else {
//                              Log.e("Error", "onResponse: " + response.code());
//                          }
//                      }
//
//                      @Override
//                      public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                      }
//                  });
//    }

    private void setUpUI() {
        rcCate = findViewById(R.id.rc_category);
    }


    public void setSubCategoryAdapter(){
        subCategoryAdapter = new SubCategoryAdapter(this, subCategoryList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcCate.setLayoutManager(gridLayoutManager);
        rcCate.setAdapter(subCategoryAdapter);
    }

}