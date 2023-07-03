package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.DatePattern;
import biblioteca.Estante;
import biblioteca.Funcionario;
import biblioteca.Obra;
import controllers.cellFactoryFormat.DatePatternDateFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class TelaFuncionario implements Initializable {

    private Parent root;
    private Stage stage;

    private int totalPages = 1;
    private int currentPage = 0;

    private String lastSearch = "";
    private Estante lastEstante = null;
    private String lastTipo = "";

    @FXML
    private TableView<Obra> tableObras;

    @FXML
    private TableColumn<Obra, String> clmAutor;

    @FXML
    private TableColumn<DatePattern, LocalDate> clmDataDePublicacao;

    @FXML
    private TableColumn<Obra, String> clmEstante;

    @FXML
    private TableColumn<Obra, String> clmGenero;

    @FXML
    private TableColumn<Obra, String> clmId;

    @FXML
    private TableColumn<Obra, String> clmNome;

    @FXML
    private TableColumn<Obra, String> clmTipo;

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<Estante> cbbEstante;

    @FXML
    private ComboBox<String> cbbTipo;

    @FXML
    private Button btnNextPage;

    @FXML
    private Button btnPreviousPage;

    @FXML
    private TextField txtCurrentPage;

    @FXML
    private Label lblPageCount;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableObras.setPlaceholder(new Label("Nenhuma obra encontrada"));

        clmId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clmAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        clmTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        clmGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        clmDataDePublicacao.setCellFactory(column -> new DatePatternDateFactory());
        clmDataDePublicacao.setCellValueFactory(new PropertyValueFactory<>("dataPublicacao"));
        clmEstante.setCellValueFactory(new PropertyValueFactory<>("estante"));

        tableObras.setRowFactory(tv -> {
            TableRow<Obra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // TODO: colocar uma tela de carregamento nisso
                    Obra obra = row.getItem();

                    Stage gerenciarObra = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/GerenciarObra.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == GerenciarObra.class) {
                            GerenciarObra controller = new GerenciarObra();
                            controller.setObra(obra);
                            controller.setTelaFuncionarioController(this);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    try {
                        gerenciarObra.setScene(new Scene(loader.load()));
                        gerenciarObra.initModality(Modality.APPLICATION_MODAL);
                        gerenciarObra.initOwner(((Node) event.getSource()).getScene().getWindow());
                        gerenciarObra.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        int total = Obra.getObrasCount();
        totalPages = (int) Math.ceil(total / 30);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (totalPages == 1) {
            btnNextPage.setDisable(true);
            txtCurrentPage.setDisable(true);
        } else {
            btnNextPage.setDisable(false);
            txtCurrentPage.setDisable(false);
        }

        txtCurrentPage.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCurrentPage.setText(newValue.replaceAll("[^\\d]", ""));
            }

            if (newValue.isEmpty() || txtCurrentPage.getText().isEmpty()) {
                return;
            }

            int currentNum = Integer.parseInt(txtCurrentPage.getText());

            if (currentNum - 1 != currentPage) {
                return;
            }

            if (totalPages == 1) {
                btnNextPage.setDisable(true);
                txtCurrentPage.setText("1");
            } else {
                btnNextPage.setDisable(false);
            }

            if (currentNum >= totalPages) {
                txtCurrentPage.setText(String.valueOf(totalPages));
                btnNextPage.setDisable(true);
            } else {
                btnNextPage.setDisable(false);
            }

            if (currentNum <= 1) {
                txtCurrentPage.setText("1");
                btnPreviousPage.setDisable(true);
            } else {
                btnPreviousPage.setDisable(false);
            }
        });

        goToPage(0);

        attPageCount();

        Estante todas = new Estante(0, "Todas");
        cbbEstante.getItems().add(todas);
        cbbEstante.getItems().addAll(Estante.getListaEstantes());

        cbbTipo.getItems().addAll("Todos", "Livro", "Revista", "Gibi");
    }

    @FXML
    void gerenciarEstantes(ActionEvent event) throws IOException {
        Stage gerenciarEstantes = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/GerenciarEstantes.fxml"));
        gerenciarEstantes.setScene(new Scene(content));
        gerenciarEstantes.initModality(Modality.APPLICATION_MODAL);
        gerenciarEstantes.initOwner(((Node) event.getSource()).getScene().getWindow());
        gerenciarEstantes.showAndWait();
    }

    @FXML
    void addObra(ActionEvent event) throws IOException {
        Stage addObra = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/CadastroObra.fxml"));
        addObra.setScene(new Scene(content));
        addObra.initModality(Modality.APPLICATION_MODAL);
        addObra.initOwner(((Node) event.getSource()).getScene().getWindow());
        addObra.showAndWait();
    }

    @FXML
    void exit(MouseEvent event) throws IOException {
        Session.logout();

        this.root = FXMLLoader.load(getClass().getResource("../telas/LoginFuncionario.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(root));
        this.stage.show();
    }

    @FXML
    void goToAdmin(ActionEvent event) {
        Funcionario funcionario = (Funcionario) Session.getLoggedUser();

        if (!funcionario.isAdmin()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Você não tem permissão para acessar essa página.");
            alert.setContentText("Você não tem permissão para acessar essa página.");

            alert.showAndWait();
            return;
        }

        try {
            // TODO: transformar essa tela em uma popup
            root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministrador.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToMyAccount(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../telas/DadosFuncionario.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Meus dados");
        stage.show();
    }

    @FXML
    void refreshTable(ActionEvent event) {
        int total = Obra.getObrasCount();
        totalPages = (int) Math.ceil(total / 30);

        ArrayList<Obra> obras = Obra.getObras(30, 0);
        tableObras.setItems(FXCollections.observableArrayList(obras));

        goToPage(0);
    }

    @FXML
    void search(ActionEvent event) {
        lastSearch = txtSearch.getText();
        lastTipo = (cbbTipo.getValue() == null || cbbTipo.getValue().equals("Todos")) ? "" : cbbTipo.getValue();
        lastEstante = (cbbEstante.getValue() == null || cbbEstante.getValue().getId() == 0) ? null
                : cbbEstante.getValue();

        int total = Obra.getObrasCount(lastSearch, lastEstante, lastTipo);
        this.totalPages = (int) Math.ceil(total / 30);
        if (totalPages == 0) {
            totalPages = 1;
        }

        if (totalPages == 1) {
            btnNextPage.setDisable(true);
            txtCurrentPage.setDisable(true);
        } else {
            btnNextPage.setDisable(false);
            txtCurrentPage.setDisable(false);
        }

        goToPage(0);
        attPageCount();
    }

    @FXML
    void goToNextPage(ActionEvent event) {
        goToPage(currentPage + 1);
        attPageCount();
    }

    @FXML
    void goToPage(ActionEvent event) {
        int page = Integer.parseInt(txtCurrentPage.getText()) - 1;
        goToPage(page);
        attPageCount();
    }

    @FXML
    void goToPreviousPage(ActionEvent event) {
        goToPage(currentPage - 1);
        attPageCount();
    }

    private void goToPage(int page) {
        currentPage = page;

        ArrayList<Obra> obras = Obra.getObras(lastSearch, lastEstante, lastTipo, 30, 30 * page);
        tableObras.setItems(FXCollections.observableArrayList(obras));
    }

    private void attPageCount() {
        lblPageCount.setText(String.format("Página %d de %d", currentPage + 1, totalPages));
        txtCurrentPage.textProperty().set(String.valueOf(currentPage + 1));
    }

}
