package medical.app.medicalappfx.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import medical.app.medicalappfx.MedicalAppFxApplication;
import medical.app.medicalappfx.dto.PersonDto;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainController {

    private final String SEARCH_URL = "http://localhost:8080/api/persons/search/";
    private final String GET_ALL_PERSON_URL = "http://localhost:8080/api/persons";
    private static final Logger logger = LogManager.getLogger(MainController.class);


    @FXML
    private Button aboutButton;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button replaceButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button showButton;

    @FXML
    private TableView<PersonDto> tableView;

    @FXML
    void aboutButtonAction(ActionEvent event) {
        openAboutView();
    }

    private void openAboutView() {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("about-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 500, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setTitle("Автор проекта");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void addButtonAction(ActionEvent event) {
        openCreateView();

    }

    private void openCreateView() {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("person-add-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (IOException e) {
            logger.error("Frame failed to load");
        }

        Stage stage = new Stage();
        stage.setTitle("Добавить пациента в базу данных");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void deleteButtonAction(ActionEvent event) {
        PersonDto personDto = tableHandler();
        if (personDto != null) {
            deletePersonById(personDto.getId(), personDto.getFullName());
            showAlert("Успешно", "Пользователь " + personDto.getFullName() + " успешно удален!");
        } else
            showAlert("Ошибка", "Выберите пациента из списка");

        try {
            Thread.sleep(1000); // Задержка на 1000 миллисекунд (1 секунда)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        populateTableView(fetchPersons(GET_ALL_PERSON_URL));

    }

    @FXML
    void updateButtonAction(ActionEvent event) {
        populateTableView(fetchPersons(GET_ALL_PERSON_URL));
    }

    @FXML
    void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void openEditView(PersonDto personDto) {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("person-edit-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 600, 400);
        } catch (IOException e) {
        }
        PersonEditController personEditController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Изменить данные пациента");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        personEditController.initialize(personDto);
    }

    @FXML
    void replaceButtonAction(ActionEvent event) {
        PersonDto personDto = tableHandler();
        if (personDto != null) {
            openEditView(personDto);
        } else
            showAlert("Ошибка", "Выберите пациента из списка");
    }

    @FXML
    void searchButtonAction(ActionEvent event) {
        if (!searchField.getText().isEmpty()) {
            List<PersonDto> person = new ArrayList<>();
            try {
                String encodedName = URLEncoder.encode(searchField.getText(), "UTF-8");
                String searchUrl = SEARCH_URL + encodedName;
                person = fetchPersons(searchUrl);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            if (person.isEmpty()) {
                showAlert("Ошибка поиска", "Пользователь с именем: " + searchField.getText() + " не найден!");
            } else {
                populateTableView(person);
            }
        } else {
            populateTableView(fetchPersons(GET_ALL_PERSON_URL));
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
    void showButtonAction(ActionEvent event) {
        PersonDto personDto = tableHandler();
        if (personDto != null) {
            openPersonFrame(personDto);
        }

    }

    private PersonDto tableHandler() {
        try {
            ObservableList<PersonDto> selectPerson = tableView.getSelectionModel().getSelectedItems();
            return selectPerson.get(0);
        } catch (IndexOutOfBoundsException e) {
            showAlert("Ошибка", "Выберете пациента из списка!");
        }
        return null;
    }

    public void initialize() {
        populateTableView(fetchPersons(GET_ALL_PERSON_URL));
    }

    public List<PersonDto> fetchPersons(String url) {
        List<PersonDto> personsList = new ArrayList<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            // Установка заголовков, если необходимо

            var response = client.execute(request);
            String json = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());  // Для корректной работы с LocalDate и LocalDateTime
            List<PersonDto> persons = mapper.readValue(json, new TypeReference<List<PersonDto>>() {
            });

            personsList = persons;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personsList;
    }

    public void deletePersonById(UUID id, String fullName) {
        HttpClient client = HttpClient.newHttpClient();
        String uri = "http://localhost:8080/api/persons/" + id;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .DELETE() // Использование метода DELETE
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(statusCode -> {
                    if (statusCode == 204) {
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

    public void populateTableView(List<PersonDto> personList) {
        // Очищаем старые столбцы
        tableView.getColumns().clear();

        // Создание столбцов

        TableColumn<PersonDto, String> fullNameColumn = new TableColumn<>("ФИО");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<PersonDto, LocalDate> dateOfBirthColumn = new TableColumn<>("Дата рождения");
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        TableColumn<PersonDto, String> addressColumn = new TableColumn<>("Адрес");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Добавление столбцов в таблицу
//        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(fullNameColumn);
        tableView.getColumns().add(dateOfBirthColumn);
        tableView.getColumns().add(addressColumn);

        // Заполнение таблицы данными
        ObservableList<PersonDto> observableList = FXCollections.observableArrayList(personList);
        tableView.setItems(observableList);
    }

    private void openPersonFrame(PersonDto personDto) {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("person-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 910, 600);
        } catch (IOException e) {
        }
        PersonController personController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Карточка пациента");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        personController.initialize(personDto);
    }

}
