package io.liney.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        RecipePojo recipe = getIntent().getExtras().getParcelable("recipe");

        mTwoPane = findViewById(R.id.master_detail_view) != null;
        if (mTwoPane) {
            StepPojo step = recipe.getSteps().get(0);
            createStepDetailFragment(step);
        }
        // the other fragment is static and does not need to implemented in code
    }

    @Override
    public void onClick(StepPojo step) {
        if (mTwoPane) {
            createStepDetailFragment(step);
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("step", step);
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
