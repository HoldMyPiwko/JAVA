package kolos_2_22.sharedstickynotesboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kolos_2_22.sharedstickynotesboard.Client.ServerThread;

public class StickyNoteClientApp extends Application {

    private ServerThread serverThread;

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Start komunikacji z serwerem
        serverThread = new ServerThread("localhost", 5000);

        // 2. Tworzenie GUI
        Pane board = new Pane();
        board.setPrefSize(600, 400);
        board.setStyle("-fx-background-color: #f3f3f3;");

        TextField noteText = new TextField();
        noteText.setPromptText("Wpisz treść notatki");
        ColorPicker colorPicker = new ColorPicker(Color.YELLOW);

        VBox controls = new VBox(10, noteText, colorPicker);
        controls.setPadding(new Insets(10));
        HBox root = new HBox(20, board, controls);
        root.setPadding(new Insets(10));

        // 3. Kliknięcie w planszę = wysyłka notatki do serwera
        board.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String text = noteText.getText().isEmpty() ? "(pusta)" : noteText.getText();
            Color color = colorPicker.getValue();

            double noteWidth = 120;
            double noteHeight = 60;
            double x = event.getX() - noteWidth / 2;
            double y = event.getY() - noteHeight / 2;

            Note note = new Note(x, y, text, toHex(color));
            serverThread.send(note);
        });

        // 4. Odbieranie notatek od serwera i rysowanie na planszy
        serverThread.setNoteConsumer(note -> Platform.runLater(() -> {
            double noteWidth = 120;
            double noteHeight = 60;
            Rectangle rect = new Rectangle(note.x(), note.y(), noteWidth, noteHeight);
            rect.setArcWidth(15);
            rect.setArcHeight(15);
            rect.setFill(Color.web(note.color()));
            rect.setStroke(Color.DARKGRAY);

            Text label = new Text(note.x() + 10, note.y() + 30, note.text());
            label.setFill(Color.BLACK);

            board.getChildren().addAll(rect, label);
        }));

        serverThread.start();

        stage.setScene(new Scene(root, 800, 430));
        stage.setTitle("Sticky Notes Board – klient");
        stage.show();
    }

    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255));
    }

    public static void main(String[] args) {
        launch();
    }
}
