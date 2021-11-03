package com.google.developer.bugmaster;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.developer.bugmaster.data.Insect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class InsectDetailActivityTest {

    private final Insect insect = new Insect("American Cockroach", "Periplaneta americana", "Pterygota", "cockroach.png", 3);

    @Rule
    public ActivityTestRule<InsectDetailsActivity> mActivityTestRule = new ActivityTestRule<>(InsectDetailsActivity.class, false, false);

    @Test
    public void friendlyNameTest() {
        Intents.init();

        Intent intent = new Intent();
        intent.putExtra("insectSelected", insect);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.friendly_name_textview)).check(matches(withText("American Cockroach")));
        Intents.release();
    }

    @Test
    public void ScientificNameTest() {
        Intents.init();

        Intent intent = new Intent();
        intent.putExtra("insectSelected", insect);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.scientific_name_textview)).check(matches(withText("Periplaneta americana")));
        Intents.release();
    }

    @Test
    public void ClassificationTest() {
        Intents.init();
        Intent intent = new Intent();
        intent.putExtra("insectSelected", insect);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.classificationText)).check(matches(withText("Classification: Pterygota")));
        Intents.release();
    }
}
