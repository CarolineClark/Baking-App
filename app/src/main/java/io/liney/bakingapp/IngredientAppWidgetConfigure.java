package io.liney.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IngredientAppWidgetConfigure extends AppCompatActivity {

    @BindView(R.id.submit_button) Button mSubmitButton;
    @BindView(R.id.radio_group_configuration_screen) RadioGroup mRadioGroup;
    private int mAppWidgetId;
    private RecipePojo mRecipePojo;
    private String mListOfIngredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_app_widget_configure);
        ButterKnife.bind(this);

        fillRadioGroupWithRecipeButtons();

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        mSubmitButton.setEnabled(false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                SharedPreferences prefs = getSharedPreferences("prefs", 0);

                prefs.edit().putString("ingredients", mListOfIngredients).apply();
                Log.d("IngredientAppWidget", mListOfIngredients);
                if (extras != null) {
                    mAppWidgetId = extras.getInt(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.recipe_app_widget_provider);
                    views.setTextViewText(R.id.appwidget_text, mListOfIngredients);
                    appWidgetManager.updateAppWidget(mAppWidgetId, views);
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                }
                finish();
            }
        });
    }

    private void fillRadioGroupWithRecipeButtons() {
        Log.d("IngredientAppWidget", "recipe buttons");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IFetchRecipes service = retrofit.create(IFetchRecipes.class);
        Call<List<RecipePojo>> recipes = service.listRecipes();
        recipes.enqueue(new Callback<List<RecipePojo>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePojo>> call, @NonNull Response<List<RecipePojo>> response) {
                Log.d("IngredientAppWidget", "making network request");
                List<RecipePojo> recipes = response.body();
                Context context = IngredientAppWidgetConfigure.this;
                if (recipes == null) {
                    Log.d("IngredientAppWidget", "recipes null");
                    Toast.makeText(context, "Recipe list is empty. Do you have internet connection?", Toast.LENGTH_LONG).show();
                    return;
                }
                for (final RecipePojo recipePojo: recipes) {
                    Log.d("IngredientAppWidget", "got back recipes");
                    RadioButton button = new RadioButton(context);
                    button.setText(recipePojo.getName());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRecipePojo = recipePojo;
                            StringBuilder sb = new StringBuilder();
                            sb.append("\n");
                            for(IngredientPojo ingredient: recipePojo.getIngredients())
                            {
                                sb.append(ingredient.getIngredient());
                                sb.append("\n");
                            }
                            mListOfIngredients = sb.toString();
                            Log.d("IngredientAppWidget", "list of ingredients: " + mListOfIngredients);

                            mSubmitButton.setEnabled(true);

                        }
                    });
                    mRadioGroup.addView(button);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePojo>> call, @NonNull Throwable t) {
                Log.d("IngredientAppWidget", "failed to make network request");
                Toast.makeText(IngredientAppWidgetConfigure.this, "Failed to fetch recipes. Do you have internet connection?", Toast.LENGTH_LONG).show();
            }
        });
    }
}
