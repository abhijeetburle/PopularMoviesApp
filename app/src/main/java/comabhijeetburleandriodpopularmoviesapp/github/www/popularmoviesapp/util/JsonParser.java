package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;

/**
 * Created by abhijeet.burle on 2016/01/27.
 */
public class JsonParser {
    private static final String LOG_TAG = JsonParser.class.getSimpleName();
    private static final int RECORDS_PERPAGE = 15;

    public static MovieDBWrapper praseMovie(String responseJson)
            throws JSONException {
        if (responseJson == null) {
            return null;
        }

        JSONObject jsonMovieRecord = new JSONObject(responseJson);

        MovieDBWrapper objMovieDBWrapper = new MovieDBWrapper(jsonMovieRecord);

        Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBWrapper + "]");
        return objMovieDBWrapper;
    }

    public static List<MovieDBWrapper> praseMovieList(String responseJson)
            throws JSONException {
        if (responseJson == null) {
            return null;
        }

        JSONObject objResponseJson = new JSONObject(responseJson);
        JSONArray jsonMovieListArray = objResponseJson.getJSONArray(GlobalContants.JSON_MOVIE_LIST);

        List<MovieDBWrapper> resultStrs = new ArrayList<MovieDBWrapper>();
        MovieDBWrapper objMovieDBWrapper;
        for (int i = 0; i < jsonMovieListArray.length() && i < RECORDS_PERPAGE; i++) {
            JSONObject jsonMovieRecord = jsonMovieListArray.getJSONObject(i);
            objMovieDBWrapper = new MovieDBWrapper(jsonMovieRecord);
            Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBWrapper + "]");
            resultStrs.add(objMovieDBWrapper);
        }
        return resultStrs;
    }

    public static List<MovieDBReviewWrapper> praseReviews(String responseJson)
        throws JSONException {
            if (responseJson == null) {
                return null;
            }

            JSONObject objResponseJson = new JSONObject(responseJson);
            JSONArray jsonMovieListArray = objResponseJson.getJSONArray(GlobalContants.JSON_REVIEW_LIST);

            List<MovieDBReviewWrapper> resultStrs = new ArrayList<MovieDBReviewWrapper>();
            MovieDBReviewWrapper objMovieDBReviewWrapper;
            for (int i = 0; i < jsonMovieListArray.length(); i++) {
                JSONObject jsonMovieRecord = jsonMovieListArray.getJSONObject(i);
                objMovieDBReviewWrapper = new MovieDBReviewWrapper();
                objMovieDBReviewWrapper.author = jsonMovieRecord.getString(GlobalContants.JSON_REVIEW_AUTHOR);
                objMovieDBReviewWrapper.content = jsonMovieRecord.getString(GlobalContants.JSON_REVIEW_CONTENT);
                objMovieDBReviewWrapper.url = jsonMovieRecord.getString(GlobalContants.JSON_REVIEW_URL);


                Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBReviewWrapper + "]");
                resultStrs.add(objMovieDBReviewWrapper);
            }
            return resultStrs;
        }

    public static List<MovieDBTrailerWrapper> praseTrailers(String responseJson) throws JSONException {
        if (responseJson == null) {
            return null;
        }

        JSONObject objResponseJson = new JSONObject(responseJson);
        JSONArray jsonMovieListArray = objResponseJson.getJSONArray(GlobalContants.JSON_TRAILER_LIST);

        List<MovieDBTrailerWrapper> resultStrs = new ArrayList<MovieDBTrailerWrapper>();
        MovieDBTrailerWrapper objMovieDBTrailerWrapper;
        for (int i = 0; i < jsonMovieListArray.length(); i++) {
            JSONObject jsonMovieRecord = jsonMovieListArray.getJSONObject(i);
            objMovieDBTrailerWrapper = new MovieDBTrailerWrapper();
            objMovieDBTrailerWrapper.key = jsonMovieRecord.getString(GlobalContants.JSON_TRAILER_KEY);
            objMovieDBTrailerWrapper.site = jsonMovieRecord.getString(GlobalContants.JSON_TRAILER_SITE);

            Log.d(LOG_TAG, "Information ::  Movie[" + objMovieDBTrailerWrapper + "]");
            resultStrs.add(objMovieDBTrailerWrapper);
        }
        return resultStrs;
    }

}