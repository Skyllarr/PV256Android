package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPreferences mPrefs;
    private Boolean mDefaultTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);
        setRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                changeTheme();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MyData mDataSet[] = {
                new MyData("Miss Peregrine's home for peculiar children",R.drawable.miss_peregrines_home_for_peculiar_children),
                new MyData("Deepwater Horizon",R.drawable.deepwater_horizon),
                new MyData("Doctor Strange",R.drawable.doctor_strange),
                new MyData("Inferno",R.drawable.inferno),
                new MyData("Jack Reacher",R.drawable.jack_reacher),
                new MyData("Rings",R.drawable.rings)};
        mAdapter = new MyAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setTheme(){
        mPrefs = this.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        mDefaultTheme = mPrefs.getBoolean("defaultTheme", true);
        if (mDefaultTheme)
            this.setTheme(R.style.AppBaseTheme);
        else
            this.setTheme(R.style.AppBaseTheme_AppBaseThemeBlue);
    }

    public void changeTheme() {
        mDefaultTheme = !mDefaultTheme;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("defaultTheme", mDefaultTheme);
        editor.commit();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
