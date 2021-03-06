package com.example.expensetracker;

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

import androidx.appcompat.app.AlertDialog;
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
import java.util.List;

public class UpdateExpenseActivity extends AppCompatActivity {

    public static final String KEY_NAME = "NAME";
    // references to edit texts and other fields in layout
    EditText editText_Amount;
    EditText editText_Category;
    TextView textView_Account;
    TextView textView_date;
    LocalDateTime localDateTime;
    LocalDate localDate;
    Toolbar toolbar;
    View view_account;
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
        textView_Account = findViewById(R.id.editText_Wallet_Update);
        textView_date = findViewById(R.id.textView_date_Update);
        view_account = findViewById(R.id.view_account);
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

                // update recyclerview
                Intent intent = new Intent();
                intent.putExtra(KEY_NAME, "IDK");
                setResult(RESULT_OK, intent);

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
        textView_Account.setText(String.valueOf(oneExpense.get(0).getWalletCategory()));
        localDate = oneExpense.get(0).getDateOfExpense();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL, yyyy");
        String formattedString = localDate.format(formatter);
        textView_date.setText(formattedString);

        // collapsing toolbar typeface
        CollapsingToolbarLayout addExpenseCollapsingBar = findViewById(R.id.add_expense_collapsing_bar);
        addExpenseCollapsingBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        addExpenseCollapsingBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // expense account on click
        view_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> accounts = dataBaseHelper.getEveryAccountList();

                if (accounts.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateExpenseActivity.this);
                    builder.setTitle("No available accounts");
                    builder.setMessage("Please add an account");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                }
                else {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(UpdateExpenseActivity.this);
                    builderSingle.setTitle("Select an account");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UpdateExpenseActivity.this, android.R.layout.simple_list_item_1, accounts);

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

        // toolbar
        toolbar = findViewById(R.id.update_expense_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_update_expense:

                        String Amount = editText_Amount.getText().toString().trim();
                        String eCategory = editText_Category.getText().toString().trim();
                        String wCategory = textView_Account.getText().toString().trim();

                        if (TextUtils.isEmpty(Amount)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateExpenseActivity.this);
                            builder.setMessage("Please Enter Amount")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Amount.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(UpdateExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if (TextUtils.isEmpty(eCategory)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateExpenseActivity.this);
                            builder.setMessage("Please Enter Expense Category")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Category.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(UpdateExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else if (TextUtils.isEmpty(wCategory)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateExpenseActivity.this);
                            builder.setMessage("Please Enter Account Category")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            textView_Account.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(UpdateExpenseActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else {
                            try {
                                float amount = Float.parseFloat(Amount);
                                expensesModel = new ExpensesModel(oneExpense.get(0).getId(), amount, eCategory, wCategory, localDate);
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
        inflater.inflate(R.menu.update_expenses_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}