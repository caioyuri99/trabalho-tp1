package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Obra;
import controllers.cellFactoryFormat.ItemDisponivelFactory;
import exceptions.Confirmation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import session.Session;

public class DetalhesObra implements Initializable {

    private Obra obra;

    @FXML
    private Button btnAdd;

    @FXML
    private ImageView imgCapa;

    @FXML
    private Label lblAutor;

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

        if (Session.isLogged()) {
            tableItems.setOnMouseClicked(event -> {
                Item item = tableItems.getSelectionModel().getSelectedItem();

                if (item != null) {
                    btnAdd.setDisable(false);
                }
            });
        }
    }

    @FXML
    void addToCarrinho(ActionEvent event) {
        Item item = tableItems.getSelectionModel().getSelectedItem();

        if (!item.isDisponivel()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Item indisponível");
            alert.setContentText("O item selecionado não está disponível para empréstimo.");

            alert.showAndWait();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            return;
        }

        try {
            ((Cliente) Session.getLoggedUser()).adicionarAoCarrinho(item);
        } catch (Exception e) {
            if (!(e instanceof Confirmation)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao adicionar item ao carrinho");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Já adicionado um item desta obra ao carrinho");
            alert.setContentText("Deseja adicionar outro item desta obra ao carrinho?");
            ButtonType confirmButton = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirmButton) {
                ((Cliente) Session.getLoggedUser()).getCarrinho().add(item);

                Alert info = new Alert(AlertType.INFORMATION);
                info.setTitle("Sucesso");
                info.setHeaderText("Item adicionado ao carrinho");
                info.setContentText("O item foi adicionado ao carrinho com sucesso.");

                info.showAndWait();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                return;
            }

            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle("Informação");
            info.setHeaderText("Item não adicionado ao carrinho");
            info.setContentText("O item não foi adicionado ao carrinho.");

            info.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText("Item adicionado ao carrinho");
        alert.setContentText("O item foi adicionado ao carrinho com sucesso.");

        alert.showAndWait();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
