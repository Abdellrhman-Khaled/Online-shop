package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;

    public OrderAdapter(Context context, List<OrderItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);

        holder.tvOrderId.setText("Order id: " + order.getOrderId());
        holder.tvUsername.setText("Username: " + order.getUsername());
        holder.tvDate.setText("Date: " + order.getDate());
        holder.tvPrice.setText("Price: " + order.getPrice());
        holder.tvDescription.setText("Description: " + order.getDescription());
        holder.ratingBar.setRating(order.getRating());
        holder.etComment.setText(order.getComment());

        holder.btnSubmit.setOnClickListener(v -> {
            int orderId = order.getOrderId();
            int rate = (int) holder.ratingBar.getRating();
            String comment = holder.etComment.getText().toString().trim();

            OrdersDB db = new OrdersDB(context);
            boolean isUpdated = db.updateTransaction(orderId, rate, comment);

            if (isUpdated) {
                Toast.makeText(context, "Feedback submitted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to submit feedback.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUsername, tvDate, tvPrice, tvDescription;
        RatingBar ratingBar;
        EditText etComment;
        Button btnSubmit;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvorderid);
            tvUsername = itemView.findViewById(R.id.orderusername);
            tvDate = itemView.findViewById(R.id.tvdate);
            tvPrice = itemView.findViewById(R.id.Price);
            tvDescription = itemView.findViewById(R.id.Description);
            ratingBar = itemView.findViewById(R.id.ordersrating);
            etComment = itemView.findViewById(R.id.orderscomment);
            btnSubmit = itemView.findViewById(R.id.ordersubmit);
        }
    }
}




