package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appcallbacks;

import java.util.List;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBReviewWrapper;

/**
 * Created by abhijeet.burle on 2016/01/27.
 */
public interface ICallbackLoaded<E> {
    public void onLoadComplete(E objLodaedData);
}
