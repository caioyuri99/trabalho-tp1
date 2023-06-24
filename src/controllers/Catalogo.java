package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import biblioteca.Obra;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import javafx.stage.Popup;
import javafx.stage.Stage;
import session.Session;

public class Catalogo implements Initializable {

    private Parent root;
    private Stage stage;

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

    @FXML
    private TextField query;

    @FXML
    private ImageView iconDivida;

    @FXML
    private Hyperlink linkDivida;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!Session.isLogged()) {
            iconCarrinho.setVisible(false);
            iconPedidos.setVisible(false);
            iconDivida.setVisible(false);
            linkCarrinho.setVisible(false);
            linkPedidos.setVisible(false);
            linkDivida.setVisible(false);

            iconExitLogin.setImage(new Image(getClass().getResourceAsStream("../imagens/login.png")));
        }

        bookContainer.setContent(this.createBookGrid(5, Obra.getObras(20, 0)));
    }

    @FXML
    void exitLogin(MouseEvent event) throws IOException {
        if (Session.isLogged()) {
            Session.loggedCPF = null;
        }

        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaInicial.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new javafx.scene.Scene(root));
        this.stage.show();
    }

    @FXML
    void search(ActionEvent event) {
        String search = query.getText();

        ArrayList<Obra> result = Obra.getObras(search, 20, 0);

        bookContainer.setContent(this.createBookGrid((int) Math.ceil(result.size() / 4.0), result));
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/DetalhesObraPopUp.fxml"));

        Popup popup = new Popup();
        loader.setControllerFactory(param -> {
            if (param == DetalhesObraPopUp.class) {
                DetalhesObraPopUp popupController = new DetalhesObraPopUp();
                popupController.setObra(obra);
                popupController.setPopup(popup);
                return popupController;
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao criar o controlador", e);
                }
            }
        });

        Parent popupContent = loader.load();

        popup.getContent().add(popupContent);
        popup.setAutoHide(true);
        popup.show(((Node) event.getSource()).getScene().getWindow());
    }

}
