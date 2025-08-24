package cs1302.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * Represents detailed movie/TV show information from the TMDB API.
 * This class contains comprehensive information including overview,
 * cast list, streaming availability, and release information.
 */
public class TmdbMovieDetails {

    /** List of cast members in the movie/show. */
    private List<TmdbCast> cast;

    /** Full description or plot summary. */
    private String overview;

    /** Information about streaming platforms where the content is available. */
    private TmdbWatchProviders watchProviders;

    /** Name of the TV show, used for TV content. */
    @SerializedName("name")
    private String tvName;

    /** Title of the movie, used for movie content. */
    @SerializedName("title")
    private String movieTitle;

    /** Release date for movies. */
    @SerializedName("release_date")
    private String releaseDate;

    /** First air date for TV shows. */
    @SerializedName("first_air_date")
    private String firstAirDate;

    /** Average user rating score. */
    @SerializedName("vote_average")
    private String voteAverage;

    /**
     * Gets the list of cast members.
     *
     * @return list of cast members, may be null if not available
     */
    public List<TmdbCast> getCast() {
        return cast;
    }

    /**
     * Sets the list of cast members.
     *
     * @param cast list of cast members to set
     */
    public void setCast(List<TmdbCast> cast) {
        this.cast = cast;
    }

    /**
     * Gets the content overview/description.
     *
     * @return the overview text, may be null if not available
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Sets the content overview/description.
     *
     * @param overview the overview text to set
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * Gets the streaming availability information.
     *
     * @return watch providers information, may be null if not available
     */
    public TmdbWatchProviders getWatchProviders() {
        return watchProviders;
    }

    /**
     * Sets the streaming availability information.
     *
     * @param watchProviders watch providers information to set
     */
    public void setWatchProviders(TmdbWatchProviders watchProviders) {
        this.watchProviders = watchProviders;
    }

    /**
     * Gets the release date or first air date.
     * For TV shows, returns the first air date. For movies, returns the release date.
     *
     * @return the date string, or null if neither date is available
     */
    public String getReleaseDate() {
        return releaseDate != null ? releaseDate : firstAirDate;
    }

    /**
     * Sets the release date.
     *
     * @param releaseDate the release date to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Gets the title of the content.
     * For TV shows, returns the show name. For movies, returns the movie title.
     *
     * @return the title of the content, may be null if not available
     */
    public String getTitle() {
        return tvName != null ? tvName : movieTitle;
    }

    /**
     * Gets the movie's average vote rating.
     *
     * @return the vote average string, may be null if not available
     */
    public String getVoteAverage() {
        return voteAverage;
    }

    /**
     * Sets the movie's average vote rating.
     *
     * @param voteAverage the vote average to set
     */
    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",
            getTitle(),
            getReleaseDate() != null ? getReleaseDate() : "No date");
    }
} // TmdbMovieDetails