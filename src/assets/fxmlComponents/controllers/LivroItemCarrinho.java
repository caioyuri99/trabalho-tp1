package assets.fxmlComponents.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Livro;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import session.Session;

public class LivroItemCarrinho implements Initializable {

    private Livro livro;
    private Text txtMulta;

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
    private Label lblTipoCapa;

    @FXML
    private AnchorPane container;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imgCapa.setImage(new Image(livro.getObra().getCapaUrl()));
        lblAutor.setText(livro.getObra().getAutor());
        lblCondicao.setText(livro.getCondicao());
        lblEditora.setText(livro.getEditora());
        lblNome.setText(livro.getObra().getNome());
        lblTipoCapa.setText(livro.getTipoCapa());
    }

    @FXML
    void removerItem(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remover item");
        alert.setHeaderText("Remover item do carrinho");
        alert.setContentText("Tem certeza que deseja remover este item do carrinho?");

        ButtonType confirmButton = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == confirmButton) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            cliente.getCarrinho().remove(livro);
            VBox parent = (VBox) container.getParent();
            parent.getChildren().remove(container);

            if (cliente.getCarrinho().size() == 0) {
                Label lblPlaceHolder = (Label) parent.lookup("#lblPlaceHoder");
                lblPlaceHolder.setManaged(true);
                lblPlaceHolder.setVisible(true);
            }

            double valor = cliente.getCarrinho().size() * 0.8;
            txtMulta.setText(String.format("R$ %.2f", valor));
        }
    }

    public void setItem(Item item) {
        this.livro = (Livro) item;
    }

    public void setTxtMulta(Text txtMulta) {
        this.txtMulta = txtMulta;
    }

}
