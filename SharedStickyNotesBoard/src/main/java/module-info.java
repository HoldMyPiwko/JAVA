module kolos_2_22.sharedstickynotesboard {
    requires javafx.controls;
    requires javafx.fxml;


    opens kolos_2_22.sharedstickynotesboard to javafx.fxml;
    exports kolos_2_22.sharedstickynotesboard;
}