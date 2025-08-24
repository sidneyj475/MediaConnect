package cs1302.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a movie from the OMDB API.
 */
public class Movie {
    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("imdbID")
    private String imdbID;

    @SerializedName("Poster")
    private String poster;

    /**
     * Default constructor used by Gson.
     */
    public Movie() {
    }

    /**
     * Gets the title of the movie.
     *
     * @return the movie title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title the movie title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the release year of the movie.
     *
     * @return the release year
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the release year of the movie.
     *
     * @param year the release year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Gets the IMDB ID of the movie.
     *
     * @return the IMDB ID
     */
    public String getImdbID() {
        return imdbID;
    }

    /**
     * Sets the IMDB ID of the movie.
     *
     * @param imdbID the IMDB ID to set
     */
    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    /**
     * Gets the URL of the movie poster.
     *
     * @return the poster URL
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the URL of the movie poster.
     *
     * @param poster the poster URL to set
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }
}