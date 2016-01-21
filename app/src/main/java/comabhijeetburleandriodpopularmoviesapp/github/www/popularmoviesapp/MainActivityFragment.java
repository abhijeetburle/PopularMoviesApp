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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    ArrayAdapter mMovieListAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<MovieDBWrapper> list_item_movie_thumbnail = new ArrayList<MovieDBWrapper>();
        mMovieListAdapter = new MovieListAdapter(getActivity(),
                R.layout.list_item_movie, R.id.imgViewMoviePosterThumbnail,
                list_item_movie_thumbnail);

        View objRootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView objlistItemView = (ListView) objRootView.findViewById(R.id.listview_movie);
        objlistItemView.setAdapter(mMovieListAdapter);

        objlistItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDBWrapper movie = (MovieDBWrapper) mMovieListAdapter.getItem(position);
                Intent objDetailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(GlobalContants.JSON_TITLE, movie.title)
                        .putExtra(GlobalContants.JSON_POSTER_PATH, movie.posterPath)
                        .putExtra(GlobalContants.JSON_OVERVIEW, movie.overview)
                        .putExtra(GlobalContants.JSON_RATING, movie.strVoteAverage)
                        .putExtra(GlobalContants.JSON_RELEASE_DATE, movie.strReleaseDate);
                startActivity(objDetailIntent);
            }
        });

        updateView();

        return objRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    public void updateView(){
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = pref.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));
            new FetchMovieListTask().execute(sortOrder);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Movie list fetching failed", e);
        }
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, List<MovieDBWrapper>> {

        private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

        private List<MovieDBWrapper> praseMovieList(String responseJson, int fetchCount)
                throws JSONException {

            JSONObject objResponseJson = new JSONObject(responseJson);
            JSONArray jsonMovieListArray = objResponseJson.getJSONArray(GlobalContants.JSON_MOVIE_LIST);

            List<MovieDBWrapper> resultStrs = new ArrayList<MovieDBWrapper>();
            MovieDBWrapper objMovieDBWrapper;
            for(int i = 0; i < jsonMovieListArray.length(); i++) {
                JSONObject jsonMovieRecord = jsonMovieListArray.getJSONObject(i);
                objMovieDBWrapper = new MovieDBWrapper();
                objMovieDBWrapper.posterPath  = jsonMovieRecord.getString(GlobalContants.JSON_POSTER_PATH);
                objMovieDBWrapper.isAdult  = jsonMovieRecord.getBoolean(GlobalContants.JSON_ADULT);
                objMovieDBWrapper.overview  = jsonMovieRecord.getString(GlobalContants.JSON_OVERVIEW);
                objMovieDBWrapper.strReleaseDate  = jsonMovieRecord.getString(GlobalContants.JSON_RELEASE_DATE);
                objMovieDBWrapper.strId  = jsonMovieRecord.getString(GlobalContants.JSON_ID);
                objMovieDBWrapper.originalTitle = jsonMovieRecord.getString(GlobalContants.JSON_ORIGINAL_TITLE);
                objMovieDBWrapper.originalLanguage  = jsonMovieRecord.getString(GlobalContants.JSON_ORIGINAL_LANGAUGE);
                objMovieDBWrapper.title  = jsonMovieRecord.getString(GlobalContants.JSON_TITLE);
                objMovieDBWrapper.backdropPath  = jsonMovieRecord.getString(GlobalContants.JSON_BACKDROP_PATH);
                objMovieDBWrapper.strPopularity  = jsonMovieRecord.getString(GlobalContants.JSON_POPULARITY);
                objMovieDBWrapper.strVoteCount  = jsonMovieRecord.getString(GlobalContants.JSON_VOTE_COUNT);
                objMovieDBWrapper.hasVideo  = jsonMovieRecord.getBoolean(GlobalContants.JSON_VIDEO);
                objMovieDBWrapper.strVoteAverage  = jsonMovieRecord.getString(GlobalContants.JSON_RATING);

                Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBWrapper + "]");
                resultStrs.add(objMovieDBWrapper);
            }
            return resultStrs;

        }
        @Override
        protected List<MovieDBWrapper> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String responseJsonStr = null;

            try {
                final String API_KEY = "api_key";
                String sortOrder = "popular";
                if(params!=null && params.length > 0){
                    if(getString(R.string.pref_sort_order_rating).equalsIgnoreCase(params[0])){
                        sortOrder = "top_rated";
                    }
                }

                Uri builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + sortOrder + "?").buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                responseJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return praseMovieList(responseJsonStr, 5);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDBWrapper> movieList) {
            if (movieList != null) {
                mMovieListAdapter.clear();
                for(MovieDBWrapper movie : movieList) {
                    mMovieListAdapter.add(movie);
                }
            }
        }
    }
}
