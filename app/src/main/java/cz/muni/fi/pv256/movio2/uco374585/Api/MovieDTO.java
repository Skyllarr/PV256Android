package cz.muni.fi.pv256.movio2.uco374585.Api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

/**
 * Created by Skylar on 1/15/2017.
 */

public class MovieDTO {

    public long id;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("backdrop_path")
    public String backdropPath;

    public String overview;

    public float popularity;

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

    public String getTitle() {
        return title;
    }

    public String title;

    public Movie create() {
        return new Movie(releaseDate, posterPath, title, backdropPath, popularity, overview);
    }
}
