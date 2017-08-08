package io.liney.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepAdapter.IStepClickHandler {

    @BindView(R.id.ingredients_linear_layout) LinearLayout mIngredientsLinearLayout;
    @BindView(R.id.steps_recycler_view) RecyclerView mStepsRecyclerView;
    @BindView(R.id.servings_text_view) TextView mServingsTextView;
    @BindView(R.id.recipe_name_text_view) TextView mNameTextView;
    private StepAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        RecipePojo recipeData = getIntent().getExtras().getParcelable("recipe");
        mServingsTextView.setText(Integer.toString(recipeData.getServings()));
        mNameTextView.setText(recipeData.getName());
        fillIngredients(recipeData.getIngredients());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new StepAdapter(this, this);
        mStepsRecyclerView.setAdapter(mAdapter);
        mAdapter.setStepsData(recipeData.getSteps());
    }

    private void fillIngredients(List<IngredientPojo> ingredients) {
        for (IngredientPojo ingredient: ingredients) {
            TextView ingredientView = new TextView(this);
            TextView quantityView = new TextView(this);
            TextView measureView = new TextView(this);

            ingredientView.setText(ingredient.getIngredient());
            quantityView.setText(Double.toString(ingredient.getQuantity()));
            measureView.setText(ingredient.getMeasure());

            mIngredientsLinearLayout.addView(ingredientView);
            mIngredientsLinearLayout.addView(quantityView);
            mIngredientsLinearLayout.addView(measureView);
        }
    }

    @Override
    public void onClick(StepPojo step) {
        Toast.makeText(this, "Step clicked: " + step.getShortDescription(), Toast.LENGTH_SHORT).show();
    }
}
