package kolos_2.kolokwium2021;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DrawController {
    @FXML
    private Canvas canvas;
    @FXML
    private Label offsetLabel;

    private double offsetX = 0;
    private double offsetY = 0;

    private static class Line {
        double x1, y1, x2, y2;
        Color color;

        Line(double x1, double y1, double x2, double y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    private final List<Line> lines = new ArrayList<>();

    public void drawLine(double x1, double y1, double x2, double y2, Color color) {
        lines.add(new Line(x1, y1, x2, y2, color));
        redraw();
    }

    public void clear() {
        lines.clear();
        offsetX = 0;
        offsetY = 0;
        Platform.runLater(this::redraw);
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Line line : lines) {
            gc.setStroke(line.color);
            gc.strokeLine(
                    line.x1 - offsetX,
                    line.y1 - offsetY,
                    line.x2 - offsetX,
                    line.y2 - offsetY
            );
        }

        offsetLabel.setText("Offset: (" + offsetX + ", " + offsetY + ")");
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> offsetY -= 10;
            case DOWN -> offsetY += 10;
            case LEFT -> offsetX -= 10;
            case RIGHT -> offsetX += 10;
        }
        redraw();
    }
}
