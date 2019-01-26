package user.model;

import user.connection.Connection;

import java.net.Socket;
import java.util.Vector;

public class User {
    //connection
    private Connection connection;
    private Socket socket;
    volatile boolean isActive = true;
    volatile boolean inState;
    //client's info
    volatile private String clientName = "";

    //messages
    volatile private String messageFromServer;
    volatile private Vector<String> messagesToServer = new Vector<String>();
    volatile private Vector<String> messagesForChat = new Vector<String>();

    //games constants
    volatile private boolean readyForGame = false;//when it is true we can start sending messages to server
    volatile private boolean gameEnded = false;
    volatile private String winnerName = "";// rename
    volatile private String opponentName = "";
    volatile private int opponentMove = -1;
    volatile boolean opponentMoveReceived = false;

    //if client can make a move
    volatile private boolean clientMove = false;// rename

    public User(boolean inputState) throws Exception {
        this.connection = new Connection(isActive);
        inState = inputState;
        //state: play with another player (true) or play with bot (false)
        if (inputState) connection.sendMessageToServer("~clientcnctn");
        else connection.sendMessageToServer("~clientbt");

        //getting messages from server
        Thread GetMessagesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive) {
                    try {
                        messageFromServer = connection.getMessageFromServer();
                        if (messageFromServer != null) executeCommand(messageFromServer);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        isActive = false;
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
                    if (messagesToServer.size() > 0) connection.sendMessageToServer(messagesToServer.remove(0));
                }
            }
        });
        SendMessagesThread.start();
    }



    private void executeCommand(String command) {
        System.out.println(command);
        String[] subLine = command.split(" ");

        if (subLine[0].equals("~activate")) readyForGame = true;
        if (subLine[0].equals("~msg")) addMessageIntoChat(subLine);
        if (subLine[0].equals("~end")) endGame(subLine);
        if (subLine[0].equals("~mkamve")) opponentMadeMove(subLine);
        if (subLine[0].equals("~clientmovetrue")) clientMove = true;
        if (subLine[0].equals("~oppname")) setOpponentName(subLine);
        if (subLine[0].equals("~setwinner")) setWinner(subLine);
    }

    private void addMessageIntoChat(String[] subLine) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        messagesForChat.add(sb.toString());
    }

    private void endGame(String[] subLine) {
        gameEnded = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        winnerName = sb.toString();
    }

    private void opponentMadeMove(String[] subLine) {
        int x = Integer.parseInt(subLine[1]);
        int y = Integer.parseInt(subLine[2]);

        opponentMove = x * 10 + y;
        opponentMoveReceived = true;
        clientMove = true;
    }

    private void setOpponentName(String[] subLine) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        opponentName = sb.toString();
    }

    private void setWinner(String[] subLine) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        this.gameEnded = true;
        this.winnerName = sb.toString();
    }

    public boolean thereIsNewMessageForChat() // new
    {
        return !messagesForChat.isEmpty();
    }

    public String getMessageForChat() // new
    {
        if(messagesForChat.size() > 0)
        {
            //return null;
            //return messageFromServer.remove(messagesForChat.size() - 1);
            return messagesForChat.remove(0);
        }
        return null;
    }

    public boolean isClientMove() { return clientMove; } // new

    public boolean isReadyForGame() { return readyForGame;} // new

    public String getWinnerName() { return winnerName;}// new

    public String getOpponentName() { return opponentName;}

    public boolean isGameEnded() {return gameEnded; }// new

    public String getClientName() { return clientName; }

    public boolean isInState() { return inState; }

    public boolean isOpponentMoveReceived() { return opponentMoveReceived;} // получен ли уже ход новых ход соперника

    public int getOpponentMove() {
        opponentMoveReceived = false;
        return opponentMove;
    } //получение хода соперника, загруженного с сервера(нужно дописать)

    public void sendChatMess(String mess) { this.messagesToServer.add("~msg" + " " + mess); }

    public void sendClientMove(int index){
        clientMove = false;
        this.messagesToServer.add("~mkamve" + " " + (index / 10) + " " + (index % 10));
    }

    private void sendMessageToServer(String messageToServer) {
        this.messagesToServer.add(messageToServer);
    }

    public void setName(String name) {
        this.messagesToServer.add("~stpclnm" + " " + name);
        this.clientName = name;
    }
}