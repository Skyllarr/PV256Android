package cz.muni.fi.pv256.movio2.uco374585;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Models.DiscoverResponse;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Skylar on 1/13/2017.
 */

public class MovieDownloader extends AsyncTask<String, Void, List<Movie>> {

    public MovieDownloader() {
    }

    @Override
    protected List<Movie> doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urls[0]).build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            if (isCancelled()) {
                return null;
            }
            DiscoverResponse movies = new Gson().fromJson(response.body().string(), DiscoverResponse.class);
            return movies.createAndFilterListOfMovies();
        } catch (Exception x) {
            Log.e("MovieDownloader", x.getMessage() == null ? x.toString() : x.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }
}