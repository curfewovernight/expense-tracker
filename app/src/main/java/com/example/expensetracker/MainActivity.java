package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, new HomeFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_expenses:
                        fragment = new ExpensesFragment();
                        break;
                    case R.id.nav_wallet:
                        fragment = new WalletFragment();
                        break;
                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, fragment).commit();

                return true;
            }
        });
    }

    private void openAddExpenseActivity() {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        startActivity(intent);
    }
}