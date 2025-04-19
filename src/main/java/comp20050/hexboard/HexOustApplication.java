package comp20050.hexboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane; // import StackPane
import javafx.stage.Stage;

public class HexOustApplication extends Application {

    public static StackPane root; // <-- Make a static StackPane

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loadedRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        root = new StackPane();            // create a StackPane
        root.getChildren().add(loadedRoot); // add your FXML loaded scene to it

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Hexagon Grid");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }



    public static void main(String[] args) {
        launch(args);
    }
}
