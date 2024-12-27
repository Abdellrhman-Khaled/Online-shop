package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class User_profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextView tvUsername, tvName, tvEmail, tvPhone, tvBirthdate, tvGender;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");
        String birthdate = sharedPreferences.getString("birthdate", "");
        String gender = sharedPreferences.getString("gender", "");

        // Initialize views
        tvUsername = findViewById(R.id.tvUsername);
        tvName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvBirthdate = findViewById(R.id.tvBirthdate);
        tvGender = findViewById(R.id.tvGender);

        // Display user data
        tvUsername.setText("Username: " + username);
        tvName.setText("Name: "+ name);
        tvEmail.setText("Email: "+ email);
        tvPhone.setText("Phone: "+ phone);
        tvBirthdate.setText("Birthdate: "+ birthdate);
        tvGender.setText("Gender: " + gender);

        findViewById(R.id.btnAddAccount).setOnClickListener(v -> {
            Intent intent = new Intent(User_profile.this, Register.class);
            startActivity(intent);
        });

        findViewById(R.id.editdiscoverBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), discover.class))
        );

        findViewById(R.id.vieworderbtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Orders.class))
        );

        findViewById(R.id.editcartBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Cart.class))
        );
        findViewById(R.id.edithomeBtn).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Products.class))
        );

        findViewById(R.id.btnEditProfile).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Edituserprofile.class))
        );


    }
}
