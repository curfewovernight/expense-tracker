package com.example.expensetracker;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ExpensesAdapter.OnExpenseListener {

    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ExpensesAdapter adapter;
    private ArrayList<ExpensesModel> mExpenses;
    LineChart lineChart;
    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    LineData lineData;
    View view;
    public static final String SHARED_PREF = "sharedPrefs";
    public static final String CURRENCY_PREF = "CURRENCY_PREF";
    private String loadedCurrency;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        view = inflater.inflate(R.layout.fragment_home1, container, false);


        loadData();


        updateRecyclerView(view);

        // summary graph of one month

        Utils.init(getActivity());

        // visual

        lineChart = view.findViewById(R.id.home_lineChart);
        lineChart.setNoDataText("No Enough Data");
        lineChart.setNoDataTextColor(Color.RED);
        //lineChart.setExtraOffsets(3.5f, 0.5f, 0.5f, 0.5f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        //xAxis.setDrawGridLines(true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setValueFormatter(new MyAxisValueFormatter());
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true); // Required to enable granularity



        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        lineChart.setDescription(null);

        Typeface inter = Typeface.createFromAsset(getContext().getAssets(), "font/inter_regular.otf");
        lineChart.setNoDataTextTypeface(inter);
        lineDataSet.setValues(dataBaseHelper.getChartValues());
        //lineDataSet.setLabel("Dataset 1");
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setColor(Color.rgb(52, 199, 88));
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        dataSets.clear();
        dataSets.add(lineDataSet);

        lineData = new LineData(dataSets);

        lineChart.clear();
        // if more than 10 entries insert data into chart
        if (dataBaseHelper.getExpenseCount() > 4) {
            lineChart.setData(lineData);
        }
        lineChart.invalidate();

        // Inflate the layout for this fragment
        return view;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        loadedCurrency = sharedPreferences.getString(CURRENCY_PREF, "₹");
    }

    class MyAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            String yAmount = String.valueOf(value) + " " + loadedCurrency + "   ";

            return yAmount;
        }
    }

    public void updateRecyclerView(View view) {
        dataBaseHelper = new DataBaseHelper(getActivity());

        mExpenses = dataBaseHelper.getOutliers();

        loadData();

        adapter = new ExpensesAdapter(mExpenses, this, loadedCurrency);

        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // graph copy pasted
        // visual

        lineChart = view.findViewById(R.id.home_lineChart);
        lineChart.setNoDataText("No Enough Data");
        lineChart.setNoDataTextColor(Color.RED);
        //lineChart.setExtraOffsets(3.5f, 0.5f, 0.5f, 0.5f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        //xAxis.setDrawGridLines(true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setValueFormatter(new MyAxisValueFormatter());
        yAxisLeft.setGranularity(1.0f);
        yAxisLeft.setGranularityEnabled(true); // Required to enable granularity



        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        lineChart.setDescription(null);

        Typeface inter = Typeface.createFromAsset(getContext().getAssets(), "font/inter_regular.otf");
        lineChart.setNoDataTextTypeface(inter);
        lineDataSet.setValues(dataBaseHelper.getChartValues());
        //lineDataSet.setLabel("Dataset 1");
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setColor(Color.rgb(52, 199, 88));
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        //lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        dataSets.clear();
        dataSets.add(lineDataSet);

        lineData = new LineData(dataSets);

        lineChart.clear();
        // if more than 10 entries insert data into chart
        if (dataBaseHelper.getExpenseCount() > 4) {
            lineChart.setData(lineData);
        }
        lineChart.invalidate();

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