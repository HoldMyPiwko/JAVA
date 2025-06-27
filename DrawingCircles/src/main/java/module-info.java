module kolos_2_22.drawingcircles {
    requires javafx.controls;
    requires javafx.fxml;


    opens kolos_2_22.drawingcircles to javafx.fxml;
    exports kolos_2_22.drawingcircles;
}