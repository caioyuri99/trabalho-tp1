package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Estante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class EditarEstante implements Initializable {

    public Estante estante;

    @FXML
    private TextField txtCategoria;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        txtCategoria.setText(estante.getCategoria());
    }

    @FXML
    void editar(ActionEvent event) {
        String categoria = txtCategoria.getText();

        if (categoria.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Campo vazio");
            alert.setContentText("Preencha o campo categoria");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja editar a estante?");
        alert.setContentText("Categoria: " + categoria);

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            estante.setCategoria(categoria);
            
            try {
                estante.editarEstante();
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao editar estante");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Estante editada com sucesso");
            alertSucesso.setContentText("Categoria: " + categoria);
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setEstante(Estante estante) {
        this.estante = estante;
    }

}
