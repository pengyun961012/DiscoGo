package com.disco.skeletalproduct;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();


    // Create a child reference
    // imagesRef now points to "user_id"
    StorageReference userRef = storageRef.child("userid");

    // Child references can also take paths
    StorageReference songRef = userRef.child("lrz.wav");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //upload file
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("/sdcard/lrz.wav"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = songRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.e("firebase","upload success!");
            }
        });

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

}
