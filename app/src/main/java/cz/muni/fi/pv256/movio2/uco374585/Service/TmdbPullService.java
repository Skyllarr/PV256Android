package cz.muni.fi.pv256.movio2.uco374585.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Api.Query;
import cz.muni.fi.pv256.movio2.uco374585.Api.TmdbAPI;
import cz.muni.fi.pv256.movio2.uco374585.Data.MovieDataSingleton;
import cz.muni.fi.pv256.movio2.uco374585.Models.DiscoverResponse;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cz.muni.fi.pv256.movio2.uco374585.Api.Query.DISCOVER_URL;

/**
 * Created by Skylar on 1/16/2017.
 */

public class TmdbPullService extends IntentService {

    private static final String TAG = "TmdbPullService";

    public TmdbPullService() {
        super("TmdbPullService");
    }

    private static String todayDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private static String weekFromToday() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 7);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        ArrayList<String> categories = workIntent.getExtras().getStringArrayList("categories");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit rest = new Retrofit.Builder()
                .baseUrl(DISCOVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        TmdbAPI tmdbAPI = rest.create(TmdbAPI.class);

        for (String category : categories) {
            Call<DiscoverResponse> call = fetchMoviesOfCategory(tmdbAPI, category);
            retrofit2.Response<DiscoverResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                Log.e(TAG, "Failed to execute call for fetching movies");
            }
            DiscoverResponse movies = new DiscoverResponse();
            if (response != null)
                movies = response.body();
            if (movies == null) {
                Log.e(TAG, "Response returned with empty body");
            }
            List<Movie> loadedMovies = movies.createAndFilterListOfMovies();
            saveMoviesOfCategory(loadedMovies, category);
        }
    }

    private Call<DiscoverResponse> fetchMoviesOfCategory(TmdbAPI tmdbAPI, String movieCategory) {
        switch (movieCategory) {
            case "category1":
                return tmdbAPI.loadMoviesFromDateToDate(todayDate(), weekFromToday(), Query.API_KEY);
            case "category2":
                return tmdbAPI.loadMoviesOfYearSortedBy(2017, "vote_average.desc", Query.API_KEY);
            case "category3":
                return tmdbAPI.loadMoviesAllTimeSortBy("popularity.desc", Query.API_KEY);
            default:
                Log.e(TAG, "Failed to load movies of category");
                return null;
        }
    }

    private void saveMoviesOfCategory(List<Movie> movies, String movieCategory) {
        switch (movieCategory) {
            case "category1":
                MovieDataSingleton.getInstance().setMoviesThisWeek(movies);
                break;
            case "category2":
                MovieDataSingleton.getInstance().setMoviesPopularThisYear(movies);
                break;
            case "category3":
                MovieDataSingleton.getInstance().setMoviesPopularAllTime(movies);
                break;
            default:
                Log.e(TAG, "Failed to save movies of category");
        }
    }
}
