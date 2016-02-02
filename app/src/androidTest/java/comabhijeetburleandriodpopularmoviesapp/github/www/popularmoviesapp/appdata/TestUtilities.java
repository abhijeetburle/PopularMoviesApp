package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.DateFormatterUtil;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.MovieDBWrapper;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util.PollingCheck;

/**
 * Created by abhijeet.burle on 2016/02/01.
 */
public class TestUtilities extends AndroidTestCase {
    public static ContentValues createMovieValues() {
        MovieDBWrapper movie = new MovieDBWrapper();
        movie.strId = "140607";
        movie.posterPath = "/fYzpM9GmpBlIC893fNjoWCwE24H.jpg";
        /*
        movie.isAdult = false;
        movie.overview =  "Thirty years after defeating the Galactic Empire, "
                +"Han Solo and his allies face a new threat from the evil "
                +"Kylo Ren and his army of Stormtroopers.";
        movie.strReleaseDate =  "";
        movie.originalTitle =  "Star Wars: The Force Awakens";
        movie.originalLanguage =  "en";
        movie.title =  "Star Wars: The Force Awakens";
        movie.backdropPath =  "/njv65RTipNSTozFLuF85jL0bcQe.jpg";
        movie.strPopularity =  "35.543819";
        movie.strVoteCount =  "2783";
        movie.hasVideo = false;
        movie.strVoteAverage =  "7.82";
        */
        movie.strRegDate = DateFormatterUtil.getCurrentDateTime();

        return movie.getContentValues();
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateMovieRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    public static void validateMovieRecord(String error, Cursor cursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            if(entry.getValue()!=null && entry.getValue().getClass() == String.class) {
                String expectedValue = entry.getValue().toString();
                Log.w(TestUtilities.class.getSimpleName(), "validateMovieRecord: ["
                                + expectedValue + "] ["
                                + cursor.getString(idx)
                                + "]"
                );
                assertEquals("Value '" + cursor.getString(idx) +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, cursor.getString(idx));
            }else if(entry.getValue()!=null && entry.getValue()!=java.lang.Boolean.class){
                String expectedValue = entry.getValue().equals(Boolean.FALSE)?"0":"1";
                Log.w(TestUtilities.class.getSimpleName(), "validateMovieRecord: ["
                                + expectedValue + "] ["
                                + cursor.getString(idx)
                                + "]"
                );
                assertEquals("Value '" + cursor.getString(idx) +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, cursor.getString(idx));
            }else if (entry.getValue()==null ){
                Log.w(TestUtilities.class.getSimpleName(), "validateMovieRecord: ["
                                + "null] ["
                                + cursor.getString(idx)
                                + "]"
                );
                assertNotNull("Value '" + cursor.getString(idx) + "'" +
                        " did not match expected value <null>");
            }else{
                Log.e(TestUtilities.class.getSimpleName(), "validateMovieRecord: " + entry.getValue().getClass().toString());
                assertNull("Unknow type not found '" + columnName + "' "+entry.getValue().getClass()+". " + error, null);
            }
        }
    }

    public static long insertMoveiRecord(Context mContext, ContentValues testValues){
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long movieRowId = -1;
        movieRowId = db.insert(MovieContract.FavouriteMovieEntry.TABLE_NAME, null, testValues);
        db.close();
        assertTrue(movieRowId != -1);
        return movieRowId;
    }

    static class TestContentObserver extends ContentObserver {
        public static final String LOG_TAG = TestContentObserver.class.getSimpleName();

        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri){
            mContentChanged = true;
            Log.i(LOG_TAG, "onChange: [" +selfChange+"]["+uri+"]");
        }

        public void waitForNotificationOrFail() {
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestUtilities.TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
