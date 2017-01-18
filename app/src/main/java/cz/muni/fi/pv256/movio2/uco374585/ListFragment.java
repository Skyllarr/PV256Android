package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cz.muni.fi.pv256.movio2.uco374585.Data.MovieDataSingleton;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.API_KEY;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.TMDB_URL;

/**
 * Created by Skylar on 12/27/2016.
 */

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
    public void onAttach(Context context) {
        Log.i(TAG, "ListFragment was attached to its context");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAndStoreMovies();
    }

    public void loadAndStoreMovies() {
        try {
            if (isInternetAvailable() && MovieDataSingleton.getInstance().isEmpty()) {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority(TMDB_URL)
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("primary_release_date.gte", todayDate())
                        .appendQueryParameter("primary_release_date.lte", weekFromToday())
                        .appendQueryParameter("api_key", API_KEY);
                String moviesThisWeekFetchMoviesUrl = builder.build().toString();

                builder.clearQuery()
                        .appendQueryParameter("primary_release_year", "" + 2017)
                        .appendQueryParameter("sort_by", "popularity.desc").appendQueryParameter("api_key", API_KEY);
                String mostPopularThisYearFetchMoviesUrl = builder.build().toString();
                builder.clearQuery()
                        .appendQueryParameter("sort_by", "vote_average.desc").appendQueryParameter("api_key", API_KEY);
                String mostPopularAllTimeMoviesUrl = builder.build().toString();

                String[] urls = new String[]{moviesThisWeekFetchMoviesUrl,
                        mostPopularThisYearFetchMoviesUrl,
                        mostPopularAllTimeMoviesUrl};

                Map<String, List<Movie>> allMoviesMap = new MovieDownloader().execute(urls).get();
                MovieDataSingleton.getInstance().setMoviesThisWeek(allMoviesMap.get(moviesThisWeekFetchMoviesUrl));
                MovieDataSingleton.getInstance().setMoviesPopularThisYear(allMoviesMap.get(mostPopularThisYearFetchMoviesUrl));
                MovieDataSingleton.getInstance().setMoviesPopularAllTime(allMoviesMap.get(mostPopularAllTimeMoviesUrl));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, "ListFragment is now visible to the user");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "ListFragment is now able to interact with the user");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "ListFragment is no longer interacting with the user either " +
                "because its activity is being paused or a fragment operation is modifying it in the activity.");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "ListFragment fragment is no longer visible to the user either " +
                "because its activity is being stopped or a fragment operation is modifying it in the activity.");
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (MovieDataSingleton.getInstance().isEmpty()) {
            View view = inflater.inflate(R.layout.no_data_screen, container, false);
            if (!isInternetAvailable()) {
                TextView noConnection = (TextView) view.findViewById(R.id.no_internet_connection);
                noConnection.setVisibility(View.VISIBLE);
            }
            return view;
        } else {
            View view = inflater.inflate(R.layout.main_fragment, container, false);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (!MovieDataSingleton.getInstance().isEmpty()) {
            fillRecyclerView(view, R.id.recycler_view_movies_category1, MovieDataSingleton.getInstance().getMoviesThisWeek());
            fillRecyclerView(view, R.id.recycler_view_movies_category2, MovieDataSingleton.getInstance().getMoviesPopularThisYear());
            fillRecyclerView(view, R.id.recycler_view_movies_category3, MovieDataSingleton.getInstance().getMoviesPopularAllTime());
        }
    }

    private void fillRecyclerView(View view, int id, List<Movie> movies) {
        if (movies != null && movies.size() != 0) {
            mRecyclerView = (RecyclerView) view.findViewById(id);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapter(movies, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "Here it is possible to clean up resources associated with ListFragment" +
                "because View is being destroyed");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "ListFragment is being destroyed, here clean up its resources");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "ListFragment is being detached from activity");
        super.onDetach();
    }
}
