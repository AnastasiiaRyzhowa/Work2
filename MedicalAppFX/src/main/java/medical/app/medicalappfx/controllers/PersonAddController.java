package medical.app.medicalappfx.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medical.app.medicalappfx.adapter.LocalDateAdapter;
import medical.app.medicalappfx.dto.ShortPersonDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PersonAddController {
    private static final String POST_URL = "http://localhost:8080/api/persons";

    @FXML
    private Button addButton;

    @FXML
    private TextField addressInput;

    @FXML
    private TextField birthDayInput;

    @FXML
    private Button closeButton;

    @FXML
    private TextField fullNameInput;

    @FXML
    void addButtonAction(ActionEvent event) {
        ShortPersonDto shortPersonDto = new ShortPersonDto();
        shortPersonDto.setFullName(fullNameInput.getText());
        shortPersonDto.setAddress(addressInput.getText());
        LocalDate date = convertToDate(birthDayInput.getText());
        if (date != null) {
            shortPersonDto.setDateOfBirth(convertToDate(birthDayInput.getText()));
            sendPostRequest(shortPersonDto);
        } else
            showAlert("Ошибка", "Неверный формат даты! Используйте \"ДД.ММ.ГГГГ\".");

    }

    public void sendPostRequest(ShortPersonDto person) {
        // Использование Gson с адаптером для LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String json = gson.toJson(person);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/persons"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("Response: " + response.body());
                showAlert("Пациент добавлен!", "Пациент успешно добавлен!");
                fullNameInput.clear();
                addressInput.clear();
                birthDayInput.clear();
            } else {
                System.out.println("Failed to send POST request: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static LocalDate convertToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
