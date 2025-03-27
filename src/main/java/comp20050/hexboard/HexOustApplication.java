package comp20050.hexboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class HexOustApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
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