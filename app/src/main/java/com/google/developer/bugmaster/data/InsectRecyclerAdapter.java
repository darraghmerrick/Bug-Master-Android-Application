package com.google.developer.bugmaster.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.utils.QuizUtils;
import com.google.developer.bugmaster.views.DangerLevelView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    public interface InsectAdapterOnClickHandler {
        void onClick(Insect symbol);
    }

    private Cursor mCursor;
    private Context context;
    private final InsectAdapterOnClickHandler clickHandler;

    public InsectRecyclerAdapter(Context context, InsectAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    public void setCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    /* ViewHolder for each insect item */
    class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Get a reference to MainActivity views

        @Nullable
        @BindView(R.id.friendly_name_main)
        TextView friendlyName;

        @Nullable
        @BindView(R.id.scientific_name_main)
        TextView scientificName;

        @Nullable
        @BindView(R.id.danger_level_main)
        DangerLevelView dangerLevel;


        InsectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //An insect was clicked
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(getItem(adapterPosition));
        }
    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.activity_main_list_item, parent, false);
        return new InsectHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        //Inflate Main Activity views with db data
        mCursor.moveToPosition(position);
        if (holder.friendlyName != null) {
            holder.friendlyName.setText(mCursor.getString(BugsDbContract.InsectContract.POSITION_FRIENDLY_NAME));
        }
        if (holder.scientificName != null) {
            holder.scientificName.setText(mCursor.getString(BugsDbContract.InsectContract.POSITION_SCIENTIFIC_NAME));
        }
        if (holder.dangerLevel != null) {
            holder.dangerLevel.setText(Integer.toString(mCursor.getInt(BugsDbContract.InsectContract.POSITION_DANGER_LEVEL)));
        }
        holder.dangerLevel.setDangerLevel(mCursor.getInt(BugsDbContract.InsectContract.POSITION_DANGER_LEVEL));
    }

    @Override

    public int getItemCount() {
        //reset Column count
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
        }
        return count;
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Insect} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    private Insect getItem(int position) {
        //Check that position clicked was within the adapter's range
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    public ArrayList<Insect> getInsectsList() {
        ArrayList<Insect> list = new ArrayList<>();
        mCursor.moveToFirst();
        list.add(new Insect(mCursor));
        while (mCursor.moveToNext()) {
            list.add(new Insect(mCursor));
        }
        list = QuizUtils.shuffleArrayList(list);
        return list;
    }
}
