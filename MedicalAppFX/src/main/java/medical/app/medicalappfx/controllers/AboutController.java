package medical.app.medicalappfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private Button closeBut;

    @FXML
    void closeButAction(ActionEvent event) {
        Stage stage = (Stage) closeBut.getScene().getWindow();
        stage.close();
    }

}
