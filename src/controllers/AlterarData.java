package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import session.Session;

public class AlterarData implements Initializable {

    @FXML
    private DatePicker dateAlterarData;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dateAlterarData.setValue(Session.getDataAtual());
    }

    @FXML
    void alterarData(ActionEvent event) {
        LocalDate novaData = dateAlterarData.getValue();

        if (novaData == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Data inválida");
            alert.setContentText("Selecione uma data válida.");
            alert.showAndWait();
            return;
        }

        Session.setDataAtual(novaData);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Data alterada");
        alert.setContentText("A data foi alterada com sucesso.");
        alert.showAndWait();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
