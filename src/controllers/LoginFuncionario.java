package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import biblioteca.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginFuncionario implements Initializable {

    private Parent root;
    private Stage stage;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCpf;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        txtCpf.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null || newValue.isEmpty()) {
                    return;
                }

                if (newValue.length() > 14) {
                    txtCpf.setText(oldValue);
                    return;
                }

                // Remove caracteres não numéricos
                String cpf = newValue.replaceAll("[^\\d]", "");

                if (newValue.length() < oldValue.length()) {
                    txtCpf.setText(newValue);
                    return;
                }

                // Formata o CPF com pontos e traço
                StringBuilder formattedCPF = new StringBuilder();
                for (int i = 0; i < cpf.length(); i++) {
                    formattedCPF.append(cpf.charAt(i));
                    if ((i + 1) % 3 == 0 && i < 8) {
                        formattedCPF.append(".");
                    } else if ((i + 1) % 3 == 0 && i < 11) {
                        formattedCPF.append("-");
                    }
                }

                txtCpf.setText(formattedCPF.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @FXML
    void goToHomeScreen(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        formatarCPF();

        String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
        String senha = passSenha.getText();

        if (cpf.isEmpty() || senha.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar logar");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();

            return;
        }

        if (cpf.length() < 11) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar logar");
            alert.setContentText("CPF inválido");
            alert.showAndWait();

            return;
        }

        Funcionario funcionario = new Funcionario();

        try {
            funcionario.login(cpf, senha);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar logar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return;
        }

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaFuncionario.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.centerOnScreen();
        this.stage.show();
    }

    private void formatarCPF() {
        String cpf = txtCpf.getText().replaceAll("[^\\d]", "");

        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        StringBuilder formattedCPF = new StringBuilder();
        for (int i = 0; i < cpf.length(); i++) {
            formattedCPF.append(cpf.charAt(i));
            if ((i + 1) % 3 == 0 && i < 8) {
                formattedCPF.append(".");
            } else if ((i + 1) % 3 == 0 && i < 11) {
                formattedCPF.append("-");
            }
        }

        txtCpf.setText(formattedCPF.toString());
    }

}
