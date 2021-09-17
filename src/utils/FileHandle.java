package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para manipulação de arquivos.
 *
 * @author Allan Capistrano
 */
public class FileHandle {

    /**
     * Escreve um texto no arquivo.
     *
     * @param path String - Caminho do arquivo.
     * @param text String - Texto que será escrito.
     * @param append boolean - Controle para adição de novas linhas ao final do
     * arquivo
     */
    public static void write(String path, String text, boolean append) {
        File file = new File(path);

        try {
            /* Caso o arquivo não exista, cria um novo vazio. */
            if (!file.exists()) {
                file.createNewFile();
            }

            /* true para realizar append no arquivo. */
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));

            writer.append(text + "\n");

            writer.close();
        } catch (IOException ioe) {
            System.err.println("Não foi possível escrever no arquivo.");
            System.out.println(ioe);
        }

    }
}
