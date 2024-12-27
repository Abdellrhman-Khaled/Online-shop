package com.example.project;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Transactions extends AppCompatActivity {

    private TransactionAdapter adapter;
    private OrdersDB ordersDB;
    private RecyclerView recyclerView;
    private TextView noDataMessage;
    private SearchView searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);

        recyclerView = findViewById(R.id.transaction_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noDataMessage = findViewById(R.id.no_data_message);
        searchField = findViewById(R.id.search_field);

        ordersDB = new OrdersDB(this);

        // Display all transactions initially
        loadTransactions(null);

        // Handle search input
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search on submit
                loadTransactions(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as text changes
                loadTransactions(newText);
                return true;
            }
        });
    }

    private void loadTransactions(String searchQuery) {
        Cursor cursor;

        if (TextUtils.isEmpty(searchQuery)) {
            // Load all transactions if search query is empty
            cursor = ordersDB.getAllTransactions();
        } else {
            // Search query
            cursor = ordersDB.searchTransactions(searchQuery);
        }

        if (cursor.getCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            noDataMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noDataMessage.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new TransactionAdapter(this, cursor);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.swapCursor(cursor);
            }
        }
    }
}
