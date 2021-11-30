//package edu.cnm.deepdive.vaccpocketkeeper.controller;
//
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.camera.core.Camera;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.ImageCaptureException;
//import androidx.camera.core.Preview;
//import androidx.camera.extensions.HdrImageCaptureExtender;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.Navigation;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.common.util.concurrent.ListenableFuture;
//import edu.cnm.deepdive.vaccpocketkeeper.R;
//import edu.cnm.deepdive.vaccpocketkeeper.adapter.VaccineAdapter;
//import edu.cnm.deepdive.vaccpocketkeeper.databinding.ActivityCameraBinding;
//import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentCameraBinding;
//import edu.cnm.deepdive.vaccpocketkeeper.databinding.FragmentVaccineBinding;
//import edu.cnm.deepdive.vaccpocketkeeper.viewmodel.VaccineViewModel;
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class CameraFragment extends Fragment {
//
//  private FragmentCameraBinding binding;
//  private Executor executor = Executors.newSingleThreadExecutor();
//  private int REQUEST_CODE_PERMISSIONS = 1001;
//  private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA",
//      "android.permission.WRITE_EXTERNAL_STORAGE"};
//
//  PreviewView mPreviewView;
//  ImageView captureImage;
//
//  @Override
//  public void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setHasOptionsMenu(true);
//
//
//  }
//
//  public View onCreateView(@NonNull LayoutInflater inflater,
//      ViewGroup container, Bundle savedInstanceState) {
//    binding = FragmentCameraBinding.inflate(inflater, container, false);
//
//    mPreviewView = binding.camera;
//    captureImage = binding.captureImg;
//
//    if (allPermissionsGranted()) {
//      startCamera(); //start camera if permission has been granted by user
//    } else {
//      ActivityCompat.requestPermissions(getContext(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//    }
//
//    return binding.getRoot();
//  }
//
//  @Override
//  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//    super.onViewCreated(view, savedInstanceState);
//  } //when fragment dies, then cleans up
//
//  @Override
//  public void onDestroyView() {
//    super.onDestroyView();
//    binding = null;
//  }
//
//  void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//
//    Preview preview = new Preview.Builder()
//        .build();
//
//    CameraSelector cameraSelector = new CameraSelector.Builder()
//        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//        .build();
//
//    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//        .build();
//
//    ImageCapture.Builder builder = new ImageCapture.Builder();
//
//    //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
//    HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
//
//    // Query if extension is available (optional).
//    if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//      // Enable the extension if available.
//      hdrImageCaptureExtender.enableExtension(cameraSelector);
//    }
//
//    final ImageCapture imageCapture = builder
//        .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
//        .build();
//    preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
//    Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview,
//        imageAnalysis, imageCapture);
//
//    captureImage.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
//        File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");
//
//        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
//            file).build();
//        imageCapture.takePicture(outputFileOptions, executor,
//            new ImageCapture.OnImageSavedCallback() {
//              @Override
//              public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                new Handler().post(new Runnable() {
//                  @Override
//                  public void run() {
//                    Toast.makeText(CameraActivity.this, "Image Saved successfully",
//                        Toast.LENGTH_SHORT).show();
//                  }
//                });
//              }
//
//              @Override
//              public void onError(@NonNull ImageCaptureException error) {
//                error.printStackTrace();
//              }
//            });
//      }
//    });
//  }
//
//  private void startCamera() {
//
//    final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(
//        this);
//
//    cameraProviderFuture.addListener(new Runnable() {
//      @Override
//      public void run() {
//        try {
//
//          ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//          bindPreview(cameraProvider);
//
//        } catch (ExecutionException | InterruptedException e) {
//          // No errors need to be handled for this Future.
//          // This should never be reached.
//        }
//      }
//    }, ContextCompat.getMainExecutor(this));
//  }
//
//  public String getBatchDirectoryName() {
//
//    String app_folder_path = "";
//    app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
//    File dir = new File(app_folder_path);
//    if (!dir.exists() && !dir.mkdirs()) {
//
//    }
//
//    return app_folder_path;
//  }
//
//  private boolean allPermissionsGranted() {
//
//    for (String permission : REQUIRED_PERMISSIONS) {
//      if (ContextCompat.checkSelfPermission(this, permission)
//          != PackageManager.PERMISSION_GRANTED) {
//        return false;
//      }
//    }
//    return true;
//  }
//
//  @Override
//  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//      @NonNull int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    if (requestCode == REQUEST_CODE_PERMISSIONS) {
//      if (allPermissionsGranted()) {
//        startCamera();
//      } else {
//        Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//        this.finish();
//      }
//    }
//  }
//
//  private void displayError(Throwable throwable) {
//    if (throwable != null) {
//      Snackbar snackbar = Snackbar.make(binding.getRoot(),
//          getString(R.string.error_message, throwable.getMessage()),
//          Snackbar.LENGTH_INDEFINITE);
//      snackbar.setAction(R.string.error_dismiss,
//          (v) -> snackbar.dismiss());//gets access to livedata from viewmodel - just observing, run contents of bucket once this object changes.
//      snackbar.show();
//    }
//  }
//
//  public void editVaccine(long id, View view) {
//    Navigation.findNavController(binding.getRoot())
//        .navigate(CameraFragmentDirections.openSettings());
//  }
//
////  public void showDoses(long id, View view) {
////    VaccineFragmentDirections.OpenDoses toDoses
////        = VaccineFragmentDirections.openDoses();
////    toDoses.setVaccineId(id);
////    Navigation.findNavController(view).navigate(toDoses);
//  //}
//}