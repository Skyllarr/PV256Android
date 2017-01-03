package cz.muni.fi.pv256.movio2.uco374585;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

/**
 * Created by Skylar on 12/30/2016.
 */

public class DummyDataMovies {
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

    public List<Movie> getDataMostPopular() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(convertDateToLong("2016/02/02"), R.drawable.rings, "Rings", R.drawable.rings_backdrop, 85f, "OVERVIEW \n\nTwo high school students, Katie Embry and Becca Kotler, have a sleepover and discuss the urban legend of a cursed videotape that will kill anyone seven days after watching it. Katie reveals that she watched the said tape with her boyfriend and two other friends last week but Becca assumes that she is trying to prank her. At 10 PM, Katie goes downstairs where she witnesses several supernatural occurrences, such as the TV turning on by itself. Frightened, she calls out to Becca but hears no response."));
        movies.add(new Movie(convertDateToLong("2016/09/29"), R.drawable.miss_peregrines_home_for_peculiar_children, "Miss Peregrine's home for peculiar children",
                R.drawable.miss_peregrines_home_for_peculiar_children_backdrop, 75f, "OVERVIEW \n\nMiss Peregrine's Home for Peculiar Children is the debut novel by American author Ransom Riggs. It is a story of a boy who, following a horrific family tragedy, follows clues that take him to an abandoned children's home on a Welsh island."));
        movies.add(new Movie(convertDateToLong("2016/09/29"), R.drawable.deepwater_horizon, "Deepwater Horizon",
                R.drawable.deepwater_horizon_backdrop, 65f, "OVERVIEW \n\nOn April 20, 2010, Deepwater Horizon, an oil drilling ship operated by private contractor Transocean, is set to begin drilling off the southern coast of Louisiana on behalf of BP. Crew members Michael Mike Williams (Mark Wahlberg) and his superior, James Jimmy Harrell (Kurt Russell), are surprised to learn that the workers assigned to pour the concrete foundation intended to keep the well stable are being sent home early without conducting a pressure test, at the insistence of BP liaison Donald Vidrine (John Malkovich)."));
        movies.add(new Movie(convertDateToLong("2016/02/02"), R.drawable.inferno, "Inferno",
                R.drawable.inferno_backdrop, 65f, "OVERVIEW \n\nInferno is a 2016 American mystery thriller film directed by Ron Howard and written by David Koepp, based on the 2013 novel of the same name by Dan Brown. The film is the sequel to The Da Vinci Code and Angels & Demons, and is the third installment in the Robert Langdon film series. It stars Tom Hanks, reprising his role as Robert Langdon, alongside Felicity Jones, Omar Sy, Sidse Babett Knudsen, Ben Foster and Irrfan Khan. Together with the previous film, it remains Hanks' only live-action sequel."));
        movies.add(new Movie(convertDateToLong("2017/02/02"), R.drawable.boo, "Boo",
                R.drawable.boo_backdrop, 85f, "OVERVIEW \n\nMadea winds up in the middle of mayhem when she spends a haunted Halloween fending off killers, paranormal poltergeists, ghosts, ghouls and zombies while keeping a watchful eye on a group of misbehaving teens."));

        return movies;
    }

    public List<Movie> getDataNextDays() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(convertDateToLong("2017/02/02"), R.drawable.boo, "Boo",
                R.drawable.boo_backdrop, 85f, "OVERVIEW \n\nMadea winds up in the middle of mayhem when she spends a haunted Halloween fending off killers, paranormal poltergeists, ghosts, ghouls and zombies while keeping a watchful eye on a group of misbehaving teens."));
        movies.add(new Movie(convertDateToLong("2016/01/03"), R.drawable.doctor_strange, "Doctor Strange",
                R.drawable.doctor_strange_backdrop, 75f, "OVERVIEW \n\nA former neurosurgeon embarks on a journey of healing only to be drawn into the world of the mystic arts."));
        movies.add(new Movie(convertDateToLong("2016/02/02"), R.drawable.inferno, "Inferno",
                R.drawable.inferno_backdrop, 65f, "OVERVIEW \n\nInferno is a 2016 American mystery thriller film directed by Ron Howard and written by David Koepp, based on the 2013 novel of the same name by Dan Brown. The film is the sequel to The Da Vinci Code and Angels & Demons, and is the third installment in the Robert Langdon film series. It stars Tom Hanks, reprising his role as Robert Langdon, alongside Felicity Jones, Omar Sy, Sidse Babett Knudsen, Ben Foster and Irrfan Khan. Together with the previous film, it remains Hanks' only live-action sequel."));
        movies.add(new Movie(convertDateToLong("2016/09/29"), R.drawable.miss_peregrines_home_for_peculiar_children, "Miss Peregrine's home for peculiar children",
                R.drawable.miss_peregrines_home_for_peculiar_children_backdrop, 75f, "OVERVIEW \n\nMiss Peregrine's Home for Peculiar Children is the debut novel by American author Ransom Riggs. It is a story of a boy who, following a horrific family tragedy, follows clues that take him to an abandoned children's home on a Welsh island."));

        return movies;
    }

    public List<Movie> getDataNow() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(convertDateToLong("2016/09/29"), R.drawable.miss_peregrines_home_for_peculiar_children, "Miss Peregrine's home for peculiar children",
                R.drawable.miss_peregrines_home_for_peculiar_children_backdrop, 75f, "OVERVIEW \n\nMiss Peregrine's Home for Peculiar Children is the debut novel by American author Ransom Riggs. It is a story of a boy who, following a horrific family tragedy, follows clues that take him to an abandoned children's home on a Welsh island."));
        movies.add(new Movie(convertDateToLong("2016/09/29"), R.drawable.deepwater_horizon, "Deepwater Horizon",
                R.drawable.deepwater_horizon_backdrop, 65f, "OVERVIEW \n\nOn April 20, 2010, Deepwater Horizon, an oil drilling ship operated by private contractor Transocean, is set to begin drilling off the southern coast of Louisiana on behalf of BP. Crew members Michael Mike Williams (Mark Wahlberg) and his superior, James Jimmy Harrell (Kurt Russell), are surprised to learn that the workers assigned to pour the concrete foundation intended to keep the well stable are being sent home early without conducting a pressure test, at the insistence of BP liaison Donald Vidrine (John Malkovich)."));
        return movies;
    }
}
