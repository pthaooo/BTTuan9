package com.example.baivolley.api;

import static com.example.baivolley.controllers.ProfileActivity.imageUri;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baivolley.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class UploadImage extends AppCompatActivity {
    double progress;
    String imageUrl;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
    }

    public void uploadFile() {
        // Upload file to Firebase Storage
        FirebaseApp.initializeApp(UploadImage.this);
        storageReference = FirebaseStorage.getInstance()
                                          .getReference();

        StorageReference ref = storageReference.child("images/" + UUID.randomUUID()
                                                                      .toString());

        ref.putFile(imageUri)
           .addOnSuccessListener(taskSnapshot -> {
               // Get the download URL
               ref.getDownloadUrl()
                  .addOnSuccessListener(uri -> {
                      // Save the download URL to the database
                      imageUrl = uri.toString();

                      // Save the download URL to the database
//                      SignupApi.upDateAvatar(imageUrl);
                  });
           })
           .addOnFailureListener(e -> {
               // Handle unsuccessful uploads
               Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
           })
           .addOnProgressListener(snapshot -> {
               // Observe state change events such as progress, pause, and resume
               progress =
                       (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
           });
    }

}