package io.liney.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import io.liney.bakingapp.matchers.RecyclerViewMatcher;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class, false, false);
    private IdlingResource mIdlingResource;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void registerIdlingResource() {
        Intent startIntent = new Intent();
        startIntent.putExtra("test", true);
        mActivityRule.launchActivity(startIntent);
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    @Test
    public void clickRecipeGoesToRecipeDetailActivityScreen() throws InterruptedException {
        // when
        onView(withId(R.id.main_recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // then
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }

    @Test
    public void clickRecipeShowsRecipeNameAtTopOfNextScreen() throws InterruptedException {
        // when
        onView(withId(R.id.main_recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // then
        ViewInteraction textView = onView(allOf(withId(R.id.recipe_name_text_view), isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));
    }

    @Test
    public void firstVideoHasExoplayerShownAndNoPreviousButton() throws InterruptedException {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.main_recipe_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.master_list_fragment), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        FragmentActivity activityInstance = getActivityInstance();

        // check simple exoplayer view is shown
        ViewInteraction viewInteraction = onView(allOf(withId(R.id.player_view), withClassName(is(SimpleExoPlayerView.class.getName()))));
        viewInteraction.check(matches(isDisplayed()));

        // check description is shown
        ViewInteraction textView = onView(allOf(withId(R.id.description_text_view), isDisplayed()));
        textView.check(matches(withText("Recipe Introduction")));

        if (!isTablet(activityInstance)) {
            // check next button is shown
            ViewInteraction nextButton = onView(allOf(withId(R.id.next_button), isDisplayed()));
            nextButton.check(matches(isDisplayed()));

            // check there is no previous button for the first element
            onView(withId(R.id.previous_button)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void secondVideoHasPreviousButtonOnPhone() throws InterruptedException {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.main_recipe_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.master_list_fragment), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        FragmentActivity activityInstance = getActivityInstance();

        // check description is shown
        ViewInteraction textView = onView(allOf(withId(R.id.description_text_view), isDisplayed()));
        textView.check(matches(withText(startsWith("1. Preheat the oven"))));

        if (!isTablet(activityInstance)) {
            // check next button is shown
            ViewInteraction nextButton = onView(allOf(withId(R.id.next_button), isDisplayed()));
            nextButton.check(matches(isDisplayed()));

            // check there is no previous button for the first element
            ViewInteraction previous = onView(withId(R.id.previous_button)).check(matches(isDisplayed()));
            previous.check(matches(isDisplayed()));
        }
    }

    public FragmentActivity getActivityInstance(){
        final FragmentActivity[] currentActivity = new FragmentActivity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity[0] = (FragmentActivity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
