package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.ObraDAO;

public class Obra {
    // ATRIBUTOS
    private int id;
    private String nome;
    private String tipo;
    private LocalDate dataPublicacao;
    private String autor;
    private String genero;
    private String sinopse;
    private String capaUrl;
    private ArrayList<Item> itens = new ArrayList<>();
    private Estante estante;

    // METODOS
    public Obra() {
    }

    public Obra(String nome, String tipo, LocalDate dataPublicacao, String autor, String genero, String sinopse,
            String capaUrl, Estante estante) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataPublicacao = dataPublicacao;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
        this.capaUrl = capaUrl;
    }

    public Obra(int id, String nome, String autor, String tipo, String genero, LocalDate dataPublicacao, Estante estante) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.tipo = tipo;
        this.genero = genero;
        this.dataPublicacao = dataPublicacao;
        this.estante = estante;
    }

    // METODOS
    public boolean registrarItem(Item item) {
        boolean res = item.cadastrar();

        if (res) {
            itens.add(item);
        }

        return res;

    }

    public boolean removerItem(Item item) {
        boolean res = item.remover();

        if (res) {
            itens.remove(item);
        }

        return res;
    }

    public boolean verificaDisponibilidade() {
        ObraDAO dao = new ObraDAO();

        return dao.isDisponivel(this);
    }

    public static ArrayList<Obra> getObras(int limit, int offset) {
        ObraDAO dao = new ObraDAO();

        return dao.getAll(limit, offset);
    }

    public static ArrayList<Obra> getObras(String pesquisa, int limit, int offset) {
        ObraDAO dao = new ObraDAO();

        return dao.getObrasByNameOrAutor(pesquisa, limit, offset);
    }

    // GETTERS & SETTERS

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataPublicacao() {
        return this.dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return this.genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopse() {
        return this.sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getCapaUrl() {
        return this.capaUrl;
    }

    public void setCapaUrl(String capaUrl) {
        this.capaUrl = capaUrl;
    }

    public ArrayList<Item> getItens() {
        return this.itens;
    }

    public void setItens(ArrayList<Item> itens) {
        this.itens = itens;
    }

    public Estante getEstante() {
        return this.estante;
    }

    public void setEstante(Estante estante) {
        this.estante = estante;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", nome='" + getNome() + "'" +
                ", tipo='" + getTipo() + "'" +
                ", dataPublicacao='" + getDataPublicacao() + "'" +
                ", autor='" + getAutor() + "'" +
                ", genero='" + getGenero() + "'" +
                ", sinopse='" + getSinopse() + "'" +
                ", capaUrl='" + getCapaUrl() + "'" +
                ", estante='" + getEstante() + "'" +
                "}";
    }

}
