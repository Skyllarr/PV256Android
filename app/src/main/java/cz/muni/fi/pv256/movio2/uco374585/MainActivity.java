package cz.muni.fi.pv256.movio2.uco374585;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private Boolean mDefaultTheme;
    private FragmentManager manager;
    private Fragment myfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            myfragment = ListFragment.newInstance();
            if (tabletSize) {
                transaction.add(R.id.home_fragment, myfragment);
            } else {
                transaction.add(R.id.fragment_container, myfragment);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        mPrefs = this.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        savedInstanceState.putString("theme", "" + mPrefs.getBoolean("defaultTheme", true));
        if (myfragment != null && myfragment.isAdded())
            getFragmentManager().putFragment(savedInstanceState, "myfragment", myfragment);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        myfragment = getFragmentManager().getFragment(inState, "myfragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                changeTheme();
                return true;
            case R.id.action_home:
                if (!getResources().getBoolean(R.bool.isTablet)) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    myfragment = ListFragment.newInstance();
                    transaction.replace(R.id.fragment_container, myfragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else
            finish();
    }

    private void setTheme() {
        mPrefs = this.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        mDefaultTheme = mPrefs.getBoolean("defaultTheme", true);
        if (mDefaultTheme)
            this.setTheme(R.style.AppBaseTheme);
        else
            this.setTheme(R.style.AppBaseTheme_AppBaseThemeBlue);
    }

    private void changeTheme() {
        mDefaultTheme = !mDefaultTheme;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("defaultTheme", mDefaultTheme);
        editor.commit();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
