package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String EXPENSES_TABLE = "EXPENSES_TABLE";
    public static final String COLUMN_EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
    public static final String COLUMN_EXPENSE_CAT = "EXPENSE_CAT";
    public static final String COLUMN_WALLET_CAT = "WALLET_CAT";
    public static final String COLUMN_DATE_OF_EXPENSE = "DATE_OF_EXPENSE";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "expenses.db", null, 1);
    }

    // Called first time u try to access database. Should have code to create database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + EXPENSES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_EXPENSE_AMOUNT + " REAL, " + COLUMN_EXPENSE_CAT + " TEXT, " + COLUMN_WALLET_CAT + " TEXT, " + COLUMN_DATE_OF_EXPENSE + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    // this is called if database version number changes. prevents aps from breaking when u change database design
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

    public boolean deleteOne(ExpensesModel expensesModel) {
        // find expense model in database, if found delete it and return true
        // if not found return false

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + EXPENSES_TABLE + " WHERE" + COLUMN_ID + " = " + expensesModel.getId();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<ExpensesModel> getEveryone() {
        ArrayList<ExpensesModel> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + EXPENSES_TABLE + " ORDER BY " + COLUMN_ID + " DESC ";

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
}
