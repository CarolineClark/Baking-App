package io.liney.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    private RecipePojo mRecipe;
    private int mStepNumber;
    private final String KEY_RECIPE = "key_recipe";
    private final String KEY_STEP_NUMBER = "key_step_number";

    @BindView(R.id.previous_button) Button mPreviousButton;
    @BindView(R.id.next_button) Button mNextButton;
    private StepDetailFragment mStepDetailFragment;
    private String FRAGMENT_KEY = "STEP_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_RECIPE)) {
                mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
            }
            if (savedInstanceState.containsKey(KEY_STEP_NUMBER)) {
                mStepNumber = savedInstanceState.getInt(KEY_STEP_NUMBER);
            }
            if (savedInstanceState.containsKey(FRAGMENT_KEY)) {
                mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_KEY);
            }
        } else {
            mRecipe = getIntent().getExtras().getParcelable("recipe");
            mStepNumber = getIntent().getExtras().getInt("stepNumber");
            mStepDetailFragment = new StepDetailFragment();
            StepPojo step = mRecipe.getSteps().get(mStepNumber);
            if (step == null) {
                Toast.makeText(this, getString(R.string.problem_loading_activity), Toast.LENGTH_SHORT).show();
                return;
            }
            mStepDetailFragment.setSteps(step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_fragment_wrapper, mStepDetailFragment)
                    .commit();
        }

        enableDisableButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_RECIPE, mRecipe);
        savedInstanceState.putInt(KEY_STEP_NUMBER, mStepNumber);
        getSupportFragmentManager().putFragment(savedInstanceState, FRAGMENT_KEY, mStepDetailFragment);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void nextStep(View view) {
        if (mStepNumber < mRecipe.getSteps().size() - 1) {
            mStepNumber = mStepNumber + 1;
            launchStep(mStepNumber);
        }
    }

    public void previousStep(View view) {
        if (mStepNumber > 0) {
            mStepNumber = mStepNumber - 1;
            launchStep(mStepNumber);
        }
    }

    private void launchStep(int stepNumber) {
        mStepDetailFragment = new StepDetailFragment();
        mStepDetailFragment.setSteps(mRecipe.getSteps().get(stepNumber));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_fragment_wrapper, mStepDetailFragment)
                .commit();
        enableDisableButtons();
    }

    private void enableDisableButtons() {
        if (mStepNumber == 0) {
            mPreviousButton.setEnabled(false);
            mPreviousButton.setVisibility(View.GONE);
        } else {
            mPreviousButton.setEnabled(true);
            mPreviousButton.setVisibility(View.VISIBLE);
        }

        if (mStepNumber == mRecipe.getSteps().size() - 1) {
            mNextButton.setEnabled(false);
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setEnabled(true);
            mNextButton.setVisibility(View.VISIBLE);
        }
    }
}
