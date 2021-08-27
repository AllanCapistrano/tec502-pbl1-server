package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import models.Patient;

/**
 *
 * @author Allan Capistrano
 */
public class TempClient {

    public static void main(String[] args) {

        String server = "localhost";
        try {
            Socket conn = new Socket(server, 12244);
            Socket conn1 = new Socket(server, 12244);
            Socket conn2 = new Socket(server, 12244);

            ObjectOutputStream output = 
                    new ObjectOutputStream(conn.getOutputStream());
            ObjectOutputStream output1 = 
                    new ObjectOutputStream(conn1.getOutputStream());
            ObjectOutputStream output2 = 
                    new ObjectOutputStream(conn2.getOutputStream());

            output.writeObject("GET /patients");
            output1.writeObject("PUT");
            output2.writeObject("exit");

            ObjectInputStream input = 
                    new ObjectInputStream(conn.getInputStream());

            ArrayList<Patient> received = (ArrayList<Patient>) input.readObject();

            for (int i = 0; i < received.size(); i++) {
                System.out.println("Paciente[" + i + "]:");
                System.out.println(received.get(i).getName());
                System.out.println(received.get(i).getMedicalRecordNumber());
                System.out.println(received.get(i).getBodyTemperatureSensor());
                System.out.println(received.get(i).getRespiratoryFrequencySensor());
                System.out.println(received.get(i).getOxygenationOfTheBlood());
                System.out.println("");
            }

            /* Fechando as conexões. */
            input.close();
            output.close();
            conn.close();

            output1.close();
            conn1.close();

            output2.close();
            conn2.close();
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
