package com.google.developer.bugmaster.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

//TODO: This class should be used in the insect list to display danger level
@SuppressLint("AppCompatCustomView")
public class DangerLevelView extends TextView {

    private int dangerLevel = -1;

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDangerLevel(int dangerLevel) {
        //TODO: Update the view appropriately based on the level input
        String[] colours = getResources().getStringArray(R.array.dangerColors);
        getBackground().setColorFilter(Color.parseColor(colours[dangerLevel - 1]),
                PorterDuff.Mode.SRC_ATOP);
        this.dangerLevel = dangerLevel;
    }

    public int getDangerLevel() {
        //TODO: Report the current level back as an integer
        return dangerLevel;
    }
}
