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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class CadastroLivro implements Initializable {

    private Obra obra;

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
    }

    @FXML
    void addLivro(ActionEvent event) {
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

        if (condicao == null || tipoCapa == null || editora.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar livro");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação ao cadastrar livro");
        alert.setContentText("Deseja mesmo cadastrar o livro?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Livro livro = new Livro(editora, edicao, condicao, tipoCapa, obra);

            try {
                obra.adicionarItem(livro);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao cadastrar livro");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Livro cadastrado com sucesso");
            alertSucesso.setContentText("O livro foi cadastrado com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
