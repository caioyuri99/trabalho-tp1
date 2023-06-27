package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Gibi;
import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class AtualizarGibi implements Initializable {

    private Obra obra;
    private Gibi gibi;

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtEditora;

    @FXML
    private TextField txtTipo;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");

        cbbCondicao.setValue(
                gibi.getCondicao().substring(0, 1).toUpperCase() + gibi.getCondicao().substring(1).toLowerCase());

        txtCategoria.setText(gibi.getCategoria());
        txtEdicao.setText(String.valueOf(gibi.getEdicao()));
        txtEditora.setText(gibi.getEditora());
        txtTipo.setText(gibi.getTipo());
    }

    @FXML
    void updateGibi(ActionEvent event) {
        String condicao = cbbCondicao.getValue();
        String categoria = txtCategoria.getText();
        int edicao;
        try {
            edicao = Integer.parseInt(txtEdicao.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar gibi");
            alert.setContentText("Edição inválida");
            alert.showAndWait();
            return;
        }
        String editora = txtEditora.getText();
        String tipo = txtTipo.getText();

        if (categoria.isEmpty() || editora.isEmpty() || tipo.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar gibi");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Atualizar gibi");
        alert.setContentText("Deseja realmente atualizar o gibi?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            gibi = new Gibi(editora, edicao, condicao, tipo, categoria);

            try {
                obra.atualizarItem(gibi);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao atualizar gibi");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Gibi atualizado");
            alertSucesso.setContentText("O gibi foi atualizado com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public void setGibi(Gibi gibi) {
        this.gibi = gibi;
    }

}
