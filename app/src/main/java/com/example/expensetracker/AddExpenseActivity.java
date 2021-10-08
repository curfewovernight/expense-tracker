package com.example.expensetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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
    TextView textView_date;
    LocalDateTime localDateTime;
    LocalDate localDate = LocalDate.now();
    Toolbar toolbar;
    View view_date;
    View discard_view;
    ExpensesModel expensesModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editText_Amount = findViewById(R.id.editText_Amount);
        editText_Category = findViewById(R.id.editText_Category);
        editText_Wallet = findViewById(R.id.editText_Wallet);
        textView_date = findViewById(R.id.textView_date);
        view_date = findViewById(R.id.view_date);
        discard_view = findViewById(R.id.expense_discard);


        // discard expense entry setup
        discard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // toolbar
        toolbar = findViewById(R.id.add_expense_toolbar);

        // collapsing toolbar typeface
        CollapsingToolbarLayout addExpenseCollapsingBar = findViewById(R.id.add_expense_collapsing_bar);
        addExpenseCollapsingBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        addExpenseCollapsingBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // set textView_date to today's date by default
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL, yyyy");
        String formattedString = localDate.format(formatter);
        textView_date.setText(formattedString);

        // material date picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker<Long> materialDatePicker = builder.build();

        view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_date.setEnabled(false);
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPositiveButtonClick(Long selection) {
                localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), ZoneId.systemDefault());
                localDate = localDateTime.toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL, yyyy");
                String formattedString = localDate.format(formatter);


                textView_date.setText(formattedString);
            }
        });

        materialDatePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                view_date.setEnabled(true);
            }
        });

//        btn_add_Expense.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View view) {
//
//                ExpensesModel expensesModel;
//
//                try {
//                    float amount = Float.parseFloat(String.valueOf(editText_Amount.getText()));
//                    expensesModel = new ExpensesModel(-1, amount, editText_Category.getText().toString(), editText_Wallet.getText().toString(), localDate);
//                    // Toast.makeText(AddExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception e) {
//                    // Toast.makeText(AddExpenseActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
//                    expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
//                }
//
//                DataBaseHelper dataBaseHelper = new DataBaseHelper(AddExpenseActivity.this);
//
//                boolean success = dataBaseHelper.addOne(expensesModel);
//
//                Toast.makeText(AddExpenseActivity.this, "Successful?" + success, Toast.LENGTH_SHORT);
//
//                // close this activity
//                // finish();
//            }
//        });

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_add_expense:

                        try {
                            float amount = Float.parseFloat(String.valueOf(editText_Amount.getText()));
                            expensesModel = new ExpensesModel(-1, amount, editText_Category.getText().toString(), editText_Wallet.getText().toString(), localDate);
                            Toast.makeText(AddExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            // Toast.makeText(AddExpenseActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                            expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
                        }

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(AddExpenseActivity.this);

                        boolean success = dataBaseHelper.addOne(expensesModel);

                        Log.d("TAG", String.valueOf(success));

                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_expenses_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}