package user.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import user.model.User;

public class StartWindow extends JPanel {
    private JButton sendUserName;
    private JTextField nameField;
    private JLabel label;
    private String name;
    private ViewController parentFrame;

    public StartWindow(ViewController parent) {

        setLayout(new GridBagLayout());
        parentFrame = parent;
        nameField = new JTextField();
        label = new JLabel("Game", JLabel.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5,5,5,5);
        add(label, gbc);


        sendUserName = new JButton("Create new game");
		sendUserName.setBounds(130, 50, 240, 40);
		sendUserName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setUserName(getUserName());
                System.out.println(getUserName());
                setVisible(false);
                getParent().getComponent(ViewController.SELECTSTATE).setVisible(true);
			}
		});
        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(sendUserName, gbc);

        setVisible(true);
    }



    public String getUserName(){
        return nameField.getText();
    }



}

