package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Change_password extends AppCompatActivity {

    private static final String PREF_NAME = "UserPrefs";
    private LinearLayout btnHome, btnDiscover, btnProfile;
    SharedPreferences sharedPreferences;
    UserDB db;

    EditText etNewPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        // Initialize UI components
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        db = new UserDB(this);
        Register hash = new Register();


        findViewById(R.id.btnConfirmReset).setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();


            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Change_password.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            } else {
                if (newPassword.equals(confirmPassword)) {

                    if(hash.isValidPassword(newPassword)){

                        String hashedPassword = hash.hashPassword(confirmPassword);
                        boolean result = db.updatePassword(username, hashedPassword);
                        if (result) {
                            Toast.makeText(Change_password.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                            resetFields();
                            Intent intent = new Intent(Change_password.this, User_profile.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Change_password.this, "Password change failed. Try again.", Toast.LENGTH_SHORT).show();
                            resetFields();
                        }
                    }else{
                        Toast.makeText(this, "Password must contain at least one uppercase,one number,one special char", Toast.LENGTH_LONG).show();
                        resetFields();
                    }


                } else {
                    Toast.makeText(Change_password.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    resetFields();
                }
            }
        });


        btnHome = findViewById(R.id.CPhomeBtn);
        btnDiscover = findViewById(R.id.CPdiscoverBtn);
        btnProfile = findViewById(R.id.CPprofileBtn);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Change_password.this, Products.class);
            startActivity(intent);
        });

        btnDiscover.setOnClickListener(v -> {
            Intent intent = new Intent(Change_password.this, discover.class);
            startActivity(intent);
        });

        findViewById(R.id.CPcartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Change_password.this, User_profile.class);
            startActivity(intent);
        });
    }

    private void resetFields() {
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    }
}
