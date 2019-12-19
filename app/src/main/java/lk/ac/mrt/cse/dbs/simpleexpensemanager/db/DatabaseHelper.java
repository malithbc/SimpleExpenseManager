package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "170051V";
    public static final String ACC_TABLE_NAME = "Account";
    public static final String TRANS_TABLE_NAME = "Transactions";
    public static final String COL_ID = "ID";
    public static final String COL_Type= "Type";
    public static final String COL_ACC_NO = "Account_no";
    public static final String COL_BANKNAME = "Bank_name";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +ACC_TABLE_NAME +"(ID INTEGER PRIMARY KEY AUTOINCREMENT, Account_no TEXT UNIQUE, Bank_name TEXT, Account_holder TEXT, Initial_amount DECIMAL)");
        db.execSQL("create table " + TRANS_TABLE_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT , Account_no TEXT UNIQUE, Type TEXT, Amount DECIMAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '" + ACC_TABLE_NAME + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TRANS_TABLE_NAME + "'");
        onCreate(db);
    }
    public boolean insertAccount (String Account_no, String Bank_name, String Account_holder, String Initial_amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Account_no", Account_no);
        contentValues.put("Bank_name", Bank_name);
        contentValues.put("Account_holder", Account_holder);
        contentValues.put("Initial_amount", Initial_amount);
        long result = db.insert("Account", null, contentValues);
        if (result == -1){
            return false;
        }
        return true;
    }

    public boolean insertTransaction (String Date, String Account_no, String Type, String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", Date);
        contentValues.put("Account_no", Account_no);
        contentValues.put("Type", Type);
        contentValues.put("Amount", Amount);
        long result = db.insert("Transactions", null, contentValues);
        if (result == -1){
            return false;
        }
        return true;
    }
}
