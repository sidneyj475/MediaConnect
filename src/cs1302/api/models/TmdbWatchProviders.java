package cs1302.api.models;

/**
 * Represents watch provider information from the TMDB API response.
 * Contains streaming availability information for different regions,
 * currently focusing on US region data.
 */
public class TmdbWatchProviders {

    /** Container for regional watch provider results. */
    private WatchResults results;

    /**
     * Gets the watch provider results.
     *
     * @return the watch provider results
     */
    public WatchResults getResults() {
        return results;
    }

    /**
     * Sets the watch provider results.
     *
     * @param results the watch provider results to set
     */
    public void setResults(WatchResults results) {
        this.results = results;
    }

    /**
     * Checks if any streaming providers are available in the US region.
     *
     * @return true if there are streaming providers available, false otherwise
     */
    public boolean hasStreamingProviders() {
        return results != null &&
            results.getUs() != null &&
            results.getUs().getFlatrate() != null &&
            !results.getUs().getFlatrate().isEmpty();
    }

    @Override
    public String toString() {
        if (hasStreamingProviders()) {
            return "Available on " + results.getUs().getFlatrate().size() + " platforms";
        }
        return "No streaming providers available";
    }

} // TmdbWatchProviders