package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;

import dao.GibiDAO;
import dao.LivroDAO;
import dao.ObraDAO;
import dao.RevistaDAO;

public class Obra implements DatePattern {
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
            String capaUrl) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataPublicacao = dataPublicacao;
        this.autor = autor;
        this.genero = genero;
        this.sinopse = sinopse;
        this.capaUrl = capaUrl;
    }

    public Obra(int id, String nome, String autor, String tipo, String genero, LocalDate dataPublicacao,
            Estante estante) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.tipo = tipo;
        this.genero = genero;
        this.dataPublicacao = dataPublicacao;
        this.estante = estante;
    }

    // METODOS
    public void registrarItem(Item item) throws Exception {
        item.cadastrar();
        itens.add(item);
    }

    public void atualizarItem(Item item) throws Exception {
        item.atualizar();
    }

    public void removerItem(Item item) throws Exception {
        item.remover();
        itens.remove(item);
    }

    public void removeAllItems() throws Exception {
        this.itens = this.getItensDaObra();

        for (Item item : this.itens) {
            item.remover();
        }
    }

    public boolean verificaDisponibilidade() {
        ObraDAO dao = new ObraDAO();

        return dao.isDisponivel(this);
    }

    public ArrayList<Item> getItensDaObra() {
        ArrayList<Item> itens = switch (this.tipo) {
            case "livro" -> new LivroDAO().getItemsOfObra(this);
            case "revista" -> new RevistaDAO().getItemsOfObra(this);
            case "gibi" -> new GibiDAO().getItemsOfObra(this);
            default -> new ArrayList<>();
        };

        return itens;
    }

    public static ArrayList<Obra> getObras(int limit, int offset) {
        ObraDAO dao = new ObraDAO();

        return dao.getAll(limit, offset);
    }

    public static ArrayList<Obra> getObras(String pesquisa, int limit, int offset) {
        ObraDAO dao = new ObraDAO();

        return dao.getObrasByNameOrAutor(pesquisa, limit, offset);
    }

    public static ArrayList<Obra> getObras(String search, String tipo, Estante estante, LocalDate fromData,
            LocalDate toData, String genero, String disponibilidade, String condicao, String editora, int limit,
            int offset) {
        ObraDAO dao = new ObraDAO();

        return dao.searchCustomQuery(search, tipo, estante, fromData, toData, genero, disponibilidade, condicao,
                editora, limit,
                offset);
    }

    public static Obra getObra(int id) {
        ObraDAO dao = new ObraDAO();

        return dao.getObra(id);
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
