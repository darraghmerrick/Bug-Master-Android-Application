package com.google.developer.bugmaster;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.developer.bugmaster.data.Insect;

import java.io.InputStream;

public class InsectDetailsActivity extends AppCompatActivity {
    Insect insect;
   private TextView friendlyNameText;
    private TextView scientificNameText;
    private TextView classificationText;
    private ImageView insectImage;
    private RatingBar dangerLevelRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Implement layout and display insect details
        setContentView(R.layout.activity_detail);
        friendlyNameText = (TextView) findViewById(R.id.friendly_name_textview);
        scientificNameText = (TextView) findViewById(R.id.scientific_name_textview);
        classificationText = (TextView) findViewById(R.id.classificationText);
        insectImage = (ImageView) findViewById(R.id.insect_imageview);
        dangerLevelRating = (RatingBar) findViewById(R.id.danger_level_rating);

        insect = getIntent().getExtras().getParcelable("insectSelected");
        bindData();
    }
    private void bindData() {
        friendlyNameText.setText(insect.name);
        scientificNameText.setText(insect.scientificName);
        classificationText.setText(getString(R.string.classification, insect.classification));
        dangerLevelRating.setRating(insect.dangerLevel);
        try {
            InputStream ims = getAssets().open(insect.imageAsset);
            Drawable d = Drawable.createFromStream(ims, null);
            insectImage.setImageDrawable(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
