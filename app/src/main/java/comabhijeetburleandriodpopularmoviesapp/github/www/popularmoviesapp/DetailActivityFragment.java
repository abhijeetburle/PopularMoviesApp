package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // <!-- original title -->
        ((TextView) rootView.findViewById(R.id.txtTitle))
                .setText(intent.getStringExtra(GlobalContants.JSON_TITLE));
        // <!-- movie poster image -->
        ImageView posterView = (ImageView) rootView.findViewById(R.id.imgPoster);
        Picasso.with(getActivity()).load(GlobalContants.POSTER_BASE_PATH +
                                            intent.getExtras().get(GlobalContants.JSON_POSTER_PATH))
                                   .into(posterView);
        // <!-- A plot synopsis (called overview in the api) -->
        ((TextView) rootView.findViewById(R.id.txtPlotSynopsis))
                .setText(intent.getStringExtra(GlobalContants.JSON_OVERVIEW));
        // <!-- user rating (called vote_average in the api) -->
        ((TextView) rootView.findViewById(R.id.txtUserRating))
                .setText("Rating: " + intent.getStringExtra(GlobalContants.JSON_RATING));
        // <!-- release date      -->
        ((TextView) rootView.findViewById(R.id.txtReleaseDate))
                .setText("Releases on: " + intent.getStringExtra(GlobalContants.JSON_RELEASE_DATE));

        return rootView;
    }
}
