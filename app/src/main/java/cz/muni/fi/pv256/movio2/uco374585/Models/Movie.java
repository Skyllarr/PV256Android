package cz.muni.fi.pv256.movio2.uco374585.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Skylar on 12/20/2016.
 */

public class Movie implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String title;
    String releaseDate;
    String coverPath;
    String backdrop;
    float popularity;
    String description;

    public Movie(Parcel parcel) {
        this.releaseDate = parcel.readString();
        this.coverPath = parcel.readString();
        this.title = parcel.readString();
        this.backdrop = parcel.readString();
        this.popularity = parcel.readFloat();
        this.description = parcel.readString();
    }

    public Movie(String releaseDate, String coverPath, String title, String backdrop, float popularity, String description) {
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.description = description;
    }

    public String getReleaseDate() {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.releaseDate);
        dest.writeString(this.coverPath);
        dest.writeString(this.title);
        dest.writeString(this.backdrop);
        dest.writeFloat(this.popularity);
        dest.writeString(this.description);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
