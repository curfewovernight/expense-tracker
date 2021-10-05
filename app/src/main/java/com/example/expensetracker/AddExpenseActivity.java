package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddExpenseActivity extends AppCompatActivity {

    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    EditText editText_Wallet;
    Button btn_add_Expense;
    TextView textView_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editText_Amount = findViewById(R.id.editText_amount);
        editText_Category = findViewById(R.id.editText_category);
        editText_Wallet = findViewById(R.id.editText_wallet);
        btn_add_Expense = findViewById(R.id.btn_add_expense);
        textView_date = findViewById(R.id.textView_date);

        // material date picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker materialDatePicker = builder.build();

        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        btn_add_Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float amount = Float.parseFloat(String.valueOf(editText_Category.getText()));

                //ExpensesModel expensesModel = new ExpensesModel(-1, amount, editText_Category.getText().toString(), editText_Wallet.getText().toString(), )

                Toast.makeText(AddExpenseActivity.this, "add button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}