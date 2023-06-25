package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import assets.fxmlComponents.controllers.ItemPedidos;
import biblioteca.Cliente;
import biblioteca.Emprestimo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import session.Session;

public class PedidosCliente implements Initializable {

    @FXML
    private VBox itemContainer;

    @FXML
    private Label lblPlaceHoder;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Cliente cliente = (Cliente) Session.getLoggedUser();
        ArrayList<Emprestimo> emprestimosAtivos = cliente.getEmprestimosAtivos();

        if (emprestimosAtivos.size() != 0) {
            lblPlaceHoder.setVisible(false);
            lblPlaceHoder.setManaged(false);
            for (Emprestimo emprestimo : emprestimosAtivos) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/assets/fxmlComponents/ItemPedidos.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == ItemPedidos.class) {
                            ItemPedidos controller = new ItemPedidos();
                            controller.setEmprestimo(emprestimo);
                            controller.setItem(emprestimo.getItemEmprestado());
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException("Erro ao criar o controlador", e);
                            }
                        }
                    });
                    itemContainer.getChildren().add(loader.load());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
