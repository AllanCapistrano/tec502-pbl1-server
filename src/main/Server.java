package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import models.Patient;
import org.json.JSONObject;

/**
 *
 * @author Allan Capistrano
 */
public class Server {

    private static final int PORT = 12244;
    private static ServerSocket server;
    private static final ArrayList<Patient> patients = new ArrayList<>();
    private static final ArrayList<String> medicalRecordNumbers = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("> Iniciando o servidor");

        try {
            /* Definindo a porta do servidor. */
            Server.server = new ServerSocket(Server.PORT);
            Socket connection;
            JSONObject received;
            ObjectInputStream input;

            while (true) {
                /* Permite a conexão com o servidor. */
                connection = Server.server.accept();

                input = new ObjectInputStream(connection.getInputStream());

                received = (JSONObject) input.readObject();

                if (received.has("exit") && received.getBoolean("exit")) {
                    System.out.println("> Encerrando o servidor");

                    break;
                } else {
                    Server.processRequests(
                            received,
                            connection
                    );
                }
                input.close(); //Talvez criar método.

                System.out.println("Qtd pacientes: " + Server.patients.size());
                System.out.println("Nome: " + Server.patients.get(0).getName());
                System.out.println("Temparatura corporal: " + Server.patients.get(0).getBodyTemperature());
                System.out.println("Frequência respiratória: " + Server.patients.get(0).getRespiratoryFrequency());
                System.out.println("Oxigenação do sangue: " + Server.patients.get(0).getBloodOxygenation());
                System.out.println("Pressão arterial: " + Server.patients.get(0).getBloodPressure());
                System.out.println("Frequência cardíaca: " + Server.patients.get(0).getHeartRate());
                System.out.println("Está em estado grave? " + Server.patients.get(0).isIsSeriousCondition());
                System.out.println("");

                /* Finalizando a conexão com o Client. */
                Server.closeClientConnection(connection);
            }

            /* Finalizando as conexões. */
            Server.closeAllConnections(connection, Server.server);

            System.out.println("> Servidor finalizado!");
        } catch (BindException e) {
            closeServer(Server.server);

            System.out.println("A porta já está em uso.");
        } catch (IOException e) {
            closeServer(Server.server);

            System.out.println("Erro de Entrada/Saída.");
        } catch (ClassNotFoundException ex) {
            closeServer(Server.server);

            System.out.println("Classe String não foi encontrada.");
        }

        closeServer(Server.server);
    }

    /**
     * Salva o número da ficha médica de todos os pacientes em uma lista.
     *
     * @param medicalRecordNumbersList ArrayList<String> - Lista que irá
     * armazenar os números das fichas médicas.
     */
    private static void addPatient(JSONObject patient, String id) {
        Patient temp = new Patient(
                patient.getString("name"),
                patient.getFloat("bodyTemperatureSensor"),
                patient.getInt("respiratoryFrequencySensor"),
                patient.getFloat("bloodOxygenationSensor"),
                patient.getInt("bloodPressureSensor"),
                patient.getInt("heartRateSensor"),
                id
        );

        Server.patients.add(temp);
    }

    /**
     * Processa as requisições que são feitas ao servidor.
     *
     * @param httpRequest String - Método HTTP enviado pelo Client.
     * @param connection Socket - Conexão com o Client.
     */
    private static void processRequests(
            JSONObject httpRequest,
            Socket connection
    ) {
        System.out.println("> Processando a requisição");

        System.out.println(httpRequest);

        switch (httpRequest.getString("method")) {
            case "GET":
                System.out.println("\tMétodo GET");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                switch (httpRequest.getString("route")) {
                    /* Envia a lista de pacientes para o Client. */
                    case "/patients":
                        System.out.println("> Enviando lista de pacientes");

                        Server.sendPatientList(connection, Server.patients);

                        break;
                    /* Envia a lista com o número da ficha médica dos pacientes 
                        para o Client. */
                    case "/patients/medical-record-numbers":
                        System.out.println("> Enviando o número da ficha "
                                + "médica dos pacientes");

                        Server.sendMedicalRecordNumbersList(
                                connection,
                                Server.medicalRecordNumbers
                        );
                }

                break;
            case "POST": // Cria e adiciona o dispositivo do pacinte na lista.
                System.out.println("\tMétodo POST");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                if (httpRequest.getString("route").contains("patients/create/")) {
                    String[] temp = httpRequest.getString("route").split("/");

                    Server.addPatient(httpRequest.getJSONObject("body"), temp[2]);
                }

                break;
            case "PUT": // Altera os valores do sensor de um paciente.
                System.out.println("\tMétodo PUT");
                System.out.println("\t\tRota: " + httpRequest.getString("route"));

                if (httpRequest.getString("route").contains("patients/edit/")) {
                    String[] temp = httpRequest.getString("route").split("/");

                    Server.updatePatient(temp[2], httpRequest.getJSONObject("body"));
                }

                break;
        }
        System.out.println("");
    }

    /**
     * Finaliza o servidor.
     *
     * @param server ServerSocket - Servidor que se deseja finalizar.
     */
    private static void closeServer(ServerSocket server) {
        try {
            server.close();
        } catch (IOException ex) {
            System.out.println("Erro ao tentar encerrar o servidor.");
        }
    }

    /**
     * Finaliza a conexão com o Client.
     *
     * @param connection Socket - Conexão que se deseja finalizar.
     */
    private static void closeClientConnection(Socket connection) {
        try {
            connection.close();
        } catch (IOException ex) {
            System.out.println("Erro ao tentar finalizar a conexão com o "
                    + "Client.");
        }
    }

    /**
     * Finaliza todas as conexões as conexões.
     *
     * @param connection Socket - Conexão que é realizada com o Client.
     * @param server ServerSocket - Servidor que será finalizado
     */
    private static void closeAllConnections(
            Socket connection,
            ServerSocket server
    ) {
        Server.closeClientConnection(connection);
        Server.closeServer(server);
    }

    /**
     * Envia a lista de pacientes para o Client.
     *
     * @param connection Socket- Conexão que é realizada com o Client.
     * @param patients ArrayList<Patient> - Lista de pacientes.
     */
    private static void sendPatientList(
            Socket connection,
            ArrayList<Patient> patients
    ) {
        try {
            ObjectOutputStream output
                    = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(patients);

            output.close(); //Talvez criar método.
        } catch (IOException ex) {
            System.out.println("Erro ao tentar enviar a lista de pacientes "
                    + "para o Client.");
        }
    }

    /**
     * Envia uma lista com o número da ficha médica dos pacientes para o Client.
     *
     * @param connection Socket- Conexão que é realizada com o Client.
     * @param medicalRecordNumbers ArrayList<String> - Lista que contém o número
     * da ficha médica dos pacientes.
     */
    private static void sendMedicalRecordNumbersList(
            Socket connection,
            ArrayList<String> medicalRecordNumbers
    ) {
        try {
            ObjectOutputStream output
                    = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(medicalRecordNumbers);

            output.close(); //Talvez criar método.
        } catch (IOException e) {
            System.out.println("Erro ao tentar enviar a lista com o número da "
                    + "ficha médica dos pacientes para o Client.");
        }
    }

    /**
     * Altera os dados dos sensores de um paciente.
     * 
     * @param id String - Identificador único do paciente.
     * @param jsonInfo JSONObject - Novos dados.
     */
    private static void updatePatient(String id, JSONObject jsonInfo) {
        for (int i = 0; i < Server.patients.size(); i++) {
            if (Server.patients.get(i).getMedicalRecordNumber().equals(id)) {
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
