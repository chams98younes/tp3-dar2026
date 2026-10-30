package clientPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientCalculatrice {

    private static final int PORT = 1234;
    private static final Pattern OPERATION = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+*/-])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");
    public static void main(String[] args) {
        String ipServeur = args.length > 0
                ? args[0] : "localhost";
        try (
                BufferedReader clavier = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            System.out.print("Entrez une opération (ex. 55 * 25) : ");
            String expression = clavier.readLine();
            Matcher matcher = OPERATION.matcher(expression);
            if (!matcher.matches()) {
                System.out.println("Opération invalide !");
                return;
            }
            double a = Double.parseDouble(matcher.group(1));
            String operateur = matcher.group(2);
            double b = Double.parseDouble(matcher.group(3));
            if (operateur.equals("/") && b == 0) {
                System.out.println("Division par zéro impossible.");
                return;
            }
            // Création de l'objet à envoyer
            Operation operation = new Operation(a, operateur, b);
            try (Socket socket = new Socket()) {
                InetAddress adresse = InetAddress.getByName(ipServeur);
                socket.connect(
                        new InetSocketAddress(adresse, PORT));
                // Créer le flux de sortie en premier
                ObjectOutputStream out =
                        new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in =
                        new ObjectInputStream(socket.getInputStream());
                // Envoyer l'objet
                out.writeObject(operation);
                out.flush();
                System.out.println("Objet Operation envoyé au serveur.");
                // Recevoir la réponse
                Object reponse = in.readObject();
                if (reponse instanceof Double) {
                    System.out.println("Résultat reçu : " + reponse);
                } else {
                    System.out.println("Réponse du serveur : " + reponse);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
