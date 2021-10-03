package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddExpenseActivity extends AppCompatActivity {

    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    Button btn_add_Expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editText_Amount = findViewById(R.id.editText_amount);
        editText_Category = findViewById(R.id.editText_category);
        btn_add_Expense = findViewById(R.id.btn_add_expense);

//        editText_Amount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(AddExpenseActivity.this, "add button", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}