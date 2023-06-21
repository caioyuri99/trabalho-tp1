package session;

public class Session {
    public static String loggedCPF = null;

    public static boolean isLogged() {
        return loggedCPF != null;
    }
}
