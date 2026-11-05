/**
 * Client console de l'amateur : se connecte à PORT_AMA.
 * Question : pourrait-il être identique à ClientProg ? Quelle différence reste-t-il (port seulement ?)
 * Si oui, une classe commune évite de dupliquer du code...
 */
public class clientama {

    private static final String HOST = "localhost";
    private static final int PORT_AMA = 4000;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT_AMA);
 
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clavier = new BufferedReader(
                    new InputStreamReader(System.in));
 
            // 1. L'invite envoyée par le service
            System.out.println(in.readLine());
 
            // 2. Ce que l'utilisateur tape part vers le serveur
            out.println(clavier.readLine());
 
            // 3. La réponse du service
            System.out.println(in.readLine());
 
        } catch (IOException e) {
            System.err.println("Problème de connexion : " + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // rien à faire
                }
            }
        }
    }
}