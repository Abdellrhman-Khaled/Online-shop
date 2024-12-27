package com.example.project;

import org.json.JSONArray;
import org.json.JSONObject;
public class OrderItem {
    private int orderId;
    private String username;
    private String date;
    private String price;
    private String description;
    private float rating;
    private String comment;

    public OrderItem(int orderId, String username, String date, String price, String description, float rating, String comment) {
        this.orderId = orderId;
        this.username = username;
        this.date = date;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.comment = comment;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
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

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
