package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Estante;
import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CadastroObra implements Initializable {

    @FXML
    private ComboBox<Estante> cbbEstante;

    @FXML
    private DatePicker dateDataPubli;

    @FXML
    private ImageView imgPreviewCapa;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtNome;

    @FXML
    private TextArea txtSinopse;

    @FXML
    private TextField txtUrlCapa;

    @FXML
    private Label lblErrorUrl;

    @FXML
    private ComboBox<String> cbbTipo;

    @FXML
    private TextField txtGenero;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<Estante> estantes = Estante.getListaEstantes();
        cbbEstante.getItems().addAll(estantes);

        cbbTipo.getItems().addAll("Livro", "Revista", "Gibi");

        txtUrlCapa.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Image image = new Image(newValue);
                imgPreviewCapa.setImage(image);
                lblErrorUrl.setVisible(false);
            } catch (Exception e) {
                lblErrorUrl.setVisible(true);
            }
        });
    }

    @FXML
    void addObra(ActionEvent event) {
        String nome = txtNome.getText();
        String tipo = cbbTipo.getValue();
        LocalDate dataPubli = dateDataPubli.getValue();
        String autor = txtAutor.getText();
        String genero = txtGenero.getText();
        String sinopse = txtSinopse.getText();
        String urlCapa = txtUrlCapa.getText();
        Estante estante = cbbEstante.getValue();

        if (nome.isEmpty() || tipo == null || dataPubli == null || autor.isEmpty() || genero.isEmpty()
                || sinopse.isEmpty() || urlCapa.isEmpty() || estante == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar obra");
            alert.setContentText("Preencha todos os campos");

            alert.showAndWait();
            return;
        }

        if (lblErrorUrl.visibleProperty().get()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao cadastrar obra");
            alert.setContentText("URL da capa inválida");

            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de cadastro");
        alert.setContentText("Deseja realmente cadastrar a obra?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Obra obra = new Obra(nome, tipo, dataPubli, autor, genero, sinopse, urlCapa);

            try {
                estante.registrarObra(obra);
            } catch (Exception e) {
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao cadastrar obra");
                alertErro.setContentText(e.getMessage());

                alertErro.showAndWait();
                return;
            }

            Alert alertSucesso = new Alert(Alert.AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Obra cadastrada com sucesso");
            alertSucesso.setContentText("A obra foi cadastrada com sucesso");

            alertSucesso.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
