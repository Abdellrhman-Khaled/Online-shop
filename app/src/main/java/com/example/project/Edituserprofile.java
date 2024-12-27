package com.example.project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Edituserprofile extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone;
    private TextView tvUsername;

    private Spinner spGender;
    private TextView tvSelectedBirthdate;
    private Button btnSelectBirthdate, btnEditProfile , btnchangepassword;
    private LinearLayout btnHome, btnDiscover, btnProfile;
    private UserDB userDB;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "UserPrefs";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        tvUsername = findViewById(R.id.edittvUsername);

        // Initialize UI elements
        etFullName = findViewById(R.id.editetFullName);
        etEmail = findViewById(R.id.editetEmail);
        etPhone = findViewById(R.id.editetPhone);
        spGender = findViewById(R.id.editspGender);
        tvSelectedBirthdate = findViewById(R.id.edittvSelectedBirthdate);
        btnSelectBirthdate = findViewById(R.id.editbtnSelectBirthdate);
        btnEditProfile = findViewById(R.id.editbtnEditProfile);
        btnHome = findViewById(R.id.edithomeBtn);
        btnDiscover = findViewById(R.id.editdiscoverBtn);
        btnProfile = findViewById(R.id.editprofileBtn);
        btnchangepassword = findViewById(R.id.editbtnchangepassword);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Edituserprofile.this, Products.class);
            startActivity(intent);
        });

        btnDiscover.setOnClickListener(v -> {
            Intent intent = new Intent(Edituserprofile.this, discover.class);
            startActivity(intent);
        });
        findViewById(R.id.editcartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Cart.class));
            }
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Edituserprofile.this, User_profile.class);
            startActivity(intent);
        });

        btnchangepassword.setOnClickListener(v -> {
            Intent intent = new Intent(Edituserprofile.this, Change_password.class);
            startActivity(intent);
        });

        userDB = new UserDB(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);


        loadUserData();

        btnSelectBirthdate.setOnClickListener(v -> showDatePicker());
        btnEditProfile.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserData() {
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            Toast.makeText(this, "No user found!", Toast.LENGTH_SHORT).show();
            return;
        }
        tvUsername.setText("Username: " + username);

        Cursor cursor = userDB.getuser(username);
        if (cursor != null && cursor.moveToFirst()) {

            etFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone_number")));
            tvSelectedBirthdate.setText(cursor.getString(cursor.getColumnIndexOrThrow("birthdate")));

            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.gender_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spGender.setAdapter(adapter);
            spGender.setSelection(adapter.getPosition(gender));
            cursor.close();
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> tvSelectedBirthdate.setText(
                        dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
        datePickerDialog.show();
    }

    private void updateUserProfile() {
        String username = sharedPreferences.getString("username", "");
        if (username.isEmpty()) {
            Toast.makeText(this, "No user found!", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();
        String birthdate = tvSelectedBirthdate.getText().toString();

        Register valid = new Register();


        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!valid.isValidFullName(fullName)){
            Toast.makeText(this, "Full name must contain only letters.", Toast.LENGTH_SHORT).show();
        }
        else if(!valid.isValidEmail(email)){
            Toast.makeText(this, "Invalid email format. Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if(!valid.isValidPhone(phone)){
            Toast.makeText(this, "Phone number must be 11 digits and start with '01'.", Toast.LENGTH_SHORT).show();
        }
        else if(!valid.isValidAge(birthdate)){
            Toast.makeText(this, "Age must be between 8 and 100 years.", Toast.LENGTH_SHORT).show();
        }
        else{

            boolean result = userDB.updateUserData(username, fullName, email, phone, gender, birthdate);

            if (result) {
                // Update SharedPreferences

                //name is not updated
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", fullName);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("gender", gender);
                editor.putString("birthdate", birthdate);
                editor.apply();

                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(Edituserprofile.this, User_profile.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
