package medical.app.medicalappfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MedicalAppFxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MedicalAppFxApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 910, 600);
        stage.setTitle("MedicalAppFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}