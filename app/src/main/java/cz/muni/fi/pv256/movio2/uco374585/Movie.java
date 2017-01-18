package cz.muni.fi.pv256.movio2.uco374585;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Skylar on 12/20/2016.
 */

public class Movie implements Parcelable {

    long releaseDate;
    String coverPath;
    String title;
    String backdrop;
    float popularity;
    String description;

    public long getReleaseDate() {
        return releaseDate;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getDescription() {
        return description;
    }

    public Movie(Parcel parcel) {
        this.releaseDate = parcel.readLong();
        this.coverPath = parcel.readString();
        this.title = parcel.readString();
        this.backdrop = parcel.readString();
        this.popularity = parcel.readFloat();
        this.description = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.releaseDate);
        dest.writeString(this.coverPath);
        dest.writeString(this.title);
        dest.writeString(this.backdrop);
        dest.writeFloat(this.popularity);
        dest.writeString(this.description);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(long releaseDate, String coverPath, String title, String backdrop, float popularity, String description) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
