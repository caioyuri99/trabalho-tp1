package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Obra;
import biblioteca.Revista;
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

public class AtualizarRevista implements Initializable {

    private Obra obra;
    private Revista revista;

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtEditora;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");

        cbbCondicao.setValue(
                revista.getCondicao().substring(0, 1).toUpperCase() + revista.getCondicao().substring(1).toLowerCase());

        txtCategoria.setText(revista.getCategoria());
        txtEdicao.setText(String.valueOf(revista.getEdicao()));
        txtNumero.setText(String.valueOf(revista.getNumero()));
        txtEditora.setText(revista.getEditora());
    }

    @FXML
    void updateRevista(ActionEvent event) {
        String condicao = cbbCondicao.getValue();
        String categoria = txtCategoria.getText();
        int edicao;
        try {
            edicao = Integer.parseInt(txtEdicao.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar revista");
            alert.setContentText("Edição inválida");
            alert.showAndWait();
            return;
        }
        int numero;
        try {
            numero = Integer.parseInt(txtNumero.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar revista");
            alert.setContentText("Número inválido");
            alert.showAndWait();
            return;
        }
        String editora = txtEditora.getText();

        if (editora.isEmpty() || categoria.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao atualizar revista");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Atualizar revista");
        alert.setContentText("Deseja realmente atualizar a revista?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            try {
                revista.setCondicao(condicao);
                revista.setCategoria(categoria);
                revista.setEdicao(edicao);
                revista.setNumero(numero);
                revista.setEditora(editora);

                obra.atualizarItem(revista);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao atualizar revista");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Revista atualizada");
            alertSucesso.setContentText("A revista foi atualizada com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }

}
