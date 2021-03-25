package threaded;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Program implementujący wielowątkowy serwer nasłuchujący na porcie 8189
 * i wysyłający echo informacji otrzymanej od klientów.
 *
 * @author Cay Horstmann
 * @version 1.23 2018-03-17
 */
public class ThreadedEchoServer {
    public static void main(String[] args) {
        try (var s = new ServerSocket(8189)) {
            int i = 1;

            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                Runnable r = new ThreadedEchoHandler(incoming);
                var t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Klasa obsługująca komunikację z pojedynczym klientem
 */
class ThreadedEchoHandler implements Runnable {
    private Socket incoming;

    public ThreadedEchoHandler(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    @Override
    public void run() {
        try (InputStream inStream = incoming.getInputStream();
             OutputStream outStream = incoming.getOutputStream();
             var in = new Scanner(inStream, StandardCharsets.UTF_8);
             var out = new PrintWriter(
                     new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true /*autoFlush*/
             )) {
            out.println("Witaj! Wpisz BYE, by zakończcyć.");

            //Wyświetla echo
            var done = false;
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println("Echo: " + line);
                if (line.trim().equals("BYE"))
                    done = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
