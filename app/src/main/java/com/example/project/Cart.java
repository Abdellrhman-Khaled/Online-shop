package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private double total_price;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        ProductDB db = new ProductDB(this);


        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList);

        cartAdapter.setCartUpdateListener(this::calculateAndDisplayTotal);

        cartRecyclerView.setAdapter(cartAdapter);

        loadCartItems();

        findViewById(R.id.carthomeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Products.class));
            }
        });

        findViewById(R.id.cartshopbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Products.class));
            }
        });

        findViewById(R.id.cartdiscoverBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), discover.class));
            }
        });

        findViewById(R.id.cartprofileBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), User_profile.class));
            }
        });





        findViewById(R.id.checkoutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences userPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String username = userPreferences.getString("username", "");
                Boolean validitems = true;
                Boolean validtransactions = true;

                if (username.isEmpty()) {
                    // Handle case where username is not found
                    Toast.makeText(Cart.this, "User not logged in. Please log in to continue.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get current date and time
                String transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Prepare cart details
                JSONArray cartArray = new JSONArray();
                for (CartItem item : cartItemList) {
                    JSONObject jsonObject = new JSONObject();
                   int result =  db.QuantityAvailable(item.name);
                   if(result >= item.quantity){
                        try {
                           jsonObject.put("name", item.name);
                           jsonObject.put("price", item.price);
                           jsonObject.put("quantity", item.quantity);
                           cartArray.put(jsonObject);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }else{
                       validitems = false;
                       Toast.makeText(Cart.this, "item name  "+ item.name + "ONLY HAS: " +result, Toast.LENGTH_SHORT).show();
                   }
                }

                if(validitems){

                    for (CartItem item : cartItemList) {
                        validtransactions =  db.updateProductQuantities(item.name, item.quantity);
                    }
                    if(validtransactions){
                        // Save transaction to the database
                        OrdersDB order = new OrdersDB(Cart.this);
                        boolean isSaved = order.saveTransaction(username, transactionDate, cartArray.toString() , 0, "", total_price);

                        if (isSaved) {
                            Toast.makeText(Cart.this, "Transaction saved successfully!", Toast.LENGTH_SHORT).show();
                            cartItemList.clear();
                            updateCartInSharedPreferences();
                            cartAdapter.notifyDataSetChanged();
                            calculateAndDisplayTotal();
                        } else {
                            Toast.makeText(Cart.this, "Failed to save transaction. try again..", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else{
                    Toast.makeText(Cart.this, "Please update the quantity of items", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loadCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
        String cartData = sharedPreferences.getString("cartItems", "[]");

        try {
            JSONArray cartArray = new JSONArray(cartData);
            cartItemList.clear();

            for (int i = 0; i < cartArray.length(); i++) {
                JSONObject item = cartArray.getJSONObject(i);
                String name = item.getString("name");
                double price = item.getDouble("price");
                cartItemList.add(new CartItem(name, price));
            }
            cartAdapter.notifyDataSetChanged();
            calculateAndDisplayTotal();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCartInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONArray updatedCartArray = new JSONArray();
        for (CartItem item : cartItemList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", item.name);
                jsonObject.put("price", item.price);
                jsonObject.put("quantity", item.quantity); // Ensure quantity is stored
                updatedCartArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("cartItems", updatedCartArray.toString());
        editor.apply();
    }



    private void calculateAndDisplayTotal() {
        double total = 0.0;
        for (CartItem item : cartItemList) {
            total += item.price * item.quantity; // Assuming CartItem has a `quantity` field
        }
        total_price =total;

        // Find the total TextView and set the calculated total
        TextView totalView = findViewById(R.id.totalview);
        totalView.setText("Total\n£" + String.format("%.2f", total));
    }


    public void addOrUpdateCartItem(String name, double price) {
        boolean found = false;

        for (CartItem item : cartItemList) {
            if (item.name.equals(name)) {
                // Item exists in the cart, increment quantity
                item.quantity++;
                found = true;
                break;
            }
        }

        if (!found) {
            // Item not found, add it as a new item
            cartItemList.add(new CartItem(name, price));
        }

        // Update SharedPreferences
        updateCartInSharedPreferences();

        // Notify adapter about changes
        cartAdapter.notifyDataSetChanged();

        // Recalculate total
        calculateAndDisplayTotal();
    }


}
