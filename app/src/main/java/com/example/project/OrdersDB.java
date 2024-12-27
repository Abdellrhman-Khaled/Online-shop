package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrdersDB extends SQLiteOpenHelper {
    public static final String DBNAME = "OrdersDB.db";

    public OrdersDB(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Orders(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "transaction_date TEXT NOT NULL, " +
                "cart_details TEXT NOT NULL, " +
                "rate INTEGER NOT NULL, " +
                "comment TEXT, " +
                "price REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops the existing table and recreates it
        db.execSQL("DROP TABLE IF EXISTS Orders");
        onCreate(db);
    }


    public boolean saveTransaction(String username, String transactionDate, String cartDetails, int rate, String comment, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("transaction_date", transactionDate);
        contentValues.put("cart_details", cartDetails);
        contentValues.put("rate", rate);
        contentValues.put("comment", comment);
        contentValues.put("price", price);

        long result = db.insert("Orders", null, contentValues);
        db.close();
        return result != -1;
    }


    public Cursor getTransactionsByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Orders WHERE username = ?", new String[]{username});
    }


    public Cursor getTransactionsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Orders WHERE transaction_date = ?", new String[]{date});
    }

    public Cursor getTransactionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Orders WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS order_id, username, transaction_date AS date, price FROM Orders", null);
    }

    public Cursor searchTransactions(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id AS order_id, username, transaction_date AS date, price " +
                "FROM Orders " +
                "WHERE id LIKE ? OR username LIKE ? OR transaction_date LIKE ? OR cart_details LIKE ?";
        String wildcardQuery = "%" + query + "%";
        return db.rawQuery(sql, new String[]{wildcardQuery, wildcardQuery, wildcardQuery, wildcardQuery});
    }

    public boolean updateTransaction(int id, int rate, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rate", rate);
        contentValues.put("comment", comment);


        int result = db.update("Orders", contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return result > 0;
    }


//    public boolean deleteAllOrders() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int result = db.delete("Orders", null, null);
//        db.close();
//        return result > 0;
//    }

}
