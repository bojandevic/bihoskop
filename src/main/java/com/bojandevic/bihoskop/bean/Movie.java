package com.bojandevic.bihoskop.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Movie implements Parcelable {
    private String title;
    private String titleSource;
    private String actors;
    private String imdbLink;
    private String description;
    private String trailer;
    private String category;
    private String posterURL;
    private String director;
    private String length;
    private String rottenTomatoesAPI;
    private Double score;
    private String times;
    private long idx;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getTitleSource());
        dest.writeString(getActors());
        dest.writeString(getImdbLink());
        dest.writeString(getDescription());
        dest.writeString(getTrailer());
        dest.writeString(getCategory());
        dest.writeString(getPosterURL());
        dest.writeString(getDirector());
        dest.writeString(getLength());
        dest.writeString(getRottenTomatoesAPI());
        dest.writeString(getTimes());
        dest.writeDouble(getScore());
        dest.writeLong(getIdx());
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            Movie m = new Movie();

            m.setTitle(source.readString());
            m.setTitleSource(source.readString());
            m.setActors(source.readString());
            m.setImdbLink(source.readString());
            m.setDescription(source.readString());
            m.setTrailer(source.readString());
            m.setCategory(source.readString());
            m.setPosterURL(source.readString());
            m.setDirector(source.readString());
            m.setLength(source.readString());
            m.setRottenTomatoesAPI(source.readString());
            m.setTimes(source.readString());
            m.setScore(source.readDouble());
            m.setIdx(source.readLong());

            return m;
        }

        @Override
        public Movie[] newArray(int size) {
            throw new UnsupportedOperationException();
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSource() {
        return titleSource;
    }

    public void setTitleSource(String titleSource) {
        this.titleSource = titleSource;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        actors = actors.replaceAll("\t|\n|(&nbsp;)", " ");
        actors = actors.replaceAll(" {2,}", " ");
        this.actors = actors;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category.trim();
        if (category.matches("^.*,$"))
            category = category.substring(0, category.length() - 1);

        this.category = category;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getRottenTomatoesAPI() {
        return rottenTomatoesAPI;
    }

    public void setRottenTomatoesAPI(String rottenTomatoesAPI) {
        this.rottenTomatoesAPI = rottenTomatoesAPI;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }
}
