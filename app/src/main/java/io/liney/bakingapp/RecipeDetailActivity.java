package io.liney.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mTwoPane = findViewById(R.id.master_detail_view) != null;

        // TODO get recipe details, and send it to the fragments.
    }

    @Override
    public void onClick(StepPojo step) {
        if (mTwoPane) {
            // update other section
        } else {
            // launch intent
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("step", step);
            startActivity(intent);
        }
    }
}
