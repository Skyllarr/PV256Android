package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/*package cz.muni.fi.pv256.movio2.uco374585;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 *//*
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}*/
@RunWith(MockitoJUnitRunner.class)
public class ListMoviesUnitTest {

    @Mock
    MainActivity mMainActivity;
    @Mock
    WifiManager mockWifiManager;
    @Mock
    private Context mockContext;
    private ListFragment listFragment;

    @Before
    public void setUp() throws Exception {
        this.listFragment = new ListFragment();
    }

    @Test
    public void checkFavourites() {
        listFragment.setFavourites(true);
        assertThat(listFragment.getFavourites(), is(true));
        listFragment.setFavourites(false);
        assertThat(listFragment.getFavourites(), is(false));
    }
}