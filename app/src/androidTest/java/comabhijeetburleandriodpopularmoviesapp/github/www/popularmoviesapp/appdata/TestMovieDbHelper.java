package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;

/**
 * Created by abhijeet.burle on 2016/02/01.
 */
public class TestMovieDbHelper extends AndroidTestCase {
    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    public void setUp() {
        deleteTheDatabase();
    }
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(FavouriteMovieEntry.TABLE_NAME);
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());
        c = db.rawQuery("PRAGMA table_info(" + FavouriteMovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumns = new HashSet<String>();
        movieColumns.add(FavouriteMovieEntry._ID);
        movieColumns.add(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID);
        movieColumns.add(FavouriteMovieEntry.COLUMN_POSTER_PATH);
        /*
        movieColumns.add(FavouriteMovieEntry.COLUMN_IS_ADULT);
        movieColumns.add(FavouriteMovieEntry.COLUMN_OVERVIEW);
        movieColumns.add(FavouriteMovieEntry.COLUMN_RELEASE_DATE);
        movieColumns.add(FavouriteMovieEntry.COLUMN_ORIGINAL_TITLE);
        movieColumns.add(FavouriteMovieEntry.COLUMN_ORIGINAL_LANGUAGE);
        movieColumns.add(FavouriteMovieEntry.COLUMN_TITLE);
        movieColumns.add(FavouriteMovieEntry.COLUMN_BACKDROP_PATH);
        movieColumns.add(FavouriteMovieEntry.COLUMN_POPULARITY);
        movieColumns.add(FavouriteMovieEntry.COLUMN_VOTE_COUNT);
        movieColumns.add(FavouriteMovieEntry.COLUMN_HAS_VIDEO);
        movieColumns.add(FavouriteMovieEntry.COLUMN_VOTE_AVERAGE);
        */
        movieColumns.add(FavouriteMovieEntry.COLUMN_REG_DATE);
        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumns.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns [" +movieColumns+"" ,
                movieColumns.isEmpty());
        db.close();
    }
    public void testMovieTable() {
        ContentValues testValues = TestUtilities.createMovieStarWarsValues();
        TestUtilities.insertMoveiRecord(mContext, testValues);

        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavouriteMovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from movie query", cursor.moveToFirst() );

        TestUtilities.validateMovieRecord("Error: Movie Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from movie query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

}
