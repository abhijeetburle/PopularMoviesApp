package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks;

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
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.FetchMovieListParam;
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
        }
        Uri builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + sortOrder + "?").buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                .build();
        List<MovieDBWrapper> page0 = getMovieList(builtUri.toString());
        if (page0 != null) {
            return new FetchMovieListParam(params[0].mMovieListAdapter, page0, params[0].sortOrder);
        }
        return null;
    }

    private List<MovieDBWrapper> getMovieList(String strURI) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJsonStr = null;

        try {
            URL url = new URL(strURI);

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
            return praseMovieList(responseJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }


    private List<MovieDBWrapper> praseMovieList(String responseJson)
            throws JSONException {
        if (responseJson == null) {
            return null;
        }

        JSONObject objResponseJson = new JSONObject(responseJson);
        JSONArray jsonMovieListArray = objResponseJson.getJSONArray(GlobalContants.JSON_MOVIE_LIST);

        List<MovieDBWrapper> resultStrs = new ArrayList<MovieDBWrapper>();
        MovieDBWrapper objMovieDBWrapper;
        for (int i = 0; i < jsonMovieListArray.length(); i++) {
            JSONObject jsonMovieRecord = jsonMovieListArray.getJSONObject(i);
            objMovieDBWrapper = new MovieDBWrapper();
            objMovieDBWrapper.posterPath = jsonMovieRecord.getString(GlobalContants.JSON_POSTER_PATH);
            objMovieDBWrapper.isAdult = jsonMovieRecord.getBoolean(GlobalContants.JSON_ADULT);
            objMovieDBWrapper.overview = jsonMovieRecord.getString(GlobalContants.JSON_OVERVIEW);
            objMovieDBWrapper.strReleaseDate = jsonMovieRecord.getString(GlobalContants.JSON_RELEASE_DATE);
            objMovieDBWrapper.strId = jsonMovieRecord.getString(GlobalContants.JSON_ID);
            objMovieDBWrapper.originalTitle = jsonMovieRecord.getString(GlobalContants.JSON_ORIGINAL_TITLE);
            objMovieDBWrapper.originalLanguage = jsonMovieRecord.getString(GlobalContants.JSON_ORIGINAL_LANGAUGE);
            objMovieDBWrapper.title = jsonMovieRecord.getString(GlobalContants.JSON_TITLE);
            objMovieDBWrapper.backdropPath = jsonMovieRecord.getString(GlobalContants.JSON_BACKDROP_PATH);
            objMovieDBWrapper.strPopularity = jsonMovieRecord.getString(GlobalContants.JSON_POPULARITY);
            objMovieDBWrapper.strVoteCount = jsonMovieRecord.getString(GlobalContants.JSON_VOTE_COUNT);
            objMovieDBWrapper.hasVideo = jsonMovieRecord.getBoolean(GlobalContants.JSON_VIDEO);
            objMovieDBWrapper.strVoteAverage = jsonMovieRecord.getString(GlobalContants.JSON_RATING);

            Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBWrapper + "]");
            resultStrs.add(objMovieDBWrapper);
        }
        return resultStrs;

    }
}
