import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try (Socket clientSocket = new Socket("127.0.0.1", 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()))) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(in.readLine());
            out.println(scanner.nextLine());
            String s1 = in.readLine();
            while (s1 != null) {
           System.out.println(s1);
            s1 = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
