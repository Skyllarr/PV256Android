package cz.muni.fi.pv256.movio2.uco374585.Models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery;
import cz.muni.fi.pv256.movio2.uco374585.Api.MovieDTO;

/**
 * Created by Skylar on 1/15/2017.
 */

public class DiscoverResponse {

    private int page;
    private MovieDTO[] results;

    public List<Movie> createAndFilterListOfMovies() {
        List<Movie> movies = new ArrayList<>();

        if (results != null) {
            for (MovieDTO movie : results) {
                if (movie.getTitle() != null && movie.getBackdropPath() != null && movie.getPosterPath() != null) {
                    movie.setPosterPath(ApiQuery.IMAGE_URL + movie.getPosterPath().substring(1));
                    movie.setBackdropPath(ApiQuery.IMAGE_URL + movie.getBackdropPath().substring(1));
                    BigDecimal popularity = new BigDecimal(Math.round(movie.getPopularity() * 10.0) / 10.0);
                    movie.setPopularity(popularity.floatValue());
                    movies.add(movie.create());
                }
            }
        }
        return movies;
    }
}
