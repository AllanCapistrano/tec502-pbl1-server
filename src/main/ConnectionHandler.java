package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import models.Patient;
import org.json.JSONObject;

/**
 * Classe que lida com as requisições enviadas para o servidor.
 *
 * @author Allan Capistrano
 */
public class ConnectionHandler implements Runnable {

    private Socket connection;
    private ObjectInputStream input;
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

            System.out.println("> Quantidade de dispositivos conectados: " + Server.patients.size());

            /* TEMPORÁRIO */
            for (int y = 0; y < Server.patients.size(); y++) {
                System.out.println(Server.patients.get(y).getName());
            }

            /* Finalizando as conexões. */
            input.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Erro de Entrada/Saída.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Classe String não foi encontrada.");
        }
    }

    /**
     * Processa as requisições que são feitas ao servidor.
     *
     * @param httpRequest JSONObject - Requisição HTTP.
     */
    private void processRequests(JSONObject httpRequest) {
        System.out.println("> Processando a requisição enviada por "
                + connection.getRemoteSocketAddress());

        switch (httpRequest.getString("method")) {
            case "GET": // Envia dados.
                System.out.println("\tMétodo GET");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                switch (httpRequest.getString("route")) {
                    /* Envia a lista de pacientes para o Client. */
                    case "/patients":
                        System.out.println("> Enviando lista de pacientes");

                        this.sendPatientList();

                        break;
                    /* Envia a lista com os identificadores dos dispositivos 
                        dos pacientes para o Client. */
                    case "/patients/devices":
                        System.out.println("> Enviando os identificadores dos "
                                + "dispositivos dos pacientes");

                        this.sendMedicalRecordNumbersList();
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
     * Envia a lista de pacientes.
     */
    private void sendPatientList() {
        try {
            ObjectOutputStream output
                    = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(Server.patients);

            output.close();
        } catch (IOException ex) {
            System.out.println("Erro ao tentar enviar a lista de pacientes.");
        }
    }

    /**
     * Envia uma lista com os identificadores dos dispositivos dos pacientes.
     */
    private void sendMedicalRecordNumbersList() {
        try {
            ObjectOutputStream output
                    = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(Server.medicalRecordNumbers);

            output.close();
        } catch (IOException e) {
            System.out.println("Erro ao tentar enviar a lista com os "
                    + "identificadores dos dispositivos.");
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
        Patient temp = new Patient(
                patient.getString("name"),
                patient.getFloat("bodyTemperatureSensor"),
                patient.getInt("respiratoryFrequencySensor"),
                patient.getFloat("bloodOxygenationSensor"),
                patient.getInt("bloodPressureSensor"),
                patient.getInt("heartRateSensor"),
                deviceId
        );

        Server.patients.add(temp); //ALTERAR.
    }

    /**
     * Altera os valores dos sensores do dispositivo de um paciente.
     *
     * @param deviceId String - Identificador único do paciente.
     * @param jsonInfo JSONObject - Novos dados.
     */
    private void updatePatientDevice(String deviceId, JSONObject jsonInfo) {
        for (int i = 0; i < Server.patients.size(); i++) {
            if (Server.patients.get(i).getDeviceId().equals(deviceId)) {
                Server.patients.get(i).setName(
                        jsonInfo.getString("name")
                );
                Server.patients.get(i).setBodyTemperature(
                        jsonInfo.getFloat("bodyTemperatureSensor")
                );
                Server.patients.get(i).setRespiratoryFrequency(
                        jsonInfo.getInt("respiratoryFrequencySensor")
                );
                Server.patients.get(i).setBloodOxygenation(
                        jsonInfo.getFloat("bloodOxygenationSensor")
                );
                Server.patients.get(i).setBloodPressure(
                        jsonInfo.getInt("bloodPressureSensor")
                );
                Server.patients.get(i).setHeartRate(
                        jsonInfo.getInt("heartRateSensor")
                );
                Server.patients.get(i).setIsSeriousCondition(
                        Server.patients.get(i).checkPatientCondition()
                );
            }
        }
    }
}
