package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

/**
 * Created by abhijeet.burle on 2016/01/20.
 */
public class MovieDBWrapper {
    public String posterPath;
    public boolean isAdult;
    public String overview;
    public String strReleaseDate;
    public String strId;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public String strPopularity;
    public String strVoteCount;
    public boolean hasVideo;
    public String strVoteAverage;

    @Override
    public String toString() {
        return "[posterPath:" + posterPath +
                "][isAdult:" + isAdult +
                "][overview:" + overview +
                "][stReleaseDate:" + strReleaseDate +
                "][strId:" + strId +
                "][originalTitle:" + originalTitle +
                "][originalLanguage:" + originalLanguage +
                "][title:" + title +
                "][backdropPath:" + backdropPath +
                "][strPopularity:" + strPopularity +
                "][strVoteCount:" + strVoteCount +
                "][hasVideo:" + hasVideo +
                "][strVoteAverage:"+ strVoteAverage +
                 "]";
    }
}
