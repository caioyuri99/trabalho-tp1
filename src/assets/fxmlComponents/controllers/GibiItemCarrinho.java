package assets.fxmlComponents.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import biblioteca.Gibi;
import biblioteca.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import session.Session;

public class GibiItemCarrinho implements Initializable {

    private Gibi gibi;
    private Text txtMulta;

    @FXML
    private AnchorPane container;

    @FXML
    private ImageView imgCapa;

    @FXML
    private Label lblAutor;

    @FXML
    private Label lblCondicao;

    @FXML
    private Label lblEditora;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblTipo;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imgCapa.setImage(new Image(gibi.getObra().getCapaUrl()));
        lblAutor.setText(gibi.getObra().getAutor());
        lblCondicao.setText(gibi.getCondicao());
        lblEditora.setText(gibi.getEditora());
        lblNome.setText(gibi.getObra().getNome());
        lblTipo.setText(gibi.getTipo());
    }

    @FXML
    void removerItem(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remover item");
        alert.setHeaderText("Remover item do carrinho");
        alert.setContentText("Deseja remover o item " + gibi.getObra().getNome() + " do carrinho?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            cliente.getCarrinho().remove(gibi);
            VBox parent = (VBox) container.getParent();
            parent.getChildren().remove(container);

            if (cliente.getCarrinho().size() == 0) {
                Label lblPlaceHoder = (Label) parent.getParent().lookup("#lblPlaceHolder");
                lblPlaceHoder.setManaged(true);
                lblPlaceHoder.setVisible(true);
            }

            double valor = cliente.getCarrinho().size() * 0.8;
            txtMulta.setText(String.format("R$ %.2f", valor));
        }
    }

    public void setItem(Item item) {
        this.gibi = (Gibi) item;
    }

    public void setTxtMulta(Text txtMulta) {
        this.txtMulta = txtMulta;
    }

}
