package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import biblioteca.Cliente;
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

public class DadosCliente implements Initializable {

    private Parent root;
    private Stage stage;

    @FXML
    private Label lblCpf;

    @FXML
    private Label lblDataNasc;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblSaldoDevedor;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Cliente cliente = (Cliente) Session.getLoggedUser();

        try {
            Session.verificaEmprestimos();

        } catch (Exception e) {
            e.printStackTrace();
        }

        lblCpf.setText(Usuario.formatCPF(cliente.getCpf()));
        lblDataNasc.setText(cliente.getDataNasc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblNome.setText(cliente.getNome());
        lblSaldoDevedor.setText(String.format("R$ %.2f", cliente.getSaldoDevedor()));
    }

    @FXML
    void alterarSenha(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../telas/UsuarioAlterarSenha.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();

        initialize(null, null);
    }

    @FXML
    void exit(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../telas/Catalogo.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Catálogo");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void verHistoricoPedidos(ActionEvent event) throws IOException {
        try {
            Session.verificaEmprestimos();
        } catch (Exception e) {
            e.printStackTrace();
        }

        root = FXMLLoader.load(getClass().getResource("../telas/HistoricoEmprestimos.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Histórico de Empréstimos");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
