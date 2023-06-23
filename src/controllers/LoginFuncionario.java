package controllers;

import java.io.IOException;

import biblioteca.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import session.Session;

public class LoginFuncionario {

    private Parent root;
    private Stage stage;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCpf;

    @FXML
    void goToHomeScreen(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        // TODO: implementar validação do CPF

        String cpf = txtCpf.getText();
        String senha = passSenha.getText();

        Funcionario funcionario = new Funcionario();

        boolean res = funcionario.fazerLogin(cpf, senha);

        if (!res) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("CPF ou senha incorretos");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        Session.loggedCPF = cpf;

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaFuncionario.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

}
