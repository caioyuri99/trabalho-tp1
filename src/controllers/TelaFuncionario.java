package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.Obra;
import controllers.cellFactoryFormat.ObraDataPublicacaoFactory;
import controllers.tabledata.ObraData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TelaFuncionario implements Initializable {

    @FXML
    private TableView<ObraData> tableObras;

    // @FXML
    // private TableColumn<ObraData, Button> btnView;

    @FXML
    private TableColumn<ObraData, String> clmAutor;

    @FXML
    private TableColumn<ObraData, LocalDate> clmDataDePublicacao;

    @FXML
    private TableColumn<ObraData, String> clmEstante;

    @FXML
    private TableColumn<ObraData, String> clmGenero;

    @FXML
    private TableColumn<ObraData, String> clmId;

    @FXML
    private TableColumn<ObraData, String> clmNome;

    @FXML
    private TableColumn<ObraData, String> clmTipo;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableObras.setPlaceholder(new Label("Nenhuma obra encontrada"));

        clmId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clmAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        clmTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        clmGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        clmDataDePublicacao.setCellFactory(column -> new ObraDataPublicacaoFactory());
        clmDataDePublicacao.setCellValueFactory(new PropertyValueFactory<>("dataDePublicacao"));
        clmEstante.setCellValueFactory(new PropertyValueFactory<>("estante"));
        // btnView.setCellValueFactory(new PropertyValueFactory<>("btnView"));

        ArrayList<Obra> obras = Obra.getObras(50, 0);
        ArrayList<ObraData> list = new ArrayList<ObraData>();
        for (Obra obra : obras) {
            // Button btnView = new Button();
            // ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("../imagens/mais.png")));
            // imgView.setPreserveRatio(true);
            // imgView.setFitHeight(20);
            // btnView.setGraphic(imgView);

            ObraData obraData = new ObraData(obra);
            list.add(obraData);

        }

        tableObras.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void test(MouseEvent event) {
        System.out.println(event.getSource().toString());
    }

}
