package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private CartUpdateListener cartUpdateListener;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Set item details
        holder.nameTextView.setText(item.name);
        holder.priceTextView.setText("£" + String.format("%.2f", item.price)); // Format price to 2 decimal places
        holder.quantityEditText.setText(String.valueOf(item.quantity));

        // Handle Add Button Click
        holder.addButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityEditText.getText().toString());
            currentQuantity++;
            holder.quantityEditText.setText(String.valueOf(currentQuantity));
            item.quantity = currentQuantity;

            // Notify cart update
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });

        // Handle Focus Change for Quantity EditText
        holder.quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String inputText = holder.quantityEditText.getText().toString().trim();
                try {
                    int inputQuantity = inputText.isEmpty() ? 1 : Integer.parseInt(inputText);
                    item.quantity = Math.max(inputQuantity, 1);  // Ensure quantity is at least 1
                } catch (NumberFormatException e) {
                    item.quantity = 1;  // Default on invalid input
                }
                holder.quantityEditText.setText(String.valueOf(item.quantity));

                // Notify cart update
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            }
        });


        // Handle Delete Button Click
        holder.deleteButton.setOnClickListener(v -> {
            cartItems.remove(position); // Remove the item from the list
            notifyItemRemoved(position); // Notify RecyclerView about the removal
            notifyItemRangeChanged(position, cartItems.size()); // Update the positions of remaining items

            // Update the cart in SharedPreferences
            if (context instanceof Cart) {
                ((Cart) context).updateCartInSharedPreferences();

                // Notify listener to recalculate the total
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        EditText  quantityEditText;
        Button deleteButton , addButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityEditText = itemView.findViewById(R.id.quantityEditText);  // Add this
            deleteButton = itemView.findViewById(R.id.deleteButton);
            addButton = itemView.findViewById(R.id.addButton);  // Add this

        }
    }

    public interface CartUpdateListener {
        void onCartUpdated();
    }



    public void setCartUpdateListener(CartUpdateListener listener) {
        this.cartUpdateListener = listener;
    }
}

