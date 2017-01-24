package cz.muni.fi.pv256.movio2.uco374585.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.MovieDetailFragment;
import cz.muni.fi.pv256.movio2.uco374585.R;
import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieCreateDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieDeleteDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieFindDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_CREATE_MOVIE_ID;
import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_DELETE_MOVIE_ID;
import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID;

/**
 * Created by Skylar on 1/24/2017.
 */

public class MovieDbCallbackPresenter implements LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String TAG = "MovieDbCallbackPresenter";
    private Context mContext;
    private MovieDetailFragment thisFr;
    private LoaderManager loaderManager;
    private Movie movie;

    public MovieDbCallbackPresenter(Context context, LoaderManager loaderManager, Movie movie, MovieDetailFragment fragment) {
        mContext = context;
        this.loaderManager = loaderManager;
        this.movie = movie;
        this.thisFr = fragment;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID:
                return new MovieFindDbLoader(mContext, args.getLong("id", 0));
            case LOADER_CREATE_MOVIE_ID:
                return new MovieCreateDbLoader(mContext, (Movie) args.getParcelable("movie"));
            case LOADER_DELETE_MOVIE_ID:
                return new MovieDeleteDbLoader(mContext, (Movie) args.getParcelable("movie"));
            default:
                throw new UnsupportedOperationException("Not know loader id");
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if (MovieDetailFragment.getInstance() == null) return;
        Log.i(TAG, "+++ onLoadFinished() called! +++");
        switch (loader.getId()) {
            case LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID:
                Log.i(TAG, "LOADER_FIND_MOVIE()");
                Bundle args = new Bundle();
                args.putLong("id", movie.getId());
                args.putParcelable("movie", movie);
                if (data.size() == 0) {
                    if (getLoaderManager().getLoader(LOADER_CREATE_MOVIE_ID) == null) {
                        getLoaderManager().initLoader(LOADER_CREATE_MOVIE_ID, args, MovieCallback.this).forceLoad();
                    } else {
                        getLoaderManager().restartLoader(LOADER_CREATE_MOVIE_ID, args, MovieCallback.this).forceLoad();
                    }
                } else {
                    if (getLoaderManager().getLoader(LOADER_DELETE_MOVIE_ID) == null) {
                        getLoaderManager().initLoader(LOADER_DELETE_MOVIE_ID, args, MovieCallback.this).forceLoad();
                    } else {
                        getLoaderManager().restartLoader(LOADER_DELETE_MOVIE_ID, args, MovieCallback.this).forceLoad();
                    }
                }
                break;
            case LOADER_CREATE_MOVIE_ID:
                if (MovieDetailFragment.getInstance() != null) {
                    Log.i(TAG, "LOADER_CREATE_MOVIE()");
                    View view = MovieDetailFragment.getInstance().getView();
                    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
                    fab.setImageResource(R.drawable.ic_done);
                    getLoaderManager().destroyLoader(LOADER_CREATE_MOVIE_ID);
                }
                break;
            case LOADER_DELETE_MOVIE_ID:
                if (MovieDetailFragment.getInstance() != null) {
                    Log.i("MovieCallback", "LOADER_DELETE_MOVIE()");
                    View view = MovieDetailFragment.getInstance().getView();
                    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
                    fab.setImageResource(R.drawable.ic_rating_star);
                    getLoaderManager().destroyLoader(LOADER_DELETE_MOVIE_ID);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown movie id passed to loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(TAG, "onLoaderReset()");
    }
}
