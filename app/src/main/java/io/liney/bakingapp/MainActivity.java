package io.liney.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new RecipeAdapter(this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setAdapter(mAdapter);

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
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipePojo>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch recipes. Do you have internet connection?", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(RecipePojo recipeData) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipeData);
        startActivity(intent);
    }
}
