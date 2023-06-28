package assets.fxmlComponents.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.interfaces.AddItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CadastroRevistaComponent implements Initializable, AddItem {

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtEditora;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");
    }

    public String getCondicao() {
        return cbbCondicao.getValue();
    }

    public String getCategoria() {
        return txtCategoria.getText();
    }

    public String getEdicao() {
        return txtEdicao.getText();
    }

    public String getEditora() {
        return txtEditora.getText();
    }

}
