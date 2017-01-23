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

public class MovieFindDbLoader extends AsyncTaskLoader<List<Movie>> {
    private MovieManager movieManager;
    private Long mId;

    public MovieFindDbLoader(Context context, Long id) {
        super(context);
        movieManager = new MovieManager(context);
        mId = id;
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mId != null && mId != 0) {
            return movieManager.findMoviesById(mId);
        }
        return Collections.emptyList();
    }
}