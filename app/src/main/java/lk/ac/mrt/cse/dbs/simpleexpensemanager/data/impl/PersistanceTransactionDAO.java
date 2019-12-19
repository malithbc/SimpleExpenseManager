package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelper;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistanceTransactionDAO implements TransactionDAO {
    private DatabaseHelper databaseHelper;

    public PersistanceTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper= databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(DatabaseHelper.COL_ACC_NO, accountNo); // Account No
        contentvalues.put(DatabaseHelper.COL_Type, expenseType.name()); // Bank Name
        contentvalues.put("Amount", amount); // Holder Name
        contentvalues.put("Date", new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date)); // Holder Name

        // Inserting Row
        db.insert(DatabaseHelper.TRANS_TABLE_NAME, null, contentvalues);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        Log.d("Came Here", contentvalues.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction>  ListTrans = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TRANS_TABLE_NAME, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account to list
                    ListTrans.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return ListTrans;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> PagiTransList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TRANS_TABLE_NAME, null, null, null, null, null, null, limit + "");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    // Adding account to list
                    PagiTransList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return PagiTransList;
    }

}
