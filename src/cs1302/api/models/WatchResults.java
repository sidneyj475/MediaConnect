package cs1302.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the watch provider results from the TMDB API, containing
 * regional streaming availability information. Currently focuses on US region
 * information but can be extended to support other regions.
 */
public class WatchResults {

    /** United States regional streaming information. */
    @SerializedName("US")
    private WatchUS us;

    /**
     * Gets the US streaming availability information.
     *
     * @return the US region watch provider information
     */
    public WatchUS getUs() {
        return us;
    }

    /**
     * Sets the US streaming availability information.
     *
     * @param us the US region watch provider information to set
     */
    public void setUs(WatchUS us) {
        this.us = us;
    }

    /**
     * Checks if US streaming options are available.
     *
     * @return true if US streaming options exist, false otherwise
     */
    public boolean hasUsStreamingOptions() {
        return us != null && us.getFlatrate() != null && !us.getFlatrate().isEmpty();
    }

    @Override
    public String toString() {
        if (hasUsStreamingOptions()) {
            return "US streaming options available";
        }
        return "No US streaming options available";
    }

} // WatchResults