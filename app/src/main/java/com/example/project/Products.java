package com.example.project;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;

import java.util.ArrayList;

public class Products extends AppCompatActivity {

    private ProductDB productDB;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ImageView searchIcon;
    private ImageView barcodeImageView;
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        productDB = new ProductDB(this);


        requestMicrophonePermission();


        recyclerView = findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        selectedCategory = intent.getStringExtra("category");
        if (selectedCategory == null) {
            selectedCategory = "";
        }


        searchEditText = findViewById(R.id.search_edit_text);
        searchIcon = findViewById(R.id.search_icon);


        searchIcon.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            loadProducts(selectedCategory, searchQuery);
        });


        barcodeImageView = findViewById(R.id.barcodeuserprofile);
        barcodeImageView.setOnClickListener(v -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        });



        ImageView micImageView = findViewById(R.id.micimgLogout);
        micImageView.setOnClickListener(v -> {
            Intent intentVoice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentVoice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intentVoice.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the product name...");
            startActivityForResult(intentVoice, 2);
        });

        loadProducts(selectedCategory, "");

        setupNavigation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Retrieve captured image for barcode scanning
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            processImageForText(photo);

        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Retrieve voice recognition result
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String voiceQuery = results.get(0); // First result is the most likely
                searchEditText.setText(voiceQuery);
                loadProducts(selectedCategory, voiceQuery);
            }
        }
    }

    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }


    private void loadProducts(String category, String searchQuery) {
        Cursor data;

        if (!category.isEmpty() && !searchQuery.isEmpty()) {
            data = productDB.getProductsByCategoryAndSearch(category, searchQuery);
        } else if (!category.isEmpty()) {
            data = productDB.getProductsByCategory(category);
        } else if (!searchQuery.isEmpty()) {
            data = productDB.searchProducts(searchQuery);
        } else {
            data = productDB.getAllProducts();
        }

        adapter = new ProductAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }


    private void processImageForText(Bitmap photo) {
        try {

            InputImage image = InputImage.fromBitmap(photo, 0);

            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(result -> {
                        String extractedText = result.getText();
                        searchEditText.setText(extractedText);
                        loadProducts(selectedCategory, extractedText);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Log.e("Text Recognition", "Failed to process image.");
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupNavigation() {
        findViewById(R.id.discoverybtn).setOnClickListener(v -> {
            startActivity(new Intent(Products.this, discover.class));
        });

        findViewById(R.id.cartbtn).setOnClickListener(v -> {
            startActivity(new Intent(Products.this, Cart.class));
        });

        findViewById(R.id.profilebtn).setOnClickListener(v -> {
            startActivity(new Intent(Products.this, User_profile.class));
        });

        findViewById(R.id.Categorieslayout).setOnClickListener(v -> {
            startActivity(new Intent(Products.this, discover.class));
        });
    }
}
