package io.liney.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {
    private boolean mTwoPane;
    private RecipePojo mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mTwoPane = findViewById(R.id.master_detail_view) != null;
        mRecipe = getIntent().getExtras().getParcelable("recipe");
        if (mTwoPane) {
            StepPojo step = mRecipe.getSteps().get(0);
            createStepDetailFragment(step);
        }
        // the other fragment is static and does not need to implemented in code
    }

    @Override
    public void onClick(int stepNumber) {
        if (mTwoPane) {
            createStepDetailFragment(mRecipe.getSteps().get(stepNumber));
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("recipe", mRecipe);
            intent.putExtra("stepNumber", stepNumber);
            startActivity(intent);
        }
    }

    private void createStepDetailFragment(StepPojo step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setSteps(step);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void createMasterViewFragment(RecipePojo recipe) {
//        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
//        recipeDetailFragment.setSteps(steps);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.step_detail_container, recipeDetailFragment)
//                .commit();
    }
}
