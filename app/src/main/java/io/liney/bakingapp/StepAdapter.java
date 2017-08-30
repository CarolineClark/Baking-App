package io.liney.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final IStepClickHandler mClickHandler;
    private final Context mContext;
    private List<StepPojo> mStepsData;
    private List<IngredientPojo> mIngredientsData;
    private final int INGREDIENTS = 0;
    private final int STEPS = 1;
    private String mServings;
    private String mRecipeName;

    StepAdapter(Context context, IStepClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public void setStepsData(List<StepPojo> steps) {
        mStepsData = steps;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder;
        View view;
        switch (viewType) {
            case INGREDIENTS:
                view = inflater.inflate(R.layout.ingrediant_adapter, parent, false);
                holder = new IngredientViewHolder(view);
                break;
            case STEPS:
                view = inflater.inflate(R.layout.step_adapter, parent, false);
                holder = new StepViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.step_adapter, parent, false);
                holder = new StepViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case INGREDIENTS:
                fillIngredients(mIngredientsData, (IngredientViewHolder)holder);
                break;
            case STEPS:
                String shortDescription = mStepsData.get(position).getShortDescription();
                ((StepViewHolder) holder).mShortDescriptionTextView.setText(shortDescription);
                break;
        }
    }

    private void fillIngredients(List<IngredientPojo> ingredients, IngredientViewHolder holder) {
        String text = "";
        for (IngredientPojo ingredient: ingredients) {
            text += ingredient.getIngredient() + ", " + Double.toString(ingredient.getQuantity()) + ", " + ingredient.getMeasure() + "\n";
        }
        holder.mIngredientTextView.setText(text);
        holder.mNameTextView.setText(mRecipeName);
        holder.mServingsTextView.setText(mServings);
    }

    @Override
    public int getItemCount() {
        if (mStepsData == null) {
            return 0;
        }
        return mStepsData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return INGREDIENTS;
        } else {
            return STEPS;
        }
    }

    void setIngredientsData(List<IngredientPojo> ingredientsData) {
        this.mIngredientsData = ingredientsData;
    }

    public void setServings(String mServings) {
        this.mServings = mServings;
    }

    public void setRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.short_description_text_view) TextView mShortDescriptionTextView;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(mStepsData.get(getAdapterPosition() - 1));
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredients_text_view) TextView mIngredientTextView;
        @BindView(R.id.servings_text_view) TextView mServingsTextView;
        @BindView(R.id.recipe_name_text_view) TextView mNameTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface IStepClickHandler {
        void onClick(StepPojo step);
    }
}
