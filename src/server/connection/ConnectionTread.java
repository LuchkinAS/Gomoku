package server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ConnectionTread extends Thread {
    private Socket clientSocket;
    private Vector<String> messagesFromClient;
    private Vector<String> messagesToClient;
    private boolean clientIsActive;

    public ConnectionTread(Socket inputClientSocket, Vector<String> inputMessagesFromClient, Vector<String> inputMessagesToClient, boolean inputClientIsActive) {
        this.clientSocket = inputClientSocket;
        this.messagesFromClient = inputMessagesFromClient;
        this.messagesToClient = inputMessagesToClient;
        this.clientIsActive = inputClientIsActive;
    }

    public void run() {
        try {
            BufferedReader streamFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter streamToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            //getting messages from client
            Thread GetMessagesThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(clientIsActive) {
                        try {
                            String messageFromClient = streamFromClient.readLine();
                            if (messageFromClient != null) messagesFromClient.add(messageFromClient);
                        } catch (IOException e) {
                            System.out.println("While getting messages from client: " + e.toString());
                        }
                    }
                }
            });
            //GetMessagesThread.setDaemon(true);
            GetMessagesThread.start();

            //sending messages to client
            Thread SendMessagesThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (clientIsActive) {
                        if (messagesToClient.size() > 0) {
                            streamToClient.println(messagesToClient.remove(0));
                        }
                    }
                }
            });
            //SendMessagesThread.setDaemon(true);
            SendMessagesThread.start();

            while (clientIsActive) { }

            System.out.println("Connection closed!");
            streamFromClient.close();
            streamToClient.close();
            clientSocket.close();
        }catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
