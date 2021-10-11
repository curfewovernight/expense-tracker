package com.example.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UpdateExpenseActivity extends AppCompatActivity {

    public static final String KEY_NAME = "NAME";
    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    EditText editText_Wallet;
    TextView textView_date;
    LocalDateTime localDateTime;
    LocalDate localDate;
    Toolbar toolbar;
    View view_date;
    View discard_view;
    View delete_view;
    ExpensesModel expensesModel;
    int ExpenseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);

        editText_Amount = findViewById(R.id.editText_Amount_Update);
        editText_Category = findViewById(R.id.editText_Category_Update);
        editText_Wallet = findViewById(R.id.editText_Wallet_Update);
        textView_date = findViewById(R.id.textView_date_Update);
        view_date = findViewById(R.id.view_date_Update);
        discard_view = findViewById(R.id.expense_discard);
        delete_view = findViewById(R.id.expense_delete);

        // discard expense edits button
        discard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // delete expense entry (row) button
        delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(UpdateExpenseActivity.this);

                boolean success = dataBaseHelper.deleteOne(ExpenseID);
                finish();
            }
        });

        // get expense id
        Intent intent = getIntent();
        ExpenseID = intent.getIntExtra("expenseID", 0);
        Log.d("TAG", "getIntExtra: " + String.valueOf(ExpenseID));

        // get the expense details (array list) for that one item
        DataBaseHelper dataBaseHelper = new  DataBaseHelper(this);
        ArrayList<ExpensesModel> oneExpense = dataBaseHelper.getOne(ExpenseID);

        // set all edit texts and date to selected expense's details
        editText_Amount.setText(String.valueOf(oneExpense.get(0).getAmount()));
        editText_Category.setText(String.valueOf(oneExpense.get(0).getExpenseCategory()));
        editText_Wallet.setText(String.valueOf(oneExpense.get(0).getWalletCategory()));
        localDate = oneExpense.get(0).getDateOfExpense();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL, yyyy");
        String formattedString = localDate.format(formatter);
        textView_date.setText(formattedString);

        // collapsing toolbar typeface
        CollapsingToolbarLayout addExpenseCollapsingBar = findViewById(R.id.add_expense_collapsing_bar);
        addExpenseCollapsingBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        addExpenseCollapsingBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

//        // set textView_date to today's date by default
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL, yyyy");
//        String formattedString = localDate.format(formatter);
//        textView_date.setText(formattedString);

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

        // toolbar
        toolbar = findViewById(R.id.update_expense_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_update_expense:

                        try {
                            float amount = Float.parseFloat(String.valueOf(editText_Amount.getText()));
                            expensesModel = new ExpensesModel(oneExpense.get(0).getId(), amount, editText_Category.getText().toString(), editText_Wallet.getText().toString(), localDate);
                            //Toast.makeText(UpdateExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(UpdateExpenseActivity.this, String.valueOf(e) + ": Update failed", Toast.LENGTH_SHORT).show();
                            //expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
                        }

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(UpdateExpenseActivity.this);

                        boolean success = dataBaseHelper.updateOne(expensesModel);

                        Log.d("TAG", String.valueOf(success));

                        // update recyclerview
                        Intent intent = new Intent();
                        intent.putExtra(KEY_NAME, "IDK");
                        setResult(RESULT_OK, intent);

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
        inflater.inflate(R.menu.update_expenses_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}