package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;

/**
 * Created by abhijeet.burle on 2016/02/02.
 */
public class TestMovieProvider  extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from FavouriteMovie table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());

        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: FavouriteMovie not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {
        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list
        String type = mContext.getContentResolver().getType(FavouriteMovieEntry.buildFavoriteListUri());
        Log.i(LOG_TAG, "["+FavouriteMovieEntry.buildFavoriteListUri()+"]["+type+"]");
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_TYPE",
                FavouriteMovieEntry.CONTENT_TYPE, type);

        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607
        type = mContext.getContentResolver().getType(
                FavouriteMovieEntry.buildFavoriteMovieUri("140607"));
        Log.i(LOG_TAG, "["+FavouriteMovieEntry.buildFavoriteMovieUri("140607")+"]["+type+"]");
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_ITEM_TYPE",
                FavouriteMovieEntry.CONTENT_ITEM_TYPE, type);

        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607/favourite_status
        type = mContext.getContentResolver().getType(
                FavouriteMovieEntry.buildCheckIsFavourite("140607"));
        Log.i(LOG_TAG, "["+FavouriteMovieEntry.buildCheckIsFavourite("140607") +"]["+type+"]");
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_ITEM_TYPE",
                FavouriteMovieEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testBasicQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieValues();
        long movieId = TestUtilities.insertMoveiRecord(mContext, testValues);
        assertTrue("Unable to Insert FavouriteMovieEntry into the Database", movieId != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicQuery", movieCursor, testValues);
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavouriteMovieEntry.buildFavoriteListUri(), true, tco);
        Uri movieUri = mContext.getContentResolver()
                .insert(FavouriteMovieEntry.buildFavoriteMovieUri(
                                testValues.get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID)
                                        .toString()
                        ),
                        testValues);

        assertTrue(movieUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating FavouriteMovieEntry.",
                cursor, testValues);
        /*
        // Get the joined Weather data for a specific date
        weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithDate(TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location data for a specific date.",
                weatherCursor, weatherValues);
         */
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our delete.
        TestUtilities.TestContentObserver objObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavouriteMovieEntry.CONTENT_URI, true, objObserver);

        deleteAllRecordsFromProvider();
        objObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(objObserver);
    }
}
