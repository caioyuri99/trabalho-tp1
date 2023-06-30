package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import biblioteca.Admin;
import biblioteca.BooleanDisplayPattern;
import biblioteca.DatePattern;
import biblioteca.Funcionario;
import biblioteca.Usuario;
import controllers.cellFactoryFormat.BooleanDisplayFactory;
import controllers.cellFactoryFormat.DatePatternDateFactory;
import controllers.cellFactoryFormat.UsuarioCpfFactory;
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
import javafx.scene.control.CheckBox;
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

public class TelaAdministradorFuncionarios implements Initializable {

    private Parent root;
    private Stage stage;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnConfirmSearch;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnSalvar;

    @FXML
    private CheckBox ckbAdmin;

    @FXML
    private TableView<Funcionario> tableFuncionarios;

    @FXML
    private TableColumn<BooleanDisplayPattern, Boolean> clmAdmin;

    @FXML
    private TableColumn<Usuario, String> clmCpf;

    @FXML
    private TableColumn<DatePattern, LocalDate> clmDataNasc;

    @FXML
    private TableColumn<Funcionario, String> clmNome;

    @FXML
    private TableColumn<Funcionario, String> clmCargo;

    @FXML
    private DatePicker dateDataNasc;

    @FXML
    private TextField txtCargo;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSenha;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tableFuncionarios.setPlaceholder(new Label("Nenhum funcionário encontrado"));

        clmAdmin.setCellFactory(column -> new BooleanDisplayFactory());
        clmAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        clmCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        clmCpf.setCellFactory(column -> new UsuarioCpfFactory());
        clmCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        clmDataNasc.setCellFactory(column -> new DatePatternDateFactory());
        clmDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
        clmNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        refreshTable();
    }

    @FXML
    void editarFuncionario(ActionEvent event) {
        Funcionario funcionario = tableFuncionarios.getSelectionModel().getSelectedItem();

        if (funcionario == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum funcionário selecionado");
            alert.setContentText("Selecione um funcionário para editar");

            alert.showAndWait();
            return;
        }

        txtCpf.setDisable(false);
        txtCpf.setText(funcionario.getCpf());
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        txtCargo.setDisable(false);
        txtCargo.setText(funcionario.getCargo());
        txtNome.setDisable(false);
        txtNome.setText(funcionario.getNome());
        dateDataNasc.setDisable(false);
        dateDataNasc.setValue(funcionario.getDataNasc());
        ckbAdmin.setDisable(false);
        ckbAdmin.setSelected(funcionario.isAdmin());
        txtSenha.setDisable(true);
        btnNew.setDisable(true);
        btnSalvar.setDisable(false);
        btnCancelar.setDisable(false);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        btnPesquisar.setDisable(true);
        tableFuncionarios.setDisable(true);

        btnCancelar.setOnAction(e -> disableFlow());

        btnSalvar.setOnAction(e -> {
            String cpf = txtCpf.getText();
            String nome = txtNome.getText();
            String cargo = txtCargo.getText();
            LocalDate dataNasc = dateDataNasc.getValue();
            boolean admin = ckbAdmin.isSelected();

            if (cpf.isEmpty() || nome.isEmpty() || cargo.isEmpty() || dataNasc == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Campos não preenchidos");
                alert.setContentText("Preencha todos os campos");

                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Confirmação de edição");
            alert.setContentText("Deseja confirmar a edição?");

            ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == btnConfirmar) {
                Admin adminUser = (Admin) Session.getLoggedUser();

                funcionario.setCpf(cpf);
                funcionario.setNome(nome);
                funcionario.setCargo(cargo);
                funcionario.setDataNasc(dataNasc);
                funcionario.setAdmin(admin);

                try {
                    adminUser.editarUsuario(funcionario);
                } catch (Exception e1) {
                    Alert alertError = new Alert(AlertType.ERROR);
                    alertError.setTitle("Erro");
                    alertError.setHeaderText("Erro ao editar funcionário");
                    alertError.setContentText(e1.getMessage());

                    alertError.showAndWait();
                    return;
                }

                Alert alertSuccess = new Alert(AlertType.INFORMATION);
                alertSuccess.setTitle("Sucesso");
                alertSuccess.setHeaderText("Funcionário editado com sucesso");
                alertSuccess.setContentText("O funcionário foi editado com sucesso");

                alertSuccess.showAndWait();

                disableFlow();
                refreshTable();
            }
        });
    }

    @FXML
    void excluirFuncionario(ActionEvent event) {
        Funcionario funcionario = tableFuncionarios.getSelectionModel().getSelectedItem();

        if (funcionario == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhum funcionário selecionado");
            alert.setContentText("Selecione um funcionário para excluir");

            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText("Confirmação de exclusão");
        alert.setContentText("Deseja confirmar a exclusão?");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnConfirmar) {
            Admin admin = (Admin) Session.getLoggedUser();

            try {
                admin.deletarUsuario(funcionario);
            } catch (Exception e) {
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.setTitle("Erro");
                alertError.setHeaderText("Erro ao excluir funcionário");
                alertError.setContentText(e.getMessage());

                alertError.showAndWait();
                return;
            }

            Alert alertSuccess = new Alert(AlertType.INFORMATION);
            alertSuccess.setTitle("Sucesso");
            alertSuccess.setHeaderText("Funcionário excluído com sucesso");
            alertSuccess.setContentText("O funcionário foi excluído com sucesso");

            alertSuccess.showAndWait();
            refreshTable();
        }
    }

    @FXML
    void newFuncionario(ActionEvent event) {
        txtCpf.setDisable(false);
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        txtCargo.setDisable(false);
        txtNome.setDisable(false);
        txtSenha.setDisable(false);
        dateDataNasc.setDisable(false);
        ckbAdmin.setDisable(false);
        btnNew.setDisable(true);
        btnSalvar.setDisable(false);
        btnCancelar.setDisable(false);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        btnPesquisar.setDisable(true);
        tableFuncionarios.setDisable(true);

        btnCancelar.setOnAction(e -> disableFlow());

        btnSalvar.setOnAction(e -> {
            String cpf = txtCpf.getText();
            LocalDate dataNasc = dateDataNasc.getValue();
            String nome = txtNome.getText();
            String senha = txtSenha.getText();
            String cargo = txtCargo.getText();
            boolean admin = ckbAdmin.isSelected();

            if (cpf.isEmpty() || dataNasc == null || nome.isEmpty() || senha.isEmpty() || cargo.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Campos vazios");
                alert.setContentText("Preencha todos os campos");
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
                Admin adminUser = (Admin) Session.getLoggedUser();
                Funcionario funcionario = new Funcionario(cpf, senha, nome, dataNasc, cargo, admin);

                try {
                    adminUser.fazerCadastro(funcionario);
                } catch (Exception e1) {
                    Alert alertError = new Alert(AlertType.ERROR);
                    alertError.setTitle("Erro");
                    alertError.setHeaderText("Erro ao cadastrar");
                    alertError.setContentText(e1.getMessage());
                    alertError.showAndWait();
                    return;
                }

                Alert alertSuccess = new Alert(AlertType.INFORMATION);
                alertSuccess.setTitle("Sucesso");
                alertSuccess.setHeaderText("Cadastro realizado");
                alertSuccess.setContentText("O funcionário foi cadastrado com sucesso");
                alertSuccess.showAndWait();

                disableFlow();
                refreshTable();
            }
        });
    }

    @FXML
    void pesquisarFuncionario(ActionEvent event) {
        txtCpf.setDisable(false);
        btnConfirmSearch.setDisable(false);
        btnRefresh.setDisable(false);
        txtCargo.setDisable(true);
        txtNome.setDisable(false);
        txtSenha.setDisable(true);
        dateDataNasc.setDisable(true);
        ckbAdmin.setDisable(true);
        btnNew.setDisable(true);
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(false);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        btnPesquisar.setDisable(true);
        tableFuncionarios.setDisable(false);

        btnCancelar.setOnAction(e -> disableFlow());

        txtCpf.setOnKeyReleased(e -> txtNome.clear());

        txtNome.setOnKeyReleased(e -> txtCpf.clear());

        btnConfirmSearch.setOnAction(e -> {
            String cpf = txtCpf.getText();
            String nome = txtNome.getText();

            ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();
            if (!cpf.isEmpty()) {
                funcionarios = Funcionario.getFuncionariosByCpfLike(cpf, 50, 0);
            } else {
                funcionarios = Funcionario.getFuncionariosByNomeLike(nome, 50, 0);
            }

            tableFuncionarios.getItems().setAll(funcionarios);

            disableFlow();
        });
    }

    @FXML
    void sair(MouseEvent event) throws IOException {
        this.root = FXMLLoader.load(getClass().getResource("../telas/TelaAdministrador.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(new Scene(this.root));
        this.stage.show();
    }

    private void refreshTable() {
        ArrayList<Funcionario> funcionarios = Funcionario.getListaFuncionarios(50, 0);
        tableFuncionarios.getItems().setAll(funcionarios);
    }

    private void disableFlow() {
        txtCpf.setDisable(true);
        txtCpf.clear();
        btnConfirmSearch.setDisable(true);
        btnRefresh.setDisable(true);
        txtCargo.setDisable(true);
        txtCargo.clear();
        txtNome.setDisable(true);
        txtNome.clear();
        txtSenha.setDisable(true);
        txtSenha.clear();
        dateDataNasc.setDisable(true);
        dateDataNasc.setValue(null);
        ckbAdmin.setDisable(true);
        ckbAdmin.setSelected(false);
        btnNew.setDisable(false);
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(true);
        btnEditar.setDisable(false);
        btnExcluir.setDisable(false);
        btnPesquisar.setDisable(false);
        tableFuncionarios.setDisable(false);

        btnCancelar.setOnAction(null);
        btnSalvar.setOnAction(null);
        txtCpf.setOnKeyReleased(null);
        txtNome.setOnKeyReleased(null);
        btnConfirmSearch.setOnAction(null);
        btnRefresh.setOnAction(null);
    }

}
