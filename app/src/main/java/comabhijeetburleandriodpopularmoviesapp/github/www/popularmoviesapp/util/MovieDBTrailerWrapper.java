package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

import android.support.annotation.NonNull;

/**
 * Created by abhijeet.burle on 2016/01/22.
 */
public class MovieDBTrailerWrapper extends MovieDBBaseWrapper{
    public static final String SITE_YOUTUBE = "YouTube";

    public String site;
    public String key;

    public String getUrl() {
        if (SITE_YOUTUBE.equalsIgnoreCase(site)) {
            return String.format("http://www.youtube.com/watch?v=%1$s", key);
        } else {
            throw new UnsupportedOperationException("Only YouTube is supported!");
        }
    }

    public String getThumbnailUrl() {
        if (SITE_YOUTUBE.equalsIgnoreCase(site)) {
            return String.format("http://img.youtube.com/vi/%1$s/0.jpg", key);
        } else {
            throw new UnsupportedOperationException("Only YouTube is supported!");
        }
    }
}
