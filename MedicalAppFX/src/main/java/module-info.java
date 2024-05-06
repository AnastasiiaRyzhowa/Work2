module medical.app.medicalappfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    // Требуется для работы с HTTP клиентом Apache
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    // Требуется для парсинга JSON через Jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    // LocalDate и LocalDateTime поддержка для Jackson
    requires com.fasterxml.jackson.datatype.jsr310;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.net.http;
    requires org.apache.logging.log4j;

    opens medical.app.medicalappfx to javafx.fxml;
    exports medical.app.medicalappfx;

    exports medical.app.medicalappfx.dto;
    opens medical.app.medicalappfx.dto to javafx.fxml;

    exports medical.app.medicalappfx.adapter;
    opens medical.app.medicalappfx.adapter to javafx.fxml;

    exports medical.app.medicalappfx.controllers;
    opens medical.app.medicalappfx.controllers to javafx.fxml;

}