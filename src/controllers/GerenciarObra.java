package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Estante;
import biblioteca.Gibi;
import biblioteca.Item;
import biblioteca.Livro;
import biblioteca.Obra;
import biblioteca.Revista;
import controllers.cellFactoryFormat.ItemDisponivelFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GerenciarObra implements Initializable {

    private Obra obra;
    private TelaFuncionario telaFuncionarioController;

    @FXML
    private Button btnAdd;

    @FXML
    private ImageView imgCapa;

    @FXML
    private Label lblAutor;

    @FXML
    private Label lblDataPubli;

    @FXML
    private Label lblEstante;

    @FXML
    private Label lblGenero;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblSinopse;

    @FXML
    private Label lblTipo;

    @FXML
    private TableView<Item> tableItems;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refreshObra();

        tableItems.setPlaceholder(new Label("Nenhum item cadastrado."));

        TableColumn<Item, Integer> clmId = new TableColumn<>("ID");
        TableColumn<Item, String> clmEditora = new TableColumn<>("Editora");
        TableColumn<Item, Integer> clmEdicao = new TableColumn<>("Edição");
        TableColumn<Item, String> clmCondicao = new TableColumn<>("Condição");
        TableColumn<Item, Boolean> clmDisponivel = new TableColumn<>("Disponibilidade");

        clmId.setVisible(false);

        clmId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmEditora.setCellValueFactory(new PropertyValueFactory<>("editora"));
        clmEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        clmCondicao.setCellValueFactory(new PropertyValueFactory<>("condicao"));
        clmDisponivel.setCellFactory(column -> new ItemDisponivelFactory());
        clmDisponivel.setCellValueFactory(new PropertyValueFactory<>("disponivel"));

        tableItems.getColumns().add(clmId);
        tableItems.getColumns().add(clmEditora);
        tableItems.getColumns().add(clmEdicao);
        tableItems.getColumns().add(clmCondicao);
        tableItems.getColumns().add(clmDisponivel);

        switch (obra.getTipo()) {
            case "livro":
                TableColumn<Item, String> clmTipoCapa = new TableColumn<>("Tipo de Capa");
                clmTipoCapa.setCellValueFactory(new PropertyValueFactory<>("tipoCapa"));
                tableItems.getColumns().add(clmTipoCapa);
                break;

            case "revista":
                TableColumn<Item, String> clmCategoria = new TableColumn<>("Categoria");
                clmCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
                tableItems.getColumns().add(clmCategoria);
                break;

            case "gibi":
                TableColumn<Item, String> clmTipo = new TableColumn<>("Tipo");

                clmTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

                tableItems.getColumns().add(clmTipo);

                break;
        }

        refreshTable();
    }

    @FXML
    void addItem(ActionEvent event) {
        switch (obra.getTipo().toLowerCase()) {
            case "livro":
                try {
                    Stage addLivro = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/CadastroLivro.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == CadastroLivro.class) {
                            CadastroLivro controller = new CadastroLivro();
                            controller.setObra(obra);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    addLivro.setScene(new Scene(loader.load()));
                    addLivro.initModality(Modality.APPLICATION_MODAL);
                    addLivro.initOwner(((Node) event.getSource()).getScene().getWindow());
                    addLivro.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "revista":
                try {
                    Stage addRevista = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/CadastroRevista.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == CadastroRevista.class) {
                            CadastroRevista controller = new CadastroRevista();
                            controller.setObra(obra);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    addRevista.setScene(new Scene(loader.load()));
                    addRevista.initModality(Modality.APPLICATION_MODAL);
                    addRevista.initOwner(((Node) event.getSource()).getScene().getWindow());
                    addRevista.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "gibi":
                try {
                    Stage addGibi = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/CadastroGibi.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == CadastroGibi.class) {
                            CadastroGibi controller = new CadastroGibi();
                            controller.setObra(obra);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    addGibi.setScene(new Scene(loader.load()));
                    addGibi.initModality(Modality.APPLICATION_MODAL);
                    addGibi.initOwner(((Node) event.getSource()).getScene().getWindow());
                    addGibi.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        refreshTable();
    }

    @FXML
    void editItem(ActionEvent event) {
        Item item = tableItems.getSelectionModel().getSelectedItem();

        if (item == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum item selecionado");
            alert.setContentText("Selecione um item para editar");
            alert.showAndWait();
            return;
        }

        switch (obra.getTipo().toLowerCase()) {
            case "livro":
                try {
                    Stage editLivro = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/AtualizarLivro.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == AtualizarLivro.class) {
                            AtualizarLivro controller = new AtualizarLivro();
                            controller.setObra(obra);
                            controller.setLivro((Livro) item);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    editLivro.setScene(new Scene(loader.load()));
                    editLivro.initModality(Modality.APPLICATION_MODAL);
                    editLivro.initOwner(((Node) event.getSource()).getScene().getWindow());
                    editLivro.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "revista":
                try {
                    Stage editRevista = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/EditarRevista.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == AtualizarRevista.class) {
                            AtualizarRevista controller = new AtualizarRevista();
                            controller.setObra(obra);
                            controller.setRevista((Revista) item);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    editRevista.setScene(new Scene(loader.load()));
                    editRevista.initModality(Modality.APPLICATION_MODAL);
                    editRevista.initOwner(((Node) event.getSource()).getScene().getWindow());
                    editRevista.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "gibi":
                try {
                    Stage editGibi = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/EditarGibi.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param == AtualizarGibi.class) {
                            AtualizarGibi controller = new AtualizarGibi();
                            controller.setObra(obra);
                            controller.setGibi((Gibi) item);
                            return controller;
                        } else {
                            try {
                                return param.getDeclaredConstructor().newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    });
                    editGibi.setScene(new Scene(loader.load()));
                    editGibi.initModality(Modality.APPLICATION_MODAL);
                    editGibi.initOwner(((Node) event.getSource()).getScene().getWindow());
                    editGibi.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        refreshTable();
    }

    @FXML
    void editObra(ActionEvent event) throws IOException {
        Stage editObra = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../telas/EditarObra.fxml"));
        loader.setControllerFactory(param -> {
            if (param == EditarObra.class) {
                EditarObra controller = new EditarObra();
                controller.setObra(obra);
                return controller;
            } else {
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        editObra.setScene(new Scene(loader.load()));
        editObra.initModality(Modality.APPLICATION_MODAL);
        editObra.initOwner(((Node) event.getSource()).getScene().getWindow());
        editObra.showAndWait();

        this.obra = Obra.getObra(this.obra.getId());
        refreshObra();
    }

    @FXML
    void deleteObra(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remover obra");
        alert.setHeaderText("Remover obra");
        alert.setContentText("Tem certeza que deseja remover a obra e todos os seus itens?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Estante estante = obra.getEstante();

            try {
                estante.removerObra(obra);
            } catch (Exception e) {
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao remover obra");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
                return;
            }

            Alert alertSuccess = new Alert(AlertType.INFORMATION);
            alertSuccess.setTitle("Sucesso");
            alertSuccess.setHeaderText("Obra removida");
            alertSuccess.setContentText("A obra foi removida com sucesso");
            alertSuccess.showAndWait();
            telaFuncionarioController.refreshTable(event);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void removeItem(ActionEvent event) {
        Item item = tableItems.getSelectionModel().getSelectedItem();

        if (item == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum item selecionado");
            alert.setContentText("Selecione um item para remover");
            alert.showAndWait();
            return;
        }

        if (!item.isDisponivel()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Item emprestado");
            alert.setContentText("Não é possível remover um item emprestado");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remover item");
        alert.setHeaderText("Remover item");
        alert.setContentText("Tem certeza que deseja remover o item?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            try {
                obra.removerItem(item);
            } catch (Exception e) {
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao remover item");
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
                return;
            }

            Alert alertSuccess = new Alert(AlertType.INFORMATION);
            alertSuccess.setTitle("Sucesso");
            alertSuccess.setHeaderText("Item removido com sucesso");
            alertSuccess.showAndWait();

            refreshTable();
        }
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public void setTelaFuncionarioController(TelaFuncionario telaFuncionarioController) {
        this.telaFuncionarioController = telaFuncionarioController;
    }

    private void refreshTable() {
        ArrayList<Item> itens = obra.obterItens();
        tableItems.getItems().setAll(itens);
    }

    private void refreshObra() {
        lblNome.setText(obra.getNome());
        lblAutor.setText(obra.getAutor());
        lblGenero.setText(obra.getGenero());
        lblTipo.setText(obra.getTipo().substring(0, 1).toUpperCase() + obra.getTipo().substring(1).toLowerCase());
        lblEstante.setText(obra.getEstante().getCategoria());
        lblSinopse.setText(obra.getSinopse());
        imgCapa.setImage(new Image(obra.getCapaUrl()));
        lblDataPubli.setText(obra.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

}
