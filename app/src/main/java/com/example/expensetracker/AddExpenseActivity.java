package com.example.expensetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    public static final String KEY_NAME = "NAME";
    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    TextView textView_Account;
    TextView textView_date;
    LocalDateTime localDateTime;
    LocalDate localDate = LocalDate.now();
    Toolbar toolbar;
    View view_account;
    View view_date;
    View discard_view;
    ExpensesModel expensesModel;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(AddExpenseActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editText_Amount = findViewById(R.id.editText_Amount);
        editText_Category = findViewById(R.id.editText_Category);
        textView_Account = findViewById(R.id.editText_Wallet);
        textView_date = findViewById(R.id.textView_date);
        view_account = findViewById(R.id.view_account);
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

        // expense account on click
        view_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> accounts = dataBaseHelper.getEveryAccountList();

                if (accounts.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                    builder.setTitle("No available accounts");
                    builder.setMessage("Please add an account");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                }
                else {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddExpenseActivity.this);
                    builderSingle.setTitle("Select an account");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddExpenseActivity.this, android.R.layout.simple_list_item_1, accounts);

                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            textView_Account.setText(strName);
                        }
                    });

                    builderSingle.show();
                    }
                }
        });

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

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_add_expense:

                        String Amount = editText_Amount.getText().toString().trim();
                        String eCategory = editText_Category.getText().toString().trim();
                        String wCategory = textView_Account.getText().toString().trim();

                        if (TextUtils.isEmpty(Amount)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                            builder.setMessage("Please Enter Amount")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Amount.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(AddExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if (TextUtils.isEmpty(eCategory)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                            builder.setMessage("Please Enter Expense Category")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Category.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(AddExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if (TextUtils.isEmpty(wCategory)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                            builder.setMessage("Please select an account")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            textView_Account.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(AddExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else {
                            try {
                                float amount = Float.parseFloat(Amount);
                                expensesModel = new ExpensesModel(-1, amount, eCategory, wCategory, localDate);
                                Toast.makeText(AddExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                // Toast.makeText(AddExpenseActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                                expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
                            }

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(AddExpenseActivity.this);

                            boolean success = dataBaseHelper.addOne(expensesModel);

                            Log.d("TAG", String.valueOf(success));

                            // update recyclerview
                            Intent intent = new Intent();
                            intent.putExtra(KEY_NAME, "IDK");
                            setResult(RESULT_OK, intent);

                            finish();
                        }

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