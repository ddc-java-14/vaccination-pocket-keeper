package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import edu.cnm.deepdive.vaccpocketkeeper.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

  private Executor executor = Executors.newSingleThreadExecutor();
  private final int REQUEST_CODE_PERMISSIONS = 1001;
  private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

  PreviewView mPreviewView;
  ImageView captureImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);

    mPreviewView = findViewById(R.id.previewView);
    captureImage = findViewById(R.id.captureImg);

    if(allPermissionsGranted()){
      startCamera(); //start camera if permission has been granted by user
    } else{
      ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }
  }

  private void startCamera() {

    final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

    cameraProviderFuture.addListener(new Runnable() {
      @Override
      public void run() {
        try {

          ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
          bindPreview(cameraProvider);

        } catch (ExecutionException | InterruptedException e) {
          // No errors need to be handled for this Future.
          // This should never be reached.
        }
      }
    }, ContextCompat.getMainExecutor(this));
  }

  void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

    Preview preview = new Preview.Builder()
        .build();

    CameraSelector cameraSelector = new CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build();

    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
        .build();

    ImageCapture.Builder builder = new ImageCapture.Builder();

    //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
    HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

    // Query if extension is available (optional).
    if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
      // Enable the extension if available.
      hdrImageCaptureExtender.enableExtension(cameraSelector);
    }

    final ImageCapture imageCapture = builder
        .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
        .build();

    preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

    captureImage.setOnClickListener(v -> {

      SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
      File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");

      ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
      imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
        @Override
        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
          new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(CameraActivity.this, "Image Saved successfully: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
              //Bitmap photo = (Bitmap) data.getExtras().get("data");
              //imageView.setImageBitmap(photo);
              //Uri photoUri = data.getData();

              //TODO: save image to a particular dose

              //TODO: come back here to add fragment or activity to retrieve file from database
              // Load the image located at photoUri into selectedImage
              //Bitmap selectedImage = loadFromUri(photoUri);

              // Load the selected image into a preview
              //ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
              //ivPreview.setImageBitmap(selectedImage);
              finish();
            }
          });
        }
        @Override
        public void onError(@NonNull ImageCaptureException error) {
          error.printStackTrace();
        }
      });
    });
  }

  public Bitmap loadFromUri(Uri photoUri) {
    Bitmap image = null;
    try {
      // check version of Android on device
      if(Build.VERSION.SDK_INT > 27){
        // on newer versions of Android, use the new decodeBitmap method
        ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
        image = ImageDecoder.decodeBitmap(source);
      } else {
        // support older versions of Android by using getBitmap
        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

  public String getBatchDirectoryName() {

    String app_folder_path = "";
    app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
    File dir = new File(app_folder_path);
    if (!dir.exists() && !dir.mkdirs()) {

    }

    return app_folder_path;
  }

  private boolean allPermissionsGranted(){

    for(String permission : REQUIRED_PERMISSIONS){
      if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
        return false;
      }
    }
    return true;
  }
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
super.onRequestPermissionsResult(requestCode,permissions, grantResults);
    if(requestCode == REQUEST_CODE_PERMISSIONS){
      if(allPermissionsGranted()){
        startCamera();
      } else{
        Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
        this.finish();
      }
    }
  }
}