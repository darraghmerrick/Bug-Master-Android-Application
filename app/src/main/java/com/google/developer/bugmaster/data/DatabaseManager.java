package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private BugsDbHelper mBugsDbHelper;

    private DatabaseManager(Context context) {
        mBugsDbHelper = new BugsDbHelper(context);
    }

    /**
     * Return a {@link Cursor} that contains every insect in the database.
     *
     * @param sortOrder Optional sort order string for the query, can be null
     * @return {@link Cursor} containing all insect results.
     */
    public Cursor queryAllInsects(String sortOrder) {
        //Readable database is all that is needed, as we are not deleting or adding insects
        SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();

        return db.query(BugsDbContract.InsectContract.TABLE_NAME,
                BugsDbContract.InsectContract.INSECT_COLUMNS_PROJECTION,
                null, null, null, null, sortOrder);
    }

    /**
     * Return a {@link Cursor} that contains a single insect for the given unique id.
     *
     * @param id Unique identifier for the insect record.
     * @return {@link Cursor} containing the insect result.
     */
    public Cursor queryInsectsById(int id) {
        //TODO: Implement the query
        SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();
        return db.query(BugsDbContract.InsectContract.TABLE_NAME,
                //INSECT_COLUMNS_PROJECTION is a String array of all column names
                BugsDbContract.InsectContract.INSECT_COLUMNS_PROJECTION,
                BugsDbContract.InsectContract._ID + " = ?",
                new String[]{Integer.toString(id)},
                null, null, null);
    }
}
