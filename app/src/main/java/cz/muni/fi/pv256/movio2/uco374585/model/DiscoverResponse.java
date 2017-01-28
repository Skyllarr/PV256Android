package cz.muni.fi.pv256.movio2.uco374585.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.api.MovieDTO;
import cz.muni.fi.pv256.movio2.uco374585.api.Query;

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
                    movie.setPosterPath(Query.IMAGE_URL + movie.getPosterPath());
                    movie.setBackdropPath(Query.IMAGE_URL + movie.getBackdropPath());
                    BigDecimal popularity = new BigDecimal(Math.round(movie.getPopularity() * 10.0) / 10.0);
                    BigDecimal voteAverage = new BigDecimal(movie.getVoteAverage());
                    movie.setPopularity(popularity.floatValue());
                    movie.setVoteAverage(voteAverage.floatValue());
                    movies.add(movie.create());
                }
            }
        }
        return movies;
    }
}
