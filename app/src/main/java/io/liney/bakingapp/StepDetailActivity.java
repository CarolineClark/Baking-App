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
    @BindView(R.id.previous_button) Button mPreviousButton;
    @BindView(R.id.next_button) Button mNextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        mRecipe = getIntent().getExtras().getParcelable("recipe");
        mStepNumber = getIntent().getExtras().getInt("stepNumber");
        StepPojo step = mRecipe.getSteps().get(mStepNumber);

        if (step == null) {
            Toast.makeText(this, "Something went wrong while loading the activity!", Toast.LENGTH_SHORT).show();
            return;
        }

        enableDisableButtons();

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setSteps(step);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_fragment_wrapper, stepDetailFragment)
                .commit();
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
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setSteps(mRecipe.getSteps().get(stepNumber));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_fragment_wrapper, stepDetailFragment)
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
