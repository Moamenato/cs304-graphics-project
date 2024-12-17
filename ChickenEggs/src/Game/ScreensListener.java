package Game;

import Texture.TextureReader;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ScreensListener extends Anim_Listener implements MouseListener, MouseMotionListener {


    int maxWidth = 100;
    int maxHeight = 100;
    double xMouse1 = 0, yMouse1 = 0;
    boolean Home = true, PlayScreen = false, LevelScreen = false;
    boolean Instruction = false;
    AudioInputStream AUDIO_STREAM;
    Clip CLIP;

    public ScreensListener() {
    }

    String[] textureNames = {
            "Home.png", "Game.png", "Levels.png", "backButton.png", "how.png", "soundon.png", "soundoff.png"
    };
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];
    boolean close = false;

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        try {
            AUDIO_STREAM = AudioSystem.getAudioInputStream(new File("src/assets/chicken dance song.wav"));
            CLIP = AudioSystem.getClip();
            CLIP.open(AUDIO_STREAM);
            CLIP.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
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

    int cnt = 1;

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        if (close) {
            CLIP.stop();
            DrawAudioButton(gl, 6);
        } else {
            CLIP.start();
        }
        if (Home) {
            DrawBackground(gl, 0);
            if (close) DrawAudioButton(gl, 6);
            else DrawAudioButton(gl, 5);
        } else if (PlayScreen) {
            DrawBackground(gl, 1);
            DrawBackButton(gl, 3);
            if (close) DrawAudioButton(gl, 6);
            else DrawAudioButton(gl, 5);
        } else if (LevelScreen) {
            DrawBackground(gl, 2);
            DrawBackButton(gl, 3);
            if (close) DrawAudioButton(gl, 6);
            else DrawAudioButton(gl, 5);
        } else if (Instruction) {
            DrawBackground(gl, 0);
            DrawBackground(gl, 4);
            DrawBackButton(gl, 3);
            if (close) DrawAudioButton(gl, 6);
            else DrawAudioButton(gl, 5);
        }


    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        double width = e.getComponent().getWidth();
        double height = e.getComponent().getHeight();
        xMouse1 = (int) convertX(x, width);
        yMouse1 = (int) convertY(y, height);
//        System.out.print(xMouse1 + " " + yMouse1);
//        System.out.println();
    }


    void check() {
        if (Home) { // Home Screen
            if ((xMouse1 >= 36 && xMouse1 <= 63) && (yMouse1 >= 79 && yMouse1 <= 94)) {
                PlayScreen = true;
                Home = false;
            } else if ((xMouse1 >= 36 && xMouse1 <= 63) && (yMouse1 >= 54 && yMouse1 <= 69)) {
                Instruction = true;
                Home = false;
            } else if ((xMouse1 >= 34 && xMouse1 <= 64) && (yMouse1 >= 14 && yMouse1 <= 30)) {
                System.exit(0);
            }
            // highScores
            else if ((xMouse1 >= 36 && xMouse1 <= 62) && (yMouse1 >= 35 && yMouse1 <= 48)) {
                printHighScores();
            }
        } else if (PlayScreen) {
            if ((xMouse1 >= 34 && xMouse1 <= 65) && (yMouse1 >= 74 && yMouse1 <= 92)) {
                LevelScreen = true;
                PlayScreen = false;
            }
            if ((xMouse1 >= 1 && xMouse1 <= 7) && (yMouse1 >= 90 && yMouse1 <= 97)) {
                PlayScreen = false;
                Home = true;
            }
            // MultiPlayer
            if ((xMouse1 >= 34 && xMouse1 <= 65) && (yMouse1 >= 45 && yMouse1 <= 64)) {
                TwoPlayer(1);
            }
            // AI
            if ((xMouse1 >= 34 && xMouse1 <= 65) && (yMouse1 >= 16 && yMouse1 <= 35)) {
                onePlayer(0);
            }
        } else if (LevelScreen) {
            //Easy
            if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 77 && yMouse1 <= 95)) {
                onePlayer(1);
                LevelScreen = true;
                PlayScreen = false;
            }
            //Medium
            if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 42 && yMouse1 <= 59)) {
                onePlayer(2);
                LevelScreen = true;
                PlayScreen = false;
            }
            // Hard
            if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 12 && yMouse1 <= 29)) {
                onePlayer(3);
                LevelScreen = true;
                PlayScreen = false;
            }
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
        if ((xMouse1 >= 91 && xMouse1 <= 97) && (yMouse1 >= 91 && yMouse1 <= 94)) {
            if (!close) {
                close = true;
            } else {
                close = false;
            }
        }
    }

    public void handleLevelsScreen() {
        //Easy
        if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 77 && yMouse1 <= 95)) {
            onePlayer(1);
            LevelScreen = true;
            PlayScreen = false;
        }
        //Medium
        if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 42 && yMouse1 <= 59)) {
            onePlayer(2);
            LevelScreen = true;
            PlayScreen = false;
        }
        // Hard
        if ((xMouse1 >= 35 && xMouse1 <= 64) && (yMouse1 >= 12 && yMouse1 <= 29)) {
            onePlayer(3);
            LevelScreen = true;
            PlayScreen = false;
        }
        if ((xMouse1 >= 1 && xMouse1 <= 7) && (yMouse1 >= 90 && yMouse1 <= 97)) {
            PlayScreen = true;
            LevelScreen = false;
        }
    }

    public void onePlayer(int level) {
        String UserName = JOptionPane.showInputDialog(null, "Enter your name");
        String message = "Hello " + UserName + "Are you want to Play?";

        if (UserName != null && !UserName.isEmpty()) {
            int response = JOptionPane.showConfirmDialog(null, message, "New Game", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (level == 0) {
                    new GameMode(UserName, level, true);
                } else {
                    new GameMode(UserName, level, false);
                }
            }
        }
    }

    public void TwoPlayer(int level) {
        String UserName = JOptionPane.showInputDialog(null, "Enter your name");
        String UserName2 = JOptionPane.showInputDialog(null, "Enter your name");

        if (UserName != null && UserName2 != null && !UserName.isEmpty() && !UserName2.isEmpty()) {
            int response = JOptionPane.showConfirmDialog(null, "New Game", "New Game", JOptionPane.OK_CANCEL_OPTION);
            if (response == JOptionPane.OK_OPTION) {
                new GameMode(UserName, UserName2, level);
            }
        }
    }

    public void printHighScores() {
        int cur = 1;
        StringBuilder message = new StringBuilder();
        try {
            File file = new File("src/Assets/scores.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                message.append(cur).append(") ").append(line).append("\n");
                cur++;
            }
            scanner.close();
            JOptionPane.showMessageDialog(null, message.toString(), "HIGH SCORES", JOptionPane.INFORMATION_MESSAGE);

        } catch (FileNotFoundException err) {
            err.printStackTrace();
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

    public void DrawAudioButton(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(0.9, 1.75 - 0.9, 0);
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

