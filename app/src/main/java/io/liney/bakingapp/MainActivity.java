package io.liney.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickHandler {
    private final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.main_recipe_recycler_view) RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra("test", false)) {
            // in a test, instantiate the idling resource
            mIdlingResource = new SimpleIdlingResource();
        }

        mAdapter = new RecipeAdapter(this, this);

        RecyclerView.LayoutManager layoutManager;
        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
        if (isPhone) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        }

        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "Idling resource called! " + mIdlingResource);
        setIdleState(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IFetchRecipes service = retrofit.create(IFetchRecipes.class);
        Call<List<RecipePojo>> recipes = service.listRecipes();
        recipes.enqueue(new Callback<List<RecipePojo>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipePojo>> call, @NonNull Response<List<RecipePojo>> response) {
                List<RecipePojo> recipes = response.body();
                if (recipes == null) {
                    Toast.makeText(MainActivity.this, "Recipe list is empty. Do you have internet connection?", Toast.LENGTH_LONG).show();
                    return;
                }
                mAdapter.setRecipes(recipes);
                setIdleState(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePojo>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch recipes. Do you have internet connection?", Toast.LENGTH_LONG).show();
                setIdleState(true);
            }
        });
    }

    private void setIdleState(boolean isIdleNow) {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(isIdleNow);
        }
    }

    @Override
    public void onClick(RecipePojo recipeData) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipeData);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
