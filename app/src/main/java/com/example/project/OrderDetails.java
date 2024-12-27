package com.example.project;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class OrderDetails extends AppCompatActivity {

    private OrdersDB ordersDB;
    private String description;
    private TextView idTextView, usernameTextView, dateTextView, cartDetailsTextView, rateTextView, commentTextView, priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        // Initialize database
        ordersDB = new OrdersDB(this);

        // Initialize UI components
        idTextView = findViewById(R.id.order_id);
        usernameTextView = findViewById(R.id.order_username);
        dateTextView = findViewById(R.id.order_date);
        cartDetailsTextView = findViewById(R.id.order_cart_details);
        rateTextView = findViewById(R.id.order_rate);
        commentTextView = findViewById(R.id.order_comment);
        priceTextView = findViewById(R.id.order_price);

        // Get order_id from intent
        int orderId = getIntent().getIntExtra("order_id", -1); // Get order_id from intent
        if (orderId == -1) {
            return;
        }

        loadOrderDetails(orderId);
    }

    private void loadOrderDetails(int orderId) {
        Cursor cursor = ordersDB.getTransactionById(orderId);

        if (cursor != null && cursor.moveToFirst()) {
            // Populate the UI with order data
            idTextView.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
            usernameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            dateTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("transaction_date")));
            description = cursor.getString(cursor.getColumnIndexOrThrow("cart_details"));
            cartDetailsTextView.setText(getDescription());
            rateTextView.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("rate"))));
            commentTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
            double price_unformatted = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

            DecimalFormat df = new DecimalFormat("0.00");
            String formattedPrice = df.format(price_unformatted);

            double price = Double.parseDouble(formattedPrice);
            priceTextView.setText(String.valueOf(price));

            cursor.close();
        }
    }

    public String getDescription() {
        try {
            JSONArray descriptionArray = new JSONArray(description);
            StringBuilder formattedDescription = new StringBuilder();

            for (int i = 0; i < descriptionArray.length(); i++) {
                JSONObject item = descriptionArray.getJSONObject(i);
                formattedDescription.append("Item ").append(i + 1).append(":\n");
                formattedDescription.append("Name: ").append(item.getString("name")).append("\n");
                formattedDescription.append("Price: $").append(String.format("%.1f", item.getDouble("price"))).append("\n");
                formattedDescription.append("Quantity: ").append(item.getInt("quantity")).append("\n");
                formattedDescription.append("\n");
            }

            return formattedDescription.toString();
        } catch (Exception e) {
            return "Invalid description format!";
        }
    }
}
