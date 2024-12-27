package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private Cursor cursor;

    public TransactionAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
        String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        double price_unformatted = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedPrice = df.format(price_unformatted);

        double price = Double.parseDouble(formattedPrice);

        holder.orderIdTextView.setText(String.valueOf(orderId));
        holder.usernameTextView.setText(username);
        holder.dateTextView.setText(date);
        holder.priceTextView.setText(String.valueOf(price));

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetails.class);
            intent.putExtra("order_id", orderId); // Pass the order_id instead of username
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdTextView;
        TextView usernameTextView;
        TextView dateTextView;
        TextView priceTextView;
        Button viewButton;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.transaction_order_id);
            usernameTextView = itemView.findViewById(R.id.transaction_username);
            dateTextView = itemView.findViewById(R.id.transaction_date);
            priceTextView = itemView.findViewById(R.id.transaction_price);
            viewButton = itemView.findViewById(R.id.transaction_view_button);
        }
    }
}
