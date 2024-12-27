package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProduct extends AppCompatActivity {

    EditText id, title, cost, desc, cat, img, rating, number_of_products, rate_count, sold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        ProductDB database = new ProductDB(this);

        id = findViewById(R.id.edit_id);
        title = findViewById(R.id.edit_title);
        cost = findViewById(R.id.edit_price);
        desc = findViewById(R.id.edit_desc);
        cat = findViewById(R.id.edit_cat);
        img = findViewById(R.id.edit_image);
        rating = findViewById(R.id.edit_rate);
        number_of_products = findViewById(R.id.edit_count);
        rate_count = findViewById(R.id.edit_rate_count);
        sold = findViewById(R.id.edit_sold);

        int productId = getIntent().getIntExtra("product_id", -1);

        // Fetch product data from database
        Cursor cursor = database.getProductById(productId);
        if (cursor != null && cursor.moveToFirst()) {
            // Populate EditText fields with product data
            id.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
            title.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            cost.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow("price"))));
            desc.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            cat.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
            img.setText(cursor.getString(cursor.getColumnIndexOrThrow("image")));
            rating.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow("rate"))));
            number_of_products.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("count"))));
            rate_count.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("rate_count"))));
            sold.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("sold"))));
            cursor.close(); // Close the cursor
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if product not found
        }

        Button edit = (Button)findViewById(R.id.edit_product);

        edit.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int product_id = Integer.parseInt(id.getText().toString());
                String name = title.getText().toString().trim();
                double price = Double.parseDouble(cost.getText().toString().trim());
                String description = desc.getText().toString().trim();
                String category = cat.getText().toString().trim();
                String image= img.getText().toString().trim();
                float rate = Float.parseFloat(rating.getText().toString().trim());
                int count = Integer.parseInt(number_of_products.getText().toString().trim());
                int rateCount = Integer.parseInt(rate_count.getText().toString().trim());
                int sold_amount = Integer.parseInt(sold.getText().toString().trim());

                int new_count = count ;

//                if (database.is_exists(product_id)){
//                    Toast.makeText(EditProduct.this, "Product ID already exists", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!isValidID(String.valueOf(product_id))) {
//                    Toast.makeText(EditProduct.this, "Product ID must be an integer number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidTitle(name)) {
//                    Toast.makeText(EditProduct.this, "Title must contain only letters or numbers", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidCost(String.valueOf(price))) {
//                    Toast.makeText(EditProduct.this, "Price must be a number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidDescription(description)) {
//                    Toast.makeText(EditProduct.this, "Description must contain only letters or numbers", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidCategory(category)) {
//                    Toast.makeText(EditProduct.this, "Category must contain only letters or numbers", Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                if (!isValidImage(image)) {
//                    Toast.makeText(EditProduct.this, "Insert a valid image path", Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                if (!isValidCount(String.valueOf(count))) {
//                    Toast.makeText(EditProduct.this, "Number of products must be an integer number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidRating(String.valueOf(rate))) {
//                    Toast.makeText(EditProduct.this, "Rating must be a number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (!isValidRateCount(String.valueOf(rateCount))) {
//                    Toast.makeText(EditProduct.this, "Rate count must be an integer number", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                boolean isUpdated = database.updateProduct(product_id, name, price, description, category, image, rate, new_count, rateCount, sold_amount);

                if (isUpdated) {
                    Toast.makeText(EditProduct.this, "Product Updated Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProduct.this, "Invalid Data - Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public boolean isValidID(String id) {
        return id.matches("^\\d+$");
    }

    public boolean isValidTitle(String title) {
        return title.matches("^[a-zA-Z0-9\\s]+$");
    }

    public boolean isValidCost(String cost) {
        return cost.matches("^[+-]?\\d+(\\.\\d+)?$");
    }

    public boolean isValidDescription(String description) {
        return description.matches("^[a-zA-Z0-9\\s]+$");
    }

    public boolean isValidCategory(String category) {
        return category.matches("^[a-zA-Z0-9\\s]+$");
    }

    public boolean isValidImage(String image) {
        return image.matches("^(?:[a-zA-Z]:\\\\)?(?:[\\w\\s]+\\\\)*[\\w\\s]+\\.(?:jpg|jpeg|png|gif|bmp|tiff|webp|svg)$\n");
    }


    public boolean isValidCount(String count) {
        return count.matches("^\\d+$");
    }


    public boolean isValidRating(String rating) {
        return rating.matches("^[+-]?\\d+(\\.\\d+)?$");
    }

    public boolean isValidRateCount(String rateCount) {
        return rateCount.matches("^\\d+$");
    }


}
