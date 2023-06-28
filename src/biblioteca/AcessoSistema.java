package biblioteca;

public interface AcessoSistema {
    public void login(String cpf, String senha) throws Exception;

    public void logout();
}
