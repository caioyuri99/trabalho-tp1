package session;

import biblioteca.Usuario;

public class Session {
    private static Usuario loggedUser = null;

    public static boolean isLogged() {
        return loggedUser != null;
    }

    public static Usuario getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(Usuario loggedUser) {
        Session.loggedUser = loggedUser;
    }
}
