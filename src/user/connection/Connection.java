package user.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

public class Connection {
    //port and socket
    private int port = 50001;
    private Socket socket;

    //messages
    private volatile LinkedList<String> messagesToServer = new LinkedList<String>();
    private volatile LinkedList<String> messagesFromServer = new LinkedList<String>();

    //activity monitor
    private boolean isActive;

    public Connection(boolean inputIsActive) throws Exception {
        this.isActive = inputIsActive;

        socket = new Socket(InetAddress.getLocalHost(), port);

        BufferedReader streamFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter streamToServer = new PrintWriter(socket.getOutputStream(), true);

        //getting messages from server
        Thread GetMessagesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        messagesFromServer.add(streamFromServer.readLine());
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                }
            }
        });
        GetMessagesThread.start();

        //sending messages to server
        Thread SendMessagesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    if (messagesToServer.size() > 0) synchronized (messagesToServer) { streamToServer.println(messagesToServer.pop()); }
                }
            }
        });
        SendMessagesThread.start();
    }

    public synchronized void sendMessageToServer(String message) { this.messagesToServer.add(message); }

    public synchronized String getMessageFromServer() {
        if (this.messagesFromServer.size() > 0) return this.messagesFromServer.pop();
        return null;
    }
}