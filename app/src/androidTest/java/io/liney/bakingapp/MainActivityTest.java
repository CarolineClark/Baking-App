package io.liney.bakingapp;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.liney.bakingapp.matchers.RecyclerViewMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mMainActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void clickRecipeGoesToRecipeDetailActivityScreen() {
        // when
        onView(withId(R.id.main_recipe_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // then
        intended(hasComponent(RecipeDetailActivity.class.getName()));
    }

    @Test
    public void clickRecipeShowsRecipeNameAtTopOfNextScreen() {
        // when
        ViewInteraction recyclerView = onView(allOf(withId(R.id.main_recipe_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // then
        ViewInteraction textView = onView(allOf(withId(R.id.recipe_name_text_view), isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));
    }
}
