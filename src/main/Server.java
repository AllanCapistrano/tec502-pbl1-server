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

        System.out.println("> Iniciando o servidor...");
        System.out.println("> Criando os pacientes...");

        /* Criando os pacientes */
        Server.createPatients(patients);

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
                    System.out.println("> Encerrano servidor...");

                    break;
                } else {
                    Server.processRequests(received, connection, patients);
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
     * @param patients - Lista na qual os pacientes serão
     */
    private static void createPatients(ArrayList<Patient> patients) {
        IdGenerate idGen = new IdGenerate(12, ".");

        /* Criando e adicionando 10 pacientes. */
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
     * Processa as requisições que são feitas ao servidor.
     *
     * @param method String - Método HTTP enviado pelo Client.
     */
    private static void processRequests(String method, Socket connection, ArrayList<Patient> patients) {
        System.out.println("> Processando a requisição...");
        switch (method) {
            case "GET": //Envia a lista de pacientes para o Client.
                System.out.println("Método GET");
                
                Server.sendPatientList(connection, patients);

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
}
