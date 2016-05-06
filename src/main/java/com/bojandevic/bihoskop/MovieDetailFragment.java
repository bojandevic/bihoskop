package com.bojandevic.bihoskop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;

import com.bojandevic.bihoskop.adapter.MovieAdapter;
import com.bojandevic.bihoskop.bean.Movie;
import com.bojandevic.bihoskop.customs.ObservableScrollView;
import com.bojandevic.bihoskop.youtube.BihoskopYoutubeFragment;

public class MovieDetailFragment extends Fragment
        implements BihoskopYoutubeFragment.PlaybackChange, ObservableScrollView.Callbacks {

    public static final String ARGUMENT_NAME = "movie_argument";

    private Boolean playing = false;

    private Movie mItem;

    private BihoskopYoutubeFragment playerFragment;
    private View                    playerPlaceholder;
    private LinearLayout            playerContainer;

    private ObservableScrollView scrollview;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARGUMENT_NAME))
            mItem = getMovie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movieDetailTitle)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.movieDetailCategory)).setText(mItem.getCategory());
            ((TextView) rootView.findViewById(R.id.movieDetailActors)).setText(mItem.getActors());
            ((TextView) rootView.findViewById(R.id.movieDetailDirector)).setText(mItem.getDirector());
            ((TextView) rootView.findViewById(R.id.movieDetailDescription)).setText(mItem.getDescription());
            ((TextView) rootView.findViewById(R.id.movieDetailLength)).setText(mItem.getLength());
            ((TextView) rootView.findViewById(R.id.movieDetailTimes)).setText(mItem.getTimes());

            RatingBar rb = (RatingBar) rootView.findViewById(R.id.movieDetailRating);

            rb.setMax(5);
            rb.setRating(mItem.getScore().floatValue());
            rb.setEnabled(false);
            rb.setFocusable(false);

            playerFragment = new BihoskopYoutubeFragment();
            playerFragment.setVideoID(mItem.getTrailer());
            playerFragment.setOnPlaybackChangeListener(this);
            getFragmentManager().beginTransaction().replace(R.id.movieDetailPlayer, playerFragment).commit();

            new AQuery(rootView).id(R.id.movieDetailPoster).image(mItem.getPosterURL(), false, true);
        }

        scrollview = (ObservableScrollView) rootView.findViewById(R.id.movieDetailScrollView);
        scrollview.setCallbacks(this);

        playerPlaceholder = rootView.findViewById(R.id.movieDetailPlayerPlaceholder);
        playerContainer = (LinearLayout) rootView.findViewById(R.id.movieDetailPlayerContainer);

        scrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScrollChanged(scrollview.getScrollY());
            }
        });

        return rootView;
    }

    public Movie getMovie() {
        return getArguments().getParcelable(ARGUMENT_NAME);
    }

    @Override
    public void onPlayerPlay() {
        playing = true;
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.scrollTo(0, scrollview.getBottom());
            }
        });
    }

    @Override
    public void onPlayerStop() {
        playing = false;
    }

    @Override
    public void onScrollChanged(int scrollY) {
        playerPlaceholder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, playerContainer.getHeight()));
        if (playing)
            playerContainer.setTranslationY(scrollY + scrollview.getHeight() - playerContainer.getHeight());
        else
            playerContainer.setTranslationY(playerPlaceholder.getTop());
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }
}
