package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by abhijeet.burle on 2016/02/01.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE = "favourite";

    public static final class FavouriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_THEMOVIEDB_ID = "themoviedb_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_IS_ADULT = "is_adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE ="original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE ="original_language";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY="popularity";
        public static final String COLUMN_VOTE_COUNT="vote_count";
        public static final String COLUMN_HAS_VIDEO="has_video";
        public static final String COLUMN_VOTE_AVERAGE="vote_average";
        public static final String COLUMN_REG_DATE = "reg_date";

        public static Uri buildFavoriteListUri() {
            // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list
            return CONTENT_URI.buildUpon().appendPath("list").build();
        }

        public static Uri buildFavoriteMovieUri(String strTheMovieDBId) {
            // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607
            return CONTENT_URI.buildUpon().appendPath("list").appendPath(strTheMovieDBId).build();
        }

        public static Uri buildCheckIsFavourite(String strTheMovieDBId) {
            // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607/favourite_status
            return CONTENT_URI.buildUpon().appendPath("list").appendPath(strTheMovieDBId).appendPath("favourite_status").build();
        }
    }
}
