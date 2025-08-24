package cs1302.api.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cs1302.api.models.*;

/**
 * Service class for making API calls to OMDB and TMDB. Handles rate limiting and request management
 * for both APIs.
 */
public class MovieApiService {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static final String OMDB_BASE_URL = "http://www.omdbapi.com/";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    private final String omdbKey;
    private final String tmdbKey;
    private final RateLimiter omdbLimiter;
    private final RateLimiter tmdbLimiter;

    /**
     * Constructs a MovieApiService and loads API keys from configuration.
     *
     * @throws RuntimeException if API keys cannot be loaded or are missing
     */
    public MovieApiService() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
            prop.load(fis);
            this.omdbKey = prop.getProperty("omdb.key");
            this.tmdbKey = prop.getProperty("tmdb.key");
            if (omdbKey == null || tmdbKey == null) {
                throw new RuntimeException("Missing API keys in config.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API keys", e);
        }
        this.omdbLimiter = new RateLimiter(30, Duration.ofMinutes(1));
        this.tmdbLimiter = new RateLimiter(40, Duration.ofSeconds(10));
    }

    /**
     * Searches for movies using the OMDB API.
     *
     * @param query the search term to look for
     * @return the OMDB API response containing search results
     * @throws IOException if an I/O error occurs during the request
     * @throws InterruptedException if the request is interrupted
     */
    public OmdbResponse searchMovies(String query) throws IOException, InterruptedException {
        omdbLimiter.acquirePermit();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format("%s?apikey=%s&s=%s", OMDB_BASE_URL, omdbKey, encodedQuery);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("OMDB API request failed with status: " + response.statusCode());
        }
        return GSON.fromJson(response.body(), OmdbResponse.class);
    }

    /**
     * Gets detailed movie information from TMDB.
     *
     * @param imdbId the IMDB ID of the movie to get details for
     * @return detailed movie information from TMDB
     * @throws IOException if an I/O error occurs during the request
     * @throws InterruptedException if the request is interrupted
     */
    public TmdbMovieDetails getMovieDetails(String imdbId)
        throws IOException, InterruptedException {
        tmdbLimiter.acquirePermit();
        String findUrl = String.format
            ("%s/find/%s?api_key=%s&external_source=imdb_id&include_adult=false",
            TMDB_BASE_URL, imdbId, tmdbKey);

        System.out.println("\nSearching for content with IMDB ID: " + imdbId);
        FindResponse findResponse = makeRequest(findUrl, FindResponse.class);

        System.out.println("Movie results: " +
            (findResponse.getMovieResults() !=
            null ? findResponse.getMovieResults().size() : "null"));
        System.out.println("TV results: " +
            (findResponse.getTvResults() != null ? findResponse.getTvResults().size() : "null"));

        if (findResponse == null ||
            (findResponse.getMovieResults() == null || findResponse.getMovieResults().isEmpty()) &&
            (findResponse.getTvResults() == null || findResponse.getTvResults().isEmpty())) {
            TmdbMovieDetails basicDetails = new TmdbMovieDetails();
            basicDetails.setOverview("Additional details could not be found for this title.");
            return basicDetails;
        }

        boolean isTvShow = false;
        int tmdbId;
        if (findResponse.getMovieResults() != null && !findResponse.getMovieResults().isEmpty()) {
            tmdbId = findResponse.getMovieResults().get(0).getId();
            System.out.println("Found as movie with ID: " + tmdbId);
        } else {
            tmdbId = findResponse.getTvResults().get(0).getId();
            isTvShow = true;
            System.out.println("Found as TV show with ID: " + tmdbId);
        }

        return loadFullDetails(tmdbId, isTvShow);
    }

    /**
     * Loads full details for a movie or TV show from TMDB.
     *
     * @param tmdbId the TMDB ID of the content
     * @param isTvShow whether the content is a TV show
     * @return detailed content information
     */
    private TmdbMovieDetails loadFullDetails(int tmdbId, boolean isTvShow) {
        String contentType = isTvShow ? "tv" : "movie";
        System.out.println("Using content type: " + contentType);

        CompletableFuture<TmdbMovieDetails> detailsFuture = loadDetails(tmdbId, contentType);
        CompletableFuture<Credits> creditsFuture = loadCredits(tmdbId, contentType);
        CompletableFuture<TmdbWatchProviders> providersFuture = loadProviders(tmdbId, contentType);

        try {
            TmdbMovieDetails details = detailsFuture.join();
            if (details == null) {
                TmdbMovieDetails basicDetails = new TmdbMovieDetails();
                basicDetails.setOverview("Failed to load content details.");
                return basicDetails;
            }

            try {
                Credits credits = creditsFuture.join();
                if (credits != null && credits.getCast() != null) {
                    details.setCast(credits.getCast());
                }
            } catch (Exception e) {
                System.out.println("Warning: Failed to get credits: " + e.getMessage());
            }

            try {
                TmdbWatchProviders providers = providersFuture.join();
                if (providers != null) {
                    details.setWatchProviders(providers);
                }
            } catch (Exception e) {
                System.out.println("Warning: Failed to get providers: " + e.getMessage());
            }

            return details;
        } catch (Exception e) {
            TmdbMovieDetails basicDetails = new TmdbMovieDetails();
            basicDetails.setOverview("Failed to load content details: " + e.getMessage());
            return basicDetails;
        }
    }

    /**
     * Loads detailed information for a movie or TV show.
     *
     * @param tmdbId ID of the content in TMDB
     * @param contentType type of content ("movie" or "tv")
     * @return future containing the detailed information
     */
    private CompletableFuture<TmdbMovieDetails> loadDetails(int tmdbId, String contentType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                tmdbLimiter.acquirePermit();
                String url = String.format("%s/%s/%d?api_key=%s",
                    TMDB_BASE_URL, contentType, tmdbId, tmdbKey);
                return makeRequest(url, TmdbMovieDetails.class);
            } catch (Exception e) {
                System.out.println("Error getting details: " + e.getMessage());
                TmdbMovieDetails basicDetails = new TmdbMovieDetails();
                basicDetails.setOverview("Failed to get content details");
                return basicDetails;
            }
        });
    }

    /**
     * Loads credits information for a movie or TV show.
     *
     * @param tmdbId ID of the content in TMDB
     * @param contentType type of content ("movie" or "tv")
     * @return future containing the credits information
     */
    private CompletableFuture<Credits> loadCredits(int tmdbId, String contentType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                tmdbLimiter.acquirePermit();
                String url = String.format("%s/%s/%d/credits?api_key=%s",
                    TMDB_BASE_URL, contentType, tmdbId, tmdbKey);
                return makeRequest(url, Credits.class);
            } catch (Exception e) {
                System.out.println("Error getting credits: " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Loads provider information for a movie or TV show.
     *
     * @param tmdbId ID of the content in TMDB
     * @param contentType type of content ("movie" or "tv")
     * @return future containing the provider information
     */
    private CompletableFuture<TmdbWatchProviders> loadProviders(int tmdbId, String contentType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                tmdbLimiter.acquirePermit();
                String url = String.format("%s/%s/%d/watch/providers?api_key=%s",
                    TMDB_BASE_URL, contentType, tmdbId, tmdbKey);
                return makeRequest(url, TmdbWatchProviders.class);
            } catch (Exception e) {
                System.out.println("Error getting providers: " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Makes an HTTP request and deserializes the response.
     *
     * @param <T> the type to deserialize the response into
     * @param url the URL to make the request to
     * @param responseType the class to deserialize into
     * @return the deserialized response
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the request is interrupted
     */
    private <T> T makeRequest(String url, Class<T> responseType)
        throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        System.out.println("Response for " + responseType.getSimpleName() + ": " + response.body());

        if (response.statusCode() != 200) {
            throw new IOException("API request failed with status: " + response.statusCode());
        }
        return GSON.fromJson(response.body(), responseType);
    }

    /**
     * Helper class for managing API rate limits.
     */
    private static class RateLimiter {
        private final Queue<Instant> requestTimes = new LinkedList<>();
        private final int maxRequests;
        private final Duration window;

        /**
         * Creates a rate limiter with the specified parameters.
         *
         * @param maxRequests maximum number of requests allowed in the time window
         * @param window the time window for rate limiting
         */
        public RateLimiter(int maxRequests, Duration window) {
            this.maxRequests = maxRequests;
            this.window = window;
        }

        /**
         * Acquires a permit to make a request, waiting if necessary.
         *
         * @throws InterruptedException if the thread is interrupted while waiting
         */
        public synchronized void acquirePermit() throws InterruptedException {
            Instant now = Instant.now();
            while (!requestTimes.isEmpty() &&
                Duration.between(requestTimes.peek(), now).compareTo(window) > 0) {
                requestTimes.poll();
            }
            while (requestTimes.size() >= maxRequests) {
                Thread.sleep(100);
                now = Instant.now();
                while (!requestTimes.isEmpty() &&
                    Duration.between(requestTimes.peek(), now).compareTo(window) > 0) {
                    requestTimes.poll();
                }
            }
            requestTimes.add(now);
        }
    }

} // MovieApiService