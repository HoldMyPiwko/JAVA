package kolos_2_22.drawingcircles;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HelloController {


    @FXML
    public TextField textField;
    @FXML
    public ComboBox chooseForm;
    @FXML
    private TextField inputField;
    @FXML
    private ListView<String> listView;
    @FXML
    private Slider sizeSlider;
    @FXML
    private Label sliderLabel;
    @FXML
    private Canvas canvas;
    private final ObservableList<String> allItems = FXCollections.observableArrayList();
    private FilteredList<String> filteredList;
    private String itemToBeingEdited = null;
    @FXML
    private ColorPicker colorPicker = new ColorPicker();
    private PrintWriter socketOut;

    public void initialize() throws IOException {
        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sliderLabel.setText("Rozmiar: " + newVal.intValue());
        });
        filteredList = new FilteredList<>(allItems, s -> true);
        listView.setItems(filteredList);


        chooseForm.getItems().addAll("Koło", "Kwadrat", "Trójkąt");
        chooseForm.setValue("Koło");


        setUpFiltering();
        editListElement();
        initListByWords();
        chooseForm();
        startClient();
    }

    public void initListByWords() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/words.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                allItems.add(line);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    public void onAdd(ActionEvent actionEvent) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("words.txt", true))) {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                if (itemToBeingEdited != null) { //tryb edycji istniejącego obiektu w liscie
                    int index = allItems.indexOf(itemToBeingEdited);
                    if (index != -1) {
                        allItems.set(index, text);
                    }
                    itemToBeingEdited = null;
                } else {
//                    allItems.add(text);
                    writer.write(text + "\n");
                }
                inputField.clear();
            }
            socketOut.println(text);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void onSort(ActionEvent actionEvent) {
        List<String> sortedList = allItems.stream().sorted().toList();
        allItems.setAll(sortedList);
    }

    public void onDelete(ActionEvent actionEvent) {
        final int indexToRemove = listView.getSelectionModel().getSelectedIndex();
        if (indexToRemove != -1) {
            allItems.remove(indexToRemove);
        }
    }

    public void startClient() {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 5000);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                socketOut = new PrintWriter(socket.getOutputStream(), true);


                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Z serwera: " + line);


                    String finalLine = line;

                    if (line.contains(";")) { //obsługa róznych typów danych do servera;

                        String[] parts = line.split(";");
                        if (parts.length == 5) {
                            String shape = parts[0];
                            Color color = Color.valueOf(parts[1]);
                            double x = Double.parseDouble(parts[2]);
                            double y = Double.parseDouble(parts[3]);
                            double r = Double.parseDouble(parts[4]);

                            Platform.runLater(() -> drawShape(shape, x, y, r, color));
                        }

                    } else {
                        Platform.runLater(() -> allItems.add(finalLine));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }).start();
    }

    public void onCanvasClick(MouseEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double x = event.getX();
        double y = event.getY();
        double r = sizeSlider.getValue();
        Color c = colorPicker.getValue();


        if (Objects.equals(chooseForm(), "Trójkąt")) {
            double v = r * Math.sin(Math.toRadians(60));
            double[] xPoints = {x, x - v, x + v};
            double[] yPoints = {y - r, y + r / 2, y + r / 2};
            gc.setFill(c);
            gc.fillPolygon(xPoints, yPoints, 3);
        }

        if (Objects.equals(chooseForm(), "Koło")) {
            gc.setFill(c);
            gc.setLineWidth(1);
            gc.fillOval(x - r, y - r, r * 2, r * 2);
        }

        if (Objects.equals(chooseForm(), "Kwadrat")) {
            gc.setFill(c);
            gc.fillRect(x - r, y - r, r * 2, r * 2);
        }

        String shape = chooseForm();
        String color = colorPicker.getValue().toString();
        double size = sizeSlider.getValue();
        String message = shape + " ;" + color + " ;" + size + " ;";

        if (socketOut != null) {
            socketOut.println(message);
        }
    }

    public void setUpFiltering() { //ulepszone sortowanie gotowym obiektem FilteredList *zajebiste*
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            final String filter = newVal.toLowerCase().trim().toLowerCase();
            filteredList.setPredicate(s -> filter.isEmpty() || s.toLowerCase().startsWith(filter));
        });
    }

    public void editListElement() {

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                final String itemToEdit = listView.getSelectionModel().getSelectedItem();
                if (itemToEdit != null) {
                    inputField.setText(itemToEdit);
                    itemToBeingEdited = itemToEdit;
                }
            }
        });
    }

    public void sortAZ() {
        List<String> sortedAZList = allItems.stream().sorted().toList();
        allItems.setAll(sortedAZList);
    }

    public void sortZA() {
        List<String> sortedZAList = allItems.stream().sorted(Comparator.reverseOrder()).toList();
        allItems.setAll(sortedZAList);
    }

    private String chooseForm() {
        String selected;
        selected = (String) chooseForm.getValue();
        return selected;
    }

    private void drawShape(String shape, double x, double y, double r, Color c) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(c);
        if (shape.equals("Koło")) {
            gc.fillOval(x - r, y - r, r * 2, r * 2);
        } else if (shape.equals("Kwadrat")) {
            gc.fillRect(x - r, y - r, r * 2, r * 2);
        } else if (shape.equals("Trójkąt")) {
            double v = r * Math.sin(Math.toRadians(60));
            double[] xPoints = {x, x - v, x + v};
            double[] yPoints = {y - r, y + r / 2, y + r / 2};
            gc.fillPolygon(xPoints, yPoints, 3);
        }
    }
}