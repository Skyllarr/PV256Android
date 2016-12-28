package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Skylar on 12/27/2016.
 */

public class ListFragment extends Fragment {

    List<Movie> movies = new ArrayList<>();
    private Movie movie;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFakeMoviesData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button mClickButton1 = (Button) view.findViewById(R.id.miss_button);
        mClickButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(movies.get(0));
            }
        });
        Button mClickButton2 = (Button) view.findViewById(R.id.deepwater_button);
        mClickButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(movies.get(1));
            }
        });
        Button mClickButton3 = (Button) view.findViewById(R.id.rings_button);
        mClickButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(movies.get(2));
            }
        });
    }

    private long convertDateToLong(String dateString) {
        SimpleDateFormat Formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date releaseDate = new Date();
        try {
            releaseDate = Formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return releaseDate.getTime();
    }

    private void createFakeMoviesData() {
        Movie rings = new Movie(convertDateToLong("2016/02/02"), "R.drawable.rings", "Rings", "R.drawable.rings_backdrop", 85f, "OVERVIEW \n\nTwo high school students, Katie Embry and Becca Kotler, have a sleepover and discuss the urban legend of a cursed videotape that will kill anyone seven days after watching it. Katie reveals that she watched the said tape with her boyfriend and two other friends last week but Becca assumes that she is trying to prank her. At 10 PM, Katie goes downstairs where she witnesses several supernatural occurrences, such as the TV turning on by itself. Frightened, she calls out to Becca but hears no response.");
        Movie miss = new Movie(convertDateToLong("2016/09/29"), "R.drawable.miss_peregrines_home_for_peculiar_children", "Miss Peregrine's home for peculiar children",
                "R.drawable.miss_peregrines_home_for_peculiar_children_backdrop", 75f, "OVERVIEW \n\nMiss Peregrine's Home for Peculiar Children is the debut novel by American author Ransom Riggs. It is a story of a boy who, following a horrific family tragedy, follows clues that take him to an abandoned children's home on a Welsh island.");
        Movie deepWater = new Movie(convertDateToLong("2016/09/29"), "R.drawable.deepwater_horizon", "Deepwater",
                "R.drawable.deepwater_horizon_backdrop", 65f, "OVERVIEW \n\nOn April 20, 2010, Deepwater Horizon, an oil drilling ship operated by private contractor Transocean, is set to begin drilling off the southern coast of Louisiana on behalf of BP. Crew members Michael Mike Williams (Mark Wahlberg) and his superior, James Jimmy Harrell (Kurt Russell), are surprised to learn that the workers assigned to pour the concrete foundation intended to keep the well stable are being sent home early without conducting a pressure test, at the insistence of BP liaison Donald Vidrine (John Malkovich).");

        movies.add(miss);
        movies.add(deepWater);
        movies.add(rings);
    }

    private void showDetail(Movie movie) {
        Fragment currentFragment = getActivity().getFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getArguments() != null) {
            Movie currentlyShownMovie = currentFragment.getArguments().getParcelable("movie");
            if (currentlyShownMovie.getTitle() == movie.getTitle())
                return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MovieDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
