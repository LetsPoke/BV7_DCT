package uebung7;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Uebung7View.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Uebung7 - WS20 - Kühnau");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}