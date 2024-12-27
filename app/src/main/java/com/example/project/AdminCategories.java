package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminCategories extends AppCompatActivity {

    private AdminCategoryAdapter adapter;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_categories);

        ProductDB productDB = new ProductDB(this);
        data = productDB.getAllCategories();

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCategoryAdapter(this, data);
        recyclerView.setAdapter(adapter);

        Button Add = findViewById(R.id.add_cat);
        Add.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategories.this, AddCategory.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductDB productDB = new ProductDB(this);
        data = productDB.getAllCategories();
        adapter.swapCursor(data);
    }
}
