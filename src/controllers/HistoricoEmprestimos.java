package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.BooleanDisplayPattern;
import biblioteca.Cliente;
import biblioteca.DatePattern;
import biblioteca.Emprestimo;
import biblioteca.Item;
import controllers.cellFactoryFormat.BooleanDisplayFactory;
import controllers.cellFactoryFormat.DatePatternDateFactory;
import controllers.cellFactoryFormat.EmprestimoBooleanPagoFactory;
import controllers.cellFactoryFormat.EmprestimoDoubleValorMultaFactory;
import controllers.cellFactoryFormat.EmprestimoItemObraAutorFactory;
import controllers.cellFactoryFormat.EmprestimoItemObraNomeFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.Session;

public class HistoricoEmprestimos implements Initializable {
    // TODO: Detalhe: implementar paginação nessa tela

    private Parent root;
    private Stage stage;

    @FXML
    private TableColumn<BooleanDisplayPattern, Boolean> clmAtrasado;

    @FXML
    private TableColumn<Emprestimo, Item> clmAutor;

    @FXML
    private TableColumn<DatePattern, LocalDate> clmDataDevolucao;

    @FXML
    private TableColumn<DatePattern, LocalDate> clmDataEmprestimo;

    @FXML
    private TableColumn<BooleanDisplayPattern, Boolean> clmDevolvido;

    @FXML
    private TableColumn<BooleanDisplayPattern, Boolean> clmMultado;

    @FXML
    private TableColumn<Emprestimo, Boolean> clmPago;

    @FXML
    private TableColumn<Emprestimo, String> clmTipo;

    @FXML
    private TableColumn<Emprestimo, Item> clmTitulo;

    @FXML
    private TableColumn<Emprestimo, Double> clmValorMulta;

    @FXML
    private DatePicker dateFromData;

    @FXML
    private DatePicker dateToData;

    @FXML
    private TableView<Emprestimo> tableEmprestimos;

    @FXML
    private TextField txtQuery;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableEmprestimos.setPlaceholder(new javafx.scene.control.Label("Nenhum empréstimo encontrado."));

        clmAtrasado.setCellFactory(column -> new BooleanDisplayFactory());
        clmAtrasado.setCellValueFactory(new PropertyValueFactory<>("atrasado"));
        clmAutor.setCellFactory(column -> new EmprestimoItemObraAutorFactory());
        clmAutor.setCellValueFactory(new PropertyValueFactory<>("item"));
        clmDataDevolucao.setCellFactory(column -> new DatePatternDateFactory());
        clmDataDevolucao.setCellValueFactory(new PropertyValueFactory<>("dataDevolucao"));
        clmDataEmprestimo.setCellFactory(column -> new DatePatternDateFactory());
        clmDataEmprestimo.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));
        clmDevolvido.setCellFactory(column -> new BooleanDisplayFactory());
        clmDevolvido.setCellValueFactory(new PropertyValueFactory<>("devolvido"));
        clmMultado.setCellFactory(column -> new BooleanDisplayFactory());
        clmMultado.setCellValueFactory(new PropertyValueFactory<>("multado"));
        clmPago.setCellFactory(column -> new EmprestimoBooleanPagoFactory());
        clmPago.setCellValueFactory(new PropertyValueFactory<>("pago"));
        clmTipo.setCellValueFactory(new PropertyValueFactory<>("tipoItem"));
        clmTitulo.setCellFactory(column -> new EmprestimoItemObraNomeFactory());
        clmTitulo.setCellValueFactory(new PropertyValueFactory<>("item"));
        clmValorMulta.setCellFactory(column -> new EmprestimoDoubleValorMultaFactory());
        clmValorMulta.setCellValueFactory(new PropertyValueFactory<>("valorMulta"));

        tableEmprestimos.setRowFactory(tv -> {
            TableRow<Emprestimo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Emprestimo rowData = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/DetalhesObra.fxml"));
                        loader.setControllerFactory(param -> {
                            if (param == DetalhesObra.class) {
                                DetalhesObra controller = new DetalhesObra();
                                controller.setObra(rowData.getItem().getObra());
                                return controller;
                            } else {
                                try {
                                    return param.getDeclaredConstructor().newInstance();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        Stage detalhesObra = new Stage();
                        detalhesObra.setTitle("Detalhes da Obra");
                        detalhesObra.setScene(new Scene(loader.load()));
                        detalhesObra.initModality(Modality.APPLICATION_MODAL);
                        detalhesObra.initOwner(tableEmprestimos.getScene().getWindow());
                        detalhesObra.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        refreshTable();
    }

    @FXML
    void exit(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../telas/DadosCliente.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Meus Dados");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void refreshTable(ActionEvent event) {
        refreshTable();
    }

    @FXML
    void search(ActionEvent event) {
        Cliente cliente = (Cliente) Session.getLoggedUser();

        String query = txtQuery.getText();
        LocalDate dateFrom = dateFromData.getValue();
        LocalDate dateTo = dateToData.getValue();

        if (query.isEmpty() && dateFrom == null && dateTo == null) {
            refreshTable();
            return;
        }

        if (dateFrom == null) {
            dateFrom = LocalDate.of(1500, 1, 1);
        }

        if (dateTo == null) {
            dateTo = LocalDate.of(3000, 1, 1);
        }

        ArrayList<Emprestimo> emprestimos = cliente.filtraHistoricoEmprestimos(query, dateFrom, dateTo, 50, 0);

        tableEmprestimos.getItems().setAll(emprestimos);
    }

    private void refreshTable() {
        Cliente cliente = (Cliente) Session.getLoggedUser();
        ArrayList<Emprestimo> emprestimos = cliente.getHistoricoEmprestimos(50, 0);

        tableEmprestimos.getItems().setAll(emprestimos);
    }
}