package cs1302.api;

import javafx.application.Application;
import cs1302.api.services.MovieApiService;
import cs1302.api.models.OmdbResponse;

/**
 * Driver for testing the API functionality.
 */
public class ApiDriver {

    /**
     * Main entry-point into the application.
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        // Test the API before launching the application
        try {
            //System.out.println("Testing API connection...");
            //MovieApiService service = new MovieApiService();
            //OmdbResponse response = service.searchMovies("Batman");

            //if (response != null) {
            //  System.out.println("Response: " + response.Response);
            //  if (response.Search != null) {
            //      System.out.println("Found " + response.Search.length + " movies");
            //      for (var movie : response.Search) {
            //          System.out.println(" - " + movie.getTitle());
            //      }
            //  }
            //}

            // After testing, launch the actual application
            Application.launch(ApiApp.class, args);
        } catch (UnsupportedOperationException e) {
            System.err.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } catch (RuntimeException re) {
            System.err.println(re);
            System.err.println("A runtime exception has occurred somewhere in the application,");
            System.err.println("and it propagated all the way up to the main method. Please");
            System.err.println("inspect the backtrace above for more information.");
            re.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during API testing:");
            e.printStackTrace();
            System.exit(1);
        } // try
    } // main

} // ApiDriver