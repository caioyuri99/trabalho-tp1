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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaInicial {

    private Parent root;
    private Stage stage;

    @FXML
    private PasswordField passSenha;

    @FXML
    private TextField txtCPF;

    @FXML
    void login(ActionEvent event) throws Exception {
        // TODO: implementar validação do CPF

        String cpf = txtCPF.getText();
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

}
