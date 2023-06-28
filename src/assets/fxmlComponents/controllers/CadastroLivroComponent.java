package assets.fxmlComponents.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.interfaces.AddItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CadastroLivroComponent implements Initializable, AddItem {

    @FXML
    private ComboBox<String> cbbCondicao;

    @FXML
    private ComboBox<String> cbbTipoCapa;

    @FXML
    private TextField txtEdicao;

    @FXML
    private TextField txtEditora;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbCondicao.getItems().addAll("Novo", "Semi-novo", "Usado", "Restaurado");
        cbbTipoCapa.getItems().addAll("Dura", "Brochura", "Espiral", "Flexível", "Canoa");
    }

    public String getCondicao() {
        return cbbCondicao.getValue();
    }

    public String getTipoCapa() {
        return cbbTipoCapa.getValue();
    }

    public String getEdicao() {
        return txtEdicao.getText();
    }

    public String getEditora() {
        return txtEditora.getText();
    }

}
