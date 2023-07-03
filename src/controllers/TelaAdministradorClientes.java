package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Admin;
import biblioteca.Cliente;
import biblioteca.DatePattern;
import biblioteca.Usuario;
import controllers.cellFactoryFormat.DatePatternDateFactory;
import controllers.cellFactoryFormat.UsuarioCpfFactory;
import controllers.cellFactoryFormat.ClienteSaldoDevedorFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import session.Session;

public class TelaAdministradorClientes implements Initializable {

    public Parent root;
    public Stage stage;

    @FXML
    private Button btnConfirmSearch;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnCancel;

    @FXML
    private DatePicker dateDataNasc;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;

    @FXML
    private TableView<Cliente> tableClientes;

    @FXML
    private TableColumn<Usuario, String> clmCpf;

    @FXML
    private TableColumn<DatePattern, LocalDate> clmDataNasc;

    @FXML
    private TableColumn<Cliente, String> clmNome;

    @FXML
    private TableColumn<Cliente, Double> clmSaldoDevedor;

    @FXML
    private Button btnRefresh;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableClientes.setPlaceholder(new Label("Nenhum cliente encontrado"));

        clmCpf.setCellFactory(column -> new UsuarioCpfFactory());
        clmCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        clmNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clmDataNasc.setCellFactory(column -> new DatePatternDateFactory());
        clmDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
        clmSaldoDevedor.setCellFactory(column -> new ClienteSaldoDevedorFactory());
        clmSaldoDevedor.setCellValueFactory(new PropertyValueFactory<>("saldoDevedor"));

        refreshTable();

        txtCpf.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null || newValue.isEmpty()) {
                    return;
                }

                if (newValue.length() > 14) {
                    txtCpf.setText(oldValue);
                    return;
                }

                // Remove caracteres não numéricos
                String cpf = newValue.replaceAll("[^\\d]", "");

                if (newValue.length() < oldValue.length()) {
                    txtCpf.setText(newValue);
                    return;
                }

                // Formata o CPF com pontos e traço
                StringBuilder formattedCPF = new StringBuilder();
                for (int i = 0; i < cpf.length(); i++) {
                    formattedCPF.append(cpf.charAt(i));
                    if ((i + 1) % 3 == 0 && i < 8) {
                        formattedCPF.append(".");
                    } else if ((i + 1) % 3 == 0 && i < 11) {
                        formattedCPF.append("-");
                    }
                }

                txtCpf.setText(formattedCPF.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @FXML
    void deleteCliente(ActionEvent event) {
        Cliente cliente = tableClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum cliente selecionado");
            alert.setContentText("Por favor, selecione um cliente na tabela.");

            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Excluir cliente");
        alert.setHeaderText("Excluir cliente");
        alert.setContentText("Tem certeza que deseja excluir o cliente " + cliente.getNome() + "?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Não", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == btnConfirmar) {
            Admin admin = (Admin) Session.getLoggedUser();

            try {
                admin.deletarUsuario(cliente);
            } catch (Exception e) {
                Alert alertErro = new Alert(AlertType.ERROR);
                alertErro.setTitle("Erro");
                alertErro.setHeaderText("Erro ao excluir cliente");
                alertErro.setContentText(e.getMessage());

                alertErro.showAndWait();

                return;
            }

            Alert alertSucesso = new Alert(AlertType.INFORMATION);
            alertSucesso.setTitle("Sucesso");
            alertSucesso.setHeaderText("Cliente excluído");
            alertSucesso.setContentText("O cliente " + cliente.getNome() + " foi excluído com sucesso.");

            alertSucesso.showAndWait();

            refreshTable();
        }
    }

    @FXML
    void editCliente(ActionEvent event) {
        Cliente cliente = tableClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum cliente selecionado");
            alert.setContentText("Por favor, selecione um cliente na tabela.");

            alert.showAndWait();

            return;
        }

        txtCpf.setDisable(false);
        txtCpf.setText(cliente.getCpf());
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        dateDataNasc.setDisable(false);
        dateDataNasc.setValue(cliente.getDataNasc());
        txtNome.setDisable(false);
        txtNome.setText(cliente.getNome());
        txtSenha.setDisable(true);
        btnNew.setDisable(true);
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
        btnEdit.setDisable(true);
        btnExcluir.setDisable(true);
        btnSearch.setDisable(true);
        tableClientes.setDisable(true);

        btnCancel.setOnAction(e -> disableFlow());

        btnSave.setOnAction(e -> {
            formatarCPF();

            String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
            LocalDate dataNasc = dateDataNasc.getValue();
            String nome = txtNome.getText();

            if (cpf.isEmpty() || dataNasc == null || nome.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Campos não preenchidos");
                alert.setContentText("Por favor, preencha todos os campos.");

                alert.showAndWait();

                return;
            }

            if (cpf.length() != 11) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("CPF inválido");
                alert.setContentText("Por favor, digite um CPF válido.");

                alert.showAndWait();

                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Editar cliente");
            alert.setHeaderText("Editar cliente");
            alert.setContentText("Tem certeza que deseja editar o cliente " + cliente.getNome() + "?");

            ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Não", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == btnConfirmar) {
                Admin admin = (Admin) Session.getLoggedUser();

                cliente.setCpf(cpf);
                cliente.setDataNasc(dataNasc);
                cliente.setNome(nome);

                try {
                    admin.editarUsuario(cliente);
                } catch (Exception e1) {
                    Alert alertErro = new Alert(AlertType.ERROR);
                    alertErro.setTitle("Erro");
                    alertErro.setHeaderText("Erro ao editar cliente");
                    alertErro.setContentText(e1.getMessage());

                    alertErro.showAndWait();

                    return;
                }

                Alert alertSucesso = new Alert(AlertType.INFORMATION);
                alertSucesso.setTitle("Sucesso");
                alertSucesso.setHeaderText("Cliente editado");
                alertSucesso.setContentText("O cliente " + cliente.getNome() + " foi editado com sucesso.");

                alertSucesso.showAndWait();

                disableFlow();
                refreshTable();
            }
        });
    }

    @FXML
    void newCliente(ActionEvent event) {
        txtCpf.setDisable(false);
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        dateDataNasc.setDisable(false);
        txtNome.setDisable(false);
        txtSenha.setDisable(false);
        btnNew.setDisable(true);
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
        btnEdit.setDisable(true);
        btnExcluir.setDisable(true);
        btnSearch.setDisable(true);
        tableClientes.setDisable(true);

        btnCancel.setOnAction(e -> disableFlow());

        btnSave.setOnAction(e -> {
            formatarCPF();

            String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
            LocalDate dataNasc = dateDataNasc.getValue();
            String nome = txtNome.getText();
            String senha = txtSenha.getText();

            if (cpf.isEmpty() || dataNasc == null || nome.isEmpty() || senha.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao cadastrar cliente");
                alert.setContentText("Preencha todos os campos");
                alert.showAndWait();
                return;
            }

            if (cpf.length() != 11) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao cadastrar cliente");
                alert.setContentText("CPF inválido");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Confirmação de cadastro");
            alert.setContentText("Deseja confirmar o cadastro?");

            ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnConfirmar) {
                Admin admin = (Admin) Session.getLoggedUser();
                Cliente cliente = new Cliente(cpf, senha, nome, dataNasc);

                try {
                    admin.fazerCadastro(cliente);
                } catch (Exception err) {
                    Alert alertErro = new Alert(AlertType.ERROR);
                    alertErro.setTitle("Erro");
                    alertErro.setHeaderText("Erro ao cadastrar cliente");
                    alertErro.setContentText(err.getMessage());
                    alertErro.showAndWait();
                    return;
                }

                Alert alertSucesso = new Alert(AlertType.INFORMATION);
                alertSucesso.setTitle("Sucesso");
                alertSucesso.setHeaderText("Cliente cadastrado com sucesso");
                alertSucesso.setContentText("Cliente cadastrado com sucesso");
                alertSucesso.showAndWait();

                disableFlow();

                refreshTable();
            }
        });
    }

    @FXML
    void sair(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministrador.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(this.root));
        this.stage.show();
    }

    @FXML
    void searchCliente(ActionEvent event) {
        txtCpf.setDisable(false);
        btnConfirmSearch.setDisable(false);
        btnRefresh.setDisable(false);
        dateDataNasc.setDisable(true);
        txtNome.setDisable(false);
        txtSenha.setDisable(true);
        btnNew.setDisable(true);
        btnSave.setDisable(true);
        btnCancel.setDisable(false);
        btnEdit.setDisable(true);
        btnExcluir.setDisable(true);
        btnSearch.setDisable(true);
        tableClientes.setDisable(false);

        btnCancel.setOnAction(e -> disableFlow());

        txtCpf.setOnKeyReleased(e -> txtNome.setText(null));

        txtNome.setOnKeyReleased(e -> txtCpf.setText(null));

        btnConfirmSearch.setOnAction(e -> {
            formatarCPF();

            String cpf = txtCpf.getText().replaceAll("[^\\d]", "");
            String nome = txtNome.getText();

            ArrayList<Cliente> clientes = new ArrayList<Cliente>();
            if (!cpf.isEmpty()) {
                clientes = Cliente.getClientesByCpfLike(cpf, 50, 0);
            } else {
                clientes = Cliente.getClientesByNomeLike(nome, 50, 0);
            }

            tableClientes.getItems().setAll(clientes);

            disableFlow();
        });

        btnRefresh.setOnAction(e -> {
            refreshTable();

            disableFlow();
        });
    }

    private void refreshTable() {
        ArrayList<Cliente> clientes = Cliente.getListaClientes(50, 0);
        tableClientes.getItems().setAll(clientes);
    }

    private void disableFlow() {
        txtCpf.setDisable(true);
        txtCpf.clear();
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        dateDataNasc.setDisable(false);
        dateDataNasc.setValue(null);
        txtNome.setDisable(false);
        txtNome.clear();
        txtSenha.setDisable(false);
        txtSenha.clear();
        btnNew.setDisable(true);
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
        btnEdit.setDisable(true);
        btnExcluir.setDisable(true);
        btnSearch.setDisable(true);
        tableClientes.setDisable(true);

        btnCancel.setOnAction(null);
        btnSave.setOnAction(null);
        txtCpf.setOnKeyReleased(null);
        txtNome.setOnKeyReleased(null);
        btnConfirmSearch.setOnAction(null);
        btnRefresh.setOnAction(null);
    }

    private void formatarCPF() {
        String cpf = txtCpf.getText().replaceAll("[^\\d]", "");

        if (cpf.length() > 11) {
            cpf = cpf.substring(0, 11);
        }

        StringBuilder formattedCPF = new StringBuilder();
        for (int i = 0; i < cpf.length(); i++) {
            formattedCPF.append(cpf.charAt(i));
            if ((i + 1) % 3 == 0 && i < 8) {
                formattedCPF.append(".");
            } else if ((i + 1) % 3 == 0 && i < 11) {
                formattedCPF.append("-");
            }
        }

        txtCpf.setText(formattedCPF.toString());
    }

}
