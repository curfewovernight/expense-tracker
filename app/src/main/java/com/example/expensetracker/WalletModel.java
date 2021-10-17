package com.example.expensetracker;

public class WalletModel {

    private int id;
    private String walletCategory;

    // constructors

    public WalletModel(int id, String walletCategory) {
        this.id = id;
        this.walletCategory = walletCategory;
    }

    // toString is necessary for printing contents of class object

    @Override
    public String toString() {
        return "WalletModel{" +
                "id=" + id +
                ", walletCategory='" + walletCategory + '\'' +
                '}';
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWalletCategory() {
        return walletCategory;
    }

    public void setWalletCategory(String walletCategory) {
        this.walletCategory = walletCategory;
    }
}
