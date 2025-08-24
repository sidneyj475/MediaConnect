package cs1302.api;

import cs1302.api.services.MovieApiService;
import cs1302.api.models.OmdbResponse;

/**
 * A test class to demonstrate the usage of the MovieApiService.
 * It performs a search for movies using the OMDB API and prints the results.
 */
public class ApiTest {
    public static void main(String[] args) {
        try {
            MovieApiService service = new MovieApiService();
            OmdbResponse response = service.searchMovies("Batman");

            if (response != null) {
                System.out.println("Response: " + response.getResponse());
                if (response.getSearch() != null) {
                    System.out.println("Found " + response.getSearch().length + " movies");
                    for (var movie : response.getSearch()) {
                        System.out.println(" - " + movie.getTitle());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
