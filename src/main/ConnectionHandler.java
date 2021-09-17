package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import models.PatientDevice;
import org.json.JSONObject;
import utils.FileHandle;
import utils.PatientToJson;

/**
 * Classe que lida com as requisições enviadas para o servidor.
 *
 * @author Allan Capistrano
 */
public class ConnectionHandler implements Runnable {

    private static final String FILE_PATH = "patients.json";
    
    private final Socket connection;
    private final ObjectInputStream input;
    private JSONObject received;

    /**
     * Método construtor.
     *
     * @param connection Socket - Conexão com o Client.
     * @throws IOException
     */
    public ConnectionHandler(Socket connection) throws IOException {
        this.connection = connection;

        this.input = new ObjectInputStream(connection.getInputStream());
    }

    @Override
    public void run() {
        try {
            /* Requisição recebida. */
            this.received = (JSONObject) this.input.readObject();

            /* Processandos a requisição. */
            this.processRequests(this.received);

            System.out.println("> Quantidade de dispositivos conectados: "
                    + Server.patientDeviceListSize());

            /* Finalizando as conexões. */
            input.close();
            connection.close();
        } catch (IOException ioe) {
            System.err.println("Erro de Entrada/Saída.");
            System.out.println(ioe);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Classe JSONObject não foi encontrada.");
            System.out.println(cnfe);
        }
    }

    /**
     * Processa as requisições que são enviados ao servidor.
     *
     * @param httpRequest JSONObject - Requisição HTTP.
     */
    private void processRequests(JSONObject httpRequest) {
        System.out.println("> Processando a requisição");

        switch (httpRequest.getString("method")) {
            case "GET": // Envia dados.
                System.out.println("\tMétodo GET");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                switch (httpRequest.getString("route")) {
                    /* Envia a lista dos dispositivos dos pacientes. */
                    case "/patients":
                        System.out.println("> Enviando lista de pacientes");

                        this.sendPatientDevicesList();

                        break;
                }

                break;
            case "POST": // Cria e adiciona o dispositivo do pacinte na lista.
                System.out.println("\tMétodo POST");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                if (httpRequest.getString("route").contains("patients/create/")) {
                    String[] temp = httpRequest.getString("route").split("/");

                    this.addPatientDevice(temp[2], httpRequest.getJSONObject("body"));
                }

                break;
            case "PUT": // Altera os valores do sensor de um paciente.
                System.out.println("\tMétodo PUT");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                if (httpRequest.getString("route").contains("patients/edit/")) {
                    String[] temp = httpRequest.getString("route").split("/");

                    this.updatePatientDevice(temp[2], httpRequest.getJSONObject("body"));
                }

                break;
        }
        System.out.println("");
    }

    /**
     * Envia a lista dos dispositivos dos pacientes.
     */
    private void sendPatientDevicesList() {
        try {
            ObjectOutputStream output
                    = new ObjectOutputStream(connection.getOutputStream());

            /* Colocando a lista de pacientes no formato JSON. */
            JSONObject json
                    = PatientToJson.handle(Server.getPatientDevicesList(), true);

            output.writeObject(json);

            output.close();
        } catch (IOException ioe) {
            System.err.println("Erro ao tentar enviar a lista dos dispositivos "
                    + "dos pacientes.");
            System.out.println(ioe);
        }
    }

    /**
     * Adiciona o dispositivo do paciente na lista de dispositivos.
     *
     * @param patient JSONObject - Informações do dispositivo do paciente no
     * formato JSON.
     * @param deviceId String - Identificador do dispositivo.
     */
    private void addPatientDevice(String deviceId, JSONObject patient) {
        PatientDevice temp = new PatientDevice(
                patient.getString("name"),
                patient.getFloat("bodyTemperatureSensor"),
                patient.getInt("respiratoryFrequencySensor"),
                patient.getFloat("bloodOxygenationSensor"),
                patient.getInt("bloodPressureSensor"),
                patient.getInt("heartRateSensor"),
                deviceId
        );

        Server.addPatientDevice(temp);

        /* Colocando a lista de pacientes no formato JSON. */
        JSONObject json
                = PatientToJson.handle(Server.getPatientDevicesList(), false);

        /* Escrevendo no arquivo. */
        FileHandle.write(FILE_PATH, json.toString(), false);
    }

    /**
     * Atualiza os valores dos sensores do dispositivo de um paciente.
     *
     * @param deviceId String - Identificador único do dispositivo do paciente.
     * @param jsonInfo JSONObject - Novos dados.
     */
    private void updatePatientDevice(String deviceId, JSONObject jsonInfo) {
        int i;

        for (i = 0; i < Server.patientDeviceListSize(); i++) {
            if (Server.getPatientDevice(i).getDeviceId().equals(deviceId)) {
                break;
            }
        }

        if (i == Server.patientDeviceListSize()) {
            this.addPatientDevice(deviceId, jsonInfo);
        } else {
            Server.getPatientDevice(i).setName(jsonInfo.getString("name"));
            Server.getPatientDevice(i).setBodyTemperature(
                    jsonInfo.getFloat("bodyTemperatureSensor")
            );
            Server.getPatientDevice(i).setRespiratoryFrequency(
                    jsonInfo.getInt("respiratoryFrequencySensor")
            );
            Server.getPatientDevice(i).setBloodOxygenation(
                    jsonInfo.getFloat("bloodOxygenationSensor")
            );
            Server.getPatientDevice(i).setBloodPressure(
                    jsonInfo.getInt("bloodPressureSensor")
            );
            Server.getPatientDevice(i).setHeartRate(
                    jsonInfo.getInt("heartRateSensor")
            );
            Server.getPatientDevice(i).setIsSeriousCondition(
                    Server.getPatientDevice(i).checkPatientCondition()
            );
        }

        /* Colocando a lista de pacientes no formato JSON. */
        JSONObject json
                = PatientToJson.handle(Server.getPatientDevicesList(), false);

        /* Escrevendo no arquivo. */
        FileHandle.write(FILE_PATH, json.toString(), false);
    }
}
