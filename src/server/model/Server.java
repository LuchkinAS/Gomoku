/*
    Сетевая игра в крестики-нолики на поле 10x10.
    Поддержка нескольких досок на сервере.
    При соединении с сервером пользователь выбирает: начать новую доску или присоединиться к существующей,
    если на ней только один игрок.
    Программа может быть как с текстовым, так и с графическим интерфейсом.
*/

package server.model;

import server.connection.AdminConnection;
import server.connection.Connection;
import server.parts.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    static volatile Vector<Game> games = new Vector<Game>();

    public static void main(String[] args) throws IOException {
        System.out.println("Gomoku server is running!");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(50001);
            //serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException e) {
            //System.out.println("Couldn't listen to port " + args[0]);
            System.exit(-1);
        }

        System.out.println("Waiting for the first client...");
        while(true) {
            if (serverSocket != null) {
                //without admin
                Socket firstClientSocket = null;
                String firstKind = "";
                while (!firstKind.equals("~clientcnctn")) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader streamFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    firstKind = streamFromClient.readLine().trim();
                    if (firstKind.equals("~stpadmn")) {
                        //set up admin
                        new AdminConnection(games, clientSocket);
                    }
                    if (firstKind.equals("~clientbt")) {
                        //set up game with bot
                        Game botGame = new Game(clientSocket);
                        games.add(botGame);
                        new Connection(games.lastElement());
                    }
                    if (firstKind.equals("~clientcnctn")) {
                        firstClientSocket = clientSocket;
                    }
                }
                Socket secondClientSocket = null;
                String secondKind = "";
                while (!secondKind.equals("~clientcnctn")) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader streamFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    secondKind = streamFromClient.readLine().trim();
                    if (secondKind.equals("~stpadmn")) {
                        //set up admin
                        new AdminConnection(games, clientSocket);
                    }
                    if (secondKind.equals("~clientbt")) {
                        //set up game with bot
                        Game botGame = new Game(clientSocket);
                        games.add(botGame);
                        new Connection(games.lastElement());
                    }
                    if (secondKind.equals("~clientcnctn")) {
                        secondClientSocket = clientSocket;
                    }
                }
                Game game = new Game(firstClientSocket, secondClientSocket);
                games.add(game);
                new Connection(games.lastElement());
            }
        }
    }
}