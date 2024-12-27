package com.example.project;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {


    private Context context;
    private Cursor cursor;

    public AdminProductAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close(); // Close old cursor
        }
        cursor = newCursor;
        notifyDataSetChanged(); // Notify adapter about data changes
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        ProductDB db = new ProductDB(context);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        float rate = cursor.getFloat(cursor.getColumnIndexOrThrow("rate"));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int rateCount = cursor.getInt(cursor.getColumnIndexOrThrow("rate_count"));
        int sold = cursor.getInt(cursor.getColumnIndexOrThrow("sold"));

        // Load the image using Glide
        Glide.with(context)
                .load(image)
                .into(holder.imageView);

        holder.idTextView.setText(String.valueOf(id));
        holder.titleTextView.setText(title);
        holder.priceTextView.setText(String.valueOf(price));
        holder.descriptionTextView.setText(description);
        holder.categoryTextView.setText(category);
        holder.rateTextView.setText(String.valueOf(rate));
        holder.countTextView.setText(String.valueOf(count));
        holder.rateCountTextView.setText(String.valueOf(rateCount));
        holder.soldTextView.setText(String.valueOf(sold));


        holder.deleteButton.setTag(productId);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productId = (int) v.getTag();
                db.deleteProductById(productId);
                cursor = db.getAllProducts(); // Refresh cursor
                notifyDataSetChanged(); // Update RecyclerView
            }
        });

        holder.editButton.setTag(productId);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productId = (int) v.getTag();
                Intent intent = new Intent(context, EditProduct.class);
                intent.putExtra("product_id", productId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0; // Handle null cursor

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView idTextView;
        TextView titleTextView;
        TextView priceTextView;
        TextView descriptionTextView;
        TextView categoryTextView;
        ImageView imageView;
        TextView rateTextView;
        TextView countTextView;
        TextView rateCountTextView;
        TextView soldTextView;

        Button deleteButton;
        Button editButton;


        @SuppressLint("WrongViewCast")
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            idTextView = itemView.findViewById(R.id.product_id);
            titleTextView = itemView.findViewById(R.id.product_title);
            priceTextView = itemView.findViewById(R.id.product_price);
            descriptionTextView = itemView.findViewById(R.id.product_desc);
            categoryTextView = itemView.findViewById(R.id.product_cat);
            rateTextView = itemView.findViewById(R.id.product_rating);
            countTextView = itemView.findViewById(R.id.product_count);
            rateCountTextView = itemView.findViewById(R.id.product_rateCount);
            soldTextView = itemView.findViewById(R.id.product_sold);
            deleteButton = itemView.findViewById(R.id.button_delete);
            editButton = itemView.findViewById(R.id.button_edit);
        }
    }
}
