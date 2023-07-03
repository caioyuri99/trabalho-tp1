package controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaInicial implements Initializable {

    private Parent root;
    private Stage stage;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCPF;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        txtCPF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null || newValue.isEmpty()) {
                    return;
                }

                if (newValue.length() > 14) {
                    txtCPF.setText(oldValue);
                    return;
                }

                // Remove caracteres não numéricos
                String cpf = newValue.replaceAll("[^\\d]", "");

                if (newValue.length() < oldValue.length()) {
                    txtCPF.setText(newValue);
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

                txtCPF.setText(formattedCPF.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        formatarCPF();

        String cpf = txtCPF.getText().replaceAll("[^\\d]", "");

        if (cpf.length() < 11) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar logar");
            alert.setContentText("CPF inválido");
            alert.showAndWait();

            return;
        }

        String senha = passSenha.getText();

        Cliente cliente = new Cliente();

        try {
            cliente.login(cpf, senha);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao tentar logar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return;
        }

        mostrarCatalogo(event);
    }

    @FXML
    void loginAreaFuncionario(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/LoginFuncionario.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void mostrarCatalogo(ActionEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/Catalogo.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void cadastrar(ActionEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/CadastroCliente.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void alterarData(MouseEvent event) throws IOException {
        Stage alterarData = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/AlterarData.fxml"));
        alterarData.setScene(new Scene(content));
        alterarData.initModality(Modality.APPLICATION_MODAL);
        alterarData.initOwner(((Node) event.getSource()).getScene().getWindow());
        alterarData.showAndWait();
    }

    public void telaCarregamento() {
        // TODO: implementar uma tela de carregamento
    }

    private void formatarCPF() {
        String cpf = txtCPF.getText().replaceAll("[^\\d]", "");

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

        txtCPF.setText(formattedCPF.toString());
    }

}
