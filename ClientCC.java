package clientPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientCC {

    private static final int PORT = 1234;
    public static void main(String[] args) {
        String ipServeur = args.length > 0
                ? args[0] : "localhost";
        try (
                Socket socket = new Socket();
                BufferedReader clavier = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            InetAddress adresse = InetAddress.getByName(ipServeur);
            socket.connect(
                    new InetSocketAddress(adresse, PORT));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);
            System.out.println(in.readLine());
            String message;
            System.out.println("Tapez un message (exit pour quitter) :");
            while ((message = clavier.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(message);
                System.out.println(in.readLine());
            }
            System.out.println("Déconnexion du client.");
        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
