package io.liney.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        StepPojo step = getIntent().getExtras().getParcelable("step");

        if (step == null) {
            Toast.makeText(this, "Something went wrong while loading the activity!", Toast.LENGTH_SHORT).show();
            return;
        }

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setSteps(step);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_fragment_wrapper, stepDetailFragment)
                .commit();
    }
}
