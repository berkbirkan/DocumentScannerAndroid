package com.berkbirkan.documentscannerml;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import com.google.firebase.ml.vision.text.*;
import com.google.firebase.ml.vision.*;
import com.google.android.gms.tasks.*;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.net.URL;

import static android.provider.MediaStore.*;

public class MainActivity extends AppCompatActivity {
    ImageView resim1;
    EditText result1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resim1=findViewById(R.id.resim);
        result1=findViewById(R.id.result);
        //result1.setText("There is no image to scan.");
        //Uri uri =
        //resim1.setImageBitmap(uri);


    }

    protected void pickimage(View view){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }else{
                Intent intent = new Intent(Intent.ACTION_PICK,Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }


    }

    protected  void takephoto(View view){
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent2,3);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 3 && resultCode==RESULT_OK && data!= null){
            super.onActivityResult(requestCode,resultCode,data);
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            resim1.setImageBitmap(bitmap);

            FirebaseVisionImage mlimage = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(mlimage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    // Task completed successfully
                                    // ...
                                    result1.setText(firebaseVisionText.getText());
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            //result1.setText("I can't read it :(");
                                        }
                                    });

        }

        if(requestCode ==2 && resultCode==RESULT_OK && data != null){
            Uri image = data.getData();


            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                resim1.setImageBitmap(bitmap);

                FirebaseVisionImage mlimage = FirebaseVisionImage.fromBitmap(bitmap);
                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();
                Task<FirebaseVisionText> result =
                        detector.processImage(mlimage)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        // Task completed successfully
                                        // ...
                                        result1.setText(firebaseVisionText.getText());
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                //result1.setText("I can't read it :(");
                                            }
                                        });



            }catch (IOException e){
                e.printStackTrace();
                //result1.setText("I can't read it :(");
            }



            
            

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
