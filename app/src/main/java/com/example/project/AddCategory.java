package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCategory extends AppCompatActivity {

    EditText cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        ProductDB db = new ProductDB(this);

        cat = findViewById(R.id.cat);


        Button add = (Button)findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String category = cat.getText().toString().trim();

                boolean isInserted = db.insertNewCategory(category);

                if (isInserted) {
                    Toast.makeText(AddCategory.this, "Insertion Successful!", Toast.LENGTH_SHORT).show();
                    resetFields();
                    Intent intent = new Intent(AddCategory.this, AdminCategories.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddCategory.this, "Category Already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void resetFields() {
        cat.setText("");
    }

}
