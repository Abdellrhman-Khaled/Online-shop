package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOptions extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_options);


        Button chart = (Button) findViewById(R.id.chart_button);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOptions.this, Chart.class);
                startActivity(intent);
            }
        });

        Button addProduct = (Button) findViewById(R.id.add);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOptions.this, AddProduct.class);
                startActivity(intent);
            }
        });

        Button transactions = (Button) findViewById(R.id.report_button);
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOptions.this, Transactions.class);
                startActivity(intent);
            }
        });

        Button cat = (Button) findViewById(R.id.categories);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminOptions.this, AdminCategories.class);
                startActivity(intent);
            }
        });

    }
}
