import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            System.out.println("Server started!");
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
            StopWords stopWords = new StopWords(new File("stop-ru.txt"));

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    // обработка одного подключения
                    out.println("Введите слова для поиска:");
                    String message = in.readLine();
                    String[] words = message.split("\\P{IsAlphabetic}+");
                    List<PageEntry> result = new ArrayList<>();
                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];
                        if (!stopWords.contains(word)) {
                            var resultWord = engine.search(word);
                            result = engine.totalNumber(resultWord, result);
                        }
                    }
                    if (result.isEmpty()) {
                        out.println("Данных слов не найдено!");
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