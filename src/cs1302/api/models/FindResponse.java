package cs1302.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the API containing movie and TV search results.
 */
public class FindResponse {

    @SerializedName("movie_results")
    private List<MediaResult> movieResults;

    @SerializedName("tv_results")
    private List<MediaResult> tvResults;

    /**
     * Represents a media result item, such as a movie or TV show.
     */
    public static class MediaResult {
        private int id;
        private String title;
        private String name;

        @SerializedName("media_type")
        private String mediaType;  // Fixed member name to follow naming convention

        /**
         * Gets the ID of the media result.
         *
         * @return the ID of the media result
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the title of the media result.
         *
         * @return the title if available, otherwise the name
         */
        public String getTitle() {
            return title != null ? title : name;
        }

        /**
         * Gets the media type of the result.
         *
         * @return the media type as a string
         */
        public String getMediaType() {
            return mediaType;
        }
    }

    /**
     * Gets the list of movie results.
     *
     * @return a list of {@link MediaResult} representing movie results
     */
    public List<MediaResult> getMovieResults() {
        return movieResults;
    }

    /**
     * Gets the list of TV results.
     *
     * @return a list of {@link MediaResult} representing TV results
     */
    public List<MediaResult> getTvResults() {
        return tvResults;
    }

    /**
     * Gets the first result available in the movie or TV results.
     *
     * @return the first {@link MediaResult} found, or null if no results are available
     */
    public MediaResult getFirstResult() {
        if (movieResults != null && !movieResults.isEmpty()) {
            return movieResults.get(0);
        } else if (tvResults != null && !tvResults.isEmpty()) {
            return tvResults.get(0);
        }
        return null;
    }
}