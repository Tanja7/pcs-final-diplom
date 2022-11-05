import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Server started!");
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    // обработка одного подключения
                    out.println("Введите слово для поиска:");
                    String word = in.readLine();
                    var result = engine.search(word);
                    if (result.isEmpty()) {
                        out.println("Данного слова не найдено!");
                    } else {
                        out.println(result);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
