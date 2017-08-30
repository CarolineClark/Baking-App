package io.liney.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {

    @BindView(R.id.steps_recycler_view) RecyclerView mStepsRecyclerView;
    private StepAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        mAdapter = new StepAdapter(this, this);
        RecipePojo recipeData = getIntent().getExtras().getParcelable("recipe");
        mAdapter.setServings(Integer.toString(recipeData.getServings()));
        mAdapter.setRecipeName(recipeData.getName());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setAdapter(mAdapter);
        List<StepPojo> steps = recipeData.getSteps();
        mAdapter.setStepsData(steps);
        mAdapter.setIngredientsData(recipeData.getIngredients());
    }

    @Override
    public void onClick(StepPojo step) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra("step", step);
        startActivity(intent);
    }
}
