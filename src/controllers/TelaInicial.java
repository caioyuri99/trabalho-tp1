package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import javafx.concurrent.Task;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class TelaInicial implements Initializable {

    private Parent root;
    private Stage stage;

    private static final double BLUR_AMOUNT = 10.0;

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
        this.stage.centerOnScreen();
        this.stage.show();
    }

    @FXML
    void mostrarCatalogo(ActionEvent event) throws IOException {
        Stage tc = telaCarregamento();

        Task<Scene> task = new Task<Scene>() {
            @Override
            protected Scene call() throws Exception {
                return new Scene(FXMLLoader.load(getClass().getResource("../telas/Catalogo.fxml")));
            }
        };

        task.setOnSucceeded(e -> {
            this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            this.stage.setScene(task.getValue());
            this.stage.show();
            tc.close();
        });

        Thread th = new Thread(task);
        th.start();
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

    private Stage telaCarregamento() throws IOException {
        Stage telaCarregamento = new Stage();

        Window ownerStage = passSenha.getScene().getWindow();

        telaCarregamento.initOwner(ownerStage);
        telaCarregamento.initModality(Modality.APPLICATION_MODAL);
        telaCarregamento.initStyle(StageStyle.UNDECORATED);

        Parent content = FXMLLoader.load(getClass().getResource("../assets/fxmlComponents/TelaCarregamento.fxml"));

        passSenha.getScene().getRoot().setEffect(new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3));

        telaCarregamento.setScene(new Scene(content));

        double x = ownerStage.getX() + ownerStage.getWidth() / 2 - content.prefWidth(-1) / 2;
        double y = ownerStage.getY() + ownerStage.getHeight() / 2 - content.prefHeight(-1) / 2;

        telaCarregamento.setX(x);
        telaCarregamento.setY(y);

        telaCarregamento.show();

        return telaCarregamento;
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
