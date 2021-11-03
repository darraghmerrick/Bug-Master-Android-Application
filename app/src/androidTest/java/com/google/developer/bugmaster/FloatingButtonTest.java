package com.google.developer.bugmaster;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
//Android JUnit4 helps control launching the app as well as running ui tests on it
@RunWith(AndroidJUnit4.class)
public class FloatingButtonTest {

    @Rule
    //ActivityTestRule provides functional testing for a single specific activity
    //The MainActivity is declared as the activity to be tested.
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    //This is th main body of the test. Follow the 3 step process
    public void floatingButtonTest() {
        Intents.init();

        //Step 1. Find View with ViewMatcher
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));

        //Step 2. Perform action on View using click()
        floatingActionButton.perform(click());

        //Step 3. Check if the action results in the expected output
        //Use the hasComponent(...) IntentMatcher
        intended(hasComponent(QuizActivity.class.getName()));
        Intents.release();
    }

}
