package comp20050.hexboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Entry point for the HexOust JavaFX application.
 * Loads the FXML view, applies CSS styling, and starts the UI.
 */
public class HexOustApplication extends Application {

    public static StackPane root; // Root layout for managing overlays (e.g., winner popup)

    /**
     * Initializes the main JavaFX stage, loads the UI from FXML,
     * and applies the stylesheet. Sets up fullscreen display.
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loadedRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        root = new StackPane();                   // Create the root layout
        root.getChildren().add(loadedRoot);       // Add the loaded FXML content

        Scene scene = new Scene(root, 800, 600);  // Create a scene with preferred dimensions
        primaryStage.setTitle("Hexagon Grid");    // Set the window title
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);         // Start in fullscreen
        primaryStage.show();

        // Apply CSS styling
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

    /**
     * Main method for launching the JavaFX application.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
