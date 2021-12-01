package edu.cnm.deepdive.vaccpocketkeeper.controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.Factory;
import androidx.lifecycle.ViewModelStore;
import com.google.common.util.concurrent.ListenableFuture;
import edu.cnm.deepdive.vaccpocketkeeper.databinding.ActivityCameraBinding;
import edu.cnm.deepdive.vaccpocketkeeper.model.entity.Dose;
import edu.cnm.deepdive.vaccpocketkeeper.model.pojo.DoseWithDoctor;
import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.DoseViewModel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Implements methods that enable a user to take a photgraph from their phone,
 * store the photograph in local storage and associate the picture with a particular {@link Dose}
 * in the database.  Interacts with the {@link DoseViewModel} to store the photo file location in the
 * database. Uses the visual layout for the Take Picture screen as specified by the activity_camera
 * layout in res/layout.  Requests user permission to access the camera and to read and write to external storage.
 */
public class CameraActivity extends AppCompatActivity {

  private Executor executor = Executors.newSingleThreadExecutor();
  private final int REQUEST_CODE_PERMISSIONS = 1001;
  private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
  private ActivityCameraBinding binding;
  private DoseViewModel doseViewModel;
  private ViewModelStore viewModelStore = null;
  private ArrayAdapter<String> adapter;
  private Dose dose;
  private List<DoseWithDoctor> doses;
  private ArrayList<String> doseArrayList;
  private int itemSelected;
  private Context context;
  private String savedImageFilePath;

  /**
   * Overrides the onCreate method in AppCompatActivity.  Instantiates local variables.
   * Specifically, uses the visual layout for the Take Picture screen as specified by the
   * activity_camera layout in res/layout. Interacts with the {@link DoseViewModel} to get
   * a dropdown list of all of the doses in the database.  Requests user permission to access
   * the camera and to read and write to external storage.
   * @param savedInstanceState a {@link Bundle}.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityCameraBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    context = this.getApplicationContext();
    dose = null;
    binding.doseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String doseName = (String) parent.getItemAtPosition(position);
        itemSelected = position;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        CameraActivity.this.dose = null;
      }
    });


    doseViewModel = getViewModelProvider().get(DoseViewModel.class);
    doseViewModel
        .getDoses()
        .observe(this, (doses) -> {
          CameraActivity.this.doses = doses;
          doseArrayList = convertArrayListofObjectsToStringArray(doses);
          if (doses != null) {
            adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item,
                doseArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.doseSpinner.setAdapter(adapter);
          }
        });

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

  private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

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

    preview.setSurfaceProvider(binding.previewView.createSurfaceProvider());

    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

    binding.captureImg.setOnClickListener(v -> {

      SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
      File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");

      ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
      imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
        @Override
        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
          new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(CameraActivity.this, "Image Saved successfully!", Toast.LENGTH_LONG).show();

              savedImageFilePath = file.getAbsolutePath();
              //saves filename to a particular dose's iamge field
              saveDose(savedImageFilePath);

//              Bitmap takenImage = BitmapFactory.decodeFile(savedImageFilePath);
//              // Load the taken image into a preview
//              ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
//              ivPreview.setImageBitmap(takenImage);

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

  private void saveDose(String savedImageFilePath) {
    doseViewModel
        .getDoseById(doses.get(itemSelected).getId())
        .observe(this, (dose) -> {
            dose.setImage(savedImageFilePath);
            doseViewModel.save(dose);
          });
  }

  private Bitmap loadFromUri(Uri photoUri) {
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

  private String getBatchDirectoryName() {

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

  /**
   * Instantiates a {@link ViewModelStore} object
   * @return a {@link ViewModelStore} object.
   */
  public ViewModelStore getViewModelStore() {
    Object nonConfigurationInstance = getLastNonConfigurationInstance();
    if (nonConfigurationInstance instanceof ViewModelStore) {
      viewModelStore = (ViewModelStore) nonConfigurationInstance;
    }
    if (viewModelStore == null) {
      viewModelStore = new ViewModelStore();
    }
    return viewModelStore;
  }

  private ViewModelProvider getViewModelProvider() {
    ViewModelProvider.Factory factory =
        (Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
    return new ViewModelProvider(getViewModelStore(), factory);
  }

  private ArrayList<String> convertArrayListofObjectsToStringArray(List<DoseWithDoctor> arrayList) {
    ArrayList<String> str = new ArrayList<>();

    for (int i = 0; i < arrayList.size(); i++) {
      str.add(arrayList.get(i).getName());
    }
    return str;
  }
}