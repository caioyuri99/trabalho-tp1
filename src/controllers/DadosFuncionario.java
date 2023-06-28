package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import biblioteca.Funcionario;
import biblioteca.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class DadosFuncionario implements Initializable {

    private Parent root;
    private Stage stage;

    @FXML
    private Label lblAdmin;

    @FXML
    private Label lblCargo;

    @FXML
    private Label lblCpf;

    @FXML
    private Label lblDataNasc;

    @FXML
    private Label lblNome;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Funcionario funcionario = (Funcionario) Session.getLoggedUser();

        lblAdmin.setText(funcionario.isAdmin() ? "Sim" : "Não");
        lblCargo.setText(funcionario.getCargo());
        lblCpf.setText(Usuario.formatCPF(funcionario.getCpf()));
        lblDataNasc.setText(funcionario.getDataNasc().format(Usuario.DATE_PATTERN));
        lblNome.setText(funcionario.getNome());
    }

    @FXML
    void alterarSenha(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("./../telas/UsuarioAlterarSenha.fxml"));
        stage.setTitle("Alterar senha");
        stage.setScene(new Scene(content));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();
    }

    @FXML
    void exit(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../telas/TelaFuncionario.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Gestão de Estoque");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
