package com.example.expensetracker;

import java.time.LocalDate;

public class ExpensesModel {

    private int id;
    private String expenseCategory;
    private float amount;
    private String walletCategory;
    private LocalDate dateOfExpense;

    // constructors

    public ExpensesModel(int id, float amount, String expenseCategory, String walletCategory, LocalDate dateOfExpense) {
        this.id = id;
        this.expenseCategory = expenseCategory;
        this.amount = amount;
        this.walletCategory = walletCategory;
        this.dateOfExpense = dateOfExpense;
    }

    public ExpensesModel() {

    }

    // toString is necessary for printing contents of class object

    @Override
    public String toString() {
        return "DBHelper{" +
                "id=" + id +
                ", expenseCategory='" + expenseCategory + '\'' +
                ", amount=" + amount +
                ", walletCategory='" + walletCategory + '\'' +
                ", dateOfExpense=" + dateOfExpense +
                '}';
    }


    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getWalletCategory() {
        return walletCategory;
    }

    public void setWalletCategory(String walletCategory) {
        this.walletCategory = walletCategory;
    }

    public LocalDate getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(LocalDate dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }
}