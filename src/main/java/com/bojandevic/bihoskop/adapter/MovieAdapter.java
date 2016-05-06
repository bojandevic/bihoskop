package com.bojandevic.bihoskop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.bojandevic.bihoskop.R;
import com.bojandevic.bihoskop.bean.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private final Context context;
    private AQuery aQuery = null;

    public ArrayList<Movie> movies;

    public MovieAdapter(Context context,
                        int resource, int itemResource,
                        ArrayList<Movie> movies) {
        super(context, resource, itemResource, movies);

        this.movies = movies;

        this.context = context;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.list_movie_item, null);
        }

        aQuery = new AQuery(v);

        Movie m = movies.get(position);
        if (m != null) {
            TextView tvTitle    = (TextView) v.findViewById(R.id.movieTitle);
            TextView tvCategory = (TextView) v.findViewById(R.id.movieCategory);
            TextView tvLength   = (TextView) v.findViewById(R.id.movieLength);

            RatingBar rbRating  = (RatingBar) v.findViewById(R.id.movieRating);


            tvTitle.setText(m.getTitle());
            tvCategory.setText(m.getCategory());
            tvLength.setText(m.getLength());

            rbRating.setMax(5);
            rbRating.setRating(m.getScore().floatValue());
            rbRating.setEnabled(false);
            rbRating.setFocusable(false);

            aQuery.id(R.id.moviePoster).image(m.getPosterURL(), false, true);
        }
        return v;
    }
}