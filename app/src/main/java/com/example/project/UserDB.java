package com.example.project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
public class UserDB extends SQLiteOpenHelper {
    public static final String DBNAME = "User.db";

    public UserDB(Context context){
        super(context, "User.db",  null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table User(username TEXT primary key,name TEXT not null, email TEXT not null, phone_number  TEXT not null, gender  TEXT, birthdate  TEXT,  password TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1 ) {

        MyDB.execSQL("drop Table if exists User");
        onCreate(MyDB);

    }

    public Boolean InsertUserData(String username, String name, String email, String phone_number ,  String gender, String birthdate,  String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("phone_number",phone_number);
        contentValues.put("gender",gender);
        contentValues.put("birthdate",birthdate);
        contentValues.put("password",password);
        long result = MyDB.insert("User",null,contentValues);
        MyDB.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public Cursor getuser(String username){

        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from User where username = ?",new String[]{username});

        if(cursor != null){
            cursor.moveToFirst();
        }

        return  cursor;

    }
    public Cursor getuser_login(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();

        // Query to match username and password
        Cursor cursor = MyDB.rawQuery(
                "SELECT * FROM User WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        // Ensure cursor is not null and points to the first record if available
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }

        return null;  // Return null if no match is found
    }

    public Boolean checkuser(String username){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from User where username = ?",new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        long result = MyDB.update("User", contentValues, "username = ?", new String[]{username});
        MyDB.close();
        return result != -1;
    }


    public boolean updateUserData(String username, String name, String email, String phone_number, String gender, String birthdate) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("phone_number", phone_number);
        contentValues.put("gender", gender);
        contentValues.put("birthdate", birthdate);
        long result = MyDB.update("User", contentValues, "username = ?", new String[]{username});
        MyDB.close();
        return result != -1;
    }


    //----------------------------------------------------------------------------------------
//    public boolean deleteAllUsers() {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        int result = MyDB.delete("User", null, null);
//        MyDB.close();
//        return result > 0;
//    }

//    boolean success = db.deleteAllUsers();
//        if (success) {
//        Toast.makeText(this, "All users deleted", Toast.LENGTH_SHORT).show();
//    } else {
//        Toast.makeText(this, "Failed to delete users", Toast.LENGTH_SHORT).show();
//    }
//----------------------------------------------------------------------------------------------
}
