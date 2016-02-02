package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.BuildConfig;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.R;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.FetchMovieListParam;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.JsonParser;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBDataReader;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * Created by abhijeet.burle on 2016/01/21.
 */
public class FetchMovieListTask extends AsyncTask<FetchMovieListParam, Void, FetchMovieListParam> {

    private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();

    @Override
    protected void onPostExecute(FetchMovieListParam param) {
        if (param != null && param.movieList != null) {
            param.mMovieListAdapter.clear();
            for (MovieDBWrapper movie : param.movieList) {
                param.mMovieListAdapter.add(movie);
            }
        }
    }

    @Override
    protected FetchMovieListParam doInBackground(FetchMovieListParam... params) {
        if (params == null || params.length < 1) {
            return null;
        }

        final String API_KEY = "api_key";
        String sortOrder = "popular";

        if ("sort-by-rating".equalsIgnoreCase(params[0].sortOrder)) {
            sortOrder = "top_rated";
        }else if ("sort-by-fav".equalsIgnoreCase(params[0].sortOrder)) {
            sortOrder = "fav";
        }
        if("fav".equals(sortOrder)){
            // Get Fav from DB
            Cursor cursor = params[0].mMovieListAdapter.getContext().getContentResolver().query(
                    FavouriteMovieEntry.buildFavoriteListUri(),
                    null,
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MovieContract.FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID);

            String movieDBId;
            Uri builtUri;
            List<MovieDBWrapper> page0 = new ArrayList<MovieDBWrapper>();
            for ( int i = 0; i < cursor.getCount(); i++, cursor.moveToNext() ) {
                movieDBId = cursor.getString(idx);
                builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + movieDBId).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();
                page0.add(getMovie(builtUri.toString()));
            }
            cursor.close();
            if (page0 != null) {
                return new FetchMovieListParam(params[0].mMovieListAdapter, page0, params[0].sortOrder);
            }
        }else {
            Uri builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + sortOrder + "?").buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
            List<MovieDBWrapper> page0 = getMovieList(builtUri.toString());
            if (page0 != null) {
                return new FetchMovieListParam(params[0].mMovieListAdapter, page0, params[0].sortOrder);
            }
        }
        return null;
    }

    private List<MovieDBWrapper> getMovieList(String strURI) {
        String responseJsonStr = MovieDBDataReader.getDataFromURI(strURI);
        if(responseJsonStr!=null) {
            try {
                return JsonParser.praseMovieList(responseJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return null;
    }

    private MovieDBWrapper getMovie(String strURI) {
        String responseJsonStr = MovieDBDataReader.getDataFromURI(strURI);
        if(responseJsonStr!=null) {
            try {
                return JsonParser.praseMovie(responseJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return null;
    }
}
