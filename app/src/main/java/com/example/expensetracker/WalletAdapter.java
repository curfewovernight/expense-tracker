package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private ArrayList<WalletModel> dataHolder;
    private OnWalletListener mOnWalletListener;

    public WalletAdapter(ArrayList<WalletModel> dataHolder, OnWalletListener onWalletListener) {
        this.dataHolder = dataHolder;
        this.mOnWalletListener = onWalletListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_list_item, parent, false);
        return new ViewHolder(view, mOnWalletListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.walletCategory.setText(String.valueOf(dataHolder.get(position).getWalletCategory()));
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnWalletListener onWalletListener;
        TextView walletCategory;

        public ViewHolder(@NonNull View itemView, OnWalletListener onWalletListener) {
            super(itemView);

            walletCategory = itemView.findViewById(R.id.wallet_frag_list_category);

            this.onWalletListener = onWalletListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onWalletListener.onWalletClick(dataHolder.get(getAdapterPosition()).getId());
        }
    }

    public interface OnWalletListener {
        void onWalletClick(int walletItemID);
    }
}
