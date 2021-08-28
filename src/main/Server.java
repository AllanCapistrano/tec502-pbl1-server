package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import models.Patient;

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

        /* Salvando os números das fichas médicas dos pacientes em uma lista. */
        Server.saveMedicalRecordNumbersList(Server.medicalRecordNumbers);

        try {
            /* Definindo a porta do servidor. */
            Server.server = new ServerSocket(Server.PORT);
            Socket connection;
            String received;
            ObjectInputStream input;

            while (true) {
                /* Permite a conexão com o servidor. */
                connection = Server.server.accept();

                input = new ObjectInputStream(connection.getInputStream());

                received = (String) input.readObject();

                if (received.equals("exit")) {
                    System.out.println("> Encerrando o servidor");

                    break;
                } else {
                    Server.processRequests(
                            received,
                            connection
                    );
                }
                input.close(); //Talvez criar método.

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
    private static void saveMedicalRecordNumbersList(
            ArrayList<String> medicalRecordNumbersList
    ) {
        // To Do
    }
    
    /**
     * Processa as requisições que são feitas ao servidor.
     * 
     * @param httpRequest String - Método HTTP enviado pelo Client.
     * @param connection Socket - Conexão com o Client.
     */
    private static void processRequests(
            String httpRequest,
            Socket connection
    ) {
        System.out.println("> Processando a requisição");

        String[] requestLine = httpRequest.split(" ");
        
        switch (requestLine[0]) {
            case "GET":
                System.out.println("Método GET");
                System.out.println("Rota: " + requestLine[1]);

                switch (requestLine[1]) {
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
            case "POST":
                System.out.println("Método POST");
                System.out.println("Rota: " + requestLine[1]);
                
                if (requestLine[1].contains("patients/create/")) {
                    String[] temp = requestLine[1].split("/");
                    
                    System.out.println("id: " + temp[2]);
                    
                }
                
                break;
            case "PUT": //Altera os valores do sensor de um paciente.
                System.out.println("Método PUT");
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
            ObjectOutputStream output = 
                    new ObjectOutputStream(connection.getOutputStream());

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
            ObjectOutputStream output = 
                    new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(medicalRecordNumbers);

            output.close(); //Talvez criar método.
        } catch (IOException e) {
            System.out.println("Erro ao tentar enviar a lista com o número da "
                    + "ficha médica dos pacientes para o Client.");
        }
    }
}
