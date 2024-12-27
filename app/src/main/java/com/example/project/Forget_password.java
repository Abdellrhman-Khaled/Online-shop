package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Forget_password extends AppCompatActivity {

    EditText etPhone, etEmail, etUsername, etNewPassword, etConfirmPassword;
    Button btnConfirm;
    UserDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPhone = findViewById(R.id.editetPhone);
        etEmail = findViewById(R.id.editetEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);

        db = new UserDB(this);
        Register hash =new Register();

        btnConfirm.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate input
            if (username.isEmpty() || phone.isEmpty() || email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve user data
            Cursor row = db.getuser(username);
            if (row != null && row.moveToFirst()) {
                @SuppressLint("Range") String dbUsername = row.getString(row.getColumnIndex("username"));
                @SuppressLint("Range") String dbEmail = row.getString(row.getColumnIndex("email"));
                @SuppressLint("Range") String dbPhoneNumber = row.getString(row.getColumnIndex("phone_number"));

                // Check if data matches
                if (username.equals(dbUsername) && phone.equals(dbPhoneNumber) && email.equals(dbEmail)) {
                    if (newPassword.equals(confirmPassword)) {
                        String hashed = hash.hashPassword(confirmPassword);
                        boolean result = db.updatePassword(username, hashed);
                        if (result) {
                            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                            resetFields();
                            Intent intent = new Intent(Forget_password.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Password change failed. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User details incorrect. Try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User details incorrect. Try again.", Toast.LENGTH_SHORT).show();
            }

            // Close cursor
            if (row != null) {
                row.close();
            }
        });
    }

    // Method to clear all input fields
    private void resetFields() {
        etUsername.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    }
}
