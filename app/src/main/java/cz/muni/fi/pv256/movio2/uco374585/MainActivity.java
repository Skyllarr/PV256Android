package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ListFragment";
    private SharedPreferences mPrefs;
    private Boolean mDefaultTheme;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            activeFragment = ListFragment.newInstance();
            if (getResources().getBoolean(R.bool.isTablet)) {
                transaction.add(R.id.home_fragment, activeFragment, "ListFragment");
            } else {
                transaction.add(R.id.fragment_container, activeFragment, "ListFragment");
            }
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate("ListFragment", 0);

            if (!fragmentPopped) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "MainActivity is now becoming visible to the user");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "MainActivity Called after your activity has been stopped, prior to it being started again.");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "MainActivity  start interacting with the user. " +
                "At this point your activity is at the top of the activity stack, with user input going to it.");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "System is about to start resuming a previous activity.");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "MainActivity is no longer visible to the user");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "MainActivity will be destroyed now.");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        mPrefs = this.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        savedInstanceState.putString("theme", "" + mPrefs.getBoolean("defaultTheme", true));
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction;
        switch (item.getItemId()) {
            case R.id.action_refresh:
                changeTheme();
                return true;
            case R.id.favourites:
                return false;
            case R.id.discover:
                transaction = getSupportFragmentManager().beginTransaction();
                activeFragment = ListFragment.newInstance();
                if (getResources().getBoolean(R.bool.isTablet)) {
                    transaction.replace(R.id.home_fragment, activeFragment, "ListFragment");
                } else {
                    transaction.replace(R.id.fragment_container, activeFragment, "ListFragment");
                }
                transaction.commit();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment listFragment = getSupportFragmentManager().findFragmentByTag("ListFragment");
        if (listFragment != null && listFragment.isVisible()) {
            finish();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
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
        getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
        mDefaultTheme = !mDefaultTheme;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("defaultTheme", mDefaultTheme);
        editor.apply();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
