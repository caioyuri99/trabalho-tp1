package biblioteca;

public abstract class Item {
    // ATRIBUTOS
    protected int id;
    protected String editora;
    protected int edicao;
    protected String condicao;
    protected boolean disponivel;
    protected Obra obra;

    // CONSTRUTORES
    public Item() {
    }

    public Item(String editora, int edicao, String condicao) {
        this.editora = editora;
        this.edicao = edicao;
        this.condicao = condicao;
    }

    public Item(String editora, int edicao, String condicao, Obra obra) {
        this.editora = editora;
        this.edicao = edicao;
        this.condicao = condicao;
        this.obra = obra;
    }

    public Item(String editora, int edicao, String condicao, boolean disponivel, Obra obra) {
        this.editora = editora;
        this.edicao = edicao;
        this.condicao = condicao;
        this.disponivel = disponivel;
        this.obra = obra;
    }

    // MÉTODOS
    public abstract void cadastrar() throws Exception;

    public abstract void atualizar() throws Exception;

    public abstract void remover() throws Exception;

    public abstract void emprestar() throws Exception;

    public abstract void devolver() throws Exception;

    @Override
    public abstract boolean equals(Object obj);

    // GETTERS & SETTERS
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEditora() {
        return this.editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getEdicao() {
        return this.edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }

    public String getCondicao() {
        return this.condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Obra getObra() {
        return this.obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
