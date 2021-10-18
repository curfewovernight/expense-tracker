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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class AddWalletActivity extends AppCompatActivity {

    public static final String KEY_NAME = "NAME";
    // references to edit texts and other fields in layout
    EditText editText_Wallet;
    Toolbar toolbar;
    View discard_view;
    WalletModel walletModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        editText_Wallet = findViewById(R.id.editText_WalletName);
        discard_view = findViewById(R.id.expense_discard);

        // discard wallet entry setup
        discard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // toolbar
        toolbar = findViewById(R.id.update_wallet_toolbar);

        // collapsing toolbar typeface
        CollapsingToolbarLayout addExpenseCollapsingBar = findViewById(R.id.add_wallet_collapsing_bar);
        addExpenseCollapsingBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        addExpenseCollapsingBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_add_wallet:

                        if (TextUtils.isEmpty(editText_Wallet.getText())) {
                            // perform if edittext is empty
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddWalletActivity.this);
                            builder.setMessage("Please Enter Account Name")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            editText_Wallet.requestFocus();

                                            InputMethodManager imm = (InputMethodManager) getSystemService(AddWalletActivity.this.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                        }
                                    }).setCancelable(false);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else {
                            try {
                                walletModel = new WalletModel(-1, editText_Wallet.getText().toString());
                                //Toast.makeText(AddWalletActivity.this, String.valueOf(walletModel), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                // Toast.makeText(AddExpenseActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                                walletModel = new WalletModel(-1, "ERROR");
                            }

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(AddWalletActivity.this);

                            boolean success = dataBaseHelper.addWalletCat(walletModel);

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
        inflater.inflate(R.menu.add_wallet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}