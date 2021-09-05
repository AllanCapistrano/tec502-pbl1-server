package main;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.PatientDevice;

/**
 * Classe do Server.
 *
 * @author Allan Capistrano
 */
public class Server {

    private static ServerSocket server;
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 12244;
    
    /* Test */
    private static DatagramSocket serverUdp;
    private static final int PORT_UDP = 15000;

    private static final ArrayList<PatientDevice> patientDevices = new ArrayList<>();
    private static final ArrayList<String> deviceIds = new ArrayList<>();

    private static ArrayList<ConnectionHandler> connHandler = new ArrayList<>();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    
    /* Tests */
    private static ArrayList<ConnectionHandlerUdp> connHandlerUdp = new ArrayList<>();
    private static ExecutorService poolUdp = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        System.out.println("> Iniciando o servidor");
        System.out.println("> Aguardando conexão");

        try {
            /* Definindo a porta do servidor. */
            Server.server = new ServerSocket();
            InetAddress addr = InetAddress.getByName(IP_ADDRESS);
            InetSocketAddress inetSocket = new InetSocketAddress(addr, PORT);
            server.bind(inetSocket);
            
            serverUdp = new DatagramSocket(PORT_UDP, addr);

            while (true) {
                /* Serviço que lida com as requisições utilizando threads. */
                ConnectionHandler connectionThread = new ConnectionHandler(server.accept());
                connHandler.add(connectionThread);
                
                /* Test UDP */
                ConnectionHandlerUdp connectionThreadUdp = new ConnectionHandlerUdp(serverUdp);
                connHandlerUdp.add(connectionThreadUdp);
                
                /* Test poll UDP */
                poolUdp.execute(connectionThreadUdp);

                /* Executando as threads. */
                pool.execute(connectionThread);
                
                
            }

        } catch (BindException be) {
            System.err.println("A porta já está em uso.");
            System.out.println(be);
        } catch (IOException ioe) {
            System.err.println("Erro de Entrada/Saída.");
            System.out.println(ioe);
        }
    }

    /**
     * Adiciona o dispositivo de um paciente na lista.
     *
     * @param patient PatientDevice - Dispositivo a ser adicionado.
     */
    public static void addPatientDevice(PatientDevice patient) {
        patientDevices.add(patient);
    }

    /**
     * Retorna a lista de dispositivos dos pacientes.
     *
     * @return ArrayList<Patient>
     */
    public static ArrayList<PatientDevice> getPatientDevicesList() {
        return patientDevices;
    }

    /**
     * Retorna a lista de identificadores dos dispositivos dos pacientes.
     *
     * @return ArrayList<String>
     */
    public static ArrayList<String> getDeviceIdsList() {
        return deviceIds;
    }

    /**
     * Retorna o tamanho da lista de dispositivos dos pacientes.
     *
     * @return int
     */
    public static int patientDeviceListSize() {
        return patientDevices.size();
    }

    /**
     * Retorna o tamanho da lista de identificadores dos dispositivos dos
     * pacientes.
     *
     * @return int
     */
    public static int deviceIdListSize() {
        return deviceIds.size();
    }

    /**
     * Retorna o dispositivo de um paciente com base na sua posição na lista.
     *
     * @param index int - Posição na lista.
     * @return PatientDevice
     */
    public static PatientDevice getPatientDevice(int index) {
        return patientDevices.get(index);
    }

    /**
     * Retorna o identificador do dispositivo de um paciente com base na sua
     * posição na lista.
     *
     * @param index int - Posição na lista.
     * @return String
     */
    public static String getDeviceId(int index) {
        return deviceIds.get(index);
    }

}
