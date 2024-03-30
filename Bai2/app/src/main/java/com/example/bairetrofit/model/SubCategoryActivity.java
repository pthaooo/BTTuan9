package com.example.bairetrofit.model;

import static com.example.bairetrofit.model.AllSubCategories.subCategories;
import static com.example.bairetrofit.model.CategoryAdapter.ans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.bairetrofit.MainActivity;
import com.example.bairetrofit.R;

import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView rcvCategory;
    SubCategoryAdapter subCategoryAdapter;
     public static List<SubCategory> subCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        AllSubCategories all = new AllSubCategories(ans.getId());
        all.getAllSubCategories();
        subCategoryAdapter = new SubCategoryAdapter(this, subCategories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvCategory.setLayoutManager(gridLayoutManager);
        rcvCategory.setAdapter(subCategoryAdapter);
    }
}