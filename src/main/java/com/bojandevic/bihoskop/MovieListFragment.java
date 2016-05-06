package com.bojandevic.bihoskop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bojandevic.bihoskop.adapter.MovieAdapter;
import com.bojandevic.bihoskop.bean.Movie;
import com.bojandevic.bihoskop.thread.MovieFetchingThread;

import java.util.ArrayList;

public class MovieListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TABKEY = "tabkey";

    public static final String REPERTOAR = MovieFetchingThread.REPERETOAR;
    public static final String USKORO = MovieFetchingThread.USKORO;

    private MovieAdapter ma;
    private SwipeRefreshLayout swipeRL;

    private String currentTabKey = MovieFetchingThread.REPERETOAR;

    private Handler initHandler;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        currentTabKey = getArguments().getString(MovieListFragment.TABKEY);

        initHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                ArrayList<Movie> movies = b.getParcelableArrayList("movies");

                if (ma == null) {
                    ma = new MovieAdapter(getActivity(),
                            R.layout.fragment_movie_list,
                            R.layout.list_movie_item,
                            movies);
                    setListAdapter(ma);
                } else {
                    ma.setMovies(movies);
                    ma.notifyDataSetChanged();
                }

                if (swipeRL != null)
                    swipeRL.setRefreshing(false);
            }
        };

        new MovieFetchingThread(initHandler, this.currentTabKey).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_movies, container, false);

        swipeRL = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRL);
        swipeRL.setOnRefreshListener(this);
        swipeRL.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);

        swipeRL.post(new Runnable() {
            @Override
            public void run() {
                swipeRL.setRefreshing(true);
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Bundle arguments = new Bundle();
        arguments.putParcelable(MovieDetailFragment.ARGUMENT_NAME, ma.getMovies().get(position));

        if (getActivity().findViewById(R.id.movieDetailFl) != null) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieDetailFl, fragment)
                    .commit();
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    new Pair<View, String>(v.findViewById(R.id.moviePoster),
                            getString(R.string.transition_poster)),
                    new Pair<View, String>(v.findViewById(R.id.movieTitle),
                            getString(R.string.transition_title)),
                    new Pair<View, String>(v.findViewById(R.id.movieCategory),
                            getString(R.string.transition_category)),
                    new Pair<View, String>(v.findViewById(R.id.movieRating),
                            getString(R.string.transition_rating))
            );

            Intent detailIntent = new Intent(getActivity(), MovieDetailActivity.class);
            detailIntent.putExtras(arguments);

            ActivityCompat.startActivity(getActivity(), detailIntent, options.toBundle());
        }
    }

    @Override
    public void onRefresh() {
        new MovieFetchingThread(initHandler, this.currentTabKey).start();
    }
}
