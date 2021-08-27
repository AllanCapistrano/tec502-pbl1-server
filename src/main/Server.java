package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import models.Patient;
import utils.IdGenerate;

/**
 *
 * @author Allan Capistrano
 */
public class Server {

    public static void main(String[] args) {

        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<String> medicalRecordNumbers = new ArrayList<>();
        IdGenerate idGen = new IdGenerate(12, ".");

        System.out.println("> Iniciando o servidor...");
        System.out.println("> Criando os pacientes...");

        /* Criando os pacientes */
        Server.createPatients(patients, idGen);

        /* Salvando os números das fichas médicas dos pacientes em uma lista. */
        Server.saveMedicalRecordNumbersList(medicalRecordNumbers, idGen);

        try {
            /* Definindo a porta do servidor. */
            ServerSocket server = new ServerSocket(12244);
            Socket connection;
            String received;
            ObjectInputStream input;

            while (true) {
                /* Permite a conexão com o servidor. */
                connection = server.accept();

                input = new ObjectInputStream(connection.getInputStream());

                received = (String) input.readObject();

                if (received.equals("exit")) {
                    System.out.println("> Encerrando o servidor...");

                    break;
                } else {
                    Server.processRequests(received, connection, patients, medicalRecordNumbers);
                }
                input.close(); //Talvez criar método.

                /* Finalizando a conexão com o Client. */
                Server.closeClientConnection(connection);

            }

            /* Finalizando as conexões. */
            Server.closeAllConnections(connection, server);

            System.out.println("> Servidor finalizado!");
        } catch (BindException e) {
            System.out.println("A porta já está em uso.");
        } catch (IOException e) {
            System.out.println("Erro de Entrada/Saída.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Classe String não foi encontrada.");
        }
    }

    /**
     * Cria e adiciona os pacientes em uma lista.
     *
     * @param patients ArrayList<Patient> - Lista na qual os pacientes serão
     * @param idGen IdGenerate - Gerador de identificadores.
     */
    private static void createPatients(ArrayList<Patient> patients, IdGenerate idGen) {
        /* Criando e adicionando 10 pacientes na lista de pacientes. */
        patients.add(new Patient("Carlos", idGen));
        patients.add(new Patient("Manuela", idGen));
        patients.add(new Patient("Isaac", idGen));
        patients.add(new Patient("Lorena", idGen));
        patients.add(new Patient("Heitor", idGen));
        patients.add(new Patient("Maria Eduarda", idGen));
        patients.add(new Patient("Davi", idGen));
        patients.add(new Patient("Maite", idGen));
        patients.add(new Patient("Anthony", idGen));
        patients.add(new Patient("Cecília", idGen));
    }

    /**
     * Salva o número da ficha médica de todos os pacientes em uma lista.
     *
     * @param medicalRecordNumbersList ArrayList<String> - Lista que irá
     * armazenar os números das fichas médicas.
     * @param idGen - Gerador de identificadores utilizado para a criação dos
     * pacientes.
     */
    private static void saveMedicalRecordNumbersList(ArrayList<String> medicalRecordNumbersList, IdGenerate idGen) {
        medicalRecordNumbersList.addAll(idGen.list());
    }

    /**
     * Processa as requisições que são feitas ao servidor.
     *
     * @param httpRequest String - Método HTTP enviado pelo Client.
     */
    private static void processRequests(
            String httpRequest,
            Socket connection,
            ArrayList<Patient> patients,
            ArrayList<String> medicalRecordNumbers
    ) {
        System.out.println("> Processando a requisição...");

        String[] requestLine = httpRequest.split(" ");

        switch (requestLine[0]) {
            case "GET":
                System.out.println("Método GET");
                System.out.println("Rota: " + requestLine[1]);

                switch (requestLine[1]) {
                    /* Envia a lista de pacientes para o Client. */
                    case "/patients":
                        System.out.println("> Enviando lista de pacientes");

                        Server.sendPatientList(connection, patients);

                        break;
                    /* Envia a lista com o número da ficha médica dos pacientes
                        para o Client. */
                    case "/patients/medical-record-numbers":
                        System.out.println("> Enviando o número da ficha médica dos pacientes");

                        Server.sendMedicalRecordNumbersList(connection, medicalRecordNumbers);
                }

                break;
            case "POST": //Não sei se precisa.
                System.out.println("Método POST");
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
            System.out.println("Erro ao tentar finalizar a conexão com o Client.");
        }
    }

    /**
     * Finaliza todas as conexões as conexões.
     *
     * @param connection Socket - Conexão que é realizada com o Client.
     * @param server ServerSocket - Servidor que será finalizado
     */
    private static void closeAllConnections(Socket connection, ServerSocket server) {
        Server.closeClientConnection(connection);
        Server.closeServer(server);
    }

    /**
     * Envia a lista de pacientes para o Client.
     *
     * @param connection Socket- Conexão que é realizada com o Client.
     * @param patients ArrayList<Patient> - Lista de pacientes.
     */
    private static void sendPatientList(Socket connection, ArrayList<Patient> patients) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(patients);

            output.close(); //Talvez criar método.
        } catch (IOException ex) {
            System.out.println("Erro ao tentar enviar a lista de pacientes para o Client.");
        }
    }

    /**
     * Envia uma lista com o número da ficha médica dos pacientes para o Client.
     *
     * @param connection Socket- Conexão que é realizada com o Client.
     * @param medicalRecordNumbers ArrayList<String> - Lista que contém o número
     * da ficha médica dos pacientes.
     */
    private static void sendMedicalRecordNumbersList(Socket connection, ArrayList<String> medicalRecordNumbers) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(medicalRecordNumbers);

            output.close(); //Talvez criar método.
        } catch (IOException e) {
            System.out.println("Erro ao tentar enviar a lista com o número da "
                    + "ficha médica dos pacientes para o Client.");
        }
    }
}
