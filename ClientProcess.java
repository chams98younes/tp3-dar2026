package serverPackage;

import clientPackage.Operation;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProcess implements Runnable {

    private Socket socket;
    private int numeroClient;
    public ClientProcess(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }
    @Override
    public void run() {
        try {
            // Créer le flux de sortie en premier
            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());
            // Recevoir l'objet Operation
            Operation operation = (Operation) in.readObject();
            double a = operation.getOperande1();
            String operateur = operation.getOperateur();
            double b = operation.getOperande2();
            System.out.println("Client n°" + numeroClient
                    + " : opération reçue : "
                    + a + " " + operateur + " " + b);
            double resultat;
            switch (operateur) {
                case "+":
                    resultat = a + b;
                    break;
                case "-":
                    resultat = a - b;
                    break;
                case "*":
                    resultat = a * b;
                    break;
                case "/":
                    if (b == 0) {
                        out.writeObject("Erreur : division par zéro.");
                        out.flush();
                        return;
                    }
                    resultat = a / b;
                    break;
                default:
                    out.writeObject("Erreur : opérateur invalide.");
                    out.flush();
                    return;
            }
            // Incrémenter le compteur global de façon synchronisée
            int total = ServeurCalculatrice.incrementerCompteur();
            System.out.println("Résultat pour client n°"
                    + numeroClient + " : " + resultat);
            System.out.println("Nombre total d'opérations traitées : "
                    + total);
            // Envoyer le résultat au client
            out.writeObject(Double.valueOf(resultat));
            out.flush();
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Erreur avec client n°"
                    + numeroClient + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Erreur fermeture socket.");
            }
        }
    }
}
