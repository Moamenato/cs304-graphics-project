package Game;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.media.opengl.GLCanvas;

import com.sun.opengl.util.FPSAnimator;

public class GameMode extends JFrame {
    public static FPSAnimator animator = null;
    public GLCanvas glcanvas;

    public static void main(String[] args) {
        final GameMode app = new GameMode();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                app.setVisible(true);
            }
        });
    }

    public GameMode() {
        super("Chicken Eggs");
        ScreensListener listener = new ScreensListener();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        glcanvas = new GLCanvas();
        animator = new FPSAnimator(glcanvas, 30);
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        glcanvas.addMouseListener(listener);
        glcanvas.addMouseMotionListener(listener);
        animator.start();
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        glcanvas.setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(this);
        setVisible(true);
    }

    public GameMode(String userName, int level, boolean AI) {
        if (AI) {
            AI_Player_Mode listener = new AI_Player_Mode(level);
            glcanvas = new GLCanvas();
            animator = new FPSAnimator(glcanvas, 30);
            listener.setPlayerName(userName);
            glcanvas.addGLEventListener(listener);
            glcanvas.addKeyListener(listener);
            animator.start();
            getContentPane().add(glcanvas, BorderLayout.CENTER);
            glcanvas.setFocusable(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(800, 800);
            setLocationRelativeTo(this);
            setVisible(true);
        } else {
            One_Player_Mode listener = new One_Player_Mode(level);
            glcanvas = new GLCanvas();
            animator = new FPSAnimator(glcanvas, 30);
            listener.setUserName(userName);
            glcanvas.addGLEventListener(listener);
            glcanvas.addKeyListener(listener);
            animator.start();
            getContentPane().add(glcanvas, BorderLayout.CENTER);
            glcanvas.setFocusable(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(800, 800);
            setLocationRelativeTo(this);
            setVisible(true);
        }

    }

    public GameMode(String userName1, String userName2, int level) {
        Two_Players_Mode listener = new Two_Players_Mode(1);
        glcanvas = new GLCanvas();
        animator = new FPSAnimator(glcanvas, 30);
        listener.setPlayer1Name(userName1);
        listener.setPlayer2Name(userName2);
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        animator.start();
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        glcanvas.setFocusable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(this);
        setVisible(true);
    }

//    public void GameModeAI(String userName, int level) {
//        AI_Player_Mode listener = new AI_Player_Mode(level);
//        GLCanvas glcanvas = new GLCanvas();
//        animator = new FPSAnimator(glcanvas, 30);
//        listener.setPlayerName(userName);
//        glcanvas.addGLEventListener(listener);
//        glcanvas.addKeyListener(listener);
//        animator.start();
//        getContentPane().add(glcanvas, BorderLayout.CENTER);
//        glcanvas.setFocusable(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 800);
//        setLocationRelativeTo(this);
//        setVisible(true);
//    }
}
