package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.Session;

public class DadosClientePopup implements Initializable {

    @FXML
    private TextField nomeCliente;

    @FXML
    private PasswordField novaSenhaCliente;

    @FXML
    private PasswordField senhaCliente;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String nomeCliente = Session.getLoggedUser().getNome();
        this.nomeCliente.setText(nomeCliente);
    }

    @FXML
    void alterarDados(ActionEvent event) {
        String nome = nomeCliente.getText();
        String senha = senhaCliente.getText();
        String novaSenha = novaSenhaCliente.getText();

        if (nome.isEmpty() && novaSenha.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao alterar dados");
            alert.setContentText("Nenhum campo foi preenchido");
            alert.showAndWait();
            return;
        }

        if (!senha.equals(Session.getLoggedUser().getSenha())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao alterar dados");
            alert.setContentText("Senha incorreta");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de alteração de dados");
        alert.setContentText("Deseja realmente alterar seus dados?");

        ButtonType buttonConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonConfirmar, buttonCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();

            if (!nome.isEmpty())
                cliente.setNome(nome);
            if (!novaSenha.isEmpty())
                cliente.setSenha(novaSenha);

            try {
                cliente.atualizarCadastro();
                Session.login(cliente);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao alterar dados");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Dados alterados com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
