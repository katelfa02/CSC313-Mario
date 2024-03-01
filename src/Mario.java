
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

    private static void setup() {
        endgame = false;

        appFrame = new JFrame("Super Mario");
        XOFFSET = 0;
        YOFFSET = 40;
        WINWIDTH = 400;// 338;
        WINHEIGHT = 400;// 271;
        pi = 3.14159265358979;
        twoPi = 2.0 * 3.14159265358979;
        endgame = false;
        p1width = 20; // 18 .5 ;
        p1height = 20; // 25;
        p1originalX = 120;// starting position x
        p1originalY = 120; // starting position y
        playerBullets = new Vector<ImageObject>();
        playerBulletsTimes = new Vector<Long>();
        bulletWidth = 5;
        playerbulletlifetime = 1600L;// 0 .75 ;
        // enemybulletlifetime = 1600L;
        // explosionlifetime = 800L;
        playerbulletgap = 1;
        // flamecount = 1;
        // flamewidth = 12.0;
        // expcount = 1;
        // level = 3;
        // asteroids = new Vector<ImageObject>();
        // asteroidsTypes = new Vector<Integer>();
        // ast1width = 32;
        // ast2width = 21;
        // ast3width = 26;

        try {
            background = ImageIO.read(new File("src/assets/KI00.png"));
            // player = ImageIO.read(new File("src/assets/cgCar.png"));
            // flame1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame3 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame4 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame5 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame6 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast3 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // playerBullet = ImageIO.read(new File("src/Assets/bullet.png"));
            // enemyShip = ImageIO.read(new File("2DRacer/src/Assets/rCar.png"));
            // enemyBullet = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // exp1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // exp2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
        } catch (IOException ioe) {

        }

    }

    private static class Animate implements Runnable {
        public void run() {
            while (endgame == false) {
                backgroundDraw();

                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {

                }
            }
        }

        private void backgroundDraw() {
            Graphics g = appFrame.getGraphics();
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(background, XOFFSET, YOFFSET, null);
        }
    }

    private static class StartGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;

            Thread t1 = new Thread(new Animate());

            t1.start();
        }
    }

    public static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
        }
    }

    private static class ImageObject {
        public ImageObject() {
        }

        public ImageObject(double xinput, double yinput, double xwidthinput, double yheightinput, double angleinput) {
            x = xinput;
            y = yinput;
            xwidth = xwidthinput;
            yheight = yheightinput;
            angle = angleinput;
            internalangle = 0.0;
            coords = new Vector<Double>();
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return xwidth;
        }

        public double getHeight() {
            return yheight;
        }

        public double getAngle() {
            return angle;
        }

        public double getInternalAngle() {
            return internalangle;
        }

        public void setAngle(double angleinput) {
            angle = angleinput;
        }

        public void setInternalAngle(double internalangleinput) {
            internalangle = internalangleinput;
        }

        public Vector<Double> getCoords() {
            return coords;
        }

        public void setCoords(Vector<Double> coordsinput) {
            coords = coordsinput;
            generateTriangles();

        }

        public void generateTriangles() {
            triangles = new Vector<Double>();
            comX = getComX();
            comY = getComY();

            for (int i = 0; i < coords.size(); i = i + 2) {
                triangles.addElement(coords.elementAt(i));
                triangles.addElement(coords.elementAt(i + 1));
                triangles.addElement(coords.elementAt((i + 2) % coords.size()));
                triangles.addElement(coords.elementAt((i + 3) % coords.size()));
                triangles.addElement(comX);
                triangles.addElement(comY);
            }
        }

        public void printTriangles() {
            for (int i = 0; i < triangles.size(); i = i + 6) {
                System.out.print("p0x : " + triangles.elementAt(i) + " , p0y : "
                        + triangles.elementAt(i + 1));
                System.out.print(" p1x : " + triangles.elementAt(i + 2) + " , p1y :"
                        + triangles.elementAt(i + 3));
                System.out.println(" p2x : " + triangles.elementAt(i + 4) + " , p2y : " + triangles.elementAt(i + 5));
            }
        }

        public double getComX() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 0; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public double getComY() {
            double ret = 0;
            if (coords.size() > 0) {
                for (int i = 1; i < coords.size(); i = i + 2) {
                    ret = ret + coords.elementAt(i);
                }
                ret = ret / (coords.size() / 2.0);
            }
            return ret;
        }

        public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            // set up window to freely move and
            // if outside window right side, create counter and move background counter
            // amount
            // if enters back into free move area, reset the counter
        }

        public void move(double xinput, double yinput) {
            x = x + xinput;
            y = y + yinput;
        }

        public void moveto(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }

        // public void rotate(double angleinput) {
        // angle = angle + angleinput;
        // while (angle > twoPi) {
        // angle = angle - twoPi;
        // }
        // while (angle < 0) {
        // angle = angle + twoPi;
        // }
        // }

        // public void spin(double internalangleinput) {
        // internalangle = internalangle + internalangleinput;
        // while (internalangle > twoPi) {
        // internalangle = internalangle - twoPi;
        // }
        // while (internalangle < 0) {
        // internalangle = internalangle + twoPi;
        // }
        // }

        private double x;
        private double y;
        private double xwidth;
        private double yheight;
        private double angle; // in Radians
        private double internalangle; // in Radians
        private Vector<Double> coords;
        private Vector<Double> triangles;
        private double comX;
        private double comY;
    }

    // public static void bindKey(JPanel myPanel, String input) {
    // myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " +
    // input), input + " pressed ");
    // myPanel.getActionMap().put(input + " pressed ", new KeyPressed(input));
    // myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke(" released " +
    // input), input + " released ");
    // myPanel.getActionMap().put(input + " released ", new KeyReleased(
    // input));

    // }

    public static void main(String[] args) {
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(WINWIDTH + 1, WINHEIGHT + 85);

        JPanel myPanel = new JPanel();

        // myPanel.imageUpdate(background, WINWIDTH, XOFFSET, YOFFSET, IFW, WINHEIGHT);

        JButton quitButton = new JButton("Select");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);
        JButton newGameButton = new JButton("Start");
        newGameButton.addActionListener(new StartGame());
        myPanel.add(newGameButton);
        // bindKey(myPanel, "UP");
        // bindKey(myPanel, "DOWN");
        // bindKey(myPanel, "LEFT");
        // bindKey(myPanel, "RIGHT");
        // bindKey(myPanel, "F");
        appFrame.getContentPane().add(myPanel);
        appFrame.setVisible(true);
    }

    private static Boolean endgame;

    private static int xdimKI;
    private static int ydimKI;
    private static int xdimTC;
    private static int ydimTC;
    private static BufferedImage player;
    private static Vector<BufferedImage> link;
    private static BufferedImage leftHeartOutline;
    private static BufferedImage rightHeartOutline;
    private static BufferedImage leftHeart;
    private static BufferedImage rightHeart;
    private static Vector<BufferedImage> bluepigEnemy;

    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean aPressed;
    private static Boolean xPressed;
    private static double lastPressed;

    private static ImageObject p1;
    private static double p1width;
    private static double p1height;
    private static double p1originalX;
    private static double p1originalY;

    private static double p1velocity;
    private static int level;
    private static Long audiolifetime;
    private static Long lastAudioStart;
    private static Clip clip;
    private static Long dropLifeLifetime;
    private static Long lastDropLife;
    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;
    private static double pi;
    private static double quarterPi;
    private static double halfPi;
    private static double threequartersPi;
    private static double fivequartersPi;
    private static double threehalvesPi;
    private static double sevenquartersPi;
    private static double twoPi;

    private static JFrame appFrame;
    private static String backgroundState;

    private static Boolean availableToDropLife;

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    private static BufferedImage background;

    private static Boolean firePressed;

    private static ImageObject enemy;
    private static BufferedImage enemyShip;
    private static BufferedImage enemyBullet;
    private static Vector<ImageObject> enemyBullets;
    private static Vector<Long> enemyBulletsTimes;
    private static Long enemybulletlifetime;
    private static Vector<ImageObject> playerBullets;
    private static Vector<Long> playerBulletsTimes;
    private static double bulletWidth;
    private static BufferedImage playerBullet;
    private static Long playerbulletlifetime;
    private static double playerbulletgap;

}
