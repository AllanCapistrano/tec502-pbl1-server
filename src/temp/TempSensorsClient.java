package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Allan Capistrano
 */
public class TempSensorsClient {

    public static void main(String[] args) {

        String server = "localhost";
        try {
            Socket conn = new Socket(server, 12244);
            Socket conn1 = new Socket(server, 12244);

            ObjectOutputStream output = 
                    new ObjectOutputStream(conn.getOutputStream());
            ObjectOutputStream output1 = 
                    new ObjectOutputStream(conn1.getOutputStream());

            output.writeObject("GET /patients/medical-record-numbers");
            output1.writeObject("exit");

            ObjectInputStream input = new ObjectInputStream(conn.getInputStream());

            ArrayList<String> received = (ArrayList<String>) input.readObject();

            for (int i = 0; i < received.size(); i++) {
                System.out.println("Paciente[" + i + "]:");
                System.out.println(received.get(i));
                System.out.println("");
            }

            /* Fechando as conexões. */
            input.close();
            output.close();
            conn.close();

            output1.close();
            conn1.close();
        } catch (UnknownHostException e) {
            System.out.println("Servidor não encontrado ou está fora do ar.");
        } catch (IOException e) {
            System.out.println("Erro de Entrada/Saída.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Classe ArrayList ou classe Patient não foi "
                    + "encontrada.");
        }
    }
}
