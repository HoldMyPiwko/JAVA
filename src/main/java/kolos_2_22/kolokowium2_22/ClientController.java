package kolos_2_22.kolokowium2_22;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController {
    @FXML
    private Label wordCountLabel;

    @FXML
    private ListView<String> wordList;

    @FXML
    private TextField filterField;

    private final List<WordEntry> allWords = new ArrayList<>();
    private final ObservableList<String> displayWords = FXCollections.observableArrayList();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    @FXML
    public void initialize(){
        startClient();
        wordList.setItems(displayWords);
        setUpListener();
    }

    public void startClient() {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 5000);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String line;
                while ((line = in.readLine()) != null) {
                    final String word = line.trim();
                    final String time = LocalTime.now().format(timeFormatter);

                    WordEntry entry = new WordEntry(word, time);
                    allWords.add(entry);


                    Platform.runLater(this::updateWordCount);
                }

                } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setUpListener(){
        filterField.textProperty().addListener((obs, oldVal, newVal) ->
                                                updateDisplayList());
    }

    public void updateWordCount(){
        wordCountLabel.setText(String.valueOf(allWords.size()));
    }

    public void updateDisplayList(){
        String filter = filterField.getText().trim().toLowerCase();

        List<String> filteredText = allWords.stream()
                .filter(w -> filter.isEmpty() || w.getWord().toLowerCase().startsWith(filter))
                .map(w -> w.getTimestamp() + " " + w.getWord())
                .sorted(Comparator.comparing(s-> normalize(s.split(" ", 2)[1]),
                        String.CASE_INSENSITIVE_ORDER)).toList();

        displayWords.addAll(filteredText);
    }

    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }
}

