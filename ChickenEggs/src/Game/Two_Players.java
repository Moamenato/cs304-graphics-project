package Game;
import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.glu.GLU;

public class Two_Players extends Anim_Listener {
    // global variables for the game
    Obj obj = new Obj();
    int whoWin = 0;
    boolean restartGame = false;
    public static int random = 3;
    public static boolean spaceClicked = false;
    public static float minutes = 0, seconds = 0;
    int newMaxWidth = Anim_Listener.maxWidth / 2;
    public BitSet keyBits = new BitSet(256);

    // Players Variables
    static GLUT glutLeft = new GLUT();
    static GLUT glutRight = new GLUT();
    int[] chickenLeft = new int[]{5, 20, 35};
    int[] chickenRight = new int[]{55, 70, 85};
    int leftStart = chickenLeft[(int) (Math.random() * 3)] + 2;
    int rightStart = chickenRight[(int) (Math.random() * 3)] + 2;
    Player left = new Player(chickenLeft, false, false, 0, 1, 0, 5, 5, leftStart, 78, 0.75f, newMaxWidth / 2.0f, 5.0f);
    Player right = new Player(chickenRight, false, false, 0, 1, 0, 5, 5, rightStart, 78, 0.75f, newMaxWidth + newMaxWidth / 2.0f, 5.0f);

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textures.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolder + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        left.reset(true);
        right.reset(false);
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        obj.drawBackground(gl);

        if (restartGame) {
            if (whoWin == 1) {
                obj.drawString(gl, glutLeft, "Left Player Wins!", -0.2f, 0.0f);
            } else if (whoWin == 0) {
                obj.drawString(gl, glutLeft, "It's a Tie!", -0.2f, 0.0f);
            } else {
                obj.drawString(gl, glutRight, "Right Player Wins!", -0.2f, 0.0f);
            }
            obj.drawString(gl, glutRight, "Press R to restart!", -0.2f, -0.1f);
        } else {
            if (spaceClicked) {
                obj.drawString(gl, glutLeft, "Game Paused!", -0.2f, 0.0f);
            }
            if (left.getCurrHealth() <= 0 && right.getCurrHealth() <= 0) {
                if (left.getTotalScore() > right.getTotalScore()) {
                    whoWin = 1;
                } else if (left.getTotalScore() < right.getTotalScore()) {
                    whoWin = -1;
                }
                restartGame = true;
            } else if (left.getCurrHealth() <= 0 || right.getCurrHealth() <= 0 ) {
                if (left.getCurrHealth() <= 0) {
                    obj.drawString(gl, glutLeft, "Game Over!", -0.75f, 0.0f);
                    obj.drawString(gl, glutLeft, "waiting right player!", -0.75f, -0.1f);
                    if (right.getScore() == 100) {
                        if ((right.getLevel() < 3)) {
                            obj.drawString(gl, glutRight, "You Win This Level!", 0.25f, 0.0f);
                            obj.drawString(gl, glutRight, "press m to next level!", 0.25f, -0.1f);
                        } else {
                            right.setCurrHealth(0);
                            restartGame = true;
                            whoWin = -1;
                        }
                    } else {
                        right.drawGame(gl, glutRight, false);
                    }
                } else {
                    obj.drawString(gl, glutRight, "Game Over!", 0.25f, 0.0f);
                    obj.drawString(gl, glutRight, "waiting left player!", 0.25f, -0.1f);
                    if (left.getScore() == 100) {
                        if ((left.getLevel() < 3)) {
                            obj.drawString(gl, glutLeft, "You Win This Level!", -0.75f, 0.0f);
                            obj.drawString(gl, glutLeft, "press n to next level!", -0.75f, -0.1f);
                        } else {
                            left.setCurrHealth(0);
                            restartGame = true;
                            whoWin = 1;
                        }
                    } else {
                        left.drawGame(gl, glutLeft, true);
                    }
                }
            }else if (right.getScore() == 100 || left.getScore() == 100){
                if (right.getScore() == 100) {
                    if ((right.getLevel() < 3)) {
                        obj.drawString(gl, glutRight, "You Win This Level!", 0.25f, 0.0f);
                        obj.drawString(gl, glutRight, "press m to next level!", 0.25f, -0.1f);
                    } else {
                        right.setCurrHealth(0);
                        restartGame = true;
                        whoWin = -1;
                    }
                } else {
                    right.drawGame(gl, glutRight, false);
                }

                if (left.getScore() == 100) {
                    if ((left.getLevel() < 3)) {
                        obj.drawString(gl, glutLeft, "You Win This Level!", -0.75f, 0.0f);
                        obj.drawString(gl, glutLeft, "press n to next level!", -0.75f, -0.1f);
                    } else {
                        left.setCurrHealth(0);
                        restartGame = true;
                        whoWin = 1;
                    }
                } else {
                    left.drawGame(gl, glutLeft, true);
                }

            }
            else {
                left.drawGame(gl, glutLeft, true);
                right.drawGame(gl, glutRight, false);
            }
        }
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            spaceClicked = !spaceClicked;
        } else {
            // handle left player key pressed
            if (isKeyPressed(KeyEvent.VK_A) && left.getXBasket() > 3 && !spaceClicked) {
                left.setXBasket(left.getXBasket() - left.getLevel());
            } else if (isKeyPressed(KeyEvent.VK_D) && left.getXBasket() < newMaxWidth - 13 && !spaceClicked) {
                left.setXBasket(left.getXBasket() + left.getLevel());
            }
            if (isKeyPressed(KeyEvent.VK_N) && left.getScore() == 100) {
                left.goNextLevel();
            }

            // handle right player key pressed
            if (isKeyPressed(KeyEvent.VK_LEFT) && right.getXBasket() > 53 && !spaceClicked) {
                right.setXBasket(right.getXBasket() - right.getLevel());
            } else if (isKeyPressed(KeyEvent.VK_RIGHT) && right.getXBasket() < 2 * newMaxWidth - 13 && !spaceClicked) {
                right.setXBasket(right.getXBasket() + right.getLevel());
            }
            if (isKeyPressed(KeyEvent.VK_M) && right.getScore() == 100) {
                right.goNextLevel();
            }
            if (isKeyPressed(KeyEvent.VK_R) && restartGame) {
                restartGame = false;
                whoWin = 0;
                left.reset(true);
                right.reset(false);
                minutes = 0;
                seconds = 0;
            }
        }
    }
}