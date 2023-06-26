package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.Item;
import biblioteca.Obra;
import controllers.cellFactoryFormat.ItemDisponivelFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GerenciarObra implements Initializable {

    private Obra obra;

    @FXML
    private Button btnAdd;

    @FXML
    private ImageView imgCapa;

    @FXML
    private Label lblAutor;

    @FXML
    private Label lblDataPubli;

    @FXML
    private Label lblEstante;

    @FXML
    private Label lblGenero;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblSinopse;

    @FXML
    private Label lblTipo;

    @FXML
    private TableView<Item> tableItems;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        lblNome.setText(obra.getNome());
        lblAutor.setText(obra.getAutor());
        lblGenero.setText(obra.getGenero());
        lblTipo.setText(obra.getTipo().substring(0, 1).toUpperCase() + obra.getTipo().substring(1).toLowerCase());
        lblEstante.setText(obra.getEstante().getCategoria());
        lblSinopse.setText(obra.getSinopse());
        imgCapa.setImage(new Image(obra.getCapaUrl()));
        lblDataPubli.setText(obra.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        tableItems.setPlaceholder(new Label("Nenhum item cadastrado."));

        ArrayList<Item> itens = obra.getItensDaObra();

        TableColumn<Item, Integer> clmId = new TableColumn<>("ID");
        TableColumn<Item, String> clmEditora = new TableColumn<>("Editora");
        TableColumn<Item, Integer> clmEdicao = new TableColumn<>("Edição");
        TableColumn<Item, String> clmCondicao = new TableColumn<>("Condição");
        TableColumn<Item, Boolean> clmDisponivel = new TableColumn<>("Disponibilidade");

        clmId.setVisible(false);

        clmId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmEditora.setCellValueFactory(new PropertyValueFactory<>("editora"));
        clmEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        clmCondicao.setCellValueFactory(new PropertyValueFactory<>("condicao"));
        clmDisponivel.setCellFactory(column -> new ItemDisponivelFactory());
        clmDisponivel.setCellValueFactory(new PropertyValueFactory<>("disponivel"));

        tableItems.getColumns().add(clmId);
        tableItems.getColumns().add(clmEditora);
        tableItems.getColumns().add(clmEdicao);
        tableItems.getColumns().add(clmCondicao);
        tableItems.getColumns().add(clmDisponivel);

        switch (obra.getTipo()) {
            case "livro":
                TableColumn<Item, String> clmTipoCapa = new TableColumn<>("Tipo de Capa");
                clmTipoCapa.setCellValueFactory(new PropertyValueFactory<>("tipoCapa"));
                tableItems.getColumns().add(clmTipoCapa);
                break;

            case "revista":
                TableColumn<Item, String> clmCategoria = new TableColumn<>("Categoria");
                clmCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
                tableItems.getColumns().add(clmCategoria);
                break;

            case "gibi":
                TableColumn<Item, String> clmTipo = new TableColumn<>("Tipo");
                TableColumn<Item, String> clmCategoriaGibi = new TableColumn<>("categoria");

                clmTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
                clmCategoriaGibi.setCellValueFactory(new PropertyValueFactory<>("categoria"));

                tableItems.getColumns().add(clmTipo);
                tableItems.getColumns().add(clmCategoriaGibi);

                break;
        }

        tableItems.getItems().addAll(itens);
    }

    @FXML
    void addItem(ActionEvent event) {

    }

    @FXML
    void addToCarrinho(ActionEvent event) {

    }

    @FXML
    void editItem(ActionEvent event) {

    }

    @FXML
    void editObra(ActionEvent event) throws IOException {
        Stage editObra = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarObra.fxml"));
        loader.setControllerFactory(param -> {
            if (param == EditarObra.class) {
                EditarObra controller = new EditarObra();
                controller.setObra(obra);
                return controller;
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        editObra.setScene(new Scene(loader.load()));
        editObra.initModality(Modality.APPLICATION_MODAL);
        editObra.initOwner(((Node) event.getSource()).getScene().getWindow());
        editObra.showAndWait();
    }

    @FXML
    void removeItem(ActionEvent event) {

    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
