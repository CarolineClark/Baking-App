package io.liney.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailFragment extends Fragment {

    StepAdapter.IStepClickHandler mCallback;
    RecipePojo mRecipeData;

    public RecipeDetailFragment() {

    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mRecipeData = args.getParcelable("recipe");
    }

    @BindView(R.id.fragment_steps_recycler_view) RecyclerView mStepsRecyclerView;
    private StepAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        RecipeDetailActivity activity = (RecipeDetailActivity) getActivity();
        RecipePojo recipeData = activity.getIntent().getParcelableExtra("recipe");

        mAdapter = new StepAdapter(getActivity(), mCallback);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setAdapter(mAdapter);

        if (recipeData != null) {
            mAdapter.setServings(Integer.toString(recipeData.getServings()));
            mAdapter.setRecipeName(recipeData.getName());
            mAdapter.setStepsData(recipeData.getSteps());
            mAdapter.setIngredientsData(recipeData.getIngredients());
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StepAdapter.IStepClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IStepClickHandler");
        }
    }
}
