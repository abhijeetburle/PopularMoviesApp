package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appcallbacks.ICallbackLoaded;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks.FetchMovieListTask;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks.FetchMovieReviewsTask;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.apptasks.FetchMovieTrailersTask;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.FetchMovieListParam;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBReviewWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBTrailerWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment{

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    @Bind(R.id.imgPoster)  ImageView posterView;
    @Bind(R.id.txtPlotSynopsis)  TextView  txtPlotSynopsis;
    @Bind(R.id.txtUserRating) TextView txtUserRating;
    @Bind(R.id.txtReleaseDate) TextView txtReleaseDate;
    @Bind(R.id.lblTrailer) TextView lblTrailer;
    @Bind(R.id.scrollviewTrailer) HorizontalScrollView scrollviewTrailer;
    @Bind(R.id.listview_trailer) ViewGroup listview_trailer;
    @Bind(R.id.lblReview) TextView lblReview;
    @Bind(R.id.listview_review) ViewGroup listview_review;

    ICallbackLoaded<List<MovieDBReviewWrapper>> mReviewCallback;
    ICallbackLoaded<List<MovieDBTrailerWrapper>> mTrailerCallback;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        MovieDBWrapper movie;
        if(args==null || args.getParcelable(GlobalContants.MOVIE_ITEM)==null){
            movie = new MovieDBWrapper();
        }else{
            movie =  args.getParcelable(GlobalContants.MOVIE_ITEM);
        }


        // <!-- original title -->
        if(movie.title!=null)
            ((TextView) rootView.findViewById(R.id.txtTitle))
                .setText(movie.title);
        // <!-- movie poster image -->
        if(movie.posterPath!=null) {
            try {
                Picasso.with(getActivity())
                        .load(GlobalContants.POSTER_BASE_PATH + movie.posterPath)
                        .placeholder(R.drawable.placeholder_loading)
                        .error(R.drawable.placeholder_error)
                        .into(posterView);
            } catch (Throwable t) {
                Log.e(LOG_TAG, "onCreateView: ",t);
            }
        }
        // <!-- A plot synopsis (called overview in the api) -->
        if(movie.overview!=null)
            txtPlotSynopsis.setText(movie.overview);
        // <!-- user rating (called vote_average in the api) -->
        if(movie.strReleaseDate!=null)
            txtUserRating .setText(movie.strVoteAverage+"/10");
        // <!-- release date      -->
        if(movie.strReleaseDate!=null)
            txtReleaseDate.setText(movie.strReleaseDate);

        // DemoCode

        List<MovieDBTrailerWrapper> objTrailers = new ArrayList<MovieDBTrailerWrapper>();
        List<MovieDBReviewWrapper> objReviews = new ArrayList<MovieDBReviewWrapper>();


        MovieDBTrailerWrapper objTrailer = new MovieDBTrailerWrapper();
        objTrailer.site="YouTube";
        objTrailer.key="SUXWAEX2jlg";
        objTrailers.add(objTrailer);
        objTrailers.add(objTrailer);

        MovieDBReviewWrapper objReview = new MovieDBReviewWrapper();
        objReview.author="Travis Bell";
        objReview.content="I felt like this was a tremendous end to Nolan's Batman trilogy";
        objReviews.add(objReview);
        objReviews.add(objReview);

        //Democode end


        mReviewCallback = new ICallbackLoaded<List<MovieDBReviewWrapper>>() {
            @Override
            public void onLoadComplete(List<MovieDBReviewWrapper> objReviews) {
                boolean hasReviews = !objReviews.isEmpty();
                lblReview.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
                listview_review.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
                if (hasReviews) {
                    addReviews(objReviews);
                }
            }
        };
        mTrailerCallback = new ICallbackLoaded<List<MovieDBTrailerWrapper>>() {
            @Override
            public void onLoadComplete(List<MovieDBTrailerWrapper> objTrailers) {
                boolean hasTrailers = !objTrailers.isEmpty();
                lblTrailer.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
                scrollviewTrailer.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
                if (hasTrailers) {
                    addTrailers(objTrailers);
                }
            }
        };

        if(movie.strId!=null&&!"".equals(movie.strId)) {
            new FetchMovieReviewsTask().execute(movie, mReviewCallback);
            new FetchMovieTrailersTask().execute(movie, mTrailerCallback);
        }

        return rootView;
    }

    private void addTrailers(List<MovieDBTrailerWrapper> objTrailers) {
        listview_trailer.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Picasso picasso = Picasso.with(getActivity());
        for (MovieDBTrailerWrapper objTrailer : objTrailers) {
            ViewGroup thumbContainer = (ViewGroup) inflater.inflate(R.layout.list_item_trailer, listview_trailer,
                    false);
            ImageView thumbView = (ImageView) thumbContainer.findViewById(R.id.imgVideoThumbnail);
            thumbView.setTag(objTrailer.getUrl());
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent=new Intent(Intent.ACTION_VIEW,
                                Uri.parse((String)v.getTag()));
                        startActivity(intent);
                    }catch (ActivityNotFoundException ex){
                        Log.e(LOG_TAG, "onClick: ", ex);
                    }
                }
            });
            picasso
                    .load(objTrailer.getThumbnailUrl())
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .resizeDimen(R.dimen.imgVideoThumbnailWidth, R.dimen.imgVideoThumbnailHeight)
                    .centerCrop()
                    .into(thumbView);
            listview_trailer.addView(thumbContainer);
        }
    }

    private void addReviews(List<MovieDBReviewWrapper> objReviews) {
        listview_review.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (MovieDBReviewWrapper objReview : objReviews) {
            ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.list_item_review, listview_review,
                    false);
            TextView reviewAuthor = (TextView) reviewContainer.findViewById(R.id.txtReviewAuthor);
            TextView reviewContent = (TextView) reviewContainer.findViewById(R.id.txtReviewContent);
            reviewAuthor.setText(objReview.author);
            reviewContent.setText(objReview.content.replace("\n\n", " ").replace("\n", " "));
            reviewContainer.setTag(objReview);
            listview_review.addView(reviewContainer);
        }
    }
}
