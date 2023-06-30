package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Livro;
import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AtualizarLivro implements Initializable {

    private Obra obra;
    private Livro livro;

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private ComboBox<String> cbbTipoCapa;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtEditora;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");
        cbbTipoCapa.getItems().addAll("Dura", "Brochura", "Espiral", "Flexível", "Canoa");

        cbbCondicao.setValue(
                livro.getCondicao().substring(0, 1).toUpperCase() +
                        livro.getCondicao().substring(1).toLowerCase());

        cbbTipoCapa.setValue(
                livro.getTipoCapa().substring(0, 1).toUpperCase() + livro.getTipoCapa().substring(1).toLowerCase());

        txtEdicao.setText(String.valueOf(livro.getEdicao()));
        txtEditora.setText(livro.getEditora());
    }

    @FXML
    void updateLivro(ActionEvent event) {
        String condicao = cbbCondicao.getValue();
        String tipoCapa = cbbTipoCapa.getValue();
        int edicao;
        try {
            edicao = Integer.parseInt(txtEdicao.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar livro");
            alert.setContentText("Edição inválida");
            alert.showAndWait();
            return;
        }
        String editora = txtEditora.getText();

        System.out.println(condicao);
        System.out.println(tipoCapa);
        System.out.println(edicao);
        System.out.println(editora);

        if (editora.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar livro");
            alert.setContentText("Editora inválida");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Atualizar livro");
        alert.setContentText("Deseja realmente atualizar o livro?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            livro.setCondicao(condicao);
            livro.setTipoCapa(tipoCapa);
            livro.setEdicao(edicao);
            livro.setEditora(editora);

            try {
                obra.atualizarItem(livro);
            } catch (Exception e) {
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao atualizar livro");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Livro atualizado");
            alertSucesso.setContentText("Livro atualizado com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

}
