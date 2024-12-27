//package com.example.project;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//public class CameraFragment extends Fragment {
//    private static final int CAMERA_REQUEST = 1888;
//    private static final int CAMERA_PERMISSION_REQUEST = 100;
//    private ImageView myCapturedImage;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.productscamera, container, false);
//
//        myCapturedImage = rootView.findViewById(R.id.uselessimage);
//        Button captureButton = rootView.findViewById(R.id.barcodeuserprofile);
//
//        captureButton.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
//            } else {
//                openCamera();
//            }
//        });
//
//        return rootView;
//    }
//
//    private void openCamera() {
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
//            Bitmap myImage = (Bitmap) data.getExtras().get("data");
//            myCapturedImage.setImageBitmap(myImage);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            }
//        }
//    }
//}





//products


//        ImageView barcodeUserProfile = findViewById(R.id.barcodeuserprofile);
//        barcodeUserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create an instance of CameraFragment
//                CameraFragment cameraFragment = new CameraFragment();
//
//                // Find the fragment container
//                FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
//
//                // Begin a fragment transaction
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                // Replace the fragment container with the CameraFragment
//                transaction.replace(R.id.fragment_container, cameraFragment);
//
//                // Optional: Add to back stack so the user can navigate back
//                transaction.addToBackStack(null);
//
//                // Commit the transaction to apply the changes
//                transaction.commit();
//
//                // Make the fragment container visible
//                fragmentContainer.setVisibility(View.VISIBLE);
//            }
//        });
//

