package Game;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class ScreensListener extends Anim_Listener implements MouseListener, MouseMotionListener {


    int maxWidth = 100;
    int maxHeight = 100;
    double xMouse1 = 0, yMouse1 = 0;
    double xMouse2 = 0, yMouse2 = 0;
    double xMouse3 = 0, yMouse3 = 0;
    int idx = 0;
    boolean Home = true, PlayScreen = false, LevelScreen = false;
    boolean Instruction = false;

    public ScreensListener() {
    }

    String[] textureNames = {
            "Home1.png", "Home2.png", "Home3.png", "backButton.png", "how.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolder + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }


    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (Home) {
            DrawBackground(gl, 0);
        } else if (PlayScreen) {
            DrawBackground(gl, 1);
            DrawBackButton(gl, 3);
        } else if (LevelScreen) {
            DrawBackground(gl, 2);
            DrawBackButton(gl, 3);
        } else if (Instruction) {
            DrawBackground(gl, 0);
            DrawBackground(gl, 4);
            DrawBackButton(gl, 3);
        }

    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        xMouse1 = (int) convertX(x, width);
        yMouse1 = (int) convertY(y, height);
        System.out.print(xMouse1 + " " + yMouse1);
        System.out.println();

    }


    void check() {
        if (Home) {
            if ((xMouse1 >= 34 && xMouse1 <= 64) && (yMouse1 >= 65 && yMouse1 <= 82)) {
                PlayScreen = true;
                Home = false;
            } else if ((xMouse1 >= 34 && xMouse1 <= 64) && (yMouse1 >= 42 && yMouse1 <= 59)) {
                Instruction = true;
                Home = false;
            } else if ((xMouse1 >= 34 && xMouse1 <= 64) && (yMouse1 >= 18 && yMouse1 <= 37)) {
                System.exit(0);
            }
        } else if (PlayScreen) {
            if ((xMouse1 >= 34 && xMouse1 <= 64) && (yMouse1 >= 65 && yMouse1 <= 82)) {
                LevelScreen = true;
                PlayScreen = false;
            }
            if ((xMouse1 >= 1 && xMouse1 <= 7) && (yMouse1 >= 90 && yMouse1 <= 97)) {
                PlayScreen = false;
                Home = true;
            }
        } else if (LevelScreen) {
            if ((xMouse1 >= 1 && xMouse1 <= 7) && (yMouse1 >= 90 && yMouse1 <= 97)) {
                PlayScreen = true;
                LevelScreen = false;
            }
        } else if (Instruction) {
            if ((xMouse1 >= 1 && xMouse1 <= 7) && (yMouse1 >= 90 && yMouse1 <= 97)) {
                Home = true;
                Instruction = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        check();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void DrawBackground(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }

    public void DrawBackButton(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(-0.9, 1.75 - 0.9, 0);
        gl.glScaled(0.1, 0.1, 1);
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }


    private double convertX(double x, double width) {
        return (x / width) * 100;
    }

    private double convertY(double y, double height) {
        return (1 - (y / height)) * 100;
    }
}

