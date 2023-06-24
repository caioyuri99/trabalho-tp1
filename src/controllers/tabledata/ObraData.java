package controllers.tabledata;

import java.time.LocalDate;

import biblioteca.Obra;
// import javafx.scene.control.Button;

public class ObraData {
    private Integer id;
    private String nome;
    private String autor;
    private String tipo;
    private String genero;
    private LocalDate dataDePublicacao;
    private String estante;
    // private Button btnView;

    public ObraData(Obra obra) {
        this.id = obra.getId();
        this.nome = obra.getNome();
        this.autor = obra.getAutor();
        this.tipo = obra.getTipo();
        this.genero = obra.getGenero();
        this.dataDePublicacao = obra.getDataPublicacao();
        this.estante = obra.getEstante().getCategoria();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return this.genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getDataDePublicacao() {
        return this.dataDePublicacao;
    }

    public void setDataDePublicacao(LocalDate dataDePublicacao) {
        this.dataDePublicacao = dataDePublicacao;
    }

    public String getEstante() {
        return this.estante;
    }

    public void setEstante(String estante) {
        this.estante = estante;
    }

    // public Button getBtnView() {
    //     return this.btnView;
    // }

    // public void setBtnView(Button btnView) {
    //     this.btnView = btnView;
    // }

}
