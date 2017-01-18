package cz.muni.fi.pv256.movio2.uco374585.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

/**
 * Created by Skylar on 1/16/2017.
 */

public class MovieDataSingleton {

    private static MovieDataSingleton instance;
    List<Movie> moviesThisWeek = new ArrayList<>();
    List<Movie> moviesPopularThisYear = new ArrayList<>();
    List<Movie> moviesPopularAllTime = new ArrayList<>();

    private MovieDataSingleton() {
        // Constructor hidden because this is a singleton
    }

    public static MovieDataSingleton getInstance() {
        if (instance == null) {
            instance = new MovieDataSingleton();
        }
        return instance;
    }

    public List<Movie> getMoviesThisWeek() {
        return moviesThisWeek;
    }

    public void setMoviesThisWeek(List<Movie> moviesThisWeek) {
        this.moviesThisWeek = moviesThisWeek;
    }

    public List<Movie> getMoviesPopularThisYear() {
        return moviesPopularThisYear;
    }

    public void setMoviesPopularThisYear(List<Movie> moviesPopularThisYear) {
        this.moviesPopularThisYear = moviesPopularThisYear;
    }

    public List<Movie> getMoviesPopularAllTime() {
        return moviesPopularAllTime;
    }

    public void setMoviesPopularAllTime(List<Movie> moviesPopularAllTime) {
        this.moviesPopularAllTime = moviesPopularAllTime;
    }

    public boolean isEmpty() {
        if (moviesPopularAllTime == null || moviesThisWeek == null || moviesPopularThisYear == null)
            return true;
        return (moviesPopularAllTime.size() == 0 &&
                moviesPopularThisYear.size() == 0 &&
                moviesThisWeek.size() == 0);
    }
}
