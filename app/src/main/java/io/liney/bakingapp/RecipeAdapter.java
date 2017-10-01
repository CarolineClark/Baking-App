package io.liney.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private RecipeClickHandler mClickHandler;
    private Context mContext;
    private List<RecipePojo> mRecipeData;

    public RecipeAdapter(Context context, RecipeClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    public void setRecipes(List<RecipePojo> recipes) {
        mRecipeData = recipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.recipe_adapter;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutId, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.mRecipeTextView.setText(mRecipeData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipeData == null) {
            return 0;
        }
        return mRecipeData.size();
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_name) TextView mRecipeTextView;

        RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mRecipeData.get(getAdapterPosition()));
        }
    }

    interface RecipeClickHandler {
        void onClick(RecipePojo recipe);
    }
}
