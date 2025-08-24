package cs1302.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the credits response from TMDB API.
 */
public class Credits {
    @SerializedName("cast")
    public List<TmdbCast> cast;

    /**
     * Gets the cast list.
     *
     * @return list of cast members
     */
    public List<TmdbCast> getCast() {
        return cast;
    }

    /**
     * Sets the cast list.
     *
     * @param cast list of cast members
     */
    public void setCast(List<TmdbCast> cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return "Credits{cast=" + (cast != null ? cast.size() : "null") + "}";
    }
}