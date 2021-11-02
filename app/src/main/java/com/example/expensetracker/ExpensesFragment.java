package com.example.expensetracker;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpensesFragment extends Fragment implements ExpensesAdapter.OnExpenseListener {

    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ExpensesAdapter adapter;
    View view;
    private ArrayList<ExpensesModel> mExpenses;
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String CURRENCY_PREF = "CURRENCY_PREF";
    private String loadedCurrency;
    static final String rupee = "₹ Indian Rupee";
    static final String usd = "$ United States Dollar";
    TextView symbolTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpensesFragment newInstance(String param1, String param2) {
        ExpensesFragment fragment = new ExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_expenses, container, false);

        // collapsing toolbar typeface
        CollapsingToolbarLayout expenseCollapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        expenseCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        expenseCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        // toolbar add icon
        Toolbar ExpenseFragmentToolBar = view.findViewById(R.id.toolbar);
        ExpenseFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_expense_action:
                        Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                        addActivityResultLauncher.launch(intent);
                        // getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);
                        return true;
                    default:
                        return false;
                }
            }
        });

        updateRecyclerView(view);

        return view;
    }

    public String loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        loadedCurrency = sharedPreferences.getString(CURRENCY_PREF, "₹");
        Log.d("TAG", loadedCurrency);
        if (loadedCurrency.contains("₹")) {
            return rupee;
        }
        else if (loadedCurrency.contains("$")) {
            return usd;
        }
        else {
            return "error";
        }
    }

    public void updateRecyclerView(View view) {
        dataBaseHelper = new DataBaseHelper(getActivity());

        mExpenses = dataBaseHelper.getEveryone();

        loadData();

        adapter = new ExpensesAdapter(mExpenses, this, loadedCurrency);

        recyclerView = view.findViewById(R.id.expense_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    ActivityResultLauncher<Intent> addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getStringExtra(UpdateExpenseActivity.KEY_NAME) != null) {
                            updateRecyclerView(view);
                        }
                    }
                }
            }
    );

    @Override
    public void onExpenseClick(int expenseItemID) {
        Intent intent = new Intent(getActivity(), UpdateExpenseActivity.class);
        intent.putExtra("expenseID", expenseItemID);
        addActivityResultLauncher.launch(intent);
    }
}