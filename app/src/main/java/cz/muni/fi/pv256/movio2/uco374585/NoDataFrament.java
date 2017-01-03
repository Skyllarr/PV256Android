package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Skylar on 12/30/2016.
 */

public class NoDataFrament extends Fragment {

    public static NoDataFrament newInstance() {
        NoDataFrament fragment = new NoDataFrament();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_data_screen, container, false);
        return view;
    }
}
