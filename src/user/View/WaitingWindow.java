package user.View;

import javax.swing.*;

public class WaitingWindow extends JPanel {
    private ViewController parentFrame;
    private JLabel waitLabel = new JLabel("Waiting for opponent.");

    WaitingWindow(ViewController parent) {
        setVisible(false);
        parentFrame = parent;
        add(waitLabel);
        WaitingThread thread = new WaitingThread(this);
        thread.start();

    }



    class WaitingThread extends Thread{

        WaitingWindow panel;
        WaitingThread(WaitingWindow ww)
        {
            panel = ww;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(parentFrame.getUser().isReadyForGame()) {
                    System.out.println("game started");
                    parentFrame.getUser().setName(parentFrame.getUserName());
                    try {
                        sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    panel.setVisible(false);
                    panel.getParent().getComponent(ViewController.GAMESTATE).setVisible(true);
                    break;
                }
            }
        }
    }
}
