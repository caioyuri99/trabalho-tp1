package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.DatePattern;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class TelaFuncionario implements Initializable {

    private Parent root;
    private Stage stage;

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

        ArrayList<Obra> obras = Obra.getObras(50, 0);
        tableObras.setItems(FXCollections.observableArrayList(obras));
    }

    @FXML
    void addEstante(ActionEvent event) throws IOException {
        Stage addEstante = new Stage();
        Parent content = FXMLLoader.load(getClass().getResource("../telas/AdicionarEstante.fxml"));
        addEstante.setScene(new Scene(content));
        addEstante.initModality(Modality.APPLICATION_MODAL);
        addEstante.initOwner(((Node) event.getSource()).getScene().getWindow());
        addEstante.showAndWait();
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
            this.root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministrador.fxml"));
            this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            this.stage.setScene(new Scene(root));
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshTable(ActionEvent event) {
        ArrayList<Obra> obras = Obra.getObras(50, 0);
        tableObras.setItems(FXCollections.observableArrayList(obras));
    }

    @FXML
    void search(ActionEvent event) {
        String search = txtSearch.getText();

        ArrayList<Obra> obras = Obra.getObras(search, 50, 0);
        tableObras.setItems(FXCollections.observableArrayList(obras));
    }

}
