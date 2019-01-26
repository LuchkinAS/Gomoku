package user.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyFieldButton extends JButton {
    private int i;
    private int j;

    boolean isUsed;
    GameWindow panel;
    ViewController frame;

    IMG img = new IMG();

    MyFieldButton(GameWindow panel, ViewController frame, int i, int j) {
        this.panel = panel;
        this.frame = frame;
        isUsed = false;
        this.i = i;
        this.j = j;
        //Icon icon =  new ImageIcon("C:\\Users\\luchk\\Downloads\\Revision 5\\Gomoku\\sourses\\null.png");
        Icon icon =  new ImageIcon(img.getNullIMGLink());
        setIcon(icon);
        setPreferredSize(new Dimension(30,30));
        addMouseListener(new GameFieldButtonMouseListener());
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public GameWindow getPanel() {
        return panel;
    }

    public void setPanel(GameWindow panel) {
        this.panel = panel;
    }

    public ViewController getFrame() {
        return frame;
    }

    public int getIndex() { return i * 10 + j;}

    public void setFrame(ViewController frame) {
        this.frame = frame;
    }


    public void setIcon(String way) {
        Icon icon = new ImageIcon(way);
        setIcon(icon);
    }

        class GameFieldButtonMouseListener implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isUsed == false && frame.getUser().isReadyForGame() && frame.getUser().isClientMove()) {
                    Icon icon;
                        //icon = new ImageIcon("C:\\Users\\luchk\\Downloads\\Revision 5\\Gomoku\\sourses\\cross.png");
                    icon = new ImageIcon(img.getCrossIMGLink());

                    setIcon(icon);
                    isUsed = true;
                    panel.setYourMoveLabel("Opponent Move");
                    frame.getUser().sendClientMove(i * 10  + j);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        }
    }

