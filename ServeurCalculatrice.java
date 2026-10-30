package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServeurCalculatrice {
    private static final int PORT = 1234;
    private static AtomicInteger compteurClients =
            new AtomicInteger(0);
    private static int compteurOperations = 0;
    // Méthode synchronisée : protège la ressource partagée
    public static synchronized int incrementerCompteur() {
        compteurOperations++;
        return compteurOperations;
    }
    public static void main(String[] args) {
        System.out.println("Serveur calculatrice multithread en attente...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                int numeroClient = compteurClients.incrementAndGet();
                System.out.println("Client n°" + numeroClient
                        + " connecté depuis : "
                        + socket.getRemoteSocketAddress());
                ClientProcess traitement =
                        new ClientProcess(socket, numeroClient);
                Thread thread = new Thread(traitement);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
