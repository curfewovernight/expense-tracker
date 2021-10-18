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

public class UpdateWalletActivity extends AppCompatActivity {

    public static final String KEY_NAME = "NAME";
    // references to edit texts and other fields in layout
    EditText editText_Wallet;
    Toolbar toolbar;
    View discard_view;
    View delete_view;
    WalletModel walletModel;
    int WalletID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wallet);

        editText_Wallet = findViewById(R.id.editText_WalletName_Update);
        discard_view = findViewById(R.id.wallet_discard);
        delete_view = findViewById(R.id.wallet_delete);

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
                DataBaseHelper dataBaseHelper = new DataBaseHelper(UpdateWalletActivity.this);

                boolean success = dataBaseHelper.deleteWalletCat(WalletID);

                // update recyclerview
                Intent intent = new Intent();
                intent.putExtra(KEY_NAME, "IDK");
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        // get expense id
        Intent intent = getIntent();
        WalletID = intent.getIntExtra("walletID", 0);
        Log.d("TAG", "getIntExtra: " + String.valueOf(WalletID));

        // get the expense details (array list) for that one item
        DataBaseHelper dataBaseHelper = new  DataBaseHelper(this);
        ArrayList<WalletModel> oneWallet = dataBaseHelper.getOneWalletCat(WalletID);

        // set all edit texts and date to selected expense's details
        editText_Wallet.setText(String.valueOf(oneWallet.get(0).getWalletCategory()));


        // collapsing toolbar typeface
        CollapsingToolbarLayout addExpenseCollapsingBar = findViewById(R.id.update_wallet_collapsing_bar);
        addExpenseCollapsingBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        addExpenseCollapsingBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        // toolbar
        toolbar = findViewById(R.id.update_wallet_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_update_expense:

                        String wCategory = editText_Wallet.getText().toString().trim();

                        if (TextUtils.isEmpty(wCategory)) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateWalletActivity.this);
                            builder.setMessage("Please Enter Account Category")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Wallet.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(UpdateWalletActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        else {
                            try {
                                walletModel = new WalletModel(oneWallet.get(0).getId(), wCategory);
                                //Toast.makeText(UpdateExpenseActivity.this, String.valueOf(expensesModel), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                Toast.makeText(UpdateWalletActivity.this, String.valueOf(e) + ": Update failed", Toast.LENGTH_SHORT).show();
                                //expensesModel = new ExpensesModel(-1, 0, "ERROR", "ERROR", localDate);
                            }

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(UpdateWalletActivity.this);

                            boolean success = dataBaseHelper.updateWalletCat(walletModel);

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
        inflater.inflate(R.menu.update_wallet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}