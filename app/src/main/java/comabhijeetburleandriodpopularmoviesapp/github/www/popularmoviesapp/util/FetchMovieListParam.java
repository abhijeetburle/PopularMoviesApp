package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by abhijeet.burle on 2016/01/21.
 */
public final class FetchMovieListParam {
    public final String sortOrder;
    public final List<MovieDBWrapper> movieList;
    public final ArrayAdapter mMovieListAdapter;

    public FetchMovieListParam(ArrayAdapter mMovieListAdapter, List<MovieDBWrapper> movieList, String sortOrder) {
        this.movieList = movieList;
        this.mMovieListAdapter = mMovieListAdapter;
        this.sortOrder = sortOrder;
    }
}
