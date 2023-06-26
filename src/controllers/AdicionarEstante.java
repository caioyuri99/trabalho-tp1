package controllers;

import java.util.Optional;

import biblioteca.Estante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class AdicionarEstante {

    @FXML
    private TextField txtCategoria;

    @FXML
    void adicionar(ActionEvent event) {
        String categoria = txtCategoria.getText();

        if (categoria.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao adicionar estante");
            alert.setContentText("Preencha todos os campos!");

            alert.showAndWait();
            return;
        }

        if (Estante.estanteExiste(categoria)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao adicionar estante");
            alert.setContentText("Estante já existe!");

            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de adição de estante");
        alert.setContentText("Deseja adicionar a estante " + categoria + "?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Estante estante = new Estante(categoria);

            try {
                estante.adicionarEstante();
            } catch (Exception e) {
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao adicionar estante");
                alertErro.setContentText(e.getMessage());

                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(Alert.AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Sucesso ao adicionar estante");
            alertSucesso.setContentText("Estante adicionada com sucesso!");

            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
