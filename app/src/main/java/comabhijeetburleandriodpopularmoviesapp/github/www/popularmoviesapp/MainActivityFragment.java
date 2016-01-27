package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appadapters.MovieListAdapter;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appcallbacks.ICallbackSelected;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks.FetchMovieListTask;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.FetchMovieListParam;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    @Bind(R.id.listview_movie) AbsListView objlistItemView;
    MovieListAdapter mMovieListAdapter;

    private boolean mUseSelectedLayout;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View objRootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, objRootView);
        List<MovieDBWrapper> list_item_movie_thumbnail = new ArrayList<MovieDBWrapper>();
        mMovieListAdapter = new MovieListAdapter(getActivity(),
                R.layout.list_item_movie, R.id.imgViewMoviePosterThumbnail,
                list_item_movie_thumbnail);



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
                MovieDBWrapper movie = (MovieDBWrapper) ((AbsListView) parent).getAdapter().getItem(position);
                ((ICallbackSelected<MovieDBWrapper>)getActivity()).onItemSelected(movie);
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        mMovieListAdapter.setUseSelectedLayout(mUseSelectedLayout);


        updateView();
        return objRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void setmUseSelectedLayout(boolean useSelectedLayout) {
        mUseSelectedLayout = useSelectedLayout;
        if (mMovieListAdapter != null) {
            mMovieListAdapter.setUseSelectedLayout(mUseSelectedLayout);
        }
    }

    public void updateView() {
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = pref.getString(getString(R.string.pref_sort_order_key),
                    getString(R.string.pref_sort_order_default));
            new FetchMovieListTask().execute(new FetchMovieListParam(mMovieListAdapter, null, sortOrder));

        } catch (Exception e) {
            Log.e(LOG_TAG, "Movie list fetching failed", e);
        }
    }
}
