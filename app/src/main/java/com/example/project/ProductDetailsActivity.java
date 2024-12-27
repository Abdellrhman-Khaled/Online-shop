package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        SharedPreferences usersharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = usersharedPreferences.getString("username", "");


        SharedPreferences sharedPreferences = getSharedPreferences("ProductDetails", MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        String title = sharedPreferences.getString("title", "");
        String image = sharedPreferences.getString("image", "");
        String description = sharedPreferences.getString("description", "");
        float price = sharedPreferences.getFloat("price", 0.0f);

        TextView titleTextView = findViewById(R.id.details_product_title);
        TextView descriptionTextView = findViewById(R.id.details_product_description);
        TextView priceTextView = findViewById(R.id.details_product_price);
        ImageView imageView = findViewById(R.id.details_product_image);

        titleTextView.setText(title);
        priceTextView.setText(String.format("$%.2f", price));
        descriptionTextView.setText(description);
        Glide.with(this).load(image).into(imageView);

        findViewById(R.id.add_to_cart_button).setOnClickListener(v -> {
            SharedPreferences cartPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
            SharedPreferences.Editor editor = cartPreferences.edit();

            // Load the existing cart items from SharedPreferences
            String existingCart = cartPreferences.getString("cartItems", "[]");

            try {
                // Parse the existing cart items into a JSONArray
                JSONArray cartArray = new JSONArray(existingCart);

                boolean productFound = false;

                // Check if the product already exists in the cart
                for (int i = 0; i < cartArray.length(); i++) {
                    JSONObject item = cartArray.getJSONObject(i);
                    if (item.getString("name").equals(title)) {
                        // Product found, show a toast message
                        productFound = true;
                        Toast.makeText(ProductDetailsActivity.this, "Item already in cart", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                // If the product was not found, add it with an initial quantity of 1
                if (!productFound) {
                    JSONObject newItem = new JSONObject();
                    newItem.put("name", title);
                    newItem.put("price", price);
                    newItem.put("quantity", 1);  // Set the initial quantity to 1
                    cartArray.put(newItem);

                    // Update the cart items in SharedPreferences
                    editor.putString("cartItems", cartArray.toString());
                    editor.apply();

                    // Navigate to the Cart activity
                    startActivity(new Intent(ProductDetailsActivity.this, Cart.class));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        findViewById(R.id.submit_button).setOnClickListener(v -> {

            // Get user input
            FeedbackDB db = new FeedbackDB(this);
            ProductDB db2 = new ProductDB(this);

            RatingBar rating_bar = findViewById(R.id.rating_bar);
            EditText comment_input = findViewById(R.id.comment_input);

            float rating = rating_bar.getRating();
            String feedback = comment_input.getText().toString().trim();

            // Check if both fields are empty
            if (rating == 0 || feedback.isEmpty()) {
                Toast.makeText(this, "Please enter both feedback and rating.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean hasrated = db.hasUserRatedProduct(username , id);

            if (hasrated) {
                Toast.makeText(this, "You have already rated this product.", Toast.LENGTH_SHORT).show();
                db.close();
                return;
            }

            // Insert feedback if provided
            if (!feedback.isEmpty()) {
                boolean feedbackInserted = db.insertFeedback(username, id, feedback);
                if (!feedbackInserted) {
                    Toast.makeText(this, "Failed to send feedback.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (rating > 0) {
                Cursor productCursor = db2.getProductById(id);
                if (productCursor.moveToFirst()) {
                    float currentRate = productCursor.getFloat(productCursor.getColumnIndexOrThrow("rate"));
                    int rateCount = productCursor.getInt(productCursor.getColumnIndexOrThrow("rate_count"));
                    float newRate = ((currentRate * rateCount) + rating) / (rateCount + 1);
                    int newRateCount = rateCount + 1;

                    boolean ratingUpdated = db2.updateProductRating(id, newRate);
                    if (!ratingUpdated) {
                        Toast.makeText(this, "Failed to update rating.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                productCursor.close();
            }
            db.close();

            // Show success message
            Toast.makeText(this, "Your feedback has been sent.", Toast.LENGTH_SHORT).show();

            // Clear inputs
            rating_bar.setRating(0);
            comment_input.setText("");
        });


        findViewById(R.id.APhomebtn).setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, Products.class));
        });

        findViewById(R.id.APprofilebtn).setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, User_profile.class));
        });

        findViewById(R.id.APdiscoverybtn).setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, discover.class));
        });

        findViewById(R.id.APcartbtn).setOnClickListener(v -> {
            startActivity(new Intent(ProductDetailsActivity.this, Cart.class));
        });


    }
}
