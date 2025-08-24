package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import cs1302.api.models.*;
import cs1302.api.services.MovieApiService;
import cs1302.api.ui.MovieCard;
import cs1302.api.ui.MovieDetailsView;

/**
 * Movie Connect application that allows users to search for movies and get details
 * including cast information and streaming availability.
 */
public class ApiApp extends Application {
    private VBox root;
    private TextField searchField;
    private Button searchButton;
    private ListView<Movie> movieList;
    private VBox detailsBox;
    private MovieApiService apiService;
    private ProgressIndicator progressIndicator;
    private Label statusLabel;
    private MovieDetailsView detailsView;

    /**
     * Constructs an ApiApp instance.
     */
    public ApiApp() {
        root = new VBox(10);
        root.setPadding(new Insets(10));
        apiService = new MovieApiService();
        detailsView = new MovieDetailsView();
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        // Banner image at top
        Label titleLabel = new Label("Media Connect");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10;");
        titleLabel.setAlignment(Pos.CENTER);

        // Search area
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchField = new TextField();
        searchField.setPromptText("Enter movie/tv title");
        searchField.setPrefWidth(300);
        searchButton = new Button("Search");
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        searchBox.getChildren().addAll(searchField, searchButton, progressIndicator);

        // Status label
        statusLabel = new Label("Enter a movie or tv series title to begin searching");
        statusLabel.setWrapText(true);

        // Results area - split into list and details
        SplitPane splitPane = new SplitPane();

        // Movie list on left
        movieList = new ListView<>();
        movieList.setPrefWidth(300);
        movieList.setCellFactory(lv -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    setText(movie.getTitle() + " (" + movie.getYear() + ")");
                }
            }
        });

        // Details pane on right
        detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(10));
        detailsBox.setAlignment(Pos.TOP_CENTER);

        // Add both to split pane
        splitPane.getItems().addAll(movieList, detailsView);

        // Main layout
        root.getChildren().addAll(titleLabel, searchBox, statusLabel, splitPane);

        // Setup event handlers
        setupEventHandlers();

        // Scene setup
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Media Connect");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.show();
    } // start

    /**
     * Sets up all event handlers for the application.
     */
    private void setupEventHandlers() {
        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());

        // Configure movieList cell factory
        movieList.setCellFactory(lv -> new ListCell<Movie>() {
                @Override
                protected void updateItem(Movie movie, boolean empty) {
                    super.updateItem(movie, empty);
                    if (empty || movie == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(new MovieCard(movie));
                    }
                }
            });

        // Setup selection listener
        movieList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadMovieDetails(newVal);
                }
            });
    }

    /**
     * Performs the movie search operation.
     */
    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showError("Please enter a movie title");
            return;
        }

        // Clear previous results
        movieList.getItems().clear();
        detailsBox.getChildren().clear();

        // Show loading state
        setLoading(true);
        statusLabel.setText("Searching for movies...");

        // Run search in background
        Thread searchThread = new Thread(() -> {
            try {
                OmdbResponse response = apiService.searchMovies(query);

                Platform.runLater(() -> {
                    if ("True".equals(response.getResponse())) {
                        movieList.getItems().addAll(response.getSearch());
                        statusLabel.setText("Found " + response.getTotalResults() + " results");
                    } else {
                        statusLabel.setText("No movies found");
                    }
                    setLoading(false);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showError("Search failed: " + ex.getMessage());
                    setLoading(false);
                });
            }
        });

        searchThread.setDaemon(true);
        searchThread.start();
    } // performSearch

    /**
     * Loads and displays detailed information for a selected movie.
     *
     * @param movie the selected movie to load details for
     */
    private void loadMovieDetails(Movie movie) {
        if (movie == null) {
            return;
        }

        // Show loading state
        detailsView.setMovie(movie); // Set basic info
        detailsView.showLoading();  // Show loading indicators

        Thread detailsThread = new Thread(() -> {
            try {
                TmdbMovieDetails details = apiService.getMovieDetails(movie.getImdbID());
                Platform.runLater(() -> detailsView.setDetails(details));
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    detailsView.clear();
                    showError("Failed to load movie details: " + ex.getMessage());
                });
            }
        });

        detailsThread.setDaemon(true);
        detailsThread.start();
    }

    /**
     * Shows an error dialog with the specified message.
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    } // showError

    /**
     * Sets the loading state of the application.
     * @param loading true if loading, false otherwise
     */
    private void setLoading(boolean loading) {
        progressIndicator.setVisible(loading);
        searchButton.setDisable(loading);
        searchField.setDisable(loading);
    } // setLoading

} // ApiApp