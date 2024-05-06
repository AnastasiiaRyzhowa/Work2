package medical.app.medicalappfx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import medical.app.medicalappfx.dto.ChartAnnotationNode;
import medical.app.medicalappfx.dto.LabelCountData;

import java.util.HashMap;
import java.util.Map;

public class ResultController {

    @FXML
    private Button exitButton;

    @FXML
    private TableView<LabelCountData> tableView;

    @FXML
    void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


    public void initialize(ObservableList<ChartAnnotationNode> annotationNodes) {
        initTable(annotationNodes);
    }

    private void initTable(ObservableList<ChartAnnotationNode> annotationNodes) {
        // Подготовим столбцы для таблицы
        TableColumn<LabelCountData, String> labelColumn = new TableColumn<>("Label");
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));

        TableColumn<LabelCountData, Integer> countColumn = new TableColumn<>("Count");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        // Очистка и добавление столбцов
        tableView.getColumns().clear();
        tableView.getColumns().addAll(labelColumn, countColumn);

        // Подготовка данных для таблицы
        tableView.setItems(aggregateLabels(annotationNodes));
    }

    private static ObservableList<LabelCountData> aggregateLabels(ObservableList<ChartAnnotationNode> nodes) {
        Map<String, Integer> counts = new HashMap<>();

        // Подсчет меток и их количества
        for (ChartAnnotationNode node : nodes) {
            String labelText = node.getNode().getText();
            counts.put(labelText, counts.getOrDefault(labelText, 0) + 1);
        }

        // Преобразование в ObservableList
        ObservableList<LabelCountData> list = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            list.add(new LabelCountData(entry.getKey(), entry.getValue()));
        }

        return list;
    }
}
