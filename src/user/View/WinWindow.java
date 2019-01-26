package user.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WinWindow extends JDialog {
    public WinWindow(ViewController viewController, String name)
    {
        super(viewController, "Win", true);
        JLabel label = new JLabel(name + " win");
        JButton endOfGame = new JButton("Exit");
        endOfGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        add(label);
        add(endOfGame);
        }
    }

