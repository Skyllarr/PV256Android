package cz.muni.fi.pv256.movio2.uco374585.Api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Skylar on 1/15/2017.
 */

public class ApiQuery {
    public static final String API_KEY = "&api_key=01f5cc7d21cfb631240703a6c7cb2790";
    public static final String DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/original/";
    public static final String THIS_WEEK_URL = "?primary_release_date.gte=" + todayDate() +
            "&primary_release_date.lte=" + weekFromToday();
    public static final String MOST_POPULAR_EVER_URL = "?sort_by=popularity.desc";
    public static final String MOST_POPULAR_THIS_YEAR_URL = "?primary_release_year=2017&sort_by=vote_average.desc";

    private static String todayDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private static String weekFromToday() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 7);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
}
