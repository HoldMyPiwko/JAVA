package kolos_2.kolokwium2021;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kolos_2.kolokwium2021.Server.Server;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Line drawing server!");
        stage.setScene(scene);
        stage.show();

        scene.getRoot().requestFocus();

        DrawController controller = fxmlLoader.getController();
        new Thread(() -> new Server(controller)).start();

    }

    public static void main(String[] args) {
        launch();
    }
}