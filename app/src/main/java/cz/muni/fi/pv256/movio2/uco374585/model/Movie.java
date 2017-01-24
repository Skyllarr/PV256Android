package cz.muni.fi.pv256.movio2.uco374585.model;

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

    Long id;
    String title;
    String releaseDate;
    String coverPath;
    String backdrop;
    float popularity;
    float voteAverage;
    String description;

    public Movie(Parcel parcel) {
        this.id = parcel.readLong();
        this.releaseDate = parcel.readString();
        this.coverPath = parcel.readString();
        this.title = parcel.readString();
        this.backdrop = parcel.readString();
        this.voteAverage = parcel.readFloat();
        this.popularity = parcel.readFloat();
        this.description = parcel.readString();
    }

    public Movie(Long id, String releaseDate, String coverPath, String title, String backdrop,
                 float popularity, float voteAverage, String description) {
        this.id = id;
        this.releaseDate = releaseDate;
        this.coverPath = coverPath;
        this.title = title;
        this.backdrop = backdrop;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.releaseDate);
        dest.writeString(this.coverPath);
        dest.writeString(this.title);
        dest.writeString(this.backdrop);
        dest.writeFloat(this.popularity);
        dest.writeFloat(this.voteAverage);
        dest.writeString(this.description);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
