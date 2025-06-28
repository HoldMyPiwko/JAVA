module kolos_2_22.rgbinpicutre {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens kolos_2_22.rgbinpicutre to javafx.fxml;
    exports kolos_2_22.rgbinpicutre;
}