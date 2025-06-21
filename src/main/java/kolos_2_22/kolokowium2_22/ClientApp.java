package kolos_2_22.kolokowium2_22;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application{
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view.fxml"));
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setTitle("Word Client");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
