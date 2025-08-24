package cs1302.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the OMDB API search endpoint.
 * Contains an array of movies matching the search criteria along with
 * total results count and status information.
 */
public class OmdbResponse {

    /** Array of movies from the search results. */
    @SerializedName("Search")
    private Movie[] search;

    /** Total number of results found for the search query. */
    private String totalResults;

    /** Response status indicating success ("True") or failure ("False"). */
    @SerializedName("Response")
    private String response;

    /** Error message if the request failed. */
    @SerializedName("Error")
    private String error;

    /**
     * Gets the array of movies from the search results.
     *
     * @return array of Movie objects, or null if no results found
     */
    public Movie[] getSearch() {
        return search != null ? search.clone() : null;
    }

    /**
     * Gets the total number of results found.
     *
     * @return the total number of results as a string
     */
    public String getTotalResults() {
        return totalResults;
    }

    /**
     * Gets the response status from the API.
     *
     * @return "True" for success, "False" for failure
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets any error message from the API.
     *
     * @return the error message, or null if no error occurred
     */
    public String getError() {
        return error;
    }

    /**
     * Returns whether the API response indicates success.
     *
     * @return true if the response was successful, false otherwise
     */
    public boolean isSuccess() {
        return "True".equals(response);
    }

} // OmdbResponse