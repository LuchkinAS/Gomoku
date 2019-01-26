package user.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import user.model.User;

public class GameWindow extends JPanel {
    private JTextField messField;
    private JButton sendChatMessage;



    private JLabel opponentNameLabel;
    private JLabel yourMoveLabel;
    private List<JLabel>  chat;
    private ViewController parentFrame;
    private JPanel gameField;
    private JPanel chatField;
    private List<MyFieldButton> field;
    private User user;
    private ArrayList<String> chatList;
    private JLabel signLabel = new JLabel("Your sign is cross.");


    public GameWindow(ViewController parent) {
        setVisible(false);
        this.parentFrame = parent;
        gameField = new JPanel();
        user = parentFrame.getUser();
        gameField.setLayout(new GridLayout(10,10));
        field = new ArrayList<>();
        /*Buttons*/
        for(int i = 0; i < 100; i++)
        {
            field.add(new MyFieldButton(this,parentFrame, i / 10, i % 10));
        }
        for(int i = 0; i < 100; i++)
        {
            gameField.add(field.get(i));
        }
        add(gameField);


        /*Chat*/
        yourMoveLabel = new JLabel(" ");
        opponentNameLabel = new JLabel(" ");
        chatField = new JPanel();
        chat = new ArrayList<JLabel>();
        chatList = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            chat.add(new JLabel(" "));
            chatList.add(" ");
        }
        chatField.setLayout(new GridLayout(17, 1));

        chatField.add(yourMoveLabel);
        chatField.add(signLabel);
        chatField.add(opponentNameLabel);
        chatField.add(new Label("Chat"));
        for(int i = 0; i < 10; i++)
        {
            chatField.add(chat.get(i));
        }
        messField = new JTextField();
        chatField.add(messField);



        sendChatMessage = new JButton("Send message");
        sendChatMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = getTextFromMessField();
                chatList.add(0, user.getClientName() + " " +  message);
                for (int i = 0; i < 10; i++)
                    chat.get(i).setText(chatList.get(i));

                user.sendChatMess(message);
            }
        });
        chatField.add(sendChatMessage);

        add(chatField);
        GameChatThread gameChatThread = new GameChatThread(this);
        gameChatThread.start();


    }

    public void setYourMoveLabel(String text) { yourMoveLabel.setText(text);}

    public void setOpponentNameLabel(String name ) {
        if(name != null)
            opponentNameLabel.setText("Your opponent is " + name);
    }

    String getTextFromMessField() {
        String mess  = messField.getText();
        messField.setText("");
        return mess;
    }



    class GameChatThread extends Thread{

        GameWindow panel;
        GameChatThread(GameWindow panel)
        {
            this.panel = panel;
        }


        @Override
        public void run() {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean first = true;
            while (true) {
                if(user != null) {
                    if(user.getOpponentName() != null){
                        if(!user.isInState() && first) {
                            setOpponentNameLabel("Bot");
                            first = false;
                        }
                        setOpponentNameLabel(user.getOpponentName());
                    }
                    if (user.thereIsNewMessageForChat()) {
                        chatList.add(0, user.getMessageForChat());
                        for (int i = 0; i < 10; i++)
                            chat.get(i).setText(chatList.get(i));
                    }
                    if(user.isClientMove()) {
                        setYourMoveLabel("Your move.");
                    }
                    else {
                        setYourMoveLabel("Opponent move");
                    }
                    if((user.isOpponentMoveReceived() && user.getOpponentMove() != -1)) {
                        int index = user.getOpponentMove();
                        field.get(index).setUsed(true);
                        IMG img = new IMG();
                        //field.get(index).setIcon("C:\\Users\\luchk\\Downloads\\Gomoku\\sourses\\zero.png");
                        field.get(index).setIcon(img.getZeroIMGLink());
                    }

                    if(user.isGameEnded()) {
                        if(user.getWinnerName() == "") {
                            new WinWindow(parentFrame, "Bot");
                        }
                        new WinWindow(parentFrame, user.getWinnerName());
                        break;
                    }
                }
            }
        }
    }
}
