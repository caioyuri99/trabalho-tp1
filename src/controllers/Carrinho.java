package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import assets.fxmlComponents.controllers.GibiItemCarrinho;
import assets.fxmlComponents.controllers.LivroItemCarrinho;
import assets.fxmlComponents.controllers.RevistaItemCarrinho;
import biblioteca.Cliente;
import biblioteca.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import session.Session;

public class Carrinho implements Initializable {

    @FXML
    private VBox itemContainer;

    @FXML
    private Label lblPlaceHoder;

    @FXML
    private Text txtMulta;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Session.verificaEmprestimos();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Cliente cliente = (Cliente) Session.getLoggedUser();

        if (cliente.getCarrinho().size() != 0) {
            lblPlaceHoder.setVisible(false);
            lblPlaceHoder.setManaged(false);
            for (Item item : cliente.getCarrinho()) {
                switch (item.getClass().getSimpleName()) {
                    case "Livro":
                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/assets/fxmlComponents/LivroItemCarrinho.fxml"));
                            loader.setControllerFactory(param -> {
                                if (param == LivroItemCarrinho.class) {
                                    LivroItemCarrinho controller = new LivroItemCarrinho();
                                    controller.setItem(item);
                                    controller.setTxtMulta(txtMulta);
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
                            itemContainer.getChildren().add(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Revista":
                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/assets/fxmlComponents/RevistaItemCarrinho.fxml"));
                            loader.setControllerFactory(param -> {
                                if (param == RevistaItemCarrinho.class) {
                                    RevistaItemCarrinho controller = new RevistaItemCarrinho();
                                    controller.setItem(item);
                                    controller.setTxtMulta(txtMulta);
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
                            itemContainer.getChildren().add(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Gibi":
                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/assets/fxmlComponents/GibiItemCarrinho.fxml"));
                            loader.setControllerFactory(param -> {
                                if (param == GibiItemCarrinho.class) {
                                    GibiItemCarrinho controller = new GibiItemCarrinho();
                                    controller.setItem(item);
                                    controller.setTxtMulta(txtMulta);
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
                            itemContainer.getChildren().add(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            this.atualizarTxtMulta();
        }
    }

    @FXML
    void limparCarrinho(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpar carrinho");
        alert.setHeaderText("Você tem certeza que deseja limpar o carrinho?");
        alert.setContentText("Todos os itens serão removidos do carrinho.");

        ButtonType buttonTypeOne = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            cliente.getCarrinho().clear();

            List<Node> childrens = new ArrayList<>(itemContainer.getChildren());
            Node keep = (Node) lblPlaceHoder;
            for (Node child : childrens) {
                if (child != keep) {
                    itemContainer.getChildren().remove(child);
                }
            }
            lblPlaceHoder.setManaged(true);
            lblPlaceHoder.setVisible(true);
        }
        this.atualizarTxtMulta();
    }

    @FXML
    void alugar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alugar itens");
        alert.setHeaderText("Você tem certeza que deseja alugar os itens do carrinho?");
        alert.setContentText("Os itens serão alugados e removidos do carrinho.");

        ButtonType buttonTypeOne = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            Cliente cliente = (Cliente) Session.getLoggedUser();
            try {
                cliente.fazerEmprestimo();
            } catch (Exception e) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao alugar itens.");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
                return;
            }

            List<Node> childrens = new ArrayList<>(itemContainer.getChildren());
            Node keep = (Node) lblPlaceHoder;
            for (Node child : childrens) {
                if (child != keep) {
                    itemContainer.getChildren().remove(child);
                }
            }
            lblPlaceHoder.setManaged(true);
            lblPlaceHoder.setVisible(true);
            this.atualizarTxtMulta();

            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Sucesso");
            alertSuccess.setHeaderText("Itens alugados com sucesso.");
            alertSuccess.setContentText("Os itens foram alugados e removidos do carrinho.");
            alertSuccess.showAndWait();
        }
    }

    public void atualizarTxtMulta() {
        double valor = ((Cliente) Session.getLoggedUser()).getCarrinho().size() * 0.8;
        txtMulta.setText(String.format("R$ %.2f", valor));
    }

}
