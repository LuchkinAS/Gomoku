package server.connection;

import server.parts.*;

import java.io.IOException;
import java.util.Vector;

public class Connection {
    //messages
    volatile private Vector<String> messagesFromFirstClient = new Vector<String>();
    volatile private Vector<String> messagesToFirstClient = new Vector<String>();
    volatile private Vector<String> messagesFromSecondClient = new Vector<String>();
    volatile private Vector<String> messagesToSecondClient = new Vector<String>();

    //working status
    volatile private boolean firstClientIsActive = true;
    volatile private boolean secondClientIsActive = true;

    //game
    private Game game;
    boolean playingWithBot = false;
    private int prevBotMove = 55;

    public Connection(Game inputGame) throws IOException {
        this.game = inputGame;
        this.playingWithBot = game.isPlayingWithBot();

        ConnectionTread firstClientThread = new ConnectionTread(game.getFirstClientSocket(), messagesFromFirstClient, messagesToFirstClient, firstClientIsActive);
        firstClientThread.setDaemon(true);
        firstClientThread.start();

        if (!playingWithBot) {
            ConnectionTread secondClientThread = new ConnectionTread(game.getSecondClientSocket(), messagesFromSecondClient, messagesToSecondClient, secondClientIsActive);
            secondClientThread.setDaemon(true);
            secondClientThread.start();
        }

        //set active state on both of clients
        messagesToFirstClient.add("~activate");
        if (playingWithBot) botMakeAMove();//if playing with bot we need to make first move by bot
        messagesToFirstClient.add("~clientmovetrue");
        if (!playingWithBot) { messagesToSecondClient.add("~activate"); }


        Thread ConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (messagesFromFirstClient.size() > 0) executeCommand(messagesFromFirstClient.remove(0), true);
                    if (messagesFromSecondClient.size() > 0) executeCommand(messagesFromSecondClient.remove(0), false);
                    checkMessageForBothClients();
                }
            }
        });
        ConnectionThread.setDaemon(true);
        ConnectionThread.start();
    }

    private void executeCommand(String message, boolean FOrS) {
        //FOrS - First or Second client is chosen to work with. true - first, false - second
        System.out.println(message);

        String[] subLine = message.split(" ");
        if (subLine[0].equals("~stpclnm")) clientSetName(subLine, FOrS);
        if (subLine[0].equals("~msg")) clientSendMessage(message, FOrS);
        if (subLine[0].equals("~mkamve")) clientMakeAMove(message, subLine, FOrS);
    }

    private void checkMessageForBothClients() {
        String temp = game.getMessageForBothClients();
        if (temp != null) {
            messagesToFirstClient.add("~msg" + " " + temp);
            messagesToSecondClient.add("~msg" + " " + temp);
        }
    }

    private void clientSetName(String[] subLine, boolean FOrS) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < subLine.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(subLine[i]);
        }
        if (FOrS) {
            game.setFirstClientName(sb.toString());
            messagesToSecondClient.add("~oppname" + " " + sb.toString());
        }
        else {
            game.setSecondClientName(sb.toString());
            messagesToFirstClient.add("~oppname" + " " + sb.toString());
        }
    }

    private void clientSendMessage(String message, boolean FOrS) {
        if (FOrS) {
            messagesToSecondClient.add(message);
            game.addChatMessage_FirstClient(message);
        }
        else {
            messagesToFirstClient.add(message);
            game.addChatMessage_SecondClient(message);
        }
    }

    private void clientMakeAMove(String message, String[] subLine, boolean FOrS) {

        if (FOrS) {
            System.out.println("clientMakeAMove firstClient" + subLine[1] + " " + subLine[2]);
            //first client made a move
            if (!playingWithBot) messagesToSecondClient.add(message);
            //need to add a move to the desk on server
            //now we do not have a desk
            game.setMove(Integer.parseInt(subLine[1]), Integer.parseInt(subLine[2]), FOrS);
            boolean win = checkDesk(game.getDesk(), FOrS);
            if (win) sendWinner(FOrS);
            if (playingWithBot) botMakeAMove();
        }
        else {
            System.out.println("clientMakeAMove secondClient" + subLine[1] + " " + subLine[2]);
            messagesToFirstClient.add(message);
            //need to add a move to the desk on server
            //now we do not have a desk
            game.setMove(Integer.parseInt(subLine[1]), Integer.parseInt(subLine[2]), FOrS);
            boolean win = checkDesk(game.getDesk(), FOrS);
            if (win) sendWinner(FOrS);
        }
    }

    private void botMakeAMove() {
        System.out.println("Bot starts thinking about move");

        Bot bot = new Bot();
        bot.setBotPreMove(prevBotMove);
        int move = bot.doMove(game.getDesk());
        prevBotMove = move;
        System.out.println("int bot move: " + move);
        messagesFromSecondClient.add("~mkamve" + " " + (move / 10) + " " + (move % 10));

        System.out.println("Bot made a move: " + + (move / 10) + " " + (move % 10));
    }

    private boolean checkDesk(Desk desk, boolean FOrS) {
        //крестик -- 1(true) нолик -- 0(false)
        boolean win;
        if(FOrS) {
            win = checkWin(desk.getDeskArray(), 1);
        }
        else{
            win = checkWin(desk.getDeskArray(), 0);
        }
        //на вход получает доску и FOrS - параметр того, какой игрок походил и чей ход надо проврять (0 или 1) - во благо экономии
        //FOrS - First or Second client is chosen to work with. true - first, false - second
        //возвращает true - если пользователь указанный при ходе побеил после хода
        //возвращает false - если нет победы
        return win;
    }

    public boolean checkWin(int [][] desk, int player) {
        //rows: done
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                if(desk[i][j] == player && desk[i][j + 1] == player
                        && desk[i][j + 2] == player && desk[i][j + 3] == player && desk[i][j + 4] == player)
                    return true;

            }
        }

        //cols: done
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 10; j++) {
                if(desk[i][j] == player && desk[i + 1][j] == player && desk[i + 2][j] == player
                        && desk[i + 3][j] == player && desk[i + 4][j] == player)
                    return true;
            }
        }

        // check main diag and below: done
        int win = 0;
        for(int k = 0; k < 6; k++) {
            for(int j = 0; k + j < 10; j++) {
                if(desk[k + j][j]==player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[k + j][j]!=player)
                    win = 0;
            }
        }

        // main diag and above: done
        win = 0;
        for(int k = 1; k < 6; k++) {
            for(int i = 0; k + i < 10; i++) {
                if(desk[i][k + i] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[i][k + i] != player)
                    win = 0;
            }
        }


        // sub diag and below: done
        win = 0;
        for(int k = 1; k < 7; k++) {
            for (int i = 0; 10 - k - i >= 0; i++) {
                if(desk[i][10 - k - i] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[i][10 - k - i] != player)
                    win = 0;
            }
        }

        // sub diag and above
        win = 0;
        for(int k = 0; k < 5; k++) {
            for (int j = 9; 10 - j + k < 10; j--)
            {
                if(desk[10 - j + k][j] == player)
                    win++;
                if(win > 4)
                    return true;
                if(desk[10 - j + k][j] != player)
                    win = 0;

            }
        }
        return false;
    }

    private void sendWinner(boolean FOrS) {
        if (FOrS) messagesToSecondClient.add("~setwinner" + " " + game.getFirstClientName());
        else messagesToFirstClient.add("~setwinner" + " " + game.getSecondClientName());
    }
}