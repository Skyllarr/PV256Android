package cz.muni.fi.pv256.movio2.uco374585.database.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.database.MovieManager;
import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

/**
 * Created by Skylar on 1/20/2017.
 */

public class MovieFindAllDbLoader extends AsyncTaskLoader<List<Movie>> {
    private MovieManager movieManager;

    public MovieFindAllDbLoader(Context context) {
        super(context);
        movieManager = new MovieManager(context);
    }

    @Override
    public List<Movie> loadInBackground() {
        return movieManager.findMovies();
    }
}