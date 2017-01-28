package cz.muni.fi.pv256.movio2.uco374585.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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

import cz.muni.fi.pv256.movio2.uco374585.api.Query;
import cz.muni.fi.pv256.movio2.uco374585.api.TMDBApi;
import cz.muni.fi.pv256.movio2.uco374585.data.MovieDataSingleton;
import cz.muni.fi.pv256.movio2.uco374585.models.DiscoverResponse;
import cz.muni.fi.pv256.movio2.uco374585.models.Movie;
import cz.muni.fi.pv256.movio2.uco374585.R;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cz.muni.fi.pv256.movio2.uco374585.api.Query.DISCOVER_URL;

/**
 * Created by Skylar on 1/16/2017.
 */

public class TmdbPullService extends IntentService {

    static final public String TMDB_RESULT = "REQUEST_PROCESSED";
    static final public String TMDB_MESSAGE = "TMDB_MSG";
    private static final String TAG = "TmdbPullService";
    private LocalBroadcastManager broadcaster;

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

    public void sendResult(String message) {
        Intent intent = new Intent(TMDB_RESULT);
        if (message != null)
            intent.putExtra(TMDB_MESSAGE, message);
        broadcaster.sendBroadcast(intent);
    }

    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    private void FetchAndstoreFromAPI(List<String> categories,
                                      TMDBApi tmdbAPI,
                                      NotificationCompat.Builder mBuilder,
                                      NotificationManager mNotifyMgr) {

        for (String category : categories) {
            Call<DiscoverResponse> call = fetchMoviesOfCategory(tmdbAPI, category);
            retrofit2.Response<DiscoverResponse> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                mNotifyMgr.notify(NotificationConstants.failedToExecuteNotifId, mBuilder.build());
            }
            DiscoverResponse movies = new DiscoverResponse();
            if (response != null)
                movies = response.body();
            if (movies == null) {
                mNotifyMgr.notify(NotificationConstants.failedToParseNotifId, mBuilder.build());
            }
            List<Movie> loadedMovies = movies.createAndFilterListOfMovies();
            saveMoviesOfCategory(loadedMovies, category);
        }
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_info_black_24dp)
                        .setContentTitle("Downloading")
                        .setContentText("Loading movies");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(NotificationConstants.noConnectionNotifId);
        mNotifyMgr.notify(NotificationConstants.downloadingNotifId, mBuilder.build());
        ArrayList<String> categories = workIntent.getExtras().getStringArrayList("categories");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit rest = new Retrofit.Builder()
                .baseUrl(DISCOVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        TMDBApi tmdbAPI = rest.create(TMDBApi.class);

        FetchAndstoreFromAPI(categories, tmdbAPI, mBuilder, mNotifyMgr);
        sendResult("FINISHED");
        mNotifyMgr.cancel(NotificationConstants.downloadingNotifId);
    }

    private Call<DiscoverResponse> fetchMoviesOfCategory(TMDBApi tmdbAPI, String movieCategory) {
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
