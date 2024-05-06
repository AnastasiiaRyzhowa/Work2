package medical.app.medicalappfx.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import medical.app.medicalappfx.MedicalAppFxApplication;
import medical.app.medicalappfx.adapter.LocalDateTimeAdapter;
import medical.app.medicalappfx.adapter.UuidAdapter;
import medical.app.medicalappfx.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DiagramController {

    private static final Logger logger = LogManager.getLogger(DiagramController.class);


    private ObservableList<ChartAnnotationNode> annotationNodes;
    private ObservableList<ChartAnnotationNode> annotationNodesForArea;
    private ElectrocardiographyDataDto localData;

    //Верхняя и нижняя граница по графику
    private double localUpperX;
    private double localLowerX;

    // Переменные для хранения начальной позиции курсора и метки
    private double mouseAnchorX;
    private double mouseAnchorY;
    private double initialTranslateX;
    private double initialTranslateY;

    @FXML
    private ListView<Label> annotationArea;

    @FXML
    private Button helpButton;

    @FXML
    private Text birthText;

    @FXML
    private Button showSaveAnnotation;

    @FXML
    private Button deleteAllAnnotation;

    @FXML
    private Button closeDiagramButton;

    @FXML
    private Text dateText;


    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private Text nameText;

    @FXML
    private Pane paneForChart;

    @FXML
    private Button resultAnnotationButton;

    @FXML
    private Button saveDiagramButton;

    @FXML
    private Button scrollEndButton;

    @FXML
    private Button scrollNullButton;

    @FXML
    private Button scrollStartButton;

    @FXML
    private CategoryAxis xAxios;

    @FXML
    private NumberAxis yAxios;
    private int sizeChart;


    @FXML
    void deleteAllAnnotationAction(ActionEvent event) {
        annotationNodes.clear();
        updateAnnotationArea();
    }

    @FXML
    void showSaveAnnotationAction(ActionEvent event) {
        annotationNodes.clear();
        localData = fetchElectrocardiography(localData.getId());
        if (localData.getNodes() != null) {
            addedAnnotationOn();
        }
        updateAnnotationArea();

    }

    @FXML
    void closeDiagramAction(ActionEvent event) {
        Stage stage = (Stage) closeDiagramButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void resultAnnotationAction(ActionEvent event) {
        openResultView(annotationNodes);
    }

    @FXML
    void helpButtonAction(ActionEvent event) {
        openHelpView();
    }

    @FXML
    void saveDiagramAction(ActionEvent event) {
        saveAnnotation();
    }

    @FXML
    void scrollEndButtonAction(ActionEvent event) {
        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());
        xAxisLocal.setUpperBound(sizeChart);
        xAxisLocal.setLowerBound(sizeChart - (sizeChart / 2));
        xAxisLocal.setTickUnit(10000);
        update();
    }

    @FXML
    void scrollNullButtonAction(ActionEvent event) {
        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());
        xAxisLocal.setUpperBound(sizeChart);
        xAxisLocal.setLowerBound(0);
        xAxisLocal.setTickUnit(10000);
        update();
    }

    @FXML
    void scrollStartButtonAction(ActionEvent event) {
        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());
        xAxisLocal.setUpperBound(sizeChart / 2);
        xAxisLocal.setLowerBound(0);
        xAxisLocal.setTickUnit(10000);
        update();
    }

    @FXML
    void clickMouse(MouseEvent event) {
        clickInDiagram(event);
    }

    @FXML
    void scrollEventChart(ScrollEvent event) {
        scrollInDiagram(event);
    }


    private void openHelpView() {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("help-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 500, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setTitle("Помощь");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
    }


    private void openResultView(ObservableList<ChartAnnotationNode> annotationNodes) {
        FXMLLoader loader = new FXMLLoader(MedicalAppFxApplication.class.getResource("result-view.fxml"));
        Scene scene = null;


        try {
            scene = new Scene(loader.load(), 510, 600);
        } catch (IOException e) {
//            logger.error("Frame failed to load");
        }
        ResultController resultController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Окно просмотра диаграммы:");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();

        resultController.initialize(annotationNodes);
    }


    private void saveAnnotation() {
        UUID electID = localData.getId();
        List<ShortNodeDto> shortNodeDtoList = new ArrayList<>();
        for (ChartAnnotationNode elem : annotationNodes) {
            ShortNodeDto shortNodeDto = new ShortNodeDto(elem.getNode().getText(), elem.getX(), elem.getY());
            shortNodeDtoList.add(shortNodeDto);
        }

        ShortElectrocardiographyDto shortElectrocardiographyDto =
                new ShortElectrocardiographyDto(shortNodeDtoList);


        sendElectrocardiographyData(electID, shortElectrocardiographyDto);
    }

    public void sendElectrocardiographyData(UUID electId, ShortElectrocardiographyDto dto) {
        try {
            URL url = new URL("http://localhost:8080/api/electrocardiography/update/data/" + electId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonInputString = gson.toJson(dto);
            System.out.println("@@@@@@@");
            System.out.println(jsonInputString);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                System.out.println("Data sent successfully.");
            } else {
                System.out.println("POST request not worked. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Метод инициализации окна. В методе вызываются методы инициализации графика и других полей окна данными
     * полученными от сервера
     *
     * @param dto - объект кардиограммы, передается из предыдущего окна
     */
    public void initialize(ElectrocardiographyDto dto, PersonDto personDto) {

        lineChart.setAnimated(false);
        lineChart.applyCss();
        lineChart.layout();

        ElectrocardiographyDataDto data = fetchElectrocardiography(dto.getId());
        localData = data;


        nameText.setText(personDto.getFullName());
        birthText.setText(formatDate(personDto.getDateOfBirth()));
        dateText.setText(formatDateTime(dto.getAppointmentDateTime()));


        //не забыть удалить и метод и вызов
        showDataElectDto(data);


        annotationNodes = FXCollections.observableArrayList();
        annotationNodesForArea = FXCollections.observableArrayList();

        initLineChart(data);


        updateAnnotationArea();
        final InvalidationListener listener = new InvalidationListener() {
            @Override
            public void invalidated(final Observable observable) {
                update();
            }
        };
        lineChart.needsLayoutProperty().addListener(listener);
        annotationNodes.addListener(listener);

//        if (localData.getNodes() != null) {
//            addedAnnotationOn(localData);
//        }


    }

    private void addedAnnotationOn() {
        for (NodeDto elem : localData.getNodes()) {
            addAnnotationInBase(elem.getLabel(), elem.getX(), elem.getY());
        }
    }

    private void showDataElectDto(ElectrocardiographyDataDto data) {
        System.out.println(data);
    }

    /**
     * Метод для форматирования LocalDate в строку формата "ДД.ММ.ГГГГ"
     *
     * @param date объект LocalDate
     * @return строка с отформатированной датой
     */
    public static String formatDate(LocalDate date) {
        // Создаем форматировщик даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        // Форматируем дату и возвращаем результат
        return date.format(formatter);
    }

    /**
     * Метод для форматирования LocalDateTime в строку формата "ДД.ММ.ГГГГ - ЧЧ:ММ:СС"
     *
     * @param dateTime объект LocalDateTime
     * @return строка с отформатированной датой
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        // Создаем форматировщик даты и времени
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        // Форматируем дату и время, возвращаем результат
        return dateTime.format(formatter);
    }


    /**
     * Метод инициализации графика
     *
     * @param data объект кардиограммы, получен от сервера
     */
    private void initLineChart(ElectrocardiographyDataDto data) {
        String[] parts = splitStringByComma(data.getData());
        List<Double> dots = new ArrayList<>();
        int len = parts.length;

        for (int i = 3; i < len; i++) {
            dots.add(Double.valueOf(parts[i]));
        }

        xAxios.setLabel("Секунды");
        yAxios.setLabel("Микрофараты");
        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Кардиограмма");

        sizeChart = dots.size() * 100;

        for (int i = 1; i < dots.size(); i++) {
            dataSeries.getData().add(new XYChart.Data<>(i * 100, dots.get(i)));
        }


        lineChart.getData().add(dataSeries);
        lineChart.setCreateSymbols(false);


        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());
        localUpperX = xAxisLocal.getUpperBound();
        localLowerX = xAxisLocal.getLowerBound();
    }

    /**
     * Метод, который необходим для того, чтобы разделить строку String на массив строк.
     *
     * @param input - строка, представляет собой координаты в виде цельной строки
     * @return возвращает массив строк с данными для составления графика
     */
    public static String[] splitStringByComma(String input) {
        // Проверяем, что входная строка не null и не пустая
        if (input == null || input.isEmpty()) {
            return new String[0];  // Возвращаем пустой массив, если нет входных данных
        }
        // Разделяем строку по запятой
        return input.split(",");
    }

    /**
     * Метод в котором реализован GET запрос для получения кардиограммы пациента
     *
     * @param id - id необходимой кардиограммы
     * @return возвращает объект кардиограммы
     */
    public ElectrocardiographyDataDto fetchElectrocardiography(UUID id) {
        String url = "http://localhost:8080/api/electrocardiography/" + id;
        ElectrocardiographyDataDto electDataDto = new ElectrocardiographyDataDto();

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


                electDataDto = fromJson(String.valueOf(response));


                return electDataDto;

            } else {
                logger.error("Ошибка при выполнении запроса. Код ошибки: {}", responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод в котором происходит преобразования Json в объект класса ElectrocardiographyDataDto
     *
     * @param json - Json в виде строки String
     * @return возвращает объект класса ElectrocardiographyDataDto
     */
    public static ElectrocardiographyDataDto fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UUID.class, new UuidAdapter())
                .create();

        Type listType = new TypeToken<ElectrocardiographyDataDto>() {
        }.getType();
        return gson.fromJson(json, listType);
    }

    private void clickInDiagram(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.isShiftDown()) {
            addAnnotation(event.getX(), event.getY());
        }
    }

    /**
     * Метод в который реализует возможность масштабирования графика и его прокрутку с помощью колеса мыши
     *
     * @param event - объект события ScrollEvent
     */
    private void scrollInDiagram(ScrollEvent event) {
        double zoomFactor = 10000.05;
        double deltaY = event.getDeltaY();
        double deltaX = event.getDeltaX();
        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());

        lineChart.getXAxis().setAutoRanging(false);
        lineChart.getYAxis().setAutoRanging(false);

        if (deltaY < 0) {
            zoomFactor = 20000 - zoomFactor;

            xAxisLocal.setUpperBound(xAxisLocal.getUpperBound() + zoomFactor);
            xAxisLocal.setLowerBound(xAxisLocal.getLowerBound() - zoomFactor);
            xAxisLocal.setTickUnit(10000);
        } else if (deltaY > 0) {
            xAxisLocal.setUpperBound(xAxisLocal.getUpperBound() - zoomFactor);
            xAxisLocal.setLowerBound(xAxisLocal.getLowerBound() + zoomFactor);
            xAxisLocal.setTickUnit(10000);
        }
        if (deltaX < 0) {
            zoomFactor = 20000 - zoomFactor;

            xAxisLocal.setUpperBound(xAxisLocal.getUpperBound() - zoomFactor);
            xAxisLocal.setLowerBound(xAxisLocal.getLowerBound() - zoomFactor);
            xAxisLocal.setTickUnit(10000);

        } else if (deltaX > 0) {
            xAxisLocal.setUpperBound(xAxisLocal.getUpperBound() + zoomFactor);
            xAxisLocal.setLowerBound(xAxisLocal.getLowerBound() + zoomFactor);
            xAxisLocal.setTickUnit(10000);

        }
        update();
        event.consume();
    }

    /**
     * Метод реализует добавления аннотаций на график
     *
     * @param displayX координата X
     * @param displayY координата Y
     */
    private void addAnnotation(double displayX, double displayY) {
        final Axis<Number> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();

        final double x = (xAxis.getValueForDisplay(xAxis.parentToLocal(displayX, 0).getX() - lineChart.getPadding().getLeft())).doubleValue();
        final double y = (yAxis.getValueForDisplay(yAxis.parentToLocal(0, displayY).getY() - lineChart.getPadding().getTop())).doubleValue();

        if (xAxis.isValueOnAxis(x) && yAxis.isValueOnAxis(y)) {
            Label label = new Label("Annotation " + System.currentTimeMillis());
            handleDoubleClick(label);
            annotationNodes.add(new ChartAnnotationNode(label, x, y));

            initEventForLabel(label);


        }

        updateAnnotationArea();
    }

    private void addAnnotationInBase(String labelsString, double x, double y) {
        final Axis<Number> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();

        if (xAxis.isValueOnAxis(x) && yAxis.isValueOnAxis(y)) {
            Label label = new Label(labelsString);

            annotationNodes.add(new ChartAnnotationNode(label, x, y));

            initEventForLabel(label);
        }

        updateAnnotationArea();
    }

    private void handleDoubleClickDeleteAnnotation(MouseEvent event, Label label) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            if (event.isShiftDown()) {
                paneForChart.getChildren().remove(label);
                annotationNodes.removeIf(node -> node.getNode() == label);
                update();
                updateAnnotationArea();
            } else {
                handleDoubleClick(label);
            }
        }

    }

    private void initEventForLabel(Label label) {

        label.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleDoubleClick(label);
            }
        });

        label.setOnMouseClicked(event -> handleDoubleClickDeleteAnnotation(event, label));

        // Нажатие кнопки мыши
        label.setOnMousePressed(event -> {
            // Сохраняем начальное положение курсора и метки
            mouseAnchorX = event.getSceneX();
            mouseAnchorY = event.getSceneY();
            initialTranslateX = label.getTranslateX();
            initialTranslateY = label.getTranslateY();


            final Axis<Number> xAxis = lineChart.getXAxis();
            final Axis<Number> yAxis = lineChart.getYAxis();

            final double x = (xAxis.getValueForDisplay(xAxis.parentToLocal(mouseAnchorX, 0).getX() - lineChart.getPadding().getLeft())).doubleValue();
            final double y = (yAxis.getValueForDisplay(yAxis.parentToLocal(0, mouseAnchorY).getY() - lineChart.getPadding().getTop())).doubleValue();
        });

        // Перетаскивание курсора с зажатой кнопкой
        label.setOnMouseDragged(event -> {
            // Обновляем смещение метки
            label.setTranslateX(initialTranslateX + event.getSceneX() - mouseAnchorX);
            label.setTranslateY(initialTranslateY + event.getSceneY() - mouseAnchorY);
        });

        updateAnnotationArea();
    }


    private void handleDoubleClick(Label label) {
        TextInputDialog dialog = new TextInputDialog(label.getText());
        dialog.setTitle("Редактирование");
        dialog.setHeaderText("Введите новый текст:");
        dialog.setContentText("Текст:");

        // Traditional approach to handling dialog results.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(label::setText); // Set the new text if present

        updateAnnotationArea();
    }

    /**
     * Метод, который обновляет состояние аннотаций на графике оставленными пользователем
     */
    private void update() {
        ObservableList<Node> children = paneForChart.getChildren();
        List<Node> childrenToRemove = new ArrayList<>();
        for (Node child : children) {
            if (!(child instanceof LineChart)) {
                childrenToRemove.add(child);
            }
        }

        paneForChart.getChildren().removeAll(childrenToRemove);

        final Axis<Number> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();

        NumberAxis xAxisLocal = ((NumberAxis) lineChart.getXAxis());
        double upperX = xAxisLocal.getUpperBound();
        double lowerX = xAxisLocal.getLowerBound();

        /* For each annotation, add a circle indicating the position and the custom node right next to it */
        for (ChartAnnotationNode annotation : annotationNodes) {
            if (annotation.getX() > lowerX && annotation.getX() < upperX) {
                final double x = xAxis.localToParent(xAxis.getDisplayPosition(annotation.getX()), 0).getX() + lineChart.getPadding().getLeft();
                final double y = yAxis.localToParent(0, yAxis.getDisplayPosition(annotation.getY())).getY() + lineChart.getPadding().getTop();
                final Node node = annotation.getNode();
                paneForChart.getChildren().add(node);
                node.relocate(x, y);
                node.toFront();
            }

        }

    }

    /**
     * Метод, обновляет список аннотаций на экране
     */
    private void updateAnnotationArea() {
        annotationArea.getItems().clear();
        annotationNodesForArea.clear();
        for (ChartAnnotationNode elem : annotationNodes) {
            Label label = new Label();
            label.setText(elem.getNode().getText());
            annotationNodesForArea.add(new ChartAnnotationNode(label, elem.getX(), elem.getY()));
        }

        for (ChartAnnotationNode elem : annotationNodesForArea) {
            annotationArea.getItems().add(elem.getNode());
        }
    }

}
