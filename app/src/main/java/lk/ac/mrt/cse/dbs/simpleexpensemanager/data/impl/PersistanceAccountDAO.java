package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelper;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistanceAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;


    public PersistanceAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.ACC_TABLE_NAME, new String[] {DatabaseHelper.COL_ACC_NO}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Adding account to list
                accountNumList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> ListAccount = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.ACC_TABLE_NAME, null, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                // Adding account to list
                ListAccount.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return ListAccount;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.ACC_TABLE_NAME, null, DatabaseHelper.COL_ACC_NO + "=?",
                new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "This " + accountNo + " is not valid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(DatabaseHelper.COL_ACC_NO, account.getAccountNo()); // Account No
        contentvalues.put("Bank_name", account.getBankName()); // Bank Name
        contentvalues.put("Account_holder", account.getAccountHolderName()); // Holder Name
        contentvalues.put("Initial_amount", account.getBalance()); // Balance

        // Inserting Row
        db.insert(DatabaseHelper.ACC_TABLE_NAME, null, contentvalues);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.ACC_TABLE_NAME, DatabaseHelper.COL_ACC_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }


    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put("Initial_amount", account.getBalance() - amount);
                break;
            case INCOME:
                values.put("Initial_amount", account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(DatabaseHelper.ACC_TABLE_NAME, values, DatabaseHelper.COL_ACC_NO + " = ?",
                new String[] { accountNo });
    }
}
