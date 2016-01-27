package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.BuildConfig;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appcallbacks.ICallbackLoaded;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.JsonParser;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBDataReader;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBReviewWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * Created by abhijeet.burle on 2016/01/27.
 */
public class FetchMovieReviewsTask extends AsyncTask<Object, Void, List<MovieDBReviewWrapper>> {

    private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();
    ICallbackLoaded handler;
    @Override
    protected void onPostExecute(List<MovieDBReviewWrapper> reviewList) {
        if (handler!= null && reviewList != null && reviewList.size() > 0) {
            handler.onLoadComplete(reviewList);
        }
    }

    @Override
    protected List<MovieDBReviewWrapper> doInBackground(Object... params) {
        if (params == null || params.length < 2) {
            return null;
        }
        final String API_KEY = "api_key";
        String movieId = ((MovieDBWrapper)params[0]).strId;
        handler = ((ICallbackLoaded)params[1]);
        if(movieId!=null) {
            Uri builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + movieId + "/reviews?").buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
            List<MovieDBReviewWrapper> objReviews = getMovieReviews(builtUri.toString());
            if (objReviews != null) {
                return objReviews;
            }
        }
        return null;
    }

    private List<MovieDBReviewWrapper> getMovieReviews(String strURI) {
        String responseJsonStr = MovieDBDataReader.getDataFromURI(strURI);
        if(responseJsonStr!=null) {
            try {
                return JsonParser.praseReviews(responseJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return null;
    }



}
