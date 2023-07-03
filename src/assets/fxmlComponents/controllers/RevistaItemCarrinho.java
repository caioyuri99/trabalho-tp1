package assets.fxmlComponents.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Revista;
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

public class RevistaItemCarrinho implements Initializable {

    private Revista revista;
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
    private Label lblEdicao;

    @FXML
    private Label lblEditora;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblNumero;

    @FXML
    private Label lblCategoria;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imgCapa.setImage(new Image(revista.getObra().getCapaUrl()));
        lblAutor.setText(revista.getObra().getAutor());
        lblCondicao.setText(revista.getCondicao());
        lblEdicao.setText(String.valueOf(revista.getEdicao()));
        lblEditora.setText(revista.getEditora());
        lblNome.setText(revista.getObra().getNome());
        lblNumero.setText(String.valueOf(revista.getNumero()));
        lblCategoria.setText(revista.getCategoria());
    }

    @FXML
    void removerItem(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remover item");
        alert.setHeaderText("Remover item do carrinho");
        alert.setContentText("Deseja remover o item do carrinho?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            cliente.getCarrinho().remove(revista);
            VBox parent = (VBox) container.getParent();
            parent.getChildren().remove(container);

            if (cliente.getCarrinho().size() == 0) {
                Label lblPlaceHoder = (Label) parent.getParent().lookup("#lblPlaceHoder");
                lblPlaceHoder.setManaged(true);
                lblPlaceHoder.setVisible(true);
            }

            double valor = cliente.getCarrinho().size() * 0.8;
            txtMulta.setText(String.format("R$ %.2f", valor));
        }
    }

    public void setItem(Item item) {
        this.revista = (Revista) item;
    }

    public void setTxtMulta(Text txtMulta) {
        this.txtMulta = txtMulta;
    }

}
