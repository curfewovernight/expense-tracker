package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private final String s;
    private ArrayList<ExpensesModel> dataHolder;
    private OnExpenseListener mOnExpenseListener;
    Context context;
    private String loadedCurrency;

    public ExpensesAdapter(ArrayList<ExpensesModel> dataHolder, OnExpenseListener onExpenseListener, String s) {
        this.dataHolder = dataHolder;
        this.mOnExpenseListener = onExpenseListener;
        this.s = s;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view, mOnExpenseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.expenseCategory.setText(dataHolder.get(position).getExpenseCategory());
        holder.expenseAmount.setText(String.valueOf(dataHolder.get(position).getAmount()));
        holder.walletCategory.setText(String.valueOf(dataHolder.get(position).getWalletCategory()));

        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        loadedCurrency = sharedPreferences.getString("CURRENCY_PREF", "");
        Log.d("TAGW", loadedCurrency);

        holder.textView_Symbol.setText(s);
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnExpenseListener onExpenseListener;
        TextView expenseCategory;
        TextView expenseAmount;
        TextView walletCategory;
        TextView textView_Symbol;

        public ViewHolder(@NonNull View itemView, OnExpenseListener onExpenseListener) {
            super(itemView);

            expenseCategory = itemView.findViewById(R.id.expenses_frag_list_category);
            expenseAmount = itemView.findViewById(R.id.expenses_frag_list_amount);
            walletCategory = itemView.findViewById(R.id.expenses_frag_list_wallet_category);
            textView_Symbol = itemView.findViewById(R.id.expenses_frag_list_unit);

            //loadData();

            this.onExpenseListener = onExpenseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onExpenseListener.onExpenseClick(dataHolder.get(getAdapterPosition()).getId());
        }
    }

    public interface OnExpenseListener {
        void onExpenseClick(int expenseItemID);
    }
}
