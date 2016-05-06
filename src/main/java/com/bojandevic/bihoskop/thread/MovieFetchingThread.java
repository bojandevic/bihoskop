package com.bojandevic.bihoskop.thread;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bojandevic.bihoskop.bean.Movie;

public class MovieFetchingThread extends Thread {
    public static final String REPERETOAR = "REPERTOAR";
    public static final String USKORO     = "USKORO";

    private Handler h;

    private String apiURL;

    public MovieFetchingThread(Handler h, String apiType) {
        this.h = h;

        if (apiType.equals(REPERETOAR))
            apiURL = "http://api.bojandevic.com/repertoar/";
        else if (apiType.equals(USKORO))
            apiURL = "http://api.bojandevic.com/uskoro/";
    }

    @Override
    public void run() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        try {
            URL u = new URL(apiURL);
            InputStream is = u.openStream();
            String jsonString = "";
            if (is != null) {
                Writer writer = new StringWriter();

                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(is,
                            "UTF-8"));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    is.close();
                }
                jsonString = writer.toString();
            }
            JSONObject service = new JSONObject(jsonString);

            JSONArray moviesJSON = service.getJSONArray("filmovi");

            for (int i = 0; i < moviesJSON.length(); i++) {
                JSONObject movieJSON = moviesJSON.getJSONObject(i);
                Movie movie= new Movie();

                movie.setIdx(i);
                movie.setTitle(movieJSON.getString("naslov"));
                movie.setCategory(movieJSON.getString("zanr"));
                movie.setActors(movieJSON.getString("uloge"));
                movie.setDirector(movieJSON.getString("rezija"));
                movie.setLength(movieJSON.getString("trajanje"));
                movie.setDescription(movieJSON.getString("sadrzaj"));
                movie.setTrailer(movieJSON.getString("trejler"));
                movie.setTimes(movieJSON.getString("vrijeme"));
                movie.setPosterURL(movieJSON.getString("slika"));

                String rating =  movieJSON.getString("ocjena");
                if (rating.matches("[0-9.]+"))
                    movie.setScore(Double.parseDouble(rating) / 2);
                else
                    movie.setScore(0.0);

                movies.add(movie);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Message msg = new Message();
        Bundle b    = new Bundle();

        b.putParcelableArrayList("movies", movies);
        msg.setData(b);

        h.sendMessage(msg);
    }
}
