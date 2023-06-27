package assets.fxmlComponents.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import biblioteca.Emprestimo;
import biblioteca.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import session.Session;

public class ItemPedidos implements Initializable {

    private Emprestimo emprestimo;
    private Item item;
    private Text txtSaldoDevedor;

    @FXML
    private AnchorPane container;

    @FXML
    private ImageView imgCapa;

    @FXML
    private Label lblAtrasado;

    @FXML
    private Label lblAutor;

    @FXML
    private Label lblDataDevolucao;

    @FXML
    private Label lblDataEmprestimo;

    @FXML
    private Label lblMultado;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblValorMulta;

    @FXML
    private Label lblQtdRenovações;

    @FXML
    private Button btnDevolver;

    @FXML
    private Button btnPagar;

    @FXML
    private Button btnRenovar;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imgCapa.setImage(new Image(item.getObra().getCapaUrl()));
        lblNome.setText(item.getObra().getNome());
        lblAutor.setText(item.getObra().getAutor());
        lblTipo.setText(emprestimo.getTipoItem());
        lblQtdRenovações.setText(String.valueOf(emprestimo.getQtdRenovacoes()));
        lblDataEmprestimo.setText(emprestimo.getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblDataDevolucao.setText(emprestimo.getDataDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        lblAtrasado.setText(emprestimo.isAtrasado() ? "Sim" : "Não");
        if (emprestimo.isAtrasado()) {
            lblAtrasado.setStyle("-fx-text-fill: red");
            btnRenovar.setDisable(true);
            lblMultado.setVisible(true);
            lblMultado.setText(emprestimo.isMultado() ? "Sim" : "Não");

            if (emprestimo.isMultado()) {
                lblMultado.setStyle("-fx-text-fill: red");
                btnDevolver.setDisable(true);
                lblValorMulta.setVisible(true);
                lblValorMulta.setText(String.valueOf(emprestimo.getValorMulta()));
                btnPagar.setVisible(true);
            }
        }
    }

    @FXML
    void devolver(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de devolução");
        alert.setContentText("Deseja realmente devolver o item?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            try {
                cliente.fazerDevolucao(emprestimo);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao devolver item");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();

                return;
            }
            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Item devolvido com sucesso");
            alertSucesso.setContentText("O item foi devolvido com sucesso!");
            alertSucesso.showAndWait();

            VBox parent = (VBox) container.getParent();
            parent.getChildren().remove(container);
            if (cliente.getEmprestimosAtivos().size() == 0) {
                Label lblPlaceHoder = (Label) parent.lookup("#lblPlaceHoder");
                lblPlaceHoder.setManaged(true);
                lblPlaceHoder.setVisible(true);
            }
        }
    }

    @FXML
    void renovar(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de renovação");
        alert.setContentText("Deseja realmente renovar o item?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            try {
                cliente.fazerRenovacao(emprestimo);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao renovar item");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();

                return;
            }
            lblQtdRenovações.setText(String.valueOf(emprestimo.getQtdRenovacoes()));
            lblDataDevolucao.setText(emprestimo.getDataDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Renovação realizada com sucesso");
            alertSucesso.setContentText("A data de devolução foi alterada para "
                    + emprestimo.getDataDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            alertSucesso.showAndWait();
        }
    }

    @FXML
    void pagar(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de pagamento");
        alert.setContentText("Deseja realmente pagar a multa?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            try {
                cliente.pagarMulta(emprestimo);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao pagar multa");
                alertErro.setContentText(e.getMessage());
                alertErro.showAndWait();

                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Multa paga com sucesso");
            alertSucesso.setContentText("A multa foi paga com sucesso");
            alertSucesso.showAndWait();

            VBox parent = (VBox) container.getParent();
            parent.getChildren().remove(container);
            if (cliente.getEmprestimosAtivos().size() == 0) {
                Label lblPlaceHoder = (Label) parent.lookup("#lblPlaceHoder");
                lblPlaceHoder.setManaged(true);
                lblPlaceHoder.setVisible(true);
            }

            txtSaldoDevedor.setText(String.valueOf(cliente.getSaldoDevedor()));
        }
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
