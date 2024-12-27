package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Orders extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    RecyclerView orderRecyclerView;
    OrderAdapter orderAdapter;
    ArrayList<OrderItem> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_orders);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        orderRecyclerView = findViewById(R.id.order_recycler_view);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders(username);

        findViewById(R.id.ordereditdiscoverBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), discover.class))
        );

        findViewById(R.id.ordereditcartBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Cart.class))
        );

        findViewById(R.id.orderedithomeBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Products.class))
        );

        findViewById(R.id.ordereditprofileBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), User_profile.class))
        );
    }

    private void loadOrders(String username) {
        OrdersDB db = new OrdersDB(this);
        Cursor cursor = db.getTransactionsByUser(username);

        orderList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("transaction_date"));
                @SuppressLint("Range") String price = cursor.getString(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("cart_details"));
                @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex("rate"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));

                orderList.add(new OrderItem(id, username, date, price, description, rating, comment));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
        }

        orderAdapter = new OrderAdapter(this, orderList);
        orderRecyclerView.setAdapter(orderAdapter);
    }
}
