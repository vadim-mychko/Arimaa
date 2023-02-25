module cz.cvut.fel.arimaa.gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.cvut.fel.arimaa.gui to javafx.fxml;
    exports cz.cvut.fel.arimaa.gui;
}
