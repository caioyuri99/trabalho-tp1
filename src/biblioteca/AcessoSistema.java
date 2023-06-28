package biblioteca;

public interface AcessoSistema {
    public void login(String login, String senha) throws Exception;

    public void logout();
}
