package serverPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcessCC implements Runnable {
    private Socket socket;
    private int numeroClient;
    public ClientProcessCC(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }
    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {
            out.println("Vous êtes le client n°" + numeroClient);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client n°" + numeroClient
                        + " : " + message);
                out.println("Message reçu par le serveur.");
            }
        } catch (Exception e) {
            System.out.println("Client n°" + numeroClient
                    + " déconnecté.");
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Erreur fermeture socket.");
            }
        }
    }
}
