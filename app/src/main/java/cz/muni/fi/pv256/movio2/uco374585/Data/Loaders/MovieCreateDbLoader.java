package cz.muni.fi.pv256.movio2.uco374585.Data.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Data.MovieManager;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

/**
 * Created by Skylar on 1/20/2017.
 */

public class MovieCreateDbLoader extends AsyncTaskLoader<List<Movie>> {
    private MovieManager movieManager;
    private Movie movie;

    public MovieCreateDbLoader(Context context, Movie movie) {
        super(context);
        movieManager = new MovieManager(context);
        this.movie = movie;
    }

    @Override
    public List<Movie> loadInBackground() {
        movieManager.createMovie(movie);
        return Collections.emptyList();
    }
}