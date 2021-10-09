package com.example.expensetracker;

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

    private ArrayList<ExpensesModel> dataHolder;
    private OnExpenseListener mOnExpenseListener;

    public ExpensesAdapter(ArrayList<ExpensesModel> dataHolder, OnExpenseListener onExpenseListener) {
        this.dataHolder = dataHolder;
        this.mOnExpenseListener = onExpenseListener;
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

        public ViewHolder(@NonNull View itemView, OnExpenseListener onExpenseListener) {
            super(itemView);

            expenseCategory = itemView.findViewById(R.id.expenses_frag_list_category);
            expenseAmount = itemView.findViewById(R.id.expenses_frag_list_amount);
            walletCategory = itemView.findViewById(R.id.expenses_frag_list_wallet_category);

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
