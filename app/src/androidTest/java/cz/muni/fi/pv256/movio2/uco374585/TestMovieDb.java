package cz.muni.fi.pv256.movio2.uco374585;

/**
 * Created by Skylar on 1/20/2017.
 */

import android.test.AndroidTestCase;
import org.junit.Assert;
import java.util.List;
import cz.muni.fi.pv256.movio2.uco374585.database.MovieContract;
import cz.muni.fi.pv256.movio2.uco374585.database.MovieManager;
import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

public class TestMovieDb extends AndroidTestCase {

    private MovieManager mManager;

    @Override
    protected void setUp() throws Exception {
        mManager = new MovieManager(mContext);
        deleteAll();
    }

    @Override
    public void tearDown() throws Exception {
        deleteAll();
    }

    public void deleteAll() {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
    }

    public void testCreateMovie() {
        Movie movie = new Movie(1L, "12-32-2019", "My cover path", "My title", "My backdrop", 10f, 10f, "my description");
        mManager.createMovie(movie);
        List<Movie> result = mManager.findMoviesById(movie.getId());
        Assert.assertTrue(result.size() == 1 && movie.getTitle().equals(result.get(0).getTitle()));
    }

    public void testUpdateMovie() {
        Movie movie = new Movie(1L, "12-32-2019", "My cover path", "My title", "My backdrop", 10f, 10f, "my description");
        mManager.createMovie(movie);
        movie.setDescription("This is new description");
        mManager.updateMovie(movie);
        List<Movie> result = mManager.findMoviesById(movie.getId());
        Assert.assertTrue(result.size() == 1);
        Assert.assertEquals(movie.getDescription(), result.get(0).getDescription());
    }

    public void testDeleteMovie() {
        Movie movie = new Movie(1L, "12-32-2019", "Rings", "Path to cover", "My backdrop", 10f, 10f, "my description");
        Movie movie2 = new Movie(2L, "12-32-2019", "Alien", "Path to cover2", "My backdrop2", 5f, 6f, "my description");
        Movie movie3 = new Movie(3L, "12-32-2019", "The thing", "Path to cover3", "My backdrop3", 17f, 5f, "my description");
        mManager.createMovie(movie);
        mManager.createMovie(movie2);
        mManager.createMovie(movie3);

        List<Movie> result = mManager.findMoviesById(movie.getId());
        Assert.assertTrue(result.size() == 1);
        mManager.deleteMovie(movie);

        List<Movie> result2 = mManager.findMoviesById(movie.getId());
        Assert.assertTrue(result2.size() == 0);

        List<Movie> result3 = mManager.findMovies();
        Assert.assertTrue(result3.size() == 2);
    }

    public void testFindMovieById() {
        Movie movie = new Movie(1L, "12-32-2019", "My cover path", "My title", "My backdrop", 10f, 10f, "my description");
        Movie movie2 = new Movie(2L, "1-22-2020", "My cover path2", "My title2", "My backdrop2", 10f, 10f, "my description2");
        mManager.createMovie(movie);
        mManager.createMovie(movie2);

        List<Movie> result = mManager.findMoviesById(movie2.getId());

        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get(0) != null);
        Assert.assertEquals(movie2.getTitle(), result.get(0).getTitle());
    }

    public void testFindAllMovie() {
        Movie movie = new Movie(1L, "12-32-2019", "My cover path", "My title", "My backdrop", 10f, 10f, "my description");
        Movie movie2 = new Movie(2L, "1-22-2020", "My cover path2", "My title2", "My backdrop2", 10f, 10f, "my description2");
        mManager.createMovie(movie);
        mManager.createMovie(movie2);

        List<Movie> result = mManager.findMovies();

        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.get(0) != null && result.get(0).getId().equals(movie.getId()));
        Assert.assertTrue(result.get(1) != null && result.get(1).getTitle().equals(movie2.getTitle()));
    }
}
