package user.View;

import javax.swing.*;
import java.awt.*;

import user.model.User;


public class ViewController extends JFrame{

    private JPanel startWindow;
    private JPanel selectWindow;
    private JPanel gameWindow;
    private JPanel waitingWindow;
    private JPanel winWindow;
    private User user;
    private int kind;
    private String userName;

    public static final int STARTSTATE = 0;
    public static final int SELECTSTATE = 1;
    public static final int WAITINGSTATE = 2;
    public static final int GAMESTATE = 3;

    public ViewController() {
        super("Game");
        setLayout(new GridBagLayout());
        setSize(700, 500);
        setLocationRelativeTo(null);
        startWindow = new StartWindow(this);
        selectWindow = new SelectWindow(this);
        add(startWindow);
        add(selectWindow);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) { this.user = user; }
    public void setUserName(String name) { userName = name; }
    public String getUserName(){ return userName; }


    public void addWindow()//добавляет оставшиеся окна после того, как user создан
    {
        waitingWindow = new WaitingWindow(this);
        gameWindow = new GameWindow(this);
        add(waitingWindow);
        add(gameWindow);
    }



    public static void main(String[] args) throws Exception {

        ViewController uv = new ViewController();


        //user.setState(0);//begin a new desk
        //важно
        //если создать новую доску то мы либо рандомом задаем крестик или нолик игроку либо как-то всегда одинаково либо игрок сам выбирает
        //я пока что не придумал

        while (true) {}
    }


}
