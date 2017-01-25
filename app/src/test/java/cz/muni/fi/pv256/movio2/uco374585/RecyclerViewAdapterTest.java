package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

/**
 * Created by Skylar on 1/25/2017.
 */

public class RecyclerViewAdapterTest {
    @Mock
    LoaderManager mLoaderManager;
    @Mock
    ListFragment mFilmsListFragment;
    @Mock
    MainActivity mMainActivity;
    @Mock
    private Context mockContext;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Movie> movies = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Movie movie = new Movie(1L, "12-32-2019", "My cover path", "My title", "My backdrop", 10f, 10f, "my description");
        movies.add(movie);
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.setMovies(movies);
    }

    @Test
    public void checkSetMovies() {
        Assert.assertEquals(recyclerViewAdapter.getData(), movies);
    }

    @Test
    public void checkItemCount() {
        Assert.assertEquals(recyclerViewAdapter.getItemCount(), 1);
    }

    @Test
    public void checkInsert() {
        recyclerViewAdapter.insert(1, new Movie(2L, "01-01-2019", "cover path", "test title", "backdrop", 10f, 10f, "description"));
        Assert.assertEquals(recyclerViewAdapter.getItemCount(), 2);
        Assert.assertEquals(recyclerViewAdapter.getData().get(1).getTitle(), "test title");
    }

    @Test
    public void checkRemoveSpecificMovie() {
        Movie testMovie = new Movie(2L, "01-01-2019", "test path", "test title", "test", 11f, 12f, "test description");
        recyclerViewAdapter.insert(1, testMovie);
        recyclerViewAdapter.remove(testMovie);
        Assert.assertEquals(recyclerViewAdapter.getItemCount(), 1);
        Assert.assertEquals(recyclerViewAdapter.getData().get(0).getTitle(), "My title");
    }
}
