package cz.muni.fi.pv256.movio2.uco374585.api;

import com.google.gson.annotations.SerializedName;

import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

/**
 * Created by Skylar on 1/15/2017.
 */

public class MovieDTO {


    @SerializedName("id")
    public Long id;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("vote_average")
    public float voteAverage;
    public String overview;
    public float popularity;
    public String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public Movie create() {
        return new Movie(id, releaseDate, posterPath, title, backdropPath, popularity, voteAverage, overview);
    }
}
