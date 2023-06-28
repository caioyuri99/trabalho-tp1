package controllers;

import java.util.Optional;

import biblioteca.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import session.Session;

public class UsuarioAlterarSenha {

    @FXML
    private PasswordField novaSenhaUser;

    @FXML
    private PasswordField senhaUser;

    @FXML
    void alterarDados(ActionEvent event) {
        String senha = senhaUser.getText();
        String novaSenha = novaSenhaUser.getText();

        if (novaSenha.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao alterar dados");
            alert.setContentText("Nova senha não pode ser vazia");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de alteração de senha");
        alert.setContentText("Deseja realmente alterar sua senha?");

        ButtonType buttonConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonConfirmar, buttonCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonConfirmar) {
            Usuario usuario = Session.getLoggedUser();

            try {
                usuario.alterarSenha(senha, novaSenha);
                Session.login(usuario);
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
            alertSucesso.setHeaderText("Senha alterada com sucesso");
            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
