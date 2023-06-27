package session;

import java.time.LocalDate;

import biblioteca.Cliente;
import biblioteca.Usuario;

public class Session {
    private static Usuario loggedUser = null;
    private static LocalDate dataAtual = LocalDate.now();

    public static boolean isLogged() {
        return loggedUser != null;
    }

    public static Usuario getLoggedUser() {
        return loggedUser;
    }

    public static void login(Usuario usuario) throws Exception {
        loggedUser = usuario;

        if (usuario instanceof Cliente) {
            ((Cliente) usuario).calcularMulta();
        }
    }

    public static void logout() {
        loggedUser = null;
    }

    public static void verificaEmprestimos() throws Exception {
        if (loggedUser instanceof Cliente) {
            ((Cliente) loggedUser).calcularMulta();
        }
    }

    public static LocalDate getDataAtual() {
        return dataAtual;
    }

    public static void setDataAtual(LocalDate dataAtual) {
        Session.dataAtual = dataAtual;
    }
}
