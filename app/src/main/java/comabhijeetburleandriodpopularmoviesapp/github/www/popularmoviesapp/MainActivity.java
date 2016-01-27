package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appcallbacks.ICallbackSelected;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

public class MainActivity extends AppCompatActivity implements ICallbackSelected<MovieDBWrapper> {

    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";
    boolean mTwoPane = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.fragment_detail_container)!=null) {
            mTwoPane = true;
            if(savedInstanceState == null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_detail_container, new DetailActivityFragment())
                        .commit();
            }
        }else{
            mTwoPane = false;
        }
        MainActivityFragment objMainActivityFragment =
                ((MainActivityFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.fragment));
        objMainActivityFragment.setmUseSelectedLayout(!mTwoPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieDBWrapper movie) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(GlobalContants.MOVIE_ITEM, movie);

            DetailActivityFragment objDetailActivityFragment = new DetailActivityFragment();
            objDetailActivityFragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_detail_container, objDetailActivityFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }else{
            Intent objDetailIntent = new Intent(this, DetailActivity.class)
                    .putExtra(GlobalContants.MOVIE_ITEM, movie);
            startActivity(objDetailIntent);
        }
    }
}
