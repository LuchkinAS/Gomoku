package user.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import user.model.User;

public class SelectWindow extends JPanel {
    private JRadioButton createNewDes;
    private JRadioButton continueSDes;
    private JRadioButton playWithBot;
    private JLabel label;
    private ViewController parentFrame;
    private JButton makeChoice;

    public SelectWindow(ViewController parent) {
        setVisible(false);
        parentFrame = parent;
        createNewDes = new JRadioButton("Begin new game.");
        continueSDes = new JRadioButton("Join to existing desk.");
        playWithBot = new JRadioButton("Playing with bot.");

        setLayout(new GridBagLayout());
        label = new JLabel("Select");
        this.parentFrame = parentFrame;
        ButtonGroup bg = new ButtonGroup();

        //bg.add(createNewDes);
        //bg.add(continueSDes);
        //bg.add(playWithBot);

        GridBagConstraints gbc = new GridBagConstraints();
        makeChoice = new JButton("Select");
        makeChoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(getKind() < 2) {
                    try {
                        parentFrame.setUser(new User(true));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                else {
                    try {
                        parentFrame.setUser(new User(false));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                parent.addWindow();
                System.out.println(getKind());
                setVisible(false);
                getParent().getComponent(ViewController.WAITINGSTATE).setVisible(true);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5,5,5,5);
        add(label, gbc);

        gbc.gridy++;
        add(createNewDes, gbc);

        gbc.gridy++;
        add(continueSDes, gbc);

        gbc.gridy++;
        add(playWithBot, gbc);

        gbc.gridy++;
        add(makeChoice, gbc);
    }


    public int getKind() {
        java.util.List<Boolean> states = new ArrayList<Boolean>();
        states.add(createNewDes.isSelected());
        states.add(continueSDes.isSelected());
        states.add(playWithBot.isSelected());
        return states.indexOf(Boolean.TRUE);
    }


}
