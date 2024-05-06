package medical.app.medicalappfx.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import medical.app.medicalappfx.adapter.LocalDateTimeAdapter;
import medical.app.medicalappfx.dto.ElectCreateDto;
import medical.app.medicalappfx.dto.PersonDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class ElectAddController {

    @FXML
    private Button addButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField dateInput;

    @FXML
    private Button fileInputButton;

    @FXML
    private Text fileNameText;
    private PersonDto localPersonDto;
    private File localFile;

    @FXML
    void addButtonAction(ActionEvent event) {
        createElect();
    }

    private void createElect() {

        if (localFile == null) {
            showAlert("Ошибка!", "Файл ЭКГ не загружен");
        } else {
            ElectCreateDto dto = new ElectCreateDto();
            String data = readFromFile();
            dto.setData(data);
            LocalDateTime localDateTime = parseStringToLocalDateTime(dateInput.getText());
            dto.setAppointmentDateTime(localDateTime);
            sendCreate(dto);
            showAlert("Успешно", "ЭКГ добавлено!");
        }
    }

    private String readFromFile() {
        StringBuilder lines = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(localFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.append(line + ",");
            }
        } catch (IOException e) {
//            e.printStackTrace();
            showAlert("Ошибка!", "Файл ЭКГ не загружен");
        }

        return String.valueOf(lines);
    }

    public static LocalDateTime parseStringToLocalDateTime(String dateTimeStr) {
        // Создаем объект DateTimeFormatter с нужным нам форматом
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd.MM.yyyy");

        // Пытаемся распарсить строку в LocalDateTime
        try {
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Некорректный формат даты и времени: " + dateTimeStr);
            showAlert("Ошибка", "Некорректный формат даты и времени. Используйте \"ЧЧ:ММ:СС - ДД.ММ.ГГГГ\"");
            return null;
        }
    }

    private static void showAlert(String title, String message) {
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

    @FXML
    void fileInputButtonAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Открыть файл");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files", "*.txt*"));
        File f = fc.showOpenDialog(null);

        if (f != null) {
            fileNameText.setText(f.getName());
            localFile = f;
        }


    }

    public void initialize(PersonDto localPersonDto) {
        this.localPersonDto = localPersonDto;
        dateInput.setText("17:30:00 - 12.05.2023");
    }

    public void sendCreate(ElectCreateDto dto) {
        // Использование Gson с адаптером для LocalDate
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(dto);

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/electrocardiography/create/person/" + localPersonDto.getId()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("Response: " + response.body());
                showAlert("ЭКГ добавлено!", "ЭКГ успешно добавлен!");
            } else {
                System.out.println("Failed to send POST request: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
