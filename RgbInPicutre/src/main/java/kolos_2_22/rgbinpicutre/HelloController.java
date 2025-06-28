package kolos_2_22.rgbinpicutre;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import kolos_2_22.rgbinpicutre.Server.ClientThread;
import kolos_2_22.rgbinpicutre.Server.Server;

import java.io.*;
import java.net.Socket;

public class HelloController {
    @FXML
    private Slider blurSlider;
    @FXML
    private Label blurLabel;
    @FXML
    private Slider graySlider;
    @FXML
    private Label grayLabel;
    @FXML
    private ImageView originalImageView;
    @FXML
    private ImageView processedImageView;

    Socket socket;
    BufferedReader in;
    PrintWriter out;


    @FXML
    public void initialize() throws IOException {
        Image image = new Image("C:\\Users\\koszo\\IntellijProject\\RgbInPicutre\\obraz.png");
        System.out.println(image.isError());
        originalImageView.setImage(image);

        graySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            grayLabel.setText(String.format("Szarość: %.2f", newVal.doubleValue()));
            try {
                onConvert();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        blurSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            blurLabel.setText("Blur: " + newVal.intValue());
        });

        socket = new Socket("localhost", 5000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
    }

    //zmiana na szarosc
    @FXML
    protected void onConvert() throws IOException {
        Image input = originalImageView.getImage();

        if (input == null) return;

        double grayFactor = graySlider.getValue();

        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = reader.getColor(x, y);

                double gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                double r = mix(gray, color.getRed(), 1 - grayFactor);
                double g = mix(gray, color.getGreen(), 1 - grayFactor);
                double b = mix(gray, color.getBlue(), 1 - grayFactor);

                Color newColor = new Color(r, g, b, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }
        new ClientThread(socket).send("Zastosowano odcień szarości na poziomie: " + grayFactor);
        processedImageView.setImage(output);
    }

    //metoda do odcienia szarosci (pojebane)
    public double mix(double original, double gray, double factor) {
        return original * (1 - factor) + gray * factor;
    }

    public void onBlur() throws IOException {
        Image input = originalImageView.getImage();
        if (input == null) return;

        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        int blurRadius = (int) blurSlider.getValue();

        new ClientThread(socket).send("Zastosowano rozmycie " + blurRadius);

        WritableImage output = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, getBlurredColor(reader, x, y, blurRadius, width, height));
            }
        }
        processedImageView.setImage(output);
    }

    //blurowanie (nic nie czaje)
    private Color getBlurredColor(PixelReader reader, int x, int y, int radius, int width, int height) {
        double r = 0, g = 0, b = 0;
        int count = 0;

        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                    Color c = reader.getColor(nx, ny);
                    r += c.getRed();
                    g += c.getGreen();
                    b += c.getBlue();
                    count++;
                }
            }
        }
        return new Color(r / count, g / count, b / count, 1.0);
    }


}