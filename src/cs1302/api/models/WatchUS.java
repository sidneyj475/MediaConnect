package cs1302.api.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/**
 * Represents US-specific streaming availability information from the TMDB API.
 * Contains information about where a movie or TV show can be streamed in the
 * United States region.
 */
public class WatchUS {

    /** List of streaming providers where content is available for flat-rate streaming. */
    @SerializedName("flatrate")
    private List<Provider> flatrate;

    /**
     * Gets the list of streaming providers.
     *
     * @return list of streaming providers where content is available, may be null
     */
    public List<Provider> getFlatrate() {
        return flatrate;
    }

    /**
     * Sets the list of streaming providers.
     *
     * @param flatrate list of streaming providers to set
     */
    public void setFlatrate(List<Provider> flatrate) {
        this.flatrate = flatrate;
    }

    /**
     * Checks if streaming providers are available.
     *
     * @return true if there are streaming providers available, false otherwise
     */
    public boolean hasStreamingProviders() {
        return flatrate != null && !flatrate.isEmpty();
    }

    @Override
    public String toString() {
        if (hasStreamingProviders()) {
            StringBuilder sb = new StringBuilder("Available on: ");
            for (Provider provider : flatrate) {
                sb.append(provider.getProviderName()).append(", ");
            }
            // Remove trailing comma and space
            return sb.substring(0, sb.length() - 2);
        }
        return "Not available for streaming";
    }

} // WatchUS