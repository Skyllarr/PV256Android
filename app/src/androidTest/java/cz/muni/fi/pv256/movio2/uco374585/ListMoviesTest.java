package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Activity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorImpl;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.MonitoringInstrumentation;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.MonitoringInstrumentation.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


/**
 * Created by Skylar on 1/24/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListMoviesTest {
    private static final String LOG_TAG = "ListMoviesTest";
    private MainActivity mainActivity;

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() throws IOException {
        mainActivity = rule.getActivity();
    }

    @After
    public void finish() throws IOException {
    }

    @Test
    public void testShowFavouriteFilms() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.action_home))
                .perform(click());
        Thread.sleep(3000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Favourites"))
                .perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.category1)).check(matches(withText(startsWith("Favourites"))));
    }

    @Test
    public void testShowDiscoverFilms() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.action_home))
                .perform(click());
        Thread.sleep(3000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Discover"))
                .perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.category1)).check(matches(withText(startsWith(rule.getActivity().getString(R.string.category1)))));
    }

    @Test
    public void testShowDetailFilm() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.action_home))
                .perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.recycler_view_movies_category1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(3000);
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testSync() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.refresh))
                .perform(click());
        Thread.sleep(2000);
        onView(withText("Up to date."))
                .inRoot(withDecorView(not(is(mainActivity
                        .getWindow()
                        .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testHome() throws IOException, InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.recycler_view_movies_category1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(3000);
        onView(withId(R.id.action_home))
                .perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.category1)).check(matches(withText(startsWith(rule.getActivity().getString(R.string.category1)))));
    }
}
