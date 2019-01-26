package server.parts;

import java.net.Socket;
import java.util.Vector;

public class Game {
    //connection
    private Socket firstClientSocket;
    private Socket secondClientSocket;

    //users names
    private String firstClientName = "";
    private String secondClientName = "";

    //desk
    private Desk desk = new Desk();

    //bot
    volatile private boolean playingWithBot = false;

    //messages
    Vector<String> chat = new Vector<String>();
    Vector<String> sendBoth = new Vector<String>();

    public Game(Socket inputFirstClientSocket, Socket inputSecondClientSocket) {
        this.firstClientSocket = inputFirstClientSocket;
        this.secondClientSocket = inputSecondClientSocket;
    }

    public Game(Socket inputFirstClientSocket) {
        this.firstClientSocket = inputFirstClientSocket;
        this.secondClientName = "Bot";
        this.playingWithBot = true;
    }

    public Socket getFirstClientSocket() {
        return this.firstClientSocket;
    }

    public Socket getSecondClientSocket() {
        return this.secondClientSocket;
    }

    public String getFirstClientName() { return this.firstClientName; }

    public String getSecondClientName() { return this.secondClientName; }

    public void setFirstClientName(String inputName) {
        this.firstClientName = inputName;
    }

    public void setSecondClientName(String inputName) {
        this.secondClientName = inputName;
    }

    public void setMove(int x, int y, boolean FOrS) {
        //FOrS - First or Second client is chosen to work with. true - first, false - second
        int value = -1;
        if (FOrS) value = 1;
        else value = 0;

        desk.setMove(x, y, value);
    }

    private String clearMessage(String message) {
        String[] subLine = message.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            sb.append(subLine[i]);
        }
        return sb.toString();
    }

    public void addChatMessage_FirstClient(String message) { this.chat.add(firstClientName + ": " +  clearMessage(message)); }

    public void addChatMessage_SecondClient(String message) { this.chat.add(secondClientName + ": " +  clearMessage(message)); }

    public void addChatMessage_Admin(String message) {
        this.chat.add("Admin: " + message);
        this.sendBoth.add("Admin: " + message);
    }

    public String getMessageForBothClients() {
        if (this.sendBoth.size() > 0) {
            return sendBoth.remove(0);
        }
        else return null;
    }

    public Vector<String> getChat() { return this.chat; }

    public boolean isPlayingWithBot() {
        return this.playingWithBot;
    }

    public Desk getDesk() {
        return this.desk;
    }
}
