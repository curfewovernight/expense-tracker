package com.example.expensetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    TextView textView_currency;
    View view;
    View currency_view;
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String CURRENCY_PREF = "CURRENCY_PREF";
    private String loadedCurrency;
    static final String rupee = "₹ Indian Rupee";
    static final String usd = "$ United States Dollar";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        textView_currency = view.findViewById(R.id.textView_Currency);
        currency_view = view.findViewById(R.id.currency_view);

        // collapsing toolbar typeface
        CollapsingToolbarLayout expenseCollapsingToolbar = view.findViewById(R.id.settings_collapsing_bar);
        expenseCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        expenseCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        loadData();

        if (loadedCurrency.contains("₹")) {
            textView_currency.setText(rupee);
        }
        else if (loadedCurrency.contains("$")) {
            textView_currency.setText(usd);
        }

        //textView_currency.shared

        currency_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Select an account");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add(rupee);
                arrayAdapter.add(usd);

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
                        if (which == 0) {
                            saveData("₹");
                            textView_currency.setText(loadData());
                        }
                        else if (which == 1) {
                            saveData("$");
                            textView_currency.setText(loadData());
                        }
                    }
                });

                builderSingle.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void saveData(String symbol) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(CURRENCY_PREF, symbol);
        editor.commit();
    }

    public String loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        loadedCurrency = sharedPreferences.getString(CURRENCY_PREF, "₹");
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
}