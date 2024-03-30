package com.example.baivolley.controllers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.baivolley.R;
import com.example.baivolley.api.Const;
import com.example.baivolley.api.RealPathUtil;
import com.example.baivolley.api.ServiceAPI;
import com.example.baivolley.model.Message1;
import com.example.baivolley.model.User;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 11;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 12;
    public static Uri imageUri;
    TextView id, userName, userEmail, gender;
    Button btnLogout;
    ImageView imageViewpprofile;
    ImageView imgCam, imgGallery;
    ImageButton imgEditAvatar;
    Dialog dialog;
    int id1 = 0;
    private Intent cameraIntent, galleryIntent;
    private ActivityResultLauncher<Intent> cameraLauncher, galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get image from camera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras())
                                                           .get("data");
                            imageViewpprofile.setImageBitmap(photo);
                            dialog.dismiss();
//                            new UploadImage().uploadFile();
                            UploadImage1();
                        }
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            Toast.makeText(this, "Don't choose image", Toast.LENGTH_SHORT)
                                 .show();
                            return;
                        }
                        imageUri = data.getData();
                        imageViewpprofile.setImageURI(imageUri);
                        dialog.dismiss();
//                        new UploadImage().uploadFile();
                        UploadImage1();
                    }
                }
        );

        if (SharedPrefManager.getInstance(this)
                             .isLoggedIn()) {
            setUpUI();

            imgEditAvatar.setOnClickListener(v -> showCustomDialog());
        } else {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }

    }

    public void setUpUI() {
        id = findViewById(R.id.tv_id);
        userName = findViewById(R.id.tv_username);
        userEmail = findViewById(R.id.tv_email);
        gender = findViewById(R.id.tv_gender);
        btnLogout = findViewById(R.id.button_logout);
        imageViewpprofile = findViewById(R.id.img_avt);
        imgEditAvatar = findViewById(R.id.image_edit_ava);

        User user = SharedPrefManager.getInstance(this)
                                     .getUser();

        id.setText(String.valueOf(user.getId()));
        userEmail.setText(user.getEmail());
        gender.setText(user.getGender());
        userName.setText(user.getName());

        Glide.with(getApplicationContext())
             .load(user.getImages())
             .into(imageViewpprofile);

        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLogout)) {
            SharedPrefManager.getInstance(this)
                             .logout();
        }
    }

    private void showCustomDialog() {
        // Create dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        // Find components in the dialog layout
        imgCam = dialog.findViewById(R.id.image_camera);
        imgGallery = dialog.findViewById(R.id.image_folder);
        Button buttonCancel = dialog.findViewById(R.id.button_cancel);

        imgCam.setOnClickListener(v -> dispatchTakePhoto());

        imgGallery.setOnClickListener(v -> dispatchTakePhotoGallery());
        // Set click listener for the Cancel button
        buttonCancel.setOnClickListener(v -> {
            // Dismiss the dialog when Cancel button is clicked
            dialog.dismiss();
        });

        // Show dialog
        dialog.show();

    }

    private void dispatchTakePhotoGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST_CODE
            );
        } else {
            // Permission already granted, launch gallery intent
            launchGallery();
        }
    }

    private void dispatchTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        } else {
            // Permission already granted, launch camera intent
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch camera intent
                launchCamera();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT)
                     .show();
            }
        }

        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch gallery intent
                launchGallery();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT)
                     .show();
            }
        }
    }

    private void launchGallery() {
        galleryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        galleryLauncher.launch(galleryIntent);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void launchCamera() {
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        }
    }

    public void UploadImage1() {
        if (SharedPrefManager.getInstance(this)
                             .isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this)
                                         .getUser();
            id1 = user.getId();
        }
//        mProgressDialog.show();
//khai báo biến và setText nếu có
        String username = userName.getText()
                                  .toString()
                                  .trim();
        RequestBody requestUsername = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                String.valueOf(id1)
        );
// create RequestBody instance from file
        String IMAGE_PATH = RealPathUtil.getRealPath(this, imageUri);
        Log.e("ffff", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
// MultipartBody. Part is used to send also the actual file name
        MultipartBody.Part partbodyavatar =
                MultipartBody.Part.createFormData(Const.MY_IMAGES, file.getName(), requestFile);
//gọi Retrofit
        ServiceAPI.servieapi.upload2(requestUsername, partbodyavatar)
                            .enqueue(new Callback<Message1>() {
                                @Override
                                public void onResponse(
                                        Call<Message1> call, Response<Message1> response
                                ) {
                                    Message1 message1 = response.body();
                                    if (message1.isSuccess()) {
                                        Toast.makeText(
                                                     ProfileActivity.this,
                                                     "Upload success",
                                                     Toast.LENGTH_SHORT
                                             )
                                             .show();
                                        Log.e("Upload", "success");
                                        SharedPrefManager.getInstance(getApplicationContext())
                                                         .logout();
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                     ProfileActivity.this,
                                                     "Upload failed",
                                                     Toast.LENGTH_SHORT
                                             )
                                             .show();
                                        Log.e("Upload", "failed1");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Message1> call, Throwable t) {
                                    Toast.makeText(
                                                 ProfileActivity.this,
                                                 "Upload failed",
                                                 Toast.LENGTH_SHORT
                                         )
                                         .show();
                                    Log.e("Upload", "failed");
                                }
                            });
    }
}