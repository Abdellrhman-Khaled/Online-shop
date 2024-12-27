package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionDB extends SQLiteOpenHelper {
    public static final String DBNAME = "TransactionDB.db";

    public TransactionDB(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Transactions(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "transaction_date TEXT NOT NULL, " +
                "cart_details TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Transactions");
        onCreate(db);
    }

    public boolean saveTransaction(String username, String transactionDate, String cartDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("transaction_date", transactionDate);
        contentValues.put("cart_details", cartDetails);

        long result = db.insert("Transactions", null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getTransactionsByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Transactions WHERE username = ?", new String[]{username});
    }

    public Cursor getTransactionsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Transactions WHERE transaction_date = ?", new String[]{date});
    }
}
