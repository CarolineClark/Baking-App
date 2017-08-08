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


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final IStepClickHandler mClickHandler;
    private final Context mContext;
    private List<StepPojo> mStepsData;

    StepAdapter(Context context, IStepClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public void setStepsData(List<StepPojo> steps) {
        mStepsData = steps;
        notifyDataSetChanged();
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_adapter, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        String shortDescription = mStepsData.get(position).getShortDescription();
        holder.mShortDescriptionTextView.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if (mStepsData == null) {
            return 0;
        }
        return mStepsData.size();
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
            mClickHandler.onClick(mStepsData.get(getAdapterPosition()));
        }
    }

    interface IStepClickHandler {
        void onClick(StepPojo step);
    }
}
