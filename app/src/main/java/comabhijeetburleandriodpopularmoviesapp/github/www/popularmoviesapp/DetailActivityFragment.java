package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBReviewWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBTrailerWrapper;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    @Bind(R.id.imgPoster)  ImageView posterView;
    @Bind(R.id.txtPlotSynopsis)  TextView  txtPlotSynopsis;
    @Bind(R.id.txtUserRating) TextView txtUserRating;
    @Bind(R.id.txtReleaseDate) TextView txtReleaseDate;
    @Bind(R.id.lblTrailer) TextView lblTrailer;
    @Bind(R.id.scrollviewTrailer) HorizontalScrollView scrollviewTrailer;
    @Bind(R.id.listview_trailer) ViewGroup listview_trailer;
    @Bind(R.id.lblReview) TextView lblReview;
    @Bind(R.id.listview_review) ViewGroup listview_review;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        // <!-- original title -->
        ((TextView) rootView.findViewById(R.id.txtTitle))
                .setText(intent.getStringExtra(GlobalContants.JSON_TITLE));
        // <!-- movie poster image -->
        Picasso.with(getActivity())
                .load(GlobalContants.POSTER_BASE_PATH + intent.getExtras().get(GlobalContants.JSON_POSTER_PATH))
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.placeholder_error)
                .error(R.drawable.ic_notifications_black_24dp)
                .into(posterView);

        // <!-- A plot synopsis (called overview in the api) -->
        txtPlotSynopsis.setText(intent.getStringExtra(GlobalContants.JSON_OVERVIEW));
        // <!-- user rating (called vote_average in the api) -->
        txtUserRating .setText("Rating: " + intent.getStringExtra(GlobalContants.JSON_RATING));
        // <!-- release date      -->
        txtReleaseDate.setText("Releases on: " + intent.getStringExtra(GlobalContants.JSON_RELEASE_DATE));

        //List<MovieDBTrailerWrapper> objTrailers = objMovie.getTrailers();
        //List<MovieDBReviewWrapper> objReviews = objMovie.getReviews();
        List<MovieDBTrailerWrapper> objTrailers = new ArrayList<MovieDBTrailerWrapper>();
        List<MovieDBReviewWrapper> objReviews = new ArrayList<MovieDBReviewWrapper>();

        // DemoCode
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

        boolean hasTrailers = !objTrailers.isEmpty();
        lblTrailer.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        scrollviewTrailer.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        if (hasTrailers) {
            addTrailers(objTrailers);
        }
        boolean hasReviews = !objReviews.isEmpty();
        lblReview.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        listview_review.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        if (hasReviews) {
            addReviews(objReviews);
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
            //thumbView.setOnClickListener(this);
            picasso
                    .load(objTrailer.getThumbnailUrl())
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
            //reviewContainer.setOnClickListener(this);
            reviewContainer.setTag(objReview);
            listview_review.addView(reviewContainer);
        }
    }

}
