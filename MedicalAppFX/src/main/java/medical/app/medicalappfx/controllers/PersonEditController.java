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
import medical.app.medicalappfx.dto.PersonDto;
import medical.app.medicalappfx.dto.ShortPersonDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;


public class PersonEditController {

    private static final String BASE_URL = "http://localhost:8080/api/persons/";

    @FXML
    private TextField addressInput;

    @FXML
    private TextField birthDayInput;

    @FXML
    private Button closeButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField fullNameInput;
    private PersonDto localPerson;

    @FXML
    void closeButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void editButtonAction(ActionEvent event) {
        saveChange();
    }

    private void saveChange() {
        ShortPersonDto shortPersonDto = new ShortPersonDto();
        shortPersonDto.setFullName(fullNameInput.getText());
        shortPersonDto.setAddress(addressInput.getText());
        LocalDate localDate = convertToDate(birthDayInput.getText());
        shortPersonDto.setDateOfBirth(localDate);

        updatePerson(localPerson.getId(), shortPersonDto);
    }

    public static LocalDate convertToDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void initialize(PersonDto personDto) {
        localPerson = personDto;
        fullNameInput.setText(personDto.getFullName());
        addressInput.setText(personDto.getAddress());
        String date = formatDate(personDto.getDateOfBirth());
        birthDayInput.setText(date);
    }

    public void updatePerson(UUID id, ShortPersonDto person) {
        // Использование Gson с адаптером для LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String json = gson.toJson(person);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/persons/update/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Response: " + response.body());
                showAlert("Успешно!", "Данные пациента изменены");
            } else {
                System.out.println("Failed to send POST request: " + response.statusCode());
                showAlert("Ошибка!", "500 Bad request!");

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }
}
