package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TelaAdministrador {

    private Parent root;
    private Stage stage;

    @FXML
    void btnClientes(ActionEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministradorClientes.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void btnFuncionarios(ActionEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministradorFuncionarios.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void voltar(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaFuncionario.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

}
