import java.util.Vector;
import java.util.Random;
import java.time.LocalTime;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Mario {
    public Mario() {
        setup();
    }

    public static void setup() {
        appFrame = new JFrame("Super Mario");
        XOFFSET = 0;
        YOFFSET = 40;
        WINWIDTH = 365;
        WINHEIGHT = 300;
        pi = 3.14159265358979;
        twoPi = 2.0 * 3.14159265358979;
        endgame = false;
        p1width = 25; // 18 .5 ;
        p1height = 25; // 25;
        p1originalX = (double) XOFFSET + (p1width / 2.0);
        p1originalY = (double) YOFFSET + ((double) WINHEIGHT / 2.0) - (p1height / 2.0);
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
            background = ImageIO.read(new File("src/Assets/ZeldaBackground.png"));
            player = ImageIO.read(new File("src/Assets/cgCar.png"));
            // flame1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame3 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame4 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame5 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // flame6 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // ast3 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            playerBullet = ImageIO.read(new File("src/Assets/bullet.png"));
            // enemyShip = ImageIO.read(new File("2DRacer/src/Assets/rCar.png"));
            // enemyBullet = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // exp1 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
            // exp2 = ImageIO.read(new File("2DRacer/src/Assets/bullet.png"));
        } catch (IOException ioe) {

        }
    }

    public static class Animate implements Runnable {
        public void run() {
            while (endgame == false) {
                backgroundDraw();
                // asteroidsDraw();
                // explosionsDraw();
                // enemyBulletsDraw();
                // enemyDraw();
                playerBulletsDraw();
                playerDraw();
                // flameDraw();

                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    public static void insertPlayerBullet() {
        ImageObject bullet = new ImageObject(0, 0, bulletWidth, bulletWidth, p1.getAngle());
        lockrotateObjAroundObjtop(bullet, p1, p1width / 2.0);
        playerBullets.addElement(bullet);
        playerBulletsTimes.addElement(System.currentTimeMillis());
    }

    private static class PlayerMover implements Runnable {
        public PlayerMover() {
            velocitystep = 0.01;
            rotatestep = 0.01;
            backgroundPosX = 0;
            playerX = 0;
            playerY = 0;

        }

        public void run() {
            while (endgame == false) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }

                if (upPressed == true) {
                    // jump action moves mario up and down
                }
                if (downPressed == true) {
                    // draw mario crouched and change hitbox
                }
                if (leftPressed == true) {
                    if (p1velocity < 2) {
                        p1velocity = p1velocity + velocitystep;
                    }
                    if (inWindow == true) {
                        backgroundPosX = backgroundPosX + 1;
                        p1.positionX = p1.positionX - 1;
                    }
                    // change mario sprite and update background
                }
                if (rightPressed == true) {
                    if (p1velocity < 0) {
                        p1.rotate(rotatestep);
                    } else {
                        p1.rotate(-rotatestep);
                    }
                }
                if (firePressed == true) {
                    try {
                        if (playerBullets.size() == 0) {
                            insertPlayerBullet();
                        } else if (System.currentTimeMillis() - playerBulletsTimes
                                .elementAt(playerBulletsTimes.size() - 1) > playerbulletlifetime / 4.0) {
                            insertPlayerBullet();
                        }
                    } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
                    }
                }

                p1.move(-p1velocity * Math.cos(p1.getAngle() - pi / 2.0),
                        p1velocity * Math.sin(p1.getAngle() - pi / 2.0));
                p1.screenWrap(XOFFSET - 100, XOFFSET - 100 + WINWIDTH, 0 - 100, 0 + WINHEIGHT - 100); // needs to be
                                                                                                      // higher on the
                // screen likely correct size
                // tho
            }
        }

        private double velocitystep;
        private double rotatestep;
    }

    public static class PlayerBulletsMover implements Runnable {
        public PlayerBulletsMover() {
            velocity = 1.0;
        }

        public void run() {
            while (endgame == false) {
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                }

                try {
                    for (int i = 0; i < playerBullets.size(); i++) {
                        playerBullets.elementAt(i).move(-velocity * Math.cos(
                                playerBullets.elementAt(i).getAngle() - pi / 2.0),
                                velocity * Math.sin(playerBullets.elementAt(i).getAngle() - pi / 2.0));
                        playerBullets.elementAt(i).screenWrap(XOFFSET, XOFFSET +
                                WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
                        if (System.currentTimeMillis() - playerBulletsTimes.elementAt(i) > playerbulletlifetime) {
                            playerBullets.remove(i);
                            playerBulletsTimes.remove(i);
                        }
                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException aie) {
                    playerBullets.clear();
                    playerBulletsTimes.clear();
                }
            }
        }

        private double velocity;
    }

    // private static class CollisionChecker implements Runnable {
    // public void run() {
    // Random randomNumbers = new Random(LocalTime.now().getNano());
    // while (endgame == false) {
    // try {
    // for (int i = 0; i < asteroids.size(); i++) {
    // for (int j = 0; j < playerBullets.size(); j++) {
    // if (collisionOccurs(asteroids.elementAt(i), playerBullets.elementAt(j)) ==
    // true) {
    // double posX = asteroids.elementAt(i).getX();
    // double posY = asteroids.elementAt(i).getY();

    // explosions.addElement(new ImageObject(posX, posY, 27, 24, 0.0));
    // explosionsTimes.addElement(System.currentTimeMillis());

    // if (asteroidsTypes.elementAt(i) == 1) {
    // asteroids.addElement(new ImageObject(posX, posY, ast2width, ast2width,
    // (double) (randomNumbers.nextInt(360))));
    // asteroidsTypes.addElement(2);
    // asteroids.remove(i);
    // asteroidsTypes.remove(i);
    // playerBullets.remove(j);
    // playerBulletsTimes.remove(j);
    // }

    // if (asteroidsTypes.elementAt(i) == 2) {
    // asteroids.addElement(new ImageObject(posX, posY, ast3width, ast3width,
    // (double) (randomNumbers.nextInt(360))));
    // asteroidsTypes.addElement(3);
    // asteroids.remove(i);
    // asteroidsTypes.remove(i);
    // playerBullets.remove(j);
    // playerBulletsTimes.remove(j);
    // }

    // if (asteroidsTypes.elementAt(i) == 3) {
    // asteroids.remove(i);
    // asteroidsTypes.remove(i);
    // playerBullets.remove(j);
    // playerBulletsTimes.remove(j);
    // }
    // }
    // }
    // }
    // for (int i = 0; i < asteroids.size(); i++) {
    // if (collisionOccurs(asteroids.elementAt(i), p1) == true) {
    // endgame = true;
    // System.out.println("Game Over You Lose!");
    // }
    // }
    // try {
    // for (int i = 0; i < playerBullets.size(); i++) {
    // if (collisionOccurs(playerBullets.elementAt(i), enemy) == true) {
    // double posX = enemy.getX();
    // double posY = enemy.getY();

    // explosions.addElement(new ImageObject(posX, posY, 27, 24, 0.0));
    // explosionsTimes.addElement(System.currentTimeMillis());

    // playerBullets.remove(i);
    // playerBulletsTimes.remove(i);
    // enemyAlive = false;
    // enemy = null;
    // enemyBullets.clear();
    // enemyBulletsTimes.clear();
    // }
    // }
    // if (collisionOccurs(enemy, p1) == true) {
    // endgame = true;
    // System.out.println("Game Over");
    // }

    // for (int i = 0; i < enemyBullets.size(); i++) {
    // if (collisionOccurs(enemyBullets.elementAt(i), p1) == true) {
    // endgame = true;
    // System.out.println("Game Over . You Lose !");
    // }
    // }
    // } catch (java.lang.NullPointerException jlnpe) {
    // }
    // } catch (java.lang.ArrayIndexOutOfBoundsException jlaioobe) {
    // }
    // }
    // }
    // }

    // private static class WinChecker implements Runnable {
    // public void run() {
    // while (endgame == false) {
    // // if (asteroids.size() == 0) {
    // // endgame = true;
    // // System.out.println("gameover");
    // // }
    // }
    // }
    // }

    // private static void generateEnemy() {
    // try {
    // Random randomNumbers = new Random(LocalTime.now().getNano());
    // enemy = new ImageObject(XOFFSET + (double) (randomNumbers.nextInt(
    // WINWIDTH)), YOFFSET + (double) (randomNumbers.nextInt(WINHEIGHT)),
    // 29.0, 16.0, (double) (randomNumbers.nextInt(360)));
    // } catch (java.lang.IllegalArgumentException jliae) {
    // }
    // }

    private static void lockrotateObjAroundObjbottom(ImageObject objOuter,
            ImageObject objInner, double dist) {

        objOuter.moveto(objInner.getX() + (dist + objInner.getWidth() / 2.0)
                * Math.cos(-objInner.getAngle() + pi / 2.0) + objOuter.getWidth() / 2.0,
                objInner.getY() + (dist + objInner.getHeight() / 2.0) * Math.sin(-objInner.getAngle() + pi / 2.0)
                        + objOuter.getHeight() / 2.0);

        objOuter.setAngle(objInner.getAngle());
    }

    private static void lockrotateObjAroundObjtop(ImageObject objOuter,
            ImageObject objInner, double dist) {
        objOuter.moveto(objInner.getX() + objOuter.getWidth() + (objInner.getWidth() / 2.0 +
                (dist + objInner.getWidth() / 2.0) * Math.cos(objInner.getAngle() + pi / 2.0)) / 2.0, objInner.getY() -
                        objOuter.getHeight() + (dist + objInner.getHeight() / 2.0) *
                                Math.sin(objInner.getAngle() / 2.0));
        objOuter.setAngle(objInner.getAngle());
    }

    public static AffineTransformOp rotateImageObject(ImageObject obj) {
        AffineTransform at = AffineTransform.getRotateInstance(-obj.getAngle(), obj.getWidth() / 2.0,
                obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }

    public static AffineTransformOp spinImageObject(ImageObject obj) {
        AffineTransform at = AffineTransform.getRotateInstance(-obj.getInternalAngle(), obj.getWidth() / 2.0,
                obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }

    public static void backgroundDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background, XOFFSET, YOFFSET, null);
    }

    // private static void enemyBulletsDraw() {
    // Graphics g = appFrame.getGraphics();
    // Graphics2D g2D = (Graphics2D) g;
    // for (int i = 0; i < enemyBullets.size(); i++) {
    // g2D.drawImage(enemyBullet, (int) (enemyBullets.elementAt(i).getX()
    // + 0.5), (int) (enemyBullets.elementAt(i).getY() + 0.5), null);
    // }
    // }

    // private static void enemyDraw() {
    // if (enemyAlive == true) {
    // try {
    // Graphics g = appFrame.getGraphics();
    // Graphics2D g2D = (Graphics2D) g;
    // g2D.drawImage(enemyShip, (int) (enemy.getX() + 0.5), (int) (enemy.getY() +
    // 0.5), null);
    // } catch (java.lang.NullPointerException jlnpe) {
    // }
    // }
    // }

    private static void playerBulletsDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        try {
            for (int i = 0; i < playerBullets.size(); i++) {
                g2D.drawImage(rotateImageObject(playerBullets.elementAt(i)).filter(playerBullet,
                        null),
                        (int) (playerBullets.elementAt(i).getX() + 0.5), (int) (playerBullets.elementAt(i).getY()
                                + 0.5),
                        null);
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
            playerBullets.clear();
            playerBulletsTimes.clear();
        }
    }

    public static void playerDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(rotateImageObject(p1).filter(player, null), (int) (p1.getX() +
                30), (int) (p1.getY() + 100), null);
    }

    public static class KeyPressed extends AbstractAction {
        public KeyPressed() {
            action = " ";
        }

        public KeyPressed(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) {
                upPressed = true;
            }
            if (action.equals("DOWN")) {
                downPressed = true;
            }
            if (action.equals("LEFT")) {
                leftPressed = true;
            }
            if (action.equals("RIGHT")) {
                rightPressed = true;
            }
            if (action.equals("F")) {
                firePressed = true;
            }
        }

        private String action;
    }

    public static class KeyReleased extends AbstractAction {
        public KeyReleased() {
            action = " ";
        }

        public KeyReleased(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) {
                upPressed = false;
            }
            if (action.equals("DOWN")) {
                downPressed = false;
            }
            if (action.equals("LEFT")) {
                leftPressed = false;
            }
            if (action.equals("RIGHT")) {
                rightPressed = false;
            }
            if (action.equals("F")) {
                firePressed = false;
            }
        }

        private String action;
    }

    public static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
        }
    }

    public static class StartGame implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            endgame = true;
            // enemyAlive = true;
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            firePressed = false;
            p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0.0);
            p1velocity = 0.0;
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
            }

            playerBullets = new Vector<ImageObject>();
            playerBulletsTimes = new Vector<Long>();

            // enemyBullets = new Vector<ImageObject>();
            // enemyBulletsTimes = new Vector<Long>();

            endgame = false;
            Thread t1 = new Thread(new Animate());
            Thread t2 = new Thread(new PlayerMover());

            Thread t5 = new Thread(new PlayerBulletsMover());
            // Thread t6 = new Thread(new EnemyShipMover());
            // Thread t7 = new Thread(new EnemyBulletsMover());
            // Thread t8 = new Thread(new CollisionChecker());
            // Thread t9 = new Thread(new WinChecker());
            t1.start();
            t2.start();
            // t3.start();
            // t4.start();
            t5.start();
            // t6.start();
            // t7.start();
            // t8.start();
            // t9.start();

        }
    }

    // private static class GameLevel implements ActionListener {
    // public int decodeLevel(String input) {
    // int ret = 3;
    // if (input.equals("One")) {
    // ret = 1;
    // } else if (input.equals("Two")) {
    // ret = 2;
    // } else if (input.equals("Three")) {
    // ret = 3;
    // } else if (input.equals("Four")) {
    // ret = 4;
    // } else if (input.equals("Five")) {
    // ret = 5;
    // } else if (input.equals("Six")) {
    // ret = 6;
    // } else if (input.equals("Seven")) {
    // ret = 7;
    // } else if (input.equals("Eight")) {
    // ret = 8;
    // } else if (input.equals("Nine")) {
    // ret = 9;
    // } else if (input.equals("Ten")) {
    // ret = 10;
    // }
    // return ret;
    // }

    // public void actionPerformed(ActionEvent e) {
    // JComboBox cb = (JComboBox) e.getSource();
    // String textLevel = (String) cb.getSelectedItem();
    // level = decodeLevel(textLevel);
    // }
    // }

    // private static Boolean isInside(double p1x, double p1y, double p2x1,
    // double p2y1, double p2x2, double p2y2) {
    // Boolean ret = false;
    // if (p1x > p2x1 && p1x < p2x2) {
    // if (p1y > p2y1 && p1y < p2y2) {
    // ret = true;
    // }
    // if (p1y > p2y2 && p1y < p2y1) {
    // ret = true;
    // }
    // }
    // if (p1x > p2x2 && p1x < p2x1) {
    // if (p1y > p2y1 && p1y < p2y2) {
    // ret = true;
    // }
    // if (p1y > p2y2 && p1y < p2y1) {
    // ret = true;
    // }
    // }
    // return ret;
    // }

    // private static Boolean collisionOccursCoordinates(double p1x1, double p1y1,
    // double p1x2, double p1y2, double p2x1,
    // double p2y1, double p2x2, double p2y2) {
    // Boolean ret = false;
    // if (isInside(p1x1, p1y1, p2x1, p2y1, p2x2, p2y2) == true) {
    // ret = true;
    // }
    // if (isInside(p1x1, p1y2, p2x1, p2y1, p2x2, p2y2) == true) {
    // ret = true;
    // }
    // if (isInside(p1x2, p1y1, p2x1, p2y1, p2x2, p2y2) == true) {
    // ret = true;
    // }
    // if (isInside(p1x2, p1y2, p2x1, p2y1, p2x2, p2y2) == true) {
    // ret = true;
    // }
    // if (isInside(p2x1, p2y1, p1x1, p1y1, p1x2, p1y2) == true) {
    // ret = true;
    // }
    // if (isInside(p2x1, p2y2, p1x1, p1y1, p1x2, p1y2) == true) {
    // ret = true;
    // }
    // if (isInside(p2x2, p2y1, p1x1, p1y1, p1x2, p1y2) == true) {
    // ret = true;
    // }
    // if (isInside(p2x2, p2y2, p1x1, p1y1, p1x2, p1y2) == true) {
    // ret = true;
    // }
    // return ret;
    // }

    // private static Boolean collisionOccurs(ImageObject obj1, ImageObject obj2) {
    // Boolean ret = false;
    // if (collisionOccursCoordinates(obj1.getX(), obj1.getY(), obj1.getX()
    // + obj1.getWidth(), obj1.getY() + obj1.getHeight(), obj2.getX(),
    // obj2.getY(), obj2.getX() + obj2.getWidth(), obj2.getY() + obj2.getHeight())
    // == true) {
    // ret = true;
    // }
    // return ret;
    // }

    public static class ImageObject {
        public ImageObject(double p1originalX, double v, double v1, double flamewidth, double flamewidth1, double v2) {
        }

        public ImageObject(double xinput, double yinput, double xwidthinput,
                double yheightinput, double angleinput) {
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

        public void move(double xinput, double yinput) {
            x = x + xinput;
            y = y + yinput;
        }

        public void moveto(double xinput, double yinput) {
            x = xinput;
            y = yinput;
        }

        public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
            if (x > rightEdge) {
                moveto(leftEdge, getY());
            }
            if (x < leftEdge) {
                moveto(rightEdge, getY());
            }
            if (y > bottomEdge) {
                moveto(getX(), topEdge);
            }
            if (y < topEdge) {
                moveto(getX(), bottomEdge);
            }
        }

        public void rotate(double angleinput) {
            angle = angle + angleinput;
            while (angle > twoPi) {
                angle = angle - twoPi;
            }
            while (angle < 0) {
                angle = angle + twoPi;
            }
        }

        public void spin(double internalangleinput) {
            internalangle = internalangle + internalangleinput;
            while (internalangle > twoPi) {
                internalangle = internalangle - twoPi;
            }
            while (internalangle < 0) {
                internalangle = internalangle + twoPi;
            }
        }

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

    public static void bindKey(JPanel myPanel, String input) {
        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " +
                input), input + " pressed ");
        myPanel.getActionMap().put(input + " pressed ", new KeyPressed(input));
        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke(" released " +
                input), input + " released ");
        myPanel.getActionMap().put(input + " released ", new KeyReleased(
                input));

    }

    public static void main(String[] args) {
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(WINWIDTH + 1, WINHEIGHT + 85);
        JPanel myPanel = new JPanel();
        // String[] levels = { "One", "Two", " Three ", "Four ", " F ive ", "Six ",
        // "Seven ", "Eight ", " Nine ", "Ten " };
        // JComboBox<String> levelMenu = new JComboBox<String>(levels);
        // levelMenu.setSelectedIndex(2);
        // levelMenu.addActionListener(new GameLevel());
        // myPanel.add(levelMenu);

        myPanel.imageUpdate(background, WINWIDTH, XOFFSET, YOFFSET, IFW, WINHEIGHT);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new StartGame());
        myPanel.add(newGameButton);
        JButton quitButton = new JButton("Quit Game");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);

        bindKey(myPanel, "UP");
        bindKey(myPanel, "DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");
        bindKey(myPanel, "F");
        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
    }

    private static Boolean endgame;
    // private static Boolean enemyAlive;
    private static BufferedImage background;
    private static BufferedImage player;
    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean firePressed;

    private static ImageObject p1;
    private static double p1width;
    private static double p1height;
    private static double p1originalX;
    private static double p1originalY;
    private static double p1velocity;
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

    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;
    private static double pi;
    private static double twoPi;
    private static JFrame appFrame;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
}