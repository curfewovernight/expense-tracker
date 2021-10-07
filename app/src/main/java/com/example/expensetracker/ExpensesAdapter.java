package com.example.expensetracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    ArrayList<ExpensesModel> dataHolder;

    public ExpensesAdapter(ArrayList<ExpensesModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView expenseCategory;
        TextView expenseAmount;
        TextView walletCategory;
        //ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseCategory = itemView.findViewById(R.id.expenses_frag_list_category);
            expenseAmount = itemView.findViewById(R.id.expenses_frag_list_amount);
            walletCategory = itemView.findViewById(R.id.expenses_frag_list_wallet_category);
            //parentLayout = itemView.findViewById(R.id.expense_constraint_layout);
        }
    }
}
