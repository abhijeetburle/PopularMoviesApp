package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abhijeet.burle on 2016/02/02.
 */
public class DateFormatterUtil {
    private final static String FORMAT_DATE_TIME="yyyy-MM-dd HH:mm:ss.SSS";
    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
        Date now = new Date();
        return sdf.format(now);
    }
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME);
        return sdf.format(date);
    }
}
