package medical.app.medicalappfx.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import medical.app.medicalappfx.MedicalAppFxApplication;
import medical.app.medicalappfx.adapter.LocalDateTimeAdapter;
import medical.app.medicalappfx.adapter.UuidAdapter;
import medical.app.medicalappfx.dto.ElectrocardiographyDto;
import medical.app.medicalappfx.dto.PersonDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonController {
    private static final Logger logger = LogManager.getLogger(PersonController.class);
    private PersonDto localPersonDto;


    @FXML
    private Button addButton;

    @FXML
    private Text addressText;

    @FXML
    private Text birthDayText;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private Text fullnameText;

    @FXML
    private Button replaceButton;

    @FXML
    private Button showButton;

    @FXML
    private TableView<ElectrocardiographyDto> tableView;

    @FXML
    void addButtonAction(ActionEvent event) {
        addDiagram();
    }

    private void addDiagram() {
        openCreateElectView();
    }

    private void openCreateElectView() {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("elect-add-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (IOException e) {
//            logger.error("Frame failed to load");
        }
        ElectAddController electAddController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Окно создания диаграммы:");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        electAddController.initialize(localPersonDto);
    }


    @FXML
    void deleteButtonAction(ActionEvent event) {
        ElectrocardiographyDto electDto = tableHandler();
        if (electDto != null) {
            deleteElectById(electDto.getId());
            showAlert("Успешно", "ЭКГ  успешно удалено!");
        } else
            showAlert("Ошибка", "Выберите ЭКГ из списка");

        try {
            Thread.sleep(1000); // Задержка на 1000 миллисекунд (1 секунда)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ElectrocardiographyDto> list = fetchElectrocardiography(localPersonDto.getId());
        initTable(list);
    }

    public void deleteElectById(UUID id) {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/api/electrocardiography/delete/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .DELETE() // Использование метода DELETE
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    if (statusCode == 204) {
                        System.out.println(statusCode);
                        System.out.println("OK");
                    } else {
                        System.out.println(statusCode);

                    }
                })
                .exceptionally(e -> {
                    System.out.print("Ошибка при выполнении запроса: ");
                    e.printStackTrace();
                    return null;
                });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void replaceButtonAction(ActionEvent event) {
        List<ElectrocardiographyDto> list = fetchElectrocardiography(localPersonDto.getId());
        initTable(list);
    }


    @FXML
    void showButtonAction(ActionEvent event) {
        openDiagramView();
    }

    private void openDiagramView() {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("diagram-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 1200, 650);
        } catch (IOException e) {
//            logger.error("Frame failed to load");
        }
        DiagramController diagramController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Окно просмотра диаграммы:");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        diagramController.initialize(tableHandler(), localPersonDto);
    }

    private ElectrocardiographyDto tableHandler() {
        ObservableList<ElectrocardiographyDto> selectedItems = tableView.getSelectionModel().getSelectedItems();
        return selectedItems.get(0);
    }

    public void initialize(PersonDto personDto) {
        localPersonDto = personDto;

        initTextField(personDto);

        List<ElectrocardiographyDto> list = fetchElectrocardiography(personDto.getId());
        initTable(list);
    }

    private void initTextField(PersonDto personDto) {
        fullnameText.setText(personDto.getFullName());
        birthDayText.setText(String.valueOf(personDto.getDateOfBirth()));
        addressText.setText(personDto.getAddress());
    }

    public void initTable(List<ElectrocardiographyDto> list) {
        // Создание и настройка столбца Даты и Времени
        TableColumn<ElectrocardiographyDto, LocalDateTime> dateTimeColumn = new TableColumn<>("Дата прохождения ЭКГ");
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDateTime"));
        dateTimeColumn.setCellFactory(col -> new TableCell<ElectrocardiographyDto, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Форматирование даты и времени
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
                    setText(item.format(formatter));
                }
            }
        });

        // Очистка и добавление столбцов в TableView
        tableView.getColumns().clear();
//        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(dateTimeColumn);

        // Загрузка данных в TableView
        ObservableList<ElectrocardiographyDto> observableList = FXCollections.observableArrayList(list);
        tableView.setItems(observableList);
    }

    public List<ElectrocardiographyDto> fetchElectrocardiography(UUID id) {
        String url = "http://localhost:8080/api/electrocardiography/person/" + id;
        List<ElectrocardiographyDto> listElect = new ArrayList<>();

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = responseReader.readLine()) != null) {
                    response.append(line);
                }
                responseReader.close();

                listElect = fromJson(String.valueOf(response));

                return listElect;

            } else {
                logger.error("Ошибка при выполнении запроса. Код ошибки: {}", responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ElectrocardiographyDto> fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UUID.class, new UuidAdapter())
                .create();

        Type listType = new TypeToken<List<ElectrocardiographyDto>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}
