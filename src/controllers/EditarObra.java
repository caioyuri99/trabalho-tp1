package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Estante;
import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditarObra implements Initializable {

    public Obra obra;

    @FXML
    private ComboBox<Estante> cbbEstante;

    @FXML
    private ComboBox<String> cbbTipo;

    @FXML
    private DatePicker dateDataPubli;

    @FXML
    private ImageView imgPreviewCapa;

    @FXML
    private Label lblErrorUrl;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtGenero;

    @FXML
    private TextField txtNome;

    @FXML
    private TextArea txtSinopse;

    @FXML
    private TextField txtUrlCapa;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cbbEstante.getItems().addAll(Estante.getListaEstantes());
        cbbTipo.getItems().addAll("Livro", "Revista", "Gibi");

        cbbEstante.setValue(obra.getEstante());
        cbbTipo.setValue(obra.getTipo().substring(0, 1).toUpperCase() + obra.getTipo().substring(1).toLowerCase());

        dateDataPubli.setValue(obra.getDataPublicacao());
        txtAutor.setText(obra.getAutor());
        txtGenero.setText(obra.getGenero());
        txtNome.setText(obra.getNome());
        txtSinopse.setText(obra.getSinopse());

        txtUrlCapa.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Image image = new Image(newValue);
                imgPreviewCapa.setImage(image);
                lblErrorUrl.setVisible(false);
            } catch (Exception e) {
                lblErrorUrl.setVisible(true);
            }
        });

        txtUrlCapa.setText(obra.getCapaUrl());
    }

    @FXML
    void confirmChanges(ActionEvent event) {
        Estante estante = cbbEstante.getValue();
        String tipo = cbbTipo.getValue();
        LocalDate dataPubli = dateDataPubli.getValue();
        String autor = txtAutor.getText();
        String genero = txtGenero.getText();
        String nome = txtNome.getText();
        String sinopse = txtSinopse.getText();
        String urlCapa = txtUrlCapa.getText();

        if (dataPubli == null || autor.isEmpty() || genero.isEmpty() || nome.isEmpty() || sinopse.isEmpty()
                || urlCapa.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao editar obra");
            alert.setContentText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        if (lblErrorUrl.visibleProperty().get()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao editar obra");
            alert.setContentText("URL da capa inválida");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmar edição");
        alert.setContentText("Tem certeza que deseja editar esta obra?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Obra obra = new Obra(nome, tipo, dataPubli, autor, genero, sinopse, urlCapa);

            try {
                estante.atualizarObra(obra);
            } catch (Exception e) {
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao editar obra");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
                return;
            }

            Alert alertSuccess = new Alert(AlertType.INFORMATION);
            alertSuccess.setTitle("Sucesso");
            alertSuccess.setHeaderText("Obra editada com sucesso");
            alertSuccess.setContentText("A obra foi editada com sucesso");
            alertSuccess.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
