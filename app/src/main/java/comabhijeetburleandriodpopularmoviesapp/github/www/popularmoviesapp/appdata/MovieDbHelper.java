package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;

/**
 * Created by abhijeet.burle on 2016/02/01.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavouriteMovieEntry.TABLE_NAME + " (" +
                FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID + " TEXT, " +
                FavouriteMovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                // Will Only store moviedb id and poster path of favourite movies
                // for details will fetch it from moviedb it self
                /*
                FavouriteMovieEntry.COLUMN_IS_ADULT + " BOOLEAN DEFAULT FALSE, " +
                FavouriteMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                FavouriteMovieEntry.COLUMN_RELEASE_DATE + " DATE, " +
                FavouriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                FavouriteMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                FavouriteMovieEntry.COLUMN_TITLE + " TEXT, " +
                FavouriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                FavouriteMovieEntry.COLUMN_POPULARITY + " TEXT, " +
                FavouriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER , " +
                FavouriteMovieEntry.COLUMN_HAS_VIDEO + " BOOLEAN DEFAULT FALSE, " +
                FavouriteMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                */
                FavouriteMovieEntry.COLUMN_REG_DATE + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP " +
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieEntry.TABLE_NAME);
    }
}
