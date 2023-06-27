package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class DadosCliente implements Initializable {

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

        lblCpf.setText(formatCPF(cliente.getCpf()));
        lblDataNasc.setText(cliente.getDataNasc().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblNome.setText(cliente.getNome());
        lblSaldoDevedor.setText(String.format("R$ %.2f", cliente.getSaldoDevedor()));
    }

    @FXML
    void alterarDados(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../telas/DadosClientePopup.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.showAndWait();

        initialize(null, null);
    }

    public static String formatCPF(String cpf) {
        // Remove caracteres não numéricos do CPF
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }

        // Formata o CPF adicionando os pontos e o traço
        cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);

        return cpf;
    }
}
