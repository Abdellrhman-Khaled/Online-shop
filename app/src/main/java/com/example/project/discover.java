package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class discover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);

        RecyclerView categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> categories = getCategoriesFromDB();


        CategoryAdapter adapter = new CategoryAdapter(categories, category -> {

            Intent intent = new Intent(discover.this, Products.class);
            intent.putExtra("category", category);
            startActivity(intent);
        });
        categoryRecyclerView.setAdapter(adapter);

        // Navigation buttons
        findViewById(R.id.discoverimgLogout).setOnClickListener(v -> {
            Intent intent = new Intent(discover.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.homebtn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Products.class));
        });

        findViewById(R.id.discovercartbtn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Cart.class));
        });

        findViewById(R.id.discoverprofilebtn).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), User_profile.class));
        });
    }

    private List<String> getCategoriesFromDB() {
        List<String> categories = new ArrayList<>();
        ProductDB dbHelper = new ProductDB(this);

        Cursor cursor = dbHelper.getAllCategories();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                categories.add(category);
            }
            cursor.close();
        }
        return categories;
    }
}
