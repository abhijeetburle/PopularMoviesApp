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
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBTrailerWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * Created by abhijeet.burle on 2016/01/27.
 */
public class FetchMovieTrailersTask  extends AsyncTask<Object, Void, List<MovieDBTrailerWrapper>> {

    private final String LOG_TAG = FetchMovieTrailersTask.class.getSimpleName();
    ICallbackLoaded<List<MovieDBTrailerWrapper>> handler;

    @Override
    protected void onPostExecute(List<MovieDBTrailerWrapper> trailerList) {
        if (handler != null && trailerList != null && trailerList.size() > 0) {
            handler.onLoadComplete(trailerList);
        }
    }

    @Override
    protected List<MovieDBTrailerWrapper> doInBackground(Object... params) {
        if (params == null || params.length < 1) {
            return null;
        }
        final String API_KEY = "api_key";
        String movieId = ((MovieDBWrapper)params[0]).strId;
        handler= (ICallbackLoaded<List<MovieDBTrailerWrapper>>)params[1];
        if(movieId!=null) {
            Uri builtUri = Uri.parse(GlobalContants.MOVIEDB_BASE_URL + movieId + "/videos?").buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
            List<MovieDBTrailerWrapper> objTrailers = getMovieTrailers(builtUri.toString());
            if (objTrailers != null) {
                return objTrailers;
            }
        }
        return null;
    }

    private List<MovieDBTrailerWrapper> getMovieTrailers(String strURI) {
        String responseJsonStr = MovieDBDataReader.getDataFromURI(strURI);
        if(responseJsonStr!=null) {
            try {
                return JsonParser.praseTrailers(responseJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return null;
    }



}
