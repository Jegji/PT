package sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            Socket socket = new Socket("localhost",2137);

            // Tworzenie strumieni danych do komunikacji z serwerem
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Pobranie liczby od użytkownika
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Podaj liczbę: ");
            int n = 1;
            while (n!=0){
                n = Integer.parseInt(reader.readLine());
                out.writeObject(n);
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
