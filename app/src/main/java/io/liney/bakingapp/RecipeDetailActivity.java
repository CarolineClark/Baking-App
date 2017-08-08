package io.liney.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.ingredients_linear_layout) LinearLayout mIngredientsLinearLayout;
    @BindView(R.id.steps_linear_layout) LinearLayout mStepsLinearLayout;
    @BindView(R.id.servings_text_view) TextView mServingsTextView;
    @BindView(R.id.recipe_name_text_view) TextView mNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        RecipePojo recipeData = getIntent().getExtras().getParcelable("recipe");
        mServingsTextView.setText(Integer.toString(recipeData.getServings()));
        mNameTextView.setText(recipeData.getName());
        fillIngredients(recipeData.getIngredients());
        fillSteps(recipeData.getSteps());
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

    private void fillSteps(List<StepPojo> steps) {
        for (StepPojo step: steps) {
            TextView shortDescription = new TextView(this);
            TextView description = new TextView(this);
            TextView videoURL = new TextView(this);
            TextView thumbnailURL = new TextView(this);

            description.setText(step.getDescription());
            shortDescription.setText(step.getShortDescription());
            videoURL.setText(step.getVideoURL());
            thumbnailURL.setText(step.getThumbnailURL());

            mStepsLinearLayout.addView(description);
            mStepsLinearLayout.addView(shortDescription);
            mStepsLinearLayout.addView(videoURL);
            mStepsLinearLayout.addView(thumbnailURL);
        }
    }
}
