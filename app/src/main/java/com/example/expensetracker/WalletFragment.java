package com.example.expensetracker;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment implements WalletAdapter.OnWalletListener {

    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    WalletAdapter adapter;
    View view;
    private ArrayList<WalletModel> mWallet;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoneySourceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
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

        view = inflater.inflate(R.layout.fragment_money_source, container, false);

        // collapsing toolbar typeface
        CollapsingToolbarLayout walletCollapsingToolbar = view.findViewById(R.id.wallet_collapsing_toolbar);
        walletCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        walletCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // toolbar add icon
        Toolbar ExpenseFragmentToolBar = view.findViewById(R.id.wallet_toolbar);
        ExpenseFragmentToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_wallet_action:
                        Intent intent = new Intent(getActivity(), AddWalletActivity.class);
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

    public void updateRecyclerView(View view) {
        dataBaseHelper = new DataBaseHelper(getActivity());

        mWallet = dataBaseHelper.getEveryWalletCat();

        adapter = new WalletAdapter(mWallet, this);

        recyclerView = view.findViewById(R.id.wallet_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.addItemDecoration(dividerItemDecoration);
    }

    ActivityResultLauncher<Intent> addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null && result.getData().getStringExtra(UpdateWalletActivity.KEY_NAME) != null) {
                            updateRecyclerView(view);
                        }
                    }
                }
            }
    );

    @Override
    public void onWalletClick(int walletItemID) {
        Intent intent = new Intent(getActivity(), UpdateWalletActivity.class);
        intent.putExtra("walletID", walletItemID);
        addActivityResultLauncher.launch(intent);
    }
}