package main;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import models.PatientDevice;
import org.json.JSONObject;

/**
 *
 * @author Allan Capistrano
 */
public class ConnectionHandlerUdp implements Runnable {

    private final DatagramSocket server;

    public ConnectionHandlerUdp(DatagramSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            byte[] incomingData = new byte[5120];
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);

            server.receive(incomingPacket);

            int byteCount = incomingPacket.getLength();
            
            ByteArrayInputStream byteStream = new ByteArrayInputStream(incomingData);
            ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(byteStream));

            PatientDevice json = (PatientDevice) input.readObject();

            // Aqui chama o método de processar a requisição.
            System.out.println(json.getName());
            System.out.println(json.getDeviceId());

            input.close();
//            server.close();
        } catch (IOException ioe) {
            System.err.println("Erro de Entrada/Saída.");
            System.out.println(ioe);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Classe JSONObject não foi encontrada. AAA"); // O PROBLEMA ESTÁ AQUI?!
            System.out.println(cnfe);
        }
    }

}
