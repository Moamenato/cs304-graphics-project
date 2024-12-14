package Game;

import com.sun.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.*;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;

public class app extends JFrame {
    public Game.onePlayerEventListener listener = new onePlayerEventListener();
    public static FPSAnimator animator = null;

    public static void main(String[] args) {
        final Game.app app = new Game.app();
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        app.setVisible(true);
                    }
                }
        );
    }

    public app() {
        super("Chicken Eggs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GLCanvas glcanvas = new GLCanvas();
        animator = new FPSAnimator(glcanvas, 60);
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        animator.start();
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        glcanvas.setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(this);
        setVisible(true);
    }
}

