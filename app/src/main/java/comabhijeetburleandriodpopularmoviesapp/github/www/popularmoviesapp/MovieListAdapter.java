package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.globalconstants.GlobalContants;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;

/**
 * Created by abhijeet.burle on 2016/01/20.
 */
public class MovieListAdapter extends ArrayAdapter<MovieDBWrapper>
{
    Context context;
    int layoutResourceId;
    int imageResourceId;
    final List<MovieDBWrapper> movies;

    class MovieItemHolder {
        ImageView image;
    }

    public MovieListAdapter(Context context, int layoutResourceId, int imageResourceId, List<MovieDBWrapper> movies){
        super(context, layoutResourceId, movies);
        this.layoutResourceId = layoutResourceId;
        this.imageResourceId = imageResourceId;
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView ( int position, View objView, ViewGroup parent){
        if(movies!=null) {
            MovieItemHolder holder = null;

            if (objView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                objView = inflater.inflate(layoutResourceId, parent, false);

                holder = new MovieItemHolder();
                holder.image = (ImageView) objView.findViewById(imageResourceId);
                objView.setTag(holder);
            } else {
                holder = (MovieItemHolder) objView.getTag();
            }


            MovieDBWrapper item = movies.get(position);
            Picasso.with(context).load(GlobalContants.THUMBNAIL_BASE_PATH + item.posterPath).into(holder.image);
        }
        return objView;
    }
}
