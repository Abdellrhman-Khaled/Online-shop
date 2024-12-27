package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    TextView tvSignup, tvForgetpassword, etUsername, etPassword;
    private ProductDB productDB;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgetpassword = findViewById(R.id.tvForgetpassword);

        cbRememberMe = findViewById(R.id.cbRememberMe);

        loadSavedCredentials();

        UserDB db = new UserDB(this);
        AdminDB admindb = new AdminDB(this);


        Register hash = new Register();


        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });

        tvForgetpassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Forget_password.class);
            startActivity(intent);
        });

        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String hashedPassword = hash.hashPassword(password);
            Cursor adminrow = admindb.getadmin_login(username, hashedPassword);

            if (adminrow != null && adminrow.moveToFirst()){
                etUsername.setText("");
                etPassword.setText("");
                Intent intent = new Intent(MainActivity.this, AdminProducts.class);
                startActivity(intent);
            }else {
                Cursor row = db.getuser_login(username, hashedPassword);

                if (row != null && row.moveToFirst()) {
                    // Extract data from the database
                    @SuppressLint("Range") String dbUsername = row.getString(row.getColumnIndex("username"));
                    @SuppressLint("Range") String dbEmail = row.getString(row.getColumnIndex("email"));
                    @SuppressLint("Range") String dbPhoneNumber = row.getString(row.getColumnIndex("phone_number"));
                    @SuppressLint("Range") String dbGender = row.getString(row.getColumnIndex("gender"));
                    @SuppressLint("Range") String dbBirthdate = row.getString(row.getColumnIndex("birthdate"));
                    @SuppressLint("Range") String dbName = row.getString(row.getColumnIndex("name"));

                    // Save user data to SharedPreferences
                    editor.putString("username", dbUsername);
                    editor.putString("name", dbName);
                    editor.putString("email", dbEmail);
                    editor.putString("phone", dbPhoneNumber);
                    editor.putString("birthdate", dbBirthdate);
                    editor.putString("gender", dbGender);
                    editor.apply();


                    if (cbRememberMe.isChecked()) {
                        saveCredentials(username, password);
                    } else {
                        clearCredentials();
                    }

                    // Reset fields and navigate to Home
                    etUsername.setText("");
                    etPassword.setText("");
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Products.class);
                    startActivity(intent);
                } else {
                    showLoginFailed();
                }

                if (row != null) {
                    row.close();
                }

            }

        });


    }



    private void saveCredentials(String username, String password) {
        editor.putString("savedUsername", username);
        editor.putString("savedPassword", password);
        editor.putBoolean("rememberMe", true);
        editor.apply();
    }

    private void loadSavedCredentials() {
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            etUsername.setText(sharedPreferences.getString("savedUsername", ""));
            etPassword.setText(sharedPreferences.getString("savedPassword", ""));
            cbRememberMe.setChecked(true);
        }
    }

    private void clearCredentials() {
        editor.remove("savedUsername");
        editor.remove("savedPassword");
        editor.remove("rememberMe");
        editor.apply();
    }

    // Helper function for failed login
    private void showLoginFailed() {
        Toast.makeText(this, "Login failed. Try again.", Toast.LENGTH_SHORT).show();
        etUsername.setText("");
        etPassword.setText("");
    }

}
