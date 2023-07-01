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

public class CadastroGibi implements Initializable {

    private Obra obra;

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtEditora;

    @FXML
    private TextField txtTipo;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");
    }

    @FXML
    void addGibi(ActionEvent event) {
        String condicao = cbbCondicao.getValue();
        int edicao;
        try {
            edicao = Integer.parseInt(txtEdicao.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar gibi");
            alert.setContentText("Edição inválida");
            alert.showAndWait();
            return;
        }
        String editora = txtEditora.getText();
        String tipo = txtTipo.getText();

        if (condicao == null || editora.isEmpty() || tipo.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar gibi");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação ao cadastrar gibi");
        alert.setContentText("Deseja realmente cadastrar o gibi?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Gibi gibi = new Gibi(editora, edicao, condicao, obra, tipo);

            try {
                obra.adicionarItem(gibi);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao cadastrar gibi");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Sucesso ao cadastrar gibi");
            alertSucesso.setContentText("Gibi cadastrado com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
