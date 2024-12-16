package Game;
import java.awt.event.*;
import java.io.IOException;
import java.util.BitSet;
import javax.media.opengl.*;
import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.glu.GLU;

public class One_Player extends Anim_Listener {
    // variables for the game
    Obj obj = new Obj();
    public static boolean spaceClicked = false;
    public BitSet keyBits = new BitSet(256);
    public static float minutes = 0, seconds = 0;

    // variables for the player
    public static int random = 3;
    static GLUT glut = new GLUT();
    int[] ChickenPositions = {10, 40, 70};
    int start = ChickenPositions[(int) (Math.random() * random)] + 2;
    Player player = new Player(ChickenPositions, false, false, 0, 1, 0, 5, 5, start, 78, 0.75f, maxWidth / 2.0f, 5.0f, 1);

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
        player.reset(maxWidth / 2.0f);
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        obj.drawBackground(gl);
        if (player.getCurrHealth() > 0 && player.getScore() < 100) {
            if (spaceClicked) {
                obj.drawString(gl, glut, "Game Paused!", -0.2f, 0.0f);
            }
            player.drawGame(gl, glut, 5);
        } else if (player.getScore() == 100) {
            if ((player.getLevel() < 3)) {
                obj.drawString(gl, glut, "You Win press n if you want to play next level", -0.5f, 0f);
            } else {
                obj.drawString(gl, glut, "Congratulations, You Win all levels press R to start again", -0.5f, 0f);
            }
        } else {
            obj.drawString(gl, glut, "Game Over, Sorry for your lose press R to start again", -0.5f, 0f);
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
            player.setSpaceClickedPlayer(!player.getSpaceClickedPlayer());
        } else {
            if (keyCode == KeyEvent.VK_LEFT && player.getXBasket() > 3) {
                player.setXBasket(player.getXBasket() - 2);
            } else if (keyCode == KeyEvent.VK_RIGHT && player.getXBasket() < maxWidth - 13) {
                player.setXBasket(player.getXBasket() + 2);
            }

            if (keyCode == KeyEvent.VK_N && player.getScore() == 100 && player.getLevel() < 3) {
                player.goNextLevel();
            }
            if (keyCode == KeyEvent.VK_R && (player.getCurrHealth() == 0 || player.getLevel() >= 3)) {
                seconds = 0;
                minutes = 0;
                player.reset(maxWidth / 2.0f);
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                spaceClicked = !spaceClicked;
                player.setSpaceClickedPlayer(!player.getSpaceClickedPlayer());
            }
        }
    }
}