package controllers;

import java.io.IOException;

import biblioteca.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.Session;

public class TelaInicial {

    private Parent root;
    private Stage stage;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCPF;

    @FXML
    void login(ActionEvent event) throws IOException {
        // TODO: implementar uma máscara para validação do CPF

        String cpf = txtCPF.getText();
        String senha = passSenha.getText();

        Cliente cliente = new Cliente();

        boolean res = cliente.fazerLogin(cpf, senha);

        if (!res) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("CPF ou senha incorretos");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        Session.loggedClienteCPF = cpf;

        this.root = FXMLLoader.load(getClass().getResource("../telas/CatalogoCliente.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

}
