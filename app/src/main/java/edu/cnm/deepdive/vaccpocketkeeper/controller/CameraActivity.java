//package edu.cnm.deepdive.vaccpocketkeeper.controller;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import androidx.core.content.FileProvider;
//import edu.cnm.deepdive.vaccpocketkeeper.R;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class CameraActivity extends AppCompatActivity {
//
//  static final int REQUEST_IMAGE_CAPTURE = 1;
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    //TODO: set the binding here???
//  }
//
//  private void dispatchTakePictureIntent() {
//    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    try {
//      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//    } catch (ActivityNotFoundException e) {
//      // display error state to the user
//    }
//  }
//
//  String currentPhotoPath;
//
//  private File createImageFile() throws IOException {
//    // Create an image file name
//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//    String imageFileName = "JPEG_" + timeStamp + "_";
//    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//    File image = File.createTempFile(
//        imageFileName,  /* prefix */
//        ".jpg",         /* suffix */
//        storageDir      /* directory */
//    );
//
//    // Save a file: path for use with ACTION_VIEW intents
//    currentPhotoPath = image.getAbsolutePath();
//    return image;
//  }
//
//  private void dispatchTakePictureIntent() {
//    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    // Ensure that there's a camera activity to handle the intent
//    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//      // Create the File where the photo should go
//      File photoFile = null;
//      try {
//        photoFile = createImageFile();
//      } catch (IOException ex) {
//        // Error occurred while creating the File
//        Log.e(getClass().getSimpleName(), "There was an error while create the File!");
//      }
//      // Continue only if the File was successfully created
//      if (photoFile != null) {
//        Uri photoURI = FileProvider.getUriForFile(this,
//            "com.example.android.fileprovider",
//            photoFile);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//      }
//    }
//  }
//
//
//}