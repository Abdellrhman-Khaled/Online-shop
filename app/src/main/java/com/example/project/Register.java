package com.example.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextView tvBirthdate, etUsername, etName, etEmail, etPhone, etPassword;
    Spinner spGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        UserDB db = new UserDB(this);

        tvBirthdate = findViewById(R.id.tvBirthdate);
        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.editetEmail);
        etPhone = findViewById(R.id.editetPhone);
        etPassword = findViewById(R.id.etPassword);
        spGender = findViewById(R.id.editspGender);

        tvBirthdate.setOnClickListener(v -> showDatePicker());
        Button registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String gender = spGender.getSelectedItem().toString();
            String birthdate = tvBirthdate.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!isValidUsername(username)) {
                Toast.makeText(this, "Username must contain only letters and numbers.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkuser(username)) {
                Toast.makeText(this, "Username already exists. Choose another one.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidFullName(name)) {
                Toast.makeText(this, "Full name must contain only letters.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must contain at least one uppercase,one number,one special char", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email format. Please enter a valid email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhone(phone)) {
                Toast.makeText(this, "Phone number must be 11 digits and start with '01'.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidAge(birthdate)) {
                Toast.makeText(this, "Age must be between 8 and 100 years.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hash the password
            String hashedPassword = hashPassword(password);

            // Insert user data
            boolean isInserted = db.InsertUserData(username, name, email, phone, gender, birthdate, hashedPassword);

            if (isInserted) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                resetFields();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Register.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String birthdate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    tvBirthdate.setText(birthdate);
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // Prevent future dates
        datePickerDialog.show();
    }

    // Reset input fields
    private void resetFields() {
        etUsername.setText("");
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
        spGender.setSelection(0);
        tvBirthdate.setText("");
    }

    // Username validation function
    public boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]+$");
    }

    // Full Name Validation Function
    public boolean isValidFullName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    // Password validation function
    public boolean isValidPassword(String password) {
        String passwordPattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&]).{6,}";
        return password.matches(passwordPattern);
    }

    // Email validation function
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Age validation function
    public boolean isValidAge(String birthdate) {
        String[] birthdateParts = birthdate.split("/");
        if (birthdateParts.length != 3) {
            return false;
        }

        int day = Integer.parseInt(birthdateParts[0]);
        int month = Integer.parseInt(birthdateParts[1]) - 1;
        int year = Integer.parseInt(birthdateParts[2]);

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.set(year, month, day);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }
        return age >= 8 && age <= 100;
    }

    // Phone number validation function
    public boolean isValidPhone(String phone) {
        return phone.length() == 11 && phone.startsWith("01");
    }

    // Hash password with SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
