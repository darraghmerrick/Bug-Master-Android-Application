package com.google.developer.bugmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.BugsDbContract;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;
import com.google.developer.bugmaster.utils.QuizUtils;

import java.util.ArrayList;
import java.util.Random;

import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, InsectRecyclerAdapter.InsectAdapterOnClickHandler {

    public static final String PREFERENCES = "prefs";
    public static final String SORT_ORDER = "sort_order";
    public static String ORIGINAL_ORDER = BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME;

    private String sortOrder = ORIGINAL_ORDER;
    private InsectRecyclerAdapter mAdapter;
    private static final String RECYCLER_POSITION = "recycler_position";

    private RecyclerView mRecyclerView;
    private DatabaseManager databaseManager;
    private int mPosition = RecyclerView.NO_POSITION;
    private LinearLayoutManager layoutManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mPosition = savedInstanceState.getInt(RECYCLER_POSITION);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new InsectRecyclerAdapter(this, this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        databaseManager = DatabaseManager.getInstance(this);

        loadSharedPreferences();
        loadData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void loadSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        sortOrder = sharedPreferences.getString(SORT_ORDER, sortOrder);
    }

    private void loadData() {
        Cursor cursor = databaseManager.queryAllInsects(sortOrder);
        //Set the cursor to the current row in the adapter, using the setCursor method.
        mAdapter.setCursor(cursor);

        mAdapter.notifyDataSetChanged();
        //Start at Position 0
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void saveSortOrder() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORT_ORDER, sortOrder);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                if (sortOrder.equals(BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME))
                    sortOrder = BugsDbContract.InsectContract.COLUMN_DANGER_LEVEL;
                else
                    sortOrder = BugsDbContract.InsectContract.COLUMN_FRIENDLY_NAME;

                saveSortOrder();
                loadData();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        Intent intentToStartQuizActivity = new Intent(this, QuizActivity.class);
        ArrayList<Insect> insects = mAdapter.getInsectsList();
        //QuizUtils.shrinkTo removes used questions from the list of questions
        insects = QuizUtils.shrinkTo(insects, QuizActivity.ANSWER_COUNT);
        Insect answer = insects.get(new Random(System.nanoTime()).nextInt(insects.size()));
        intentToStartQuizActivity.putExtra(EXTRA_INSECTS, insects);
        intentToStartQuizActivity.putExtra(EXTRA_ANSWER, answer);
        startActivity(intentToStartQuizActivity);
    }


    @Override
    public void onClick(Insect insect) {
        Intent intentToStartDetailActivity = new Intent(this, InsectDetailsActivity.class);
        //Intent to start detailed insect activity with insect data Extra passed to it.
        intentToStartDetailActivity.putExtra("insectSelected", insect);
        startActivity(intentToStartDetailActivity);
    }

    public void onSaveInstanceState(Bundle savedState) {
        //So that list position is saved on rotation
        super.onSaveInstanceState(savedState);
        mPosition = layoutManager.findFirstVisibleItemPosition();
        savedState.putInt(RECYCLER_POSITION, mPosition);
    }
}
