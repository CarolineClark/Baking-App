package io.liney.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {
    private static final String KEY_RECIPE = "KEY_RECIPE";
    private static final String FRAGMENT_KEY = "FRAGMENT_KEY";

    private boolean mTwoPane;
    private RecipePojo mRecipe;
    private StepDetailFragment mStepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTwoPane = findViewById(R.id.master_detail_view) != null;

        if (savedInstanceState != null && mTwoPane) {
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
            mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            replaceStepFragment();

        } else {
            mRecipe = getIntent().getExtras().getParcelable("recipe");
            if (mTwoPane) {
                StepPojo step = mRecipe.getSteps().get(0);
                createStepDetailFragmentWithData(step);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, mStepDetailFragment, StepDetailFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    public void onClick(int stepNumber) {
        if (mTwoPane) {
            createAndPlaceFragmentWithData(mRecipe.getSteps().get(stepNumber));
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("recipe", mRecipe);
            intent.putExtra("stepNumber", stepNumber);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mTwoPane) {
            savedInstanceState.putParcelable(KEY_RECIPE, mRecipe);
            getSupportFragmentManager().putFragment(savedInstanceState, FRAGMENT_KEY, mStepDetailFragment);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    private void createAndPlaceFragmentWithData(StepPojo step) {
        createStepDetailFragmentWithData(step);
        replaceStepFragment();
    }

    private void replaceStepFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, mStepDetailFragment, StepDetailFragment.TAG)
                .commit();
    }

    private void createStepDetailFragmentWithData(StepPojo step) {
        mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setSteps(step);
    }

}
