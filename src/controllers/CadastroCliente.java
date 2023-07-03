package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CadastroCliente implements Initializable {

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
    void register(ActionEvent event) throws IOException {
        formatarCPF();

        String nome = txtNome.getText();
        String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
        String senha = passSenha.getText();
        String confSenha = passConfSenha.getText();
        LocalDate dataNascimento = dataNasc.getValue();

        if (cpf.length() != 11) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("CPF inválido");
            alert.setContentText("Verifique os dados e tente novamente.");
            alert.showAndWait();

            return;
        }

        if (!senha.equals(confSenha)) {
            // TODO: Detalhe: exibir essa informação graficamente e não como alerta

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

        try {
            cliente.fazerCadastro();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar cliente");
            alert.setContentText(e.getMessage());
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
