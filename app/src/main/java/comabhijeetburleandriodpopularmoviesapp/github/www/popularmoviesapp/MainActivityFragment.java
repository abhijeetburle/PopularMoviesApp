package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks.FetchMovieListTask;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.FetchMovieListParam;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<MovieDBWrapper> list_item_movie_thumbnail = new ArrayList<MovieDBWrapper>();
        ArrayAdapter mMovieListAdapter = new MovieListAdapter(getActivity(),
                R.layout.list_item_movie, R.id.imgViewMoviePosterThumbnail,
                list_item_movie_thumbnail);

        View objRootView = inflater.inflate(R.layout.fragment_main, container, false);
        AbsListView objlistItemView = (AbsListView) objRootView.findViewById(R.id.listview_movie);
        objlistItemView.setAdapter(mMovieListAdapter);
/*
        objlistItemView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(LOG_TAG, "[onScrollStateChanged]["+view.getId()+"]["+scrollState+"]");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(LOG_TAG, "[onScroll]["+view.getId()+"]["+firstVisibleItem+"]["+visibleItemCount+"]["+totalItemCount+"]");
            }
        });
*/
        objlistItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDBWrapper movie = (MovieDBWrapper) ((AbsListView) view).getAdapter().getItem(position);
                Intent objDetailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(GlobalContants.JSON_TITLE, movie.title)
                        .putExtra(GlobalContants.JSON_POSTER_PATH, movie.posterPath)
                        .putExtra(GlobalContants.JSON_OVERVIEW, movie.overview)
                        .putExtra(GlobalContants.JSON_RATING, movie.strVoteAverage)
                        .putExtra(GlobalContants.JSON_RELEASE_DATE, movie.strReleaseDate);
                startActivity(objDetailIntent);
            }
        });

        updateView(objRootView);
        return objRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView(getView());
    }

    public void updateView(View objRootView) {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = pref.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));
            AbsListView objlistItemView = (AbsListView) objRootView.findViewById(R.id.listview_movie);
            new FetchMovieListTask().execute(new FetchMovieListParam(((ArrayAdapter) objlistItemView.getAdapter()), null, sortOrder));

        } catch (Exception e) {
            Log.e(LOG_TAG, "Movie list fetching failed", e);
        }
    }


}
