package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FeedbackDB extends SQLiteOpenHelper {

    public static final String DBNAME = "FeedbackDB.db";

    public FeedbackDB(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Feedback(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, product_id INTEGER, feedback TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Feedback");
        onCreate(db);
    }

    public boolean insertFeedback(String username, int productId, String feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("product_id", productId);
        contentValues.put("feedback", feedback); //possible vulnerability

        long result = db.insert("Feedback", null, contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getFeedbackByProductId(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Feedback WHERE product_id = ?", new String[]{String.valueOf(productId)});
    }


    public boolean hasUserRatedProduct(String username, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Feedback WHERE username = ? AND product_id = ?",
                new String[]{username, String.valueOf(productId)}
        );
        boolean hasRated = cursor.moveToFirst();
        cursor.close();
        db.close();
        return hasRated;
    }
}
