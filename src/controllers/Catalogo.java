package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.Obra;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import session.Session;

public class Catalogo implements Initializable {

    @FXML
    private ScrollPane bookContainer;

    @FXML
    private ImageView iconCarrinho;

    @FXML
    private ImageView iconExitLogin;

    @FXML
    private ImageView iconPedidos;

    @FXML
    private Hyperlink linkCarrinho;

    @FXML
    private Hyperlink linkPedidos;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!Session.isLogged()) {
            iconCarrinho.setVisible(false);
            iconPedidos.setVisible(false);
            linkCarrinho.setVisible(false);
            linkPedidos.setVisible(false);

            iconExitLogin.setImage(new Image(getClass().getResourceAsStream("../imagens/login.png")));
        }

        bookContainer.setContent(createBookGrid(5, Obra.getObras(20, 0)));
    }

    public static GridPane createBookGrid(int rows, ArrayList<Obra> obras) {
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
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                grid.add(createBookCell(obras.get(index).getCapaUrl(), obras.get(index).getNome(),
                        obras.get(index).getAutor()), j, i);
            }
        }

        return grid;
    }

    public static VBox createBookCell(String urlImage, String title, String author) {
        VBox cell = new VBox();
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setMaxWidth(163);
        cell.setMaxHeight(211);

        ImageView image = new ImageView(urlImage);
        image.setPreserveRatio(false);
        image.setFitWidth(133);
        image.setFitHeight(160);

        Label label = new Label(title);
        label.setWrapText(true);
        label.setPrefWidth(150);
        label.setPrefHeight(50);
        label.setAlignment(Pos.TOP_LEFT);
        Tooltip.install(label, new Tooltip(String.format("%s\n%s", title, author)));
        label.getStyleClass().add("book-title");

        cell.getChildren().addAll(image, label);

        return cell;
    }

}
