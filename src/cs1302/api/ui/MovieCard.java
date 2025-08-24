package cs1302.api.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import cs1302.api.models.Movie;

/**
 * A custom JavaFX component that displays movie information in a card format.
 * Used in the movie list view to show each search result.
 */
public class MovieCard extends HBox {
    private static final double POSTER_WIDTH = 50.0;
    private static final Image DEFAULT_POSTER =
        new Image("file:resources/readme-banner.png");

    private final Label titleLabel;
    private final Label yearLabel;
    private final ImageView posterView;
    private Movie movie;

    /**
     * Constructs a MovieCard for the specified movie.
     *
     * @param movie the movie to display
     */
    public MovieCard(Movie movie) {
        super(10); // 10px spacing between elements
        this.movie = movie;

        // Set up container styling
        setPadding(new Insets(5));
        setAlignment(Pos.CENTER_LEFT);

        // Create poster image view
        posterView = new ImageView();
        posterView.setFitWidth(POSTER_WIDTH);
        posterView.setPreserveRatio(true);

        // Create labels for movie info
        titleLabel = new Label();
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold");

        yearLabel = new Label();
        yearLabel.setStyle("-fx-font-style: italic");

        // Create container for text
        VBox textContainer = new VBox(5);
        textContainer.getChildren().addAll(titleLabel, yearLabel);

        // Add all elements to card
        getChildren().addAll(posterView, textContainer);

        // Update the display
        updateDisplay();
    }

    /**
     * Gets the movie associated with this card.
     *
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie for this card and updates the display.
     *
     * @param movie the new movie to display
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        updateDisplay();
    }

    /**
     * Updates the display with the current movie's information.
     */
    private void updateDisplay() {
        if (movie != null) {
            titleLabel.setText(movie.getTitle());
            yearLabel.setText(movie.getYear());

            // Load movie poster if available
            if (movie.getPoster() != null && !movie.getPoster().equals("N/A")) {
                try {
                    Image poster = new Image(movie.getPoster(), true); // Load in background
                    posterView.setImage(poster);
                } catch (IllegalArgumentException e) {
                    posterView.setImage(DEFAULT_POSTER);
                }
            } else {
                posterView.setImage(DEFAULT_POSTER);
            }
        } else {
            titleLabel.setText("");
            yearLabel.setText("");
            posterView.setImage(DEFAULT_POSTER);
        }
    }

    /**
     * Creates a loading state version of the movie card.
     *
     * @return a MovieCard configured to show a loading state
     */
    public static MovieCard createLoadingCard() {
        Movie loadingMovie = new Movie();
        loadingMovie.setTitle("Loading...");
        loadingMovie.setYear("");
        return new MovieCard(loadingMovie);
    }

    /**
     * Creates an error state version of the movie card.
     *
     * @param errorMessage the error message to display
     * @return a MovieCard configured to show an error state
     */
    public static MovieCard createErrorCard(String errorMessage) {
        Movie errorMovie = new Movie();
        errorMovie.setTitle("Error");
        errorMovie.setYear(errorMessage);
        return new MovieCard(errorMovie);
    }
}