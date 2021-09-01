package main;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.Patient;

/**
 * Classe do servidor.
 *
 * @author Allan Capistrano
 */
public class Server {

    private static final int PORT = 12244;
    private static ServerSocket server;

    private static final ArrayList<Patient> patients = new ArrayList<>();
    private static final ArrayList<String> medicalRecordNumbers = new ArrayList<>();

    private static ArrayList<ConnectionHandler> connHandler = new ArrayList<>();
    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        System.out.println("> Iniciando o servidor");
        System.out.println("> Aguardando conexão");

        try {
            /* Definindo a porta do servidor. */
            Server.server = new ServerSocket(Server.PORT);

            while (true) {
                /* Serviço que lida com as requisições utilizando threads. */
                ConnectionHandler connectionThread = new ConnectionHandler(server.accept());
                connHandler.add(connectionThread);

                /* Executando as threads. */
                pool.execute(connectionThread);
            }

        } catch (BindException e) {
            System.out.println("A porta já está em uso.");
        } catch (IOException e) {
            System.out.println("Erro de Entrada/Saída.");
        }
    }

    /**
     * Adiciona o dispositivo de um paciente na lista.
     *
     * @param patient Patient - Dispositivo a ser adicionado.
     */
    public static void addPatient(Patient patient) {
        patients.add(patient);
    }

    /**
     * Retorna a lista de dispositivos dos pacientes.
     *
     * @return ArrayList<Patient>
     */
    public static ArrayList<Patient> getPatientsList() {
        return patients;
    }

    /**
     * Retorna a lista de identificadores dos dispositivos dos pacientes.
     *
     * @return ArrayList<String>
     */
    public static ArrayList<String> getDeviceIdsList() {
        return medicalRecordNumbers;
    }

    /**
     * Retorna o tamanho da lista de dispositivos dos pacientes.
     *
     * @return int
     */
    public static int patientsListSize() {
        return patients.size();
    }

    /**
     * Retorna o tamanho da lista de identificadores dos dispositivos dos
     * pacientes.
     *
     * @return int
     */
    public static int deviceIdsListSize() {
        return medicalRecordNumbers.size();
    }

    /**
     * Retorna o dispositivo de um paciente com base na sua posição na lista.
     *
     * @param index int - Posição na lista.
     * @return Patient
     */
    public static Patient getPatient(int index) {
        return patients.get(index);
    }

    /**
     * Retorna o identificador do dispositivo de um paciente com base na sua
     * posição na lista.
     *
     * @param index int - Posição na lista.
     * @return String
     */
    public static String getDeviceId(int index) {
        return medicalRecordNumbers.get(index);
    }

}
