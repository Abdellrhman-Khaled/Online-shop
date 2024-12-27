package com.example.project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDB extends SQLiteOpenHelper {

    public static final String DBNAME = "ProductDB.db";

    // Constructor
    public ProductDB(Context context) {
        super(context, DBNAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Products(id INTEGER PRIMARY KEY, title TEXT, price REAL, description TEXT, category TEXT, image TEXT, rate REAL, count INTEGER, rate_count INTEGER, sold INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE Products ADD COLUMN sold INTEGER DEFAULT 0");
        }
    }

    // Method to insert product data into the database
    public boolean insertProductData(int id, String title, double price, String description, String category, String image, float rate, int count, int rateCount, int sold) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("category", category);
        contentValues.put("image", image);
        contentValues.put("rate", rate);
        contentValues.put("count", count);
        contentValues.put("rate_count", rateCount);
        contentValues.put("sold", sold);

        long result = db.insert("Products", null, contentValues);
        db.close();
        return result != -1;
    }

    // Method to update a product's details
    public boolean updateProduct(int id, String title, double price, String description, String category, String image, float rate, int count, int rateCount, int sold) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("category", category);
        contentValues.put("image", image);
        contentValues.put("rate", rate);
        contentValues.put("count", count);
        contentValues.put("rate_count", rateCount);
        contentValues.put("sold", sold);

        int result = db.update("Products", contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // Method to get all products from the database
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products", null);
    }

    // Method to get a product by its ID
    public Cursor getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Method to get products by category
    public Cursor getProductsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products WHERE category = ?", new String[]{category});
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT DISTINCT category FROM Products", null);
    }

    // Method to delete a product by its ID


    public boolean is_exists(int id) {
        getProductById(id);
        if (getProductById(id).getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getTop5MostSoldProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query to get the top 5 most sold products, ordered by the 'sold' column in descending order
        String query = "SELECT * FROM Products ORDER BY sold DESC LIMIT 5";
        return db.rawQuery(query, null);
    }

    // Method to delete all products
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Products");
        db.close();
    }

    // Method to search products by title
    public Cursor searchProducts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products WHERE title LIKE ?", new String[]{"%" + query + "%"});
    }


    // Method to filter products by category and search query
    public Cursor getProductsByCategoryAndSearch(String category, String searchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products WHERE category = ? AND title LIKE ?", new String[]{category, "%" + searchQuery + "%"});
    }

    // Method to update product rating
    public boolean updateProductRating(int productId, float newRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getProductById(productId);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") float currentRating = cursor.getFloat(cursor.getColumnIndex("rate"));
            @SuppressLint("Range") int rateCount = cursor.getInt(cursor.getColumnIndex("rate_count"));

            float updatedRating = (currentRating * rateCount + newRating) / (rateCount + 1);
            rateCount++;

            ContentValues contentValues = new ContentValues();
            contentValues.put("rate", updatedRating);
            contentValues.put("rate_count", rateCount);

            int result = db.update("Products", contentValues, "id = ?", new String[]{String.valueOf(productId)});
            cursor.close();
            db.close();
            return result > 0;
        }
        return false;
    }


    // Method to update both sold and count quantities
    public boolean updateProductQuantities(String title, int quantitySold) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT sold, count FROM Products WHERE title = ?", new String[]{title});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int currentSold = cursor.getInt(cursor.getColumnIndex("sold"));
            @SuppressLint("Range") int currentCount = cursor.getInt(cursor.getColumnIndex("count"));

            // Calculate new values
            int newSold = currentSold + quantitySold;
            int newCount = currentCount - quantitySold;

            if (newCount < 0) {
                cursor.close();
                db.close();
                return false;  // Prevent negative stock
            }

            // Update the database
            ContentValues contentValues = new ContentValues();
            contentValues.put("sold", newSold);
            contentValues.put("count", newCount);

            int result = db.update("Products", contentValues, "title = ?", new String[]{title});
            cursor.close();
            db.close();
            return result > 0;
        }
        return false;
    }



    // Method to check if a product's requested quantity is available
    public int QuantityAvailable(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        cursor = db.rawQuery(
                "SELECT sold, count FROM Products WHERE title = ?", new String[]{title});

        int availableQuantity = 0;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int currentSold = cursor.getInt(cursor.getColumnIndex("sold"));
            @SuppressLint("Range") int currentCount = cursor.getInt(cursor.getColumnIndex("count"));

            availableQuantity = currentCount - currentSold;
        }
        return availableQuantity;
    }





    public boolean deleteProductById(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Products", "id = ?", new String[]{String.valueOf(productId)});
        db.close();
        return result > 0;
    }



    // Method to insert a new category and ensure no products are assigned to it
    public boolean insertNewCategory(String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Trim the input to prevent issues with leading/trailing spaces
            newCategory = newCategory.trim();

            // Check if the category already exists in the Products table
            Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT category) FROM Products WHERE category = ?", new String[]{newCategory});
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                cursor.close();
                db.close();
                return false; // Category already exists in Products table
            }
            cursor.close();

            // Insert the new category into the Products table by updating an empty product's category
            ContentValues contentValues = new ContentValues();
            contentValues.put("category", newCategory);
            long result = db.insert("Products", null, contentValues);

            db.close();
            return result != -1; // Return true if insertion was successful
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }


    // Method to delete a category and its products
    public void deleteCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Products WHERE category = ?", new String[]{category});
        db.close();
    }

    // Method to update all products with a specific category to a new category
    public boolean updateCategory(String oldCategory, String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", newCategory);

        int rowsAffected = db.update("Products", contentValues, "category = ?", new String[]{oldCategory});
        db.close();

        return rowsAffected > 0; // Return true if at least one row was updated
    }




}
