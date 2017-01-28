package cz.muni.fi.pv256.movio2.uco374585.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.models.Movie;

/**
 * Created by Skylar on 1/19/2017.
 */

public class MovieManager {
    private static final String TAG = "MovieManager";
    private static final int COL_MOVIE_ID = 1;
    private static final int COL_TITLE = 2;
    private static final int COL_RELEASE_DATE = 3;
    private static final int COL_POSTER_PATH = 4;
    private static final int COL_BACKDROP_PATH = 5;
    private static final int COL_POPULARITY = 6;
    private static final int COL_VOTE_AVERAGE = 7;
    private static final int COL_OVERVIEW = 8;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW
    };

    private static final String WHERE_MOVIE_ID = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";

    private Context mContext;

    public MovieManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public List<Movie> findMovies() {
        Cursor cursor = mContext
                .getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_COLUMNS, "",
                        new String[]{}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Movie> movies = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    movies.add(getMovie(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return movies;
        }
        if (cursor != null) {
            cursor.close();
        }

        return Collections.emptyList();
    }

    public List<Movie> findMoviesById(Long id) {
        Cursor cursor = mContext
                .getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_COLUMNS, WHERE_MOVIE_ID,
                        new String[]{String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Movie> movies = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    movies.add(getMovie(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return movies;
        }
        if (cursor != null) {
            cursor.close();
        }
        return Collections.emptyList();
    }

    public void createMovie(Movie movie) {
        if (movie == null) {
            Log.i(TAG, "Cannot create movie");
        }
        ContentUris.parseId(mContext.getContentResolver()
                .insert(MovieContract.MovieEntry.CONTENT_URI, prepareMovieValues(movie)));
    }

    public void updateMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("Cannot update movie");
        }
        mContext.getContentResolver()
                .update(MovieContract.MovieEntry.CONTENT_URI,
                        prepareMovieValues(movie),
                        WHERE_MOVIE_ID, new String[]{String.valueOf(movie.getId())});
    }

    private ContentValues prepareMovieValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getCoverPath());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getDescription());
        return values;
    }

    private Movie getMovie(Cursor cursor) {
        return new Movie(
                cursor.getLong(COL_MOVIE_ID),
                cursor.getString(COL_RELEASE_DATE),
                cursor.getString(COL_POSTER_PATH),
                cursor.getString(COL_TITLE),
                cursor.getString(COL_BACKDROP_PATH),
                cursor.getFloat(COL_VOTE_AVERAGE),
                cursor.getFloat(COL_POPULARITY),
                cursor.getString(COL_OVERVIEW)
        );
    }

    public void deleteMovie(Movie movie) {
        assert movie != null;
        mContext.getContentResolver()
                .delete(MovieContract.MovieEntry.CONTENT_URI,
                        WHERE_MOVIE_ID,
                        new String[]{String.valueOf(movie.getId())});
    }
}