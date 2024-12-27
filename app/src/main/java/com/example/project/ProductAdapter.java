package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final Cursor cursor;

    public ProductAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }


        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        double rate = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));


        holder.titleTextView.setText(title);
        holder.priceTextView.setText(String.valueOf(price + "$"));


        Glide.with(context)
                .load(image)
                .into(holder.imageView);

        holder.viewDetailsButton.setOnClickListener(v -> {
            // Store the product data in SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("ProductDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", id);
            editor.putString("title", title);
            editor.putString("image", image);
            editor.putString("description", description);
            editor.putFloat("price", (float) price);
            editor.apply();

            // Start the ProductDetailsActivity
            context.startActivity(new Intent(context, ProductDetailsActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView priceTextView;
        ImageView imageView;
        Button viewDetailsButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            titleTextView = itemView.findViewById(R.id.product_title);
            priceTextView = itemView.findViewById(R.id.product_price);
            viewDetailsButton = itemView.findViewById(R.id.view_details_button);
        }
    }
}
