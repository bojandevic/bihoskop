package com.bojandevic.bihoskop.youtube;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bojandevic.bihoskop.AppStatics;
import com.bojandevic.bihoskop.R;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;

/**
 * An abstract activity which deals with recovering from errors which may occur during API
 * initialization, but can be corrected through user action.
 */
public class BihoskopYoutubeFragment extends YouTubePlayerSupportFragment implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.OnFullscreenListener,
        YouTubePlayer.PlaybackEventListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    private String videoID = "";

    private Activity      activity;
    private View          movieDetails;
    private YouTubePlayer youTubePlayer;

    private Boolean isFullscreen = false;

    public interface PlaybackChange {
        void onPlayerPlay();
        void onPlayerStop();
    }

    ArrayList<PlaybackChange> listeners = new ArrayList<PlaybackChange>();

    public BihoskopYoutubeFragment() {

    }

    public void setVideoID(String url) {
        videoID = url.trim().replaceFirst("https?://(www\\.)?youtube.com/watch\\?v=", "");
    }

    public void setOnPlaybackChangeListener(PlaybackChange pcl) {
        listeners.add(pcl);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activity     = getActivity();
        movieDetails = activity.findViewById(R.id.movieDetails);
        initialize(AppStatics.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "Morate imati instaliranu youtube aplikaciju da bi gledali trejlere.";
            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;

        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        youTubePlayer.setOnFullscreenListener(this);
        youTubePlayer.setPlaybackEventListener(this);
        if (!wasRestored) {
            youTubePlayer.cueVideo(videoID);
        }
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    private void doLayout() {
        FrameLayout.LayoutParams playerParams = (FrameLayout.LayoutParams) getView().getLayoutParams();

        if (isFullscreen && youTubePlayer.isPlaying()) {
            playerParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            movieDetails.setVisibility(View.GONE);
        }
        else {
            playerParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            movieDetails.setVisibility(View.VISIBLE);
        }
    }

    public void setPlayerControlFlags(boolean isPlaying) {
        int controlFlags = youTubePlayer.getFullscreenControlFlags();
        if (isPlaying) {
            controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
            for (PlaybackChange pbc : listeners) {
                pbc.onPlayerPlay();
            }
        } else {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
            for (PlaybackChange pbc : listeners) {
                pbc.onPlayerStop();
            }
        }
        youTubePlayer.setFullscreenControlFlags(controlFlags);
    }

    @Override
    public void onPlaying() {
        setPlayerControlFlags(true);
    }

    @Override
    public void onPaused() {
        setPlayerControlFlags(false);
    }

    @Override
    public void onStopped() {
        setPlayerControlFlags(false);
    }

    @Override
    public void onBuffering(boolean b) {
        setPlayerControlFlags(true);
    }

    @Override
    public void onSeekTo(int i) {

    }
}

