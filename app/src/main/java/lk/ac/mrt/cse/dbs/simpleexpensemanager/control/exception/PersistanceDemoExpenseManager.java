package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelper;

/**
 *
 */
public class PersistanceDemoExpenseManager extends ExpenseManager {
    private DatabaseHelper databaseHelper;

    public PersistanceDemoExpenseManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO PersistanceTransactionDAO = new PersistanceTransactionDAO(databaseHelper);
        setTransactionsDAO(PersistanceTransactionDAO);

        AccountDAO PersistanceAccountDAO = new PersistanceAccountDAO(databaseHelper);
        setAccountsDAO(PersistanceAccountDAO);

        /*** End ***/
    }
}

