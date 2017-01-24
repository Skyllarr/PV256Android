package cz.muni.fi.pv256.movio2.uco374585.api;

/**
 * Created by Skylar on 1/16/2017.
 */

import cz.muni.fi.pv256.movio2.uco374585.model.DiscoverResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbAPI {

    @GET("discover/movie")
    Call<DiscoverResponse> loadMoviesFromDateToDate(@Query("primary_release_date.gte") String dateFrom,
                                                    @Query("primary_release_date.lte") String dateTo,
                                                    @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<DiscoverResponse> loadMoviesOfYearSortedBy(@Query("primary_release_year") int year,
                                                    @Query("sort_by") String sortBy,
                                                    @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<DiscoverResponse> loadMoviesAllTimeSortBy(@Query("sort_by") String sortBy,
                                                   @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDTO> findMovieById(@Path("id") long id, @Query("api_key") String apiKey);
}