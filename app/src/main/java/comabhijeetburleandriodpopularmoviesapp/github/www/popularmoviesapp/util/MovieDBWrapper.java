package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhijeet.burle on 2016/01/20.
 */
public class MovieDBWrapper extends MovieDBBaseWrapper implements Parcelable {
    public String posterPath;
    public boolean isAdult;
    public String overview;
    public String strReleaseDate;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public String strPopularity;
    public String strVoteCount;
    public boolean hasVideo;
    public String strVoteAverage;

    public MovieDBWrapper() {
    }

    protected MovieDBWrapper(Parcel in) {
        posterPath = in.readString();
        isAdult = in.readByte() != 0;
        overview = in.readString();
        strReleaseDate = in.readString();
        strId = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        strPopularity = in.readString();
        strVoteCount = in.readString();
        hasVideo = in.readByte() != 0;
        strVoteAverage = in.readString();
    }

    public static final Creator<MovieDBWrapper> CREATOR = new Creator<MovieDBWrapper>() {
        @Override
        public MovieDBWrapper createFromParcel(Parcel in) {
            return new MovieDBWrapper(in);
        }

        @Override
        public MovieDBWrapper[] newArray(int size) {
            return new MovieDBWrapper[size];
        }
    };
    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(strReleaseDate);
        dest.writeString(strId);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(strPopularity);
        dest.writeString(strVoteCount);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeString(strVoteAverage);
    }

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
