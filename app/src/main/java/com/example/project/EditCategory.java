package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditCategory extends AppCompatActivity {

    EditText cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category);

        ProductDB database = new ProductDB(this);
        cat = findViewById(R.id.edit_cat);

        String category = getIntent().getStringExtra("category_name");

        // Fetch product data from database
        Cursor cursor = database.getProductsByCategory(category);
        if (cursor != null && cursor.moveToFirst()) {
            // Populate EditText fields with product data
            cat.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if product not found
        }

        Button edit = (Button)findViewById(R.id.edit_button);

        edit.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String new_category = cat.getText().toString().trim();


                boolean isUpdated = database.updateCategory(category, new_category);

                if (isUpdated) {
                    Toast.makeText(EditCategory.this, "Product Updated Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditCategory.this, "Invalid Data - Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
