package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class AdminDB extends SQLiteOpenHelper {
    public static final String DBNAME = "Admin.db";

    public AdminDB(Context context){
        super(context, "Admin.db",  null, 1);

    }

    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table Admin(username TEXT primary key,name TEXT not null, email TEXT not null, phone_number  TEXT not null, gender  TEXT, birthdate  TEXT,  password TEXT)");

    }

    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1 ) {

        MyDB.execSQL("drop Table if exists Admin");
        onCreate(MyDB);

    }

    public Boolean InsertAdminData(String username, String name, String email, String phone_number ,  String gender, String birthdate,  String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("phone_number",phone_number);
        contentValues.put("gender",gender);
        contentValues.put("birthdate",birthdate);
        contentValues.put("password",password);
        long result = MyDB.insert("Admin",null,contentValues);
        MyDB.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getadmin_login(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();

        // Query to match username and password
        Cursor cursor = MyDB.rawQuery(
                "SELECT * FROM Admin WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        // Ensure cursor is not null and points to the first record if available
        if (cursor != null && cursor.moveToFirst()) {
            return cursor;
        }

        return null;  // Return null if no match is found
    }
}


//
//AdminDB admindb = new AdminDB(this);
//String adminpassword = "Abdo12345!";
//String hashedadminpassword = hash.hashPassword(adminpassword);
//Boolean adminresult =  admindb.InsertAdminData("Abdo12345" ,"Abdelrahman Khaled" ,"abdellrahmankhaledd@gmail.com","01090724926","male","05/10/2003",hashedadminpassword);
//
//        if(adminresult){
//        Toast.makeText(this, "admin added successfully",Toast.LENGTH_SHORT).show();
//        }

