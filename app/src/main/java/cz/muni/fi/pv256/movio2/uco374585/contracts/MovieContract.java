package cz.muni.fi.pv256.movio2.uco374585.contracts;

import android.support.annotation.NonNull;

/**
 * Created by Skylar on 1/24/2017.
 */

public class MovieContract {
    interface DetailView {
        void changeFab(Boolean isFavourite);
    }

    interface DetailListener {
        void onClickSaved(@NonNull Long movieId);
    }
}
