package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminProducts extends AppCompatActivity {

    AdminProductAdapter adapter; // Declare adapter as a class member
    Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_products);



        ProductDB productDB = new ProductDB(this);
        data = productDB.getAllProducts();

        RecyclerView recyclerView = findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminProductAdapter(this, data);
        recyclerView.setAdapter(adapter);



        Button options = (Button) findViewById(R.id.admin_panel_button);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProducts.this, AdminOptions.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductDB productDB = new ProductDB(this);
        data = productDB.getAllProducts();
        adapter.swapCursor(data);
    }
}
