package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Estante;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GerenciarEstantes implements Initializable {

    @FXML
    private ListView<Estante> listEstantes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        listEstantes.setCellFactory(list -> {
            ListCell<Estante> cell = new TextFieldListCell<>();
            cell.getStyleClass().add("hover-effect");
            return cell;
        });

        ArrayList<Estante> estantes = Estante.getListaEstantes();
        listEstantes.setItems(FXCollections.observableArrayList(estantes));
    }

    @FXML
    void addEstante(ActionEvent event) throws IOException {
        Stage addEstante = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/AdicionarEstante.fxml"));
        addEstante.setTitle("Adicionar Estante");
        addEstante.setScene(new Scene(content));
        addEstante.initModality(Modality.APPLICATION_MODAL);
        addEstante.initOwner(((Node) event.getSource()).getScene().getWindow());
        addEstante.showAndWait();
    }

    @FXML
    void deleteEstante(ActionEvent event) {
        Estante estante = listEstantes.getSelectionModel().getSelectedItem();

        if (estante == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhuma estante selecionada");
            alert.setContentText("Por favor, selecione uma estante para deletar");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Deseja deletar a estante?");
        alert.setContentText("Categoria: " + estante.getCategoria());

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            try {
                estante.deletarEstante();
                listEstantes.getItems().remove(estante);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao deletar estante");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Estante deletada com sucesso");
            alertSucesso.setContentText("Categoria: " + estante.getCategoria());
            alertSucesso.showAndWait();
        }
    }

    @FXML
    void editEstante(ActionEvent event) throws IOException {
        Stage editEstante = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/EditarEstante.fxml"));
        editEstante.setTitle("Editar Estante");
        editEstante.setScene(new Scene(content));
        editEstante.initModality(Modality.APPLICATION_MODAL);
        editEstante.initOwner(((Node) event.getSource()).getScene().getWindow());
        editEstante.showAndWait();
    }

}
