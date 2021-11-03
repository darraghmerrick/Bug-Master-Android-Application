package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.utils.BugsDbUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.COLUMN_CLASSIFICATION;
import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.COLUMN_DANGER_LEVEL;
import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME;
import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.COLUMN_IMAGE_ASSET;
import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.COLUMN_SCIENTIFIC_NAME;
import static com.google.developer.bugmaster.data.BugsDbContract.InsectContract.TABLE_NAME;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 * This is only run on the first running of the app.
 * After that the database persists and does not need to be created each time.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    //Log Statement
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;

    //Used to read data from res/ and assets/
    private Resources mResources;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DONE: Create and fill the database
        String builder = "CREATE TABLE " + BugsDbContract.InsectContract.TABLE_NAME + " ("
                + BugsDbContract.InsectContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME + " TEXT NOT NULL, "
                + BugsDbContract.InsectContract.COLUMN_SCIENTIFIC_NAME + " TEXT NOT NULL, "
                + BugsDbContract.InsectContract.COLUMN_CLASSIFICATION + " TEXT NOT NULL, "
                + BugsDbContract.InsectContract.COLUMN_IMAGE_ASSET + " TEXT NOT NULL, "
                + BugsDbContract.InsectContract.COLUMN_DANGER_LEVEL + " INTEGER NOT NULL,"
                + "UNIQUE (" + BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME + ") ON CONFLICT REPLACE);";

        db.execSQL(builder);
        try {
            //The readInsectsFromResources method will read the JSON table, parse the relevant fields
            //and populate the columns for each column on each row (insect)
            readInsectsFromResources(db);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            //The database is built here line by line
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();

        //TODO: Parse JSON data and insert into the provided database instance

        //Create a JSONArray of JSONObjects of unParsed insect Data
        JSONObject rawInsectsObject = new JSONObject(rawJson);
        JSONArray rawInsectsArray = rawInsectsObject.getJSONArray(BugsDbUtils.UTILS_LIST);
        //ContentValues are used to store a set of values that the ContentResolver can process.
        ContentValues[] parsedInsectsData = new ContentValues[rawInsectsArray.length()];

        for (int i = 0; i < rawInsectsArray.length(); i++) {
            JSONObject insectData = rawInsectsArray.getJSONObject(i);
            //Use the BugsDbUtils class to generate keys, to pull values from Insect.class
            Insect insect = new Insect(
                    insectData.getString(BugsDbUtils.UTILS_FRIENDLY_NAME),
                    insectData.getString(BugsDbUtils.UTILS_SCIENTIFIC_NAME),
                    insectData.getString(BugsDbUtils.UTILS_CLASSIFICATION),
                    insectData.getString(BugsDbUtils.UTILS_IMAGE_ASSET),
                    insectData.getInt(BugsDbUtils.UTILS_DANGER_LEVEL)
            );
            // The result is a single parsed insect with the relevant data fields
            ContentValues parsedSingleInsectData = new ContentValues();
            parsedSingleInsectData.put(COLUMN_FRIENDLY_NAME, insect.name);
            parsedSingleInsectData.put(COLUMN_SCIENTIFIC_NAME, insect.scientificName);
            parsedSingleInsectData.put(COLUMN_CLASSIFICATION, insect.classification);
            parsedSingleInsectData.put(COLUMN_IMAGE_ASSET, insect.imageAsset);
            parsedSingleInsectData.put(COLUMN_DANGER_LEVEL, insect.dangerLevel);

            //Each single parsed insect is added to the ContentValues array.
            //The ContentValues are added to the insects database by the content provider
            parsedInsectsData[i] = parsedSingleInsectData;
        }

        if (parsedInsectsData.length != 0) {
            db.beginTransaction();
            int rowsInserted;
            rowsInserted = 0;
            try {
                for (ContentValues contentValues : parsedInsectsData) {
                    long _id = db.insert(BugsDbContract.InsectContract.TABLE_NAME,
                            null, contentValues);
                    if (_id != -1) rowsInserted++;
                }
                db.setTransactionSuccessful();
                Log.i(TAG,rowsInserted + " insect table rows were populated with data");

            } finally {
                db.endTransaction();
            }
        }
    }
}
