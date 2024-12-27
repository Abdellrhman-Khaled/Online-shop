package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProduct extends AppCompatActivity {

    EditText id, title, cost, desc, cat, img, rating, number_of_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        ProductDB db = new ProductDB(this);

        id = findViewById(R.id.add_id);
        title = findViewById(R.id.add_title);
        cost = findViewById(R.id.add_price);
        desc = findViewById(R.id.add_desc);
        cat = findViewById(R.id.add_cat);
        img = findViewById(R.id.add_image);
        number_of_products = findViewById(R.id.add_count);

        Button add = (Button)findViewById(R.id.add_product);

        add.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int product_id = Integer.parseInt(id.getText().toString());
                String name = title.getText().toString().trim();
                double price = Double.parseDouble(cost.getText().toString().trim());
                String description = desc.getText().toString().trim();
                String category = cat.getText().toString().trim();
                String image= img.getText().toString().trim();
                float rate = 0.0F;
                int count = Integer.parseInt(number_of_products.getText().toString().trim());
                int rateCount = 0;
                int sold = 0;


                if (db.is_exists(product_id)){
                    Toast.makeText(AddProduct.this, "Product ID already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidID(String.valueOf(product_id))) {
                    Toast.makeText(AddProduct.this, "Product ID must be an integer number", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!isValidCost(String.valueOf(price))) {
                    Toast.makeText(AddProduct.this, "Price must be a number", Toast.LENGTH_SHORT).show();
                    return;
                }

//
//                if (!isValidImage(image)) {
//                    Toast.makeText(AddProduct.this, "Insert a valid image path", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (!isValidCount(String.valueOf(count))) {
                    Toast.makeText(AddProduct.this, "Number of products must be an integer number", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Insert Product data
                boolean isInserted = db.insertProductData(product_id, name, price, description, category, image, rate, count, rateCount, sold);

                if (isInserted) {
                    Toast.makeText(AddProduct.this, "Insertion Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProduct.this, AdminProducts.class);
                    startActivity(intent);
                    resetFields();
                } else {
                    Toast.makeText(AddProduct.this, "Invalid Data - Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void resetFields() {
        id.setText("");
        title.setText("");
        cost.setText("");
        desc.setText("");
        cat.setText("");
        img.setSelection(0);
        number_of_products.setText("");
    }

    public boolean isValidID(String id) {
        return id.matches("^(0|[1-9][0-9]*)$");
    }


    public boolean isValidCost(String cost) {
        return cost.matches("^[+-]?\\d+(\\.\\d+)?$");
    }


    public boolean isValidImage(String image) {
        return image.matches("^(?:[a-zA-Z]:\\\\)?(?:[\\w\\s]+\\\\)*[\\w\\s]+\\.(?:jpg|jpeg|png|gif|bmp|tiff|webp|svg)$\n");
    }


    public boolean isValidCount(String count) {
        return count.matches("^(0|[1-9][0-9]*)$");
    }
}
