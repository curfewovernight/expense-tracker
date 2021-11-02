package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String EXPENSES_TABLE = "EXPENSES_TABLE";
    public static final String WALLET_TABLE = "WALLET_TABLE";
    public static final String COLUMN_EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
    public static final String COLUMN_EXPENSE_CAT = "EXPENSE_CAT";
    public static final String COLUMN_WALLET_CAT = "WALLET_CAT";
    public static final String COLUMN_DATE_OF_EXPENSE = "DATE_OF_EXPENSE";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "expense_tracker.db", null, 1);
    }

    // Called first time u try to access database. Should have code to create database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createExpensesTable = "CREATE TABLE IF NOT EXISTS " + EXPENSES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_EXPENSE_AMOUNT + " REAL, " + COLUMN_EXPENSE_CAT + " TEXT, " + COLUMN_WALLET_CAT + " TEXT, " + COLUMN_DATE_OF_EXPENSE + " TEXT)";
        String createWalletTable = "CREATE TABLE IF NOT EXISTS " + WALLET_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_WALLET_CAT + " TEXT)";

        sqLiteDatabase.execSQL(createExpensesTable);
        sqLiteDatabase.execSQL(createWalletTable);
    }

    // this is called if database version number changes. prevents apps from breaking when u change database design
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(ExpensesModel expensesModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // convert localdate to ISO 8601 and string
        LocalDate localDate = expensesModel.getDateOfExpense();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");

        String date = localDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(dtf);

        contentValues.put(COLUMN_EXPENSE_AMOUNT, expensesModel.getAmount());
        contentValues.put(COLUMN_EXPENSE_CAT, expensesModel.getExpenseCategory());
        contentValues.put(COLUMN_WALLET_CAT, expensesModel.getWalletCategory());
        contentValues.put(COLUMN_DATE_OF_EXPENSE, date);

        long insert = sqLiteDatabase.insert(EXPENSES_TABLE, null, contentValues);

        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    // add one wallet category
    public boolean addWalletCat(WalletModel walletModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_WALLET_CAT, walletModel.getWalletCategory());

        long insert = sqLiteDatabase.insert(WALLET_TABLE, null, contentValues);

        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean updateOne(ExpensesModel expensesModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // convert localdate to ISO 8601 and string
        LocalDate localDate = expensesModel.getDateOfExpense();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");

        String date = localDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(dtf);

        contentValues.put(COLUMN_EXPENSE_AMOUNT, expensesModel.getAmount());
        contentValues.put(COLUMN_EXPENSE_CAT, expensesModel.getExpenseCategory());
        contentValues.put(COLUMN_WALLET_CAT, expensesModel.getWalletCategory());
        contentValues.put(COLUMN_DATE_OF_EXPENSE, date);

        String id = String.valueOf(expensesModel.getId());

        int insert = sqLiteDatabase.update(EXPENSES_TABLE, contentValues, COLUMN_ID + " = " + id, null);

        if (insert != 1) {
            return false;
        }
        else {
            Log.d("TAG", "Number of rows affected: " + String.valueOf(insert));
            return true;
        }
    }

    // update a wallet category from wallet table
    public boolean updateWalletCat(WalletModel walletModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_WALLET_CAT, walletModel.getWalletCategory());

        String id = String.valueOf(walletModel.getId());

        int insert = sqLiteDatabase.update(WALLET_TABLE, contentValues, COLUMN_ID + " = " + id, null);

        if (insert != 1) {
            return false;
        }
        else {
            Log.d("TAG", "Number of rows affected: " + String.valueOf(insert));
            return true;
        }
    }

    public boolean deleteOne(int expensesID) {
        // find expense model in database, if found delete it and return true
        // if not found return false

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = String.valueOf(expensesID);

        int delete = sqLiteDatabase.delete(EXPENSES_TABLE, COLUMN_ID + " = " + id, null);

        Log.d("TAG", "No. of rows deleted (should not be anything but 1): " + String.valueOf(delete));
        return true;
    }

    // delete one category from wallet table

    public boolean deleteWalletCat(int walletID) {
        // find wallet model in database, if found delete it and return true
        // if not found return false

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = String.valueOf(walletID);

        int delete = sqLiteDatabase.delete(WALLET_TABLE, COLUMN_ID + " = " + id, null);

        Log.d("TAG", "No. of rows deleted (should not be anything but 1): " + String.valueOf(delete));
        return true;
    }

    public ArrayList<ExpensesModel> getEveryone() {
        ArrayList<ExpensesModel> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + EXPENSES_TABLE + " ORDER BY " + COLUMN_DATE_OF_EXPENSE + " DESC ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new expense objects. put them in return list
            do {
                int expenseID = cursor.getInt(0);
                Float expenseAmount = cursor.getFloat(1);
                String expenseCategory = cursor.getString(2);
                String walletCategory = cursor.getString(3);
                String dateOfExpense = cursor.getString(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
                LocalDate localDate = LocalDate.parse(dateOfExpense, dtf);

                ExpensesModel newModel = new ExpensesModel(expenseID, expenseAmount, expenseCategory, walletCategory, localDate);

                returnList.add(newModel);
            } while (cursor.moveToNext());

        }
        else {
            // failure. do not add anything to list
        }

        // close both cursor and the database when done.
        cursor.close();
        db.close();

        return returnList;
    }

    public ArrayList<ExpensesModel> getOutliers() {
        ArrayList<ExpensesModel> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        float average = 0;

        String avgStr = "SELECT AVG(" + COLUMN_EXPENSE_AMOUNT + ") FROM " + EXPENSES_TABLE;

        Cursor avg = db.rawQuery(avgStr, null);
        if (avg.moveToFirst()) {
            average = avg.getInt(0);
            Log.d("TAG", String.valueOf(average));
        }

        // get data from database
        String queryString = "SELECT * FROM " + EXPENSES_TABLE + " WHERE " + COLUMN_EXPENSE_AMOUNT + " > " + average + " ORDER BY " + COLUMN_DATE_OF_EXPENSE + " DESC ";

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new expense objects. put them in return list
            do {
                int expenseID = cursor.getInt(0);
                Float expenseAmount = cursor.getFloat(1);
                String expenseCategory = cursor.getString(2);
                String walletCategory = cursor.getString(3);
                String dateOfExpense = cursor.getString(4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
                LocalDate localDate = LocalDate.parse(dateOfExpense, dtf);

                ExpensesModel newModel = new ExpensesModel(expenseID, expenseAmount, expenseCategory, walletCategory, localDate);

                returnList.add(newModel);
            } while (cursor.moveToNext());
        }
        else {
            // failure. do not add anything to list
        }

        // close both cursor and the database when done.
        cursor.close();
        db.close();

        return returnList;
    }

    public ArrayList<Entry> getChartValues () {
        ArrayList<Entry> returnList = new ArrayList<>();

        String[] columns = {COLUMN_DATE_OF_EXPENSE, COLUMN_EXPENSE_AMOUNT};

        SQLiteDatabase database = getReadableDatabase();

        String queryString = "SELECT DATE_OF_EXPENSE, EXPENSE_AMOUNT FROM " + EXPENSES_TABLE + " ORDER BY " + COLUMN_DATE_OF_EXPENSE + " ASC ";

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String dateOfExpense = cursor.getString(0);
                Float expenseAmount = cursor.getFloat(1);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
                LocalDate localDate = LocalDate.parse(dateOfExpense, dtf);

                int day = localDate.getDayOfMonth();

                returnList.add(new Entry(day, expenseAmount));

            } while (cursor.moveToNext());
        }
        else {
            // fail
        }

        cursor.close();
        database.close();
        Log.d("TAG", String.valueOf(returnList));
        return returnList;
    }

    public ArrayList<WalletModel> getEveryWalletCat() {
        ArrayList<WalletModel> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + WALLET_TABLE + " ORDER BY " + COLUMN_WALLET_CAT + " ASC ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new expense objects. put them in return list

            do {
                int walletID = cursor.getInt(0);
                String walletCategory = cursor.getString(1);

                WalletModel newModel = new WalletModel(walletID, walletCategory);

                returnList.add(newModel);
            } while (cursor.moveToNext());
        }
        else {
            // failure. do not add anything to list
        }

        // close both cursor and the database when done.
        cursor.close();
        db.close();

        return returnList;
    }

    // return list string type of all account categories
    public List<String> getEveryAccountList() {
        List<String> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT " + COLUMN_WALLET_CAT + " FROM " + WALLET_TABLE + " ORDER BY " + COLUMN_WALLET_CAT + " ASC ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new expense objects. put them in return list

            do {
                String walletCategory = cursor.getString(0);

                returnList.add(walletCategory);
                Log.d("TAG", walletCategory);
            } while (cursor.moveToNext());
        }
        else {
            // failure. do not add anything to list
        }

        // close both cursor and the database when done.
        cursor.close();
        db.close();

        Log.d("TAG", String.valueOf(returnList.size()));
        return returnList;
    }

    public ArrayList<ExpensesModel> getOne(int expensesModel) {
        ArrayList<ExpensesModel> returnList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        // get data from database
        String queryString = "SELECT * FROM " + EXPENSES_TABLE + " WHERE " + COLUMN_ID + " = " + expensesModel;

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            int expenseID = cursor.getInt(0);
            Float expenseAmount = cursor.getFloat(1);
            String expenseCategory = cursor.getString(2);
            String walletCategory = cursor.getString(3);
            String dateOfExpense = cursor.getString(4);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");

            LocalDate localDate = LocalDate.parse(dateOfExpense, dtf);

            ExpensesModel newModel = new ExpensesModel(expenseID, expenseAmount, expenseCategory, walletCategory, localDate);

            returnList.add(newModel);
        }
        else {
            // pass empty list and make toast in fragment
        }

        return returnList;
    }

    public ArrayList<WalletModel> getOneWalletCat(int walletID) {
        ArrayList<WalletModel> returnList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        // get data from database
        String queryString = "SELECT * FROM " + WALLET_TABLE + " WHERE " + COLUMN_ID + " = " + walletID;

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            int expenseID = cursor.getInt(0);
            String walletCategory = cursor.getString(1);

            WalletModel newModel = new WalletModel(expenseID, walletCategory);

            returnList.add(newModel);
        }
        else {
            // pass empty list and make toast in fragment
        }

        return returnList;
    }

    public  int getExpenseCount() {
        int returnInt = 0;

        SQLiteDatabase database = this.getReadableDatabase();

        // get data from database
        String queryString = "SELECT COUNT(" + COLUMN_ID + ") FROM " + EXPENSES_TABLE;

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            returnInt = cursor.getInt(0);
            Log.d("TAG", String.valueOf(returnInt));
        }

        return returnInt;
    }

}