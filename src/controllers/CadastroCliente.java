package controllers;

import java.io.IOException;
import java.time.LocalDate;

import biblioteca.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CadastroCliente {

    private Parent root;
    private Stage stage;

    @FXML
    private DatePicker dataNasc;

    @FXML
    private PasswordField passConfSenha;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtNome;

    @FXML
    void register(ActionEvent event) throws IOException {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String senha = passSenha.getText();
        String confSenha = passConfSenha.getText();
        LocalDate dataNascimento = dataNasc.getValue();

        if (!senha.equals(confSenha)) {
            // TODO: exibir essa informação graficamente e não como alerta

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Senhas não coincidem");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confSenha.isEmpty() || dataNascimento == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Preencha todos os campos");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        Cliente cliente = new Cliente(cpf, senha, nome, dataNascimento);

        boolean res = cliente.fazerCadastro();

        if (!res) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("CPF já cadastrado");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Cadastro realizado com sucesso");
        alert.setContentText("Você já pode fazer login.");
        alert.showAndWait();

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void toHomeScreen(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

}
