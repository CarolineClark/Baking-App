package io.liney.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    private static final String EXOPLAYER_CURRENT_POSITION = "EXOPLAYER_CURRENT_POSITION";
    public static final String TAG = "StepDetailFragment";

    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.description_text_view)
    TextView descriptionTextView;

    @BindView(R.id.thumbnail_image_view)
    ImageView mThumbnailImageView;

    StepPojo mSteps;
    private String mVideoURL;
    private final String KEY_STEPS = "key-steps";
    private long mExoPlayerCurrentTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void setSteps(StepPojo steps) {
        mSteps = steps;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExoPlayerCurrentTime = 0;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_STEPS)) {
                mSteps = savedInstanceState.getParcelable(KEY_STEPS);
            }
            if (savedInstanceState.containsKey(EXOPLAYER_CURRENT_POSITION)) {
                mExoPlayerCurrentTime = savedInstanceState.getLong(EXOPLAYER_CURRENT_POSITION);
            }
        }
        mVideoURL = mSteps.getVideoURL();
        showInformation(mExoPlayerCurrentTime);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(EXOPLAYER_CURRENT_POSITION, mExoPlayerCurrentTime);
        savedInstanceState.putParcelable(KEY_STEPS, mSteps);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        if (mExoPlayer != null) {
            mExoPlayerCurrentTime = mExoPlayer.getCurrentPosition();
        }

        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            initVideoPlayer(mExoPlayerCurrentTime);
        }
    }

    private void showInformation(long currentPosition) {
        descriptionTextView.setText(mSteps.getDescription());
        initVideoPlayer(currentPosition);
        displayThumbnailImage(mSteps.getThumbnailURL());
    }

    private void initVideoPlayer(long currentPosition) {
        if (mVideoURL != null && !mVideoURL.equals("")) {
            initializePlayer(Uri.parse(mVideoURL), currentPosition);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void displayThumbnailImage(String thumbnailURL) {
        if (thumbnailURL != null && !thumbnailURL.equals("")) {
            mThumbnailImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(thumbnailURL)
                    .into(mThumbnailImageView);
        } else {
            mThumbnailImageView.setVisibility(View.GONE);
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, long currentPosition) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayer.seekTo(currentPosition);
            mPlayerView.setPlayer(mExoPlayer);
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
