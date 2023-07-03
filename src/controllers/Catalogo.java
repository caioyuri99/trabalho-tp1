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
import javafx.scene.control.Button;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class Catalogo implements Initializable {
    // TODO: implementar a paginação
    // TODO: trocar os hiperlinks por labels formatadas como links
    private Parent root;
    private Stage stage;
    private int currentPage = 0;
    private int totalPages = 0;

    // parâmetros de pesquisa
    private boolean withFilter = false;
    private String lastSearch = "";
    private String lastTipo = "";
    private Estante lastEstante = null;
    private LocalDate lastFromData = null;
    private LocalDate lastToData = null;
    private String lastGenero = "";
    private Boolean lastDisponibilidade = null;
    private String lastCondicao = "";
    private String lastEditora = "";

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

    @FXML
    private Button btnNextPage;

    @FXML
    private Button btnPreviousPage;

    @FXML
    private Label lblPageCount;

    @FXML
    private Label lblTotalResult;

    @FXML
    private TextField txtPageNumber;

    @FXML
    private GridPane bookGrid;

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
        } else {
            try {
                Session.verificaEmprestimos();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // TODO: colocar uma espécie de placehoder no bookcontainer
        int total = Obra.getObrasCount();
        lblTotalResult.setText(String.format("%d resultados encontrados", total));
        totalPages = (int) Math.ceil(total / 20.0);
        btnPreviousPage.setDisable(true);

        if (totalPages == 1) {
            btnNextPage.setDisable(true);
            txtPageNumber.setDisable(true);
        } else {
            btnNextPage.setDisable(false);
            txtPageNumber.setDisable(false);
        }

        txtPageNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPageNumber.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (newValue.isEmpty() || txtPageNumber.getText().isEmpty()) {
                return;
            }

            int currentNum = Integer.parseInt(txtPageNumber.getText());

            if (currentNum - 1 != currentPage) {
                return;
            }

            if (currentNum >= totalPages) {
                txtPageNumber.setText(String.valueOf(totalPages));
                btnNextPage.setDisable(true);
            } else {
                btnNextPage.setDisable(false);
            }

            if (currentNum <= 1) {
                txtPageNumber.setText("1");
                btnPreviousPage.setDisable(true);
            } else {
                btnPreviousPage.setDisable(false);
            }
        });

        attPageCount();

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        bookGrid.getRowConstraints().addAll(rowConstraints);

        goToPageBasicSearch(currentPage);

        ArrayList<Estante> filterEstanteOpts = Estante.getListaEstantes();
        Estante todos = new Estante();
        todos.setId(0);
        todos.setCategoria("Todos");
        filterEstante.getItems().add(todos);
        filterEstante.getItems().addAll(filterEstanteOpts);

        filterCondicao.getItems().addAll("Todos", "Novo", "Semi-novo", "Usado", "Restaurado");
        filterDisponibilidade.getItems().addAll("Todos", "Disponível", "Indisponível");
        filterTipo.getItems().addAll("Todos", "Livro", "Revista", "Gibi");
    }

    @FXML
    void exitLogin(MouseEvent event) throws IOException {
        if (Session.isLogged()) {
            // TODO: se o usuário tiver itens no carrinho, mostrar uma mensagem avisando que
            // o carrinho será esvaziado e perguntando se ele tem certeza que quer sair
            Session.logout();
        }

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void search(ActionEvent event) {
        // TODO: fazer o botão de lupa ativar a busca
        withFilter = false;

        lastSearch = query.getText();

        int total = Obra.getObrasCount(lastSearch);
        lblTotalResult.setText(String.format("%d resultados encontrados", total));
        totalPages = (int) Math.ceil(total / 20.0);
        System.out.println(totalPages);

        if (totalPages == 1) {
            txtPageNumber.setDisable(true);
            btnNextPage.setDisable(true);
        } else {
            txtPageNumber.setDisable(false);
            btnNextPage.setDisable(false);
        }

        currentPage = 0;
        attPageCount();
        goToPageBasicSearch(currentPage);
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
        // TODO: fazer o botão de lupa ativar a busca
        // TODO: fazer os campos de filtro ativarem a busca ao pressionar a tecla enter
        withFilter = true;

        lastSearch = query.getText();

        // filtros obra
        lastTipo = filterTipo.getValue();
        lastTipo = (lastTipo == "Todos" || lastTipo == null) ? "" : lastTipo.toLowerCase();

        lastEstante = filterEstante.getValue();
        lastEstante = (lastEstante == null || lastEstante.getId() == 0) ? null : lastEstante;

        lastFromData = filterFromDataPubli.getValue();
        lastToData = filterToDataPubli.getValue();

        lastGenero = filterGenero.getText();

        if (filterDisponibilidade.getValue() == "Todos" || filterDisponibilidade.getValue() == null) {
            lastDisponibilidade = null;
        } else {
            lastDisponibilidade = (filterDisponibilidade.getValue() == "Disponível") ? true : false;
        }

        // filtros item
        lastCondicao = filterCondicao.getValue();
        lastCondicao = (lastCondicao == "Todos" || lastCondicao == null) ? "" : lastCondicao;

        lastEditora = filterEditora.getText();

        int total = Obra.getObrasCount(lastSearch, lastTipo, lastEstante, lastFromData, lastToData, lastGenero,
                lastDisponibilidade, lastCondicao, lastEditora);
        lblTotalResult.setText(String.format("%d resultados encontrados", total));
        totalPages = (int) Math.ceil(total / 20.0);

        if (totalPages == 1) {
            txtPageNumber.setDisable(true);
            btnNextPage.setDisable(true);
        } else {
            txtPageNumber.setDisable(false);
            btnNextPage.setDisable(false);
        }

        currentPage = 0;
        attPageCount();
        goToPageWithFilters(currentPage);
    }

    @FXML
    void goToNextPage(ActionEvent event) {
        currentPage++;

        if (withFilter) {
            this.goToPageWithFilters(currentPage);
        } else {
            this.goToPageBasicSearch(currentPage);
        }

        this.attPageCount();
    }

    @FXML
    void goToPreviousPage(ActionEvent event) {
        currentPage--;

        if (withFilter) {
            this.goToPageWithFilters(currentPage);
        } else {
            this.goToPageBasicSearch(currentPage);
        }

        this.attPageCount();
    }

    @FXML
    void goToPage(ActionEvent event) {
        if (txtPageNumber.getText().isEmpty() || Integer.parseInt(txtPageNumber.getText()) < 1) {
            txtPageNumber.setText("1");
        }

        if (Integer.parseInt(txtPageNumber.getText()) > totalPages)
            txtPageNumber.setText(Integer.toString(totalPages));

        currentPage = Integer.parseInt(txtPageNumber.getText()) - 1;

        if (withFilter) {
            this.goToPageWithFilters(currentPage);
        } else {
            this.goToPageBasicSearch(currentPage);
        }

        this.attPageCount();
    }

    private void fillBookGrid(int rows, ArrayList<Obra> obras) {
        bookGrid.getChildren().clear();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 4 && (i * 4 + j) < obras.size(); j++) {
                int index = i * 4 + j;
                bookGrid.add(this.createBookCell(obras.get(index).getId(), obras.get(index).getCapaUrl(),
                        obras.get(index).getNome(),
                        obras.get(index).getAutor()), j, i);
            }
        }
    }

    private VBox createBookCell(int id, String urlImage, String title, String author) {
        VBox cell = new VBox();
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setMaxWidth(163);
        cell.setPrefWidth(163);
        cell.setMaxHeight(211);
        cell.setPrefHeight(211);
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

    private void viewObraEvent(Event event, int id) throws IOException {
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

    private void attPageCount() {
        lblPageCount.setText(String.format("Página %d de %d", currentPage + 1, totalPages));
        txtPageNumber.textProperty().set(String.valueOf(currentPage + 1));
    }

    private void goToPageBasicSearch(int page) {
        this.fillBookGrid(5, Obra.getObras(lastSearch, 20, 20 * page));
    }

    private void goToPageWithFilters(int page) {
        this.fillBookGrid(5, Obra.getObras(lastSearch, lastTipo, lastEstante, lastFromData,
                lastToData, lastGenero, lastDisponibilidade, lastCondicao, lastEditora, 20, 20 * page));
    }

}
