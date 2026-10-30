package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServeurMultiThread {

    private static final int PORT = 1234;
    private static AtomicInteger compteurClients = new AtomicInteger(0);
    public static void main(String[] args) {
        System.out.println("Serveur multithread en attente...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                int numeroClient = compteurClients.incrementAndGet();
                System.out.println("Client n°" + numeroClient
                        + " connecté depuis : "
                        + socket.getRemoteSocketAddress());
                ClientProcessCC traitement =
                        new ClientProcessCC(socket, numeroClient);
                Thread thread = new Thread(traitement);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
