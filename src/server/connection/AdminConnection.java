package server.connection;

import server.parts.*;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class AdminConnection {
    //messages
    volatile private Vector<String> messagesFromAdmin = new Vector<String>();
    volatile private Vector<String> messagesToAdmin = new Vector<String>();

    //working status
    volatile private boolean adminIsActive = true;

    //info
    Vector<Game> games;

    public AdminConnection(Vector<Game> inputGames, Socket adminSocket) throws IOException {
        this.games = inputGames;

        ConnectionTread adminThread = new ConnectionTread(adminSocket, messagesFromAdmin, messagesToAdmin, adminIsActive);
        adminThread.setDaemon(true);
        adminThread.start();

        System.out.println("Admin connected!");

        Thread ConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (messagesFromAdmin.size() > 0) executeCommand(messagesFromAdmin.remove(0));
                }
            }
        });
        ConnectionThread.setDaemon(true);
        ConnectionThread.start();
    }

    private void executeCommand(String message) {
        System.out.println(message);

        String[] subLine = message.split(" ");
        if (subLine[0].equals("~gtnmsallusrs")) sendPairsOfNames();
        if (subLine[0].equals("~gtmssgs")) sendMessages(subLine);

        subLine = message.split("%%");
        if (subLine[0].equals("~msgfromadmin")) sendMessagesToClients(subLine[1], subLine[2]);
    }

    private void sendPairsOfNames() {
        //"~nmallusers" + " " + "~?#" + "Gosha" + "~?#" + "Misha Misha"
        StringBuilder sb = new StringBuilder();
        sb.append("~nmallusers");
        for (int i = 0; i < games.size(); i++) {
            sb.append("%%");
            sb.append(games.get(i).getFirstClientName());
            sb.append("  and  ");
            sb.append(games.get(i).getSecondClientName());
        }
        messagesToAdmin.add(sb.toString());
    }

    private void sendMessages(String[] subLine) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        String title = sb.toString();
        //title = "Name 1  and  Name 2"

        for (int i = 0; i < games.size(); i++) {
            StringBuilder titleSb = new StringBuilder();
            titleSb.append(games.get(i).getFirstClientName());
            titleSb.append("  and  ");
            titleSb.append(games.get(i).getSecondClientName());

            if (title.equals(titleSb.toString())) {
                //send messages of i-game
                Vector<String> messages = games.get(i).getChat();
                StringBuilder tempSb = new StringBuilder();
                tempSb.append("~allmessages");
                for (int j = 0; j < messages.size(); j++) {
                    tempSb.append("%%");
                    tempSb.append(messages.get(j));
                }
                System.out.println(tempSb.toString());
                messagesToAdmin.add(tempSb.toString());
            }
        }
    }

    private void sendMessagesToClients(String clients, String message) {
        System.out.println("clients: (" + clients + ") mess: (" + message + ")");
        for (int i = 0; i < games.size(); i++) {
            StringBuilder titleSb = new StringBuilder();
            titleSb.append(games.get(i).getFirstClientName());
            titleSb.append("  and  ");
            titleSb.append(games.get(i).getSecondClientName());

            if (clients.equals(titleSb.toString())) {
                games.get(i).addChatMessage_Admin(message);
            }
        }
    }
}