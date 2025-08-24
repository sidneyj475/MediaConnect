package cs1302.api.ui;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import cs1302.api.models.*;

/**
 * Represents detailed movie information in a scrollable view.
 */
public class MovieDetailsView extends ScrollPane {
    private static final double MAX_POSTER_WIDTH = 200.0;
    private static final double MAX_POSTER_HEIGHT = 300.0;
    private static final Image DEFAULT_POSTER =
        new Image("file:resources/no-image.png");

    private final VBox contentBox;
    private final ImageView posterView;
    private final Label titleLabel;
    private final Label yearLabel;
    private final Label overviewLabel;
    private final ListView<String> castList;
    private final ListView<String> streamingList;

    /**
     * Constructs a new MovieDetailsView.
     */
    public MovieDetailsView() {
        // Initialize main content container
        contentBox = new VBox(15); // 15px spacing
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Initialize components
        posterView = new ImageView(DEFAULT_POSTER);
        posterView.setFitWidth(MAX_POSTER_WIDTH);
        posterView.setFitHeight(MAX_POSTER_HEIGHT);
        posterView.setPreserveRatio(true);

        titleLabel = createStyledLabel("", "-fx-font-size: 20px; -fx-font-weight: bold;");
        yearLabel = createStyledLabel("", "-fx-font-size: 14px; -fx-font-style: italic;");
        overviewLabel = createStyledLabel("", "-fx-font-size: 14px;");
        overviewLabel.setWrapText(true);

        // Cast section
        Label castHeader = createStyledLabel("Cast", "-fx-font-weight: bold;");
        castList = new ListView<>();
        castList.setPrefHeight(150);

        // Streaming section
        Label streamingHeader = createStyledLabel("Available on", "-fx-font-weight: bold;");
        streamingList = new ListView<>();
        streamingList.setPrefHeight(100);

        // Add all components to the content box
        contentBox.getChildren().addAll(
            posterView,
            titleLabel,
            yearLabel,
            createSectionLabel("Overview"),
            overviewLabel,
            castHeader,
            castList,
            streamingHeader,
            streamingList
        );

        // Configure scroll pane
        setContent(contentBox);
        setFitToWidth(true);
    }

    /**
     * Sets basic movie information.
     *
     * @param movie the movie
     */
    public void setMovie(Movie movie) {
        if (movie != null) {
            titleLabel.setText(movie.getTitle());
            yearLabel.setText(movie.getYear());

            // Load poster if available, with error handling
            if (movie.getPoster() != null && !movie.getPoster().equals("N/A")) {
                try {
                    Image poster = new Image(movie.getPoster(), true); // Load in background
                    poster.errorProperty().addListener((obs, oldError, newError) -> {
                        if (newError) {
                            posterView.setImage(DEFAULT_POSTER);
                        }
                    });

                    posterView.setImage(poster);
                    posterView.setFitWidth(MAX_POSTER_WIDTH);
                    posterView.setFitHeight(MAX_POSTER_HEIGHT);
                    posterView.setPreserveRatio(true);
                } catch (IllegalArgumentException e) {
                    posterView.setImage(DEFAULT_POSTER);
                }
            } else {
                posterView.setImage(DEFAULT_POSTER);
            }
        } else {
            clear();
        }
    }

    /**
     * Sets detailed movie information.
     *
     * @param details the detailed movie information
     */
    public void setDetails(TmdbMovieDetails details) {
        if (details != null) {
            // Handle case where we only have basic info
            if ("Details Unavailable".equals(details.getTitle())) {
                // Keep the original title from the initial Movie object
                overviewLabel.setText(details.getOverview());
                // Clear or show placeholder for unavailable info
                castList.getItems().clear();
                castList.getItems().add("Cast information not available");
                streamingList.getItems().clear();
                streamingList.getItems().add("Streaming information not available");
                return; // Exit early since we don't have full details
            }

            // Update title (might be TV show)
            if (details.getTitle() != null) {
                titleLabel.setText(details.getTitle());
            }

            // Update overview
            overviewLabel.setText(details.getOverview() != null ?
                details.getOverview() : "No overview available");

            // Update cast list
            castList.getItems().clear();
            if (details.getCast() != null && !details.getCast().isEmpty()) {
                details.getCast().forEach(cast ->
                    castList.getItems().add(cast.toString())
                );
            } else {
                castList.getItems().add("No cast information available");
            }

            // Update streaming services
            streamingList.getItems().clear();
            if (details.getWatchProviders() != null &&
                details.getWatchProviders().getResults() != null &&
                details.getWatchProviders().getResults().getUs() != null &&
                details.getWatchProviders().getResults().getUs().getFlatrate() != null) {

                details.getWatchProviders()
                    .getResults()
                    .getUs()
                    .getFlatrate()
                    .forEach(provider ->
                        streamingList.getItems().add(provider.getProviderName())
                    );
            } else {
                streamingList.getItems().add("No streaming information available");
            }
        } else {
            clear();
            overviewLabel.setText("Failed to load movie details");
        }
    }

    /**
     * Creates a styled section label.
     *
     * @param text the label text
     * @return the styled label
     */
    private Label createSectionLabel(String text) {
        return createStyledLabel(text, "-fx-font-size: 16px; -fx-font-weight: bold;");
    }

    /**
     * Creates a label with custom styling.
     *
     * @param text the label text
     * @param style the CSS style string
     * @return the styled label
     */
    private Label createStyledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    /**
     * Clears all content from the view.
     */
    public void clear() {
        titleLabel.setText("");
        yearLabel.setText("");
        overviewLabel.setText("");
        posterView.setImage(DEFAULT_POSTER);
        castList.getItems().clear();
        streamingList.getItems().clear();
    }

    /**
     * Shows loading state in the view.
     */
    public void showLoading() {
        titleLabel.setText("Loading...");
        yearLabel.setText("");
        overviewLabel.setText("Loading overview...");
        castList.getItems().clear();
        castList.getItems().add("Loading cast...");
        streamingList.getItems().clear();
        streamingList.getItems().add("Loading streaming info...");
    }

    /**
     * Shows an error state in the view.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        clear();
        titleLabel.setText("Error");
        overviewLabel.setText(message);
    }
}