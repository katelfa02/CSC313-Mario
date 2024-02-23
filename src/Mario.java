
import java.io.BufferedInputStream;
import java.util.Vector;
import java.util.Random;
import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Mario {

    public Mario() {
        setup();
    }

    private void setup() {
        endgame = false;
    }

    private static class Animate implements Runnable {
        public void run() {
            while (endgame == false) {
                backgroundDraw();
            }
        }

        private void backgroundDraw() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'backgroundDraw'");
        }
    }

    private static class StartGame implements ActionListener {
        public void actionPerformend(ActionEvent e) {
            endgame = true;
        }
    }

    public static class Quitgame implements ActionListener {
        public void actionPerformend(ActionEvent e) {
            endgame = true;
        }
    }

    // private static class ImageObject{
    // public ImageObject(){
    // maxFrames = 1;
    // currentFrame = 0;
    // bounce = false;
    // life = 1;
    // maxLife = 1;
    // dropLife = 0;
    // }

    // }
    public static void main(String[] args) {
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(WINWIDTH + 1, WINHEIGHT + 85);

        JPanel myPanel = new JPanel();

        JButton quitButton = new JButton("Select");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);
        JButton newGameButton = new JButton("Start");
        newGameButton.addActionListener(new StartGame());
        myPanel.add(newGameButton);
        bindKey(myPanel, "UP");
        bindKey(myPanel, "DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");
        bindKey(myPanel, "F");
        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
    }

    private static Boolean endgame;
}
