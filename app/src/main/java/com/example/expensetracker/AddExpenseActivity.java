package com.example.expensetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AddExpenseActivity extends AppCompatActivity {

    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    EditText editText_Wallet;
    Button btn_add_Expense;
    TextView textView_date;
    LocalDateTime localDateTime;
    LocalDate localDate;

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
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPositiveButtonClick(Long selection) {
                localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault());
                localDate = localDateTime.toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String formattedString = localDate.format(formatter);


                textView_date.setText("Selected Date: " + formattedString);
            }
        });

        btn_add_Expense.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                ExpensesModel expensesModel;

                try {
                    float amount = Float.parseFloat(String.valueOf(editText_Amount.getText()));
                    expensesModel = new ExpensesModel(-1, amount, editText_Category.getText().toString(), editText_Wallet.getText().toString(), localDate);
                    // Toast.makeText(AddExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    // Toast.makeText(AddExpenseActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(AddExpenseActivity.this);

                boolean success = dataBaseHelper.addOne(expensesModel);

                Toast.makeText(AddExpenseActivity.this, "Successful?" + success, Toast.LENGTH_SHORT);
            }
        });
    }
}