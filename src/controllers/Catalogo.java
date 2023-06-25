package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.Estante;
import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class Catalogo implements Initializable {
    // TODO: implementar a paginação
    private Parent root;
    private Stage stage;

    @FXML
    private ScrollPane bookContainer;

    @FXML
    private ComboBox<String> filterCondicao;

    @FXML
    private ComboBox<String> filterDisponibilidade;

    @FXML
    private TextField filterEditora;

    @FXML
    private ComboBox<Estante> filterEstante;

    @FXML
    private DatePicker filterFromDataPubli;

    @FXML
    private TextField filterGenero;

    @FXML
    private ComboBox<String> filterTipo;

    @FXML
    private DatePicker filterToDataPubli;

    @FXML
    private ImageView iconCarrinho;

    @FXML
    private ImageView iconDados;

    @FXML
    private ImageView iconExitLogin;

    @FXML
    private ImageView iconPedidos;

    @FXML
    private Hyperlink linkCarrinho;

    @FXML
    private Hyperlink linkDados;

    @FXML
    private Hyperlink linkPedidos;

    @FXML
    private TextField query;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!Session.isLogged()) {
            iconCarrinho.setVisible(false);
            iconPedidos.setVisible(false);
            iconDados.setVisible(false);
            linkCarrinho.setVisible(false);
            linkPedidos.setVisible(false);
            linkDados.setVisible(false);

            iconExitLogin.setImage(new Image(getClass().getResourceAsStream("../imagens/login.png")));
        }

        bookContainer.setContent(this.createBookGrid(5, Obra.getObras(20, 0)));

        Estante todos = new Estante();
        todos.setId(0);
        todos.setCategoria("Todos");
        filterEstante.getItems().add(todos);

        ArrayList<Estante> filterEstanteOpts = Estante.getListaEstantes();
        for (Estante estante : filterEstanteOpts) {
            filterEstante.getItems().add(estante);
        }

        filterCondicao.getItems().addAll("Todos", "Novo", "Semi-novo", "Usado", "Restaurado");
        filterDisponibilidade.getItems().addAll("Todos", "Disponível", "Indisponível");
        filterTipo.getItems().addAll("Todos", "Livro", "Revista", "Gibi");
    }

    @FXML
    void exitLogin(MouseEvent event) throws IOException {
        if (Session.isLogged()) {
            Session.setLoggedUser(null);
        }

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void search(ActionEvent event) {
        String search = query.getText();

        ArrayList<Obra> result = Obra.getObras(search, 20, 0);

        bookContainer.setContent(this.createBookGrid((int) Math.ceil(result.size() / 4.0), result));
    }

    @FXML
    void mostrarCarrinho(ActionEvent event) throws IOException {
        Stage carrinho = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/Carrinho.fxml"));
        Parent content = loader.load();
        carrinho.setScene(new Scene(content));
        carrinho.initModality(Modality.APPLICATION_MODAL);
        carrinho.initOwner(((Node) event.getSource()).getScene().getWindow());
        carrinho.showAndWait();
    }

    @FXML
    void mostrarPedidos(MouseEvent event) throws IOException {
        Stage pedidos = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/PedidosCliente.fxml"));
        Parent content = loader.load();
        pedidos.setScene(new Scene(content));
        pedidos.initModality(Modality.APPLICATION_MODAL);
        pedidos.initOwner(((Node) event.getSource()).getScene().getWindow());
        pedidos.showAndWait();
    }

    @FXML
    void mostrarDados(MouseEvent event) throws IOException {
        Stage dados = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/DadosCliente.fxml"));
        Parent content = loader.load();
        dados.setScene(new Scene(content));
        dados.initModality(Modality.APPLICATION_MODAL);
        dados.initOwner(((Node) event.getSource()).getScene().getWindow());
        dados.showAndWait();
    }

    @FXML
    void queryWithFilters(ActionEvent event) {
        String search = query.getText();

        // filtros obra
        String tipo = filterTipo.getValue();
        tipo = (tipo == "Todos" || tipo == null) ? "" : tipo;
        Estante estante = filterEstante.getValue();
        estante = (estante == null || estante.getId() == 0) ? null : estante;
        LocalDate fromData = filterFromDataPubli.getValue();
        LocalDate toData = filterToDataPubli.getValue();
        String genero = filterGenero.getText();
        String disponibilidade = filterDisponibilidade.getValue();
        disponibilidade = (disponibilidade == "Todos" || disponibilidade == null) ? "" : disponibilidade;

        // filtros item
        String condicao = filterCondicao.getValue();
        condicao = (condicao == "Todos" || condicao == null) ? "" : condicao;
        String editora = filterEditora.getText();

        ArrayList<Obra> obras = Obra.getObras(search, tipo, estante, fromData, toData, genero, disponibilidade,
                condicao, editora, 20, 0);

        bookContainer.setContent(this.createBookGrid((int) Math.ceil(obras.size() / 4.0), obras));
    }

    public GridPane createBookGrid(int rows, ArrayList<Obra> obras) {
        GridPane grid = new GridPane();

        for (int i = 0; i < 4; i++) {
            grid.addColumn(i);
        }

        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPrefWidth(652);
        grid.setPrefHeight(rows * 211);

        for (int i = 0; i < rows; i++) {
            grid.addRow(i);
            for (int j = 0; j < 4 && (i * 4 + j) < obras.size(); j++) {
                int index = i * 4 + j;
                grid.add(this.createBookCell(obras.get(index).getId(), obras.get(index).getCapaUrl(),
                        obras.get(index).getNome(),
                        obras.get(index).getAutor()), j, i);
            }
        }

        return grid;
    }

    public VBox createBookCell(int id, String urlImage, String title, String author) {
        VBox cell = new VBox();
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setMaxWidth(163);
        cell.setMaxHeight(211);
        cell.getStyleClass().add("custom-vbox");

        ImageView image = new ImageView(urlImage);
        image.setPreserveRatio(false);
        image.setFitWidth(133);
        image.setFitHeight(160);
        image.getStyleClass().add("book-cover");

        Label label = new Label(title);
        label.setWrapText(true);
        label.setPrefWidth(150);
        label.setPrefHeight(50);
        label.setAlignment(Pos.TOP_LEFT);
        Tooltip.install(label, new Tooltip(String.format("%s\n%s", title, author)));
        label.getStyleClass().add("book-title");

        cell.getChildren().addAll(image, label);

        cell.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                this.viewObraEvent(event, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return cell;
    }

    public void viewObraEvent(Event event, int id) throws IOException {
        Obra obra = Obra.getObra(id);

        Stage details = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/DetalhesObra.fxml"));

        loader.setControllerFactory(param -> {
            if (param == DetalhesObra.class) {
                DetalhesObra controller = new DetalhesObra();
                controller.setObra(obra);
                return controller;
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar o controlador", e);
                }
            }
        });

        Parent content = loader.load();
        details.setScene(new Scene(content));
        details.initModality(Modality.APPLICATION_MODAL);
        details.initOwner(bookContainer.getScene().getWindow());
        details.showAndWait();
    }

}
