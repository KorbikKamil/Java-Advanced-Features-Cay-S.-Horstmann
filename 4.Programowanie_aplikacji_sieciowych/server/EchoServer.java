package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Program implementujący prosty serwer nasłuchujący na porcie 8189
 * i wysyłający echo informacji otrzymanej od klienta.
 *
 * @author Cay Horstmann
 * @version 1.22 2018-03-17
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        //Tworzy gniazdo serwera
        try (var s = new ServerSocket(8189)) {
            try (Socket incoming = s.accept()) {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                try (var in = new Scanner(inStream, StandardCharsets.UTF_8)) {
                    var out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true /* autoFlush */);
                    out.println("Witaj! Wpisz BYE, by zakończyć.");

                    //Wysyła echo informacji otrzymanej od klienta
                    var done = false;
                    while (!done && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Echo: " + line);
                        if (line.trim().equals("BYE")) done = true;
                    }
                }
            }
        }
    }
}
