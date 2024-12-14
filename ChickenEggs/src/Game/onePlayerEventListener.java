package Game;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.util.List;

import static Game.app.animator;

public class onePlayerEventListener implements GLEventListener, KeyListener {
    int maxWidth = 100, maxHeight = 100;
    String[] textureNames = {"Chicken.png", "Egg.png", "Egg 2.png", "Basket.png", "Heart 1.png", "Heart 2.png", "Back.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    static GLUT glut = new GLUT();

    // variables for the game
    int[] ChickenPositions = new int[]{10, 40, 70};
    float randomL = 3;
    float xBasket = maxWidth / 2.0f, yBasket = 5.0f;
    float xEgg = ChickenPositions[(int) (Math.random() * randomL)] + 2, yEgg = 78;
    List<List<Float>> list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));
    int maxHealth = 5, currHealth = 5;
    boolean isCollision = false;
    int score = 0;
    float eggSpeed = 0.75f;
    int level = 1;
    boolean spaceClicked = false;
    float minutes = 0, seconds = 0;

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textures.length; i++) {
            try {
                texture[i] = TextureReader.readTexture("assets//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Image data
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
        DrawBackground(gl);

        if (currHealth > 0 && score < 100)
            drawGame(gl);
        else if (score == 100) {
            if ((level < 3)) {
                drawString(gl, "You Win press n if you want play next level", -0.5f, 0f);
            } else {
                drawString(gl, "Congratulations, You Win all levels press R to start again", -0.5f, 0f);
            }
        } else
            drawString(gl, "Game Over, Sorry for your lose press R to start again", -0.5f, 0f);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && xBasket > 3) {
            xBasket -= 2;
        } else if (keyCode == KeyEvent.VK_RIGHT && xBasket < maxWidth - 13) {
            xBasket += 2;
        }

        if (keyCode == KeyEvent.VK_N && score == 100 && level < 3) {
            level++;
            ChickenPositions = new int[]{10, 30, 50, 70};
            randomL = 4;
            score = 0;
            maxHealth--;
            currHealth = maxHealth;
            eggSpeed = (level == 2) ? 1.0f : 1.2f;
            list.clear();
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * randomL)] + 2f, 78f)));
            minutes = 0;
            seconds = 0;
        }
        if (keyCode == KeyEvent.VK_R) {
            reset();
        }

        if (keyCode == KeyEvent.VK_SPACE) {
            spaceClicked = !spaceClicked;
            if (spaceClicked) {
                if (animator.isAnimating())
                    animator.stop();
            } else {
                if (!animator.isAnimating())
                    animator.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void setOrgCoOrdVertex(GL gl) {
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
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);    // Turn Blending On
        gl.glPushMatrix();
        setOrgCoOrdVertex(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite(GL gl, float x, float y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        setOrgCoOrdVertex(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public double sqrDistance(int x, int y, int x2, int y2) {
        return Math.pow(x - x2, 2) + Math.pow(y - y2, 2);
    }

    public void drawGame(GL gl) {
        List<Integer> pops = new ArrayList<>();
        for (List<Float> i : list) {
            double dist = sqrDistance((int) xBasket, (int) yBasket, Math.round(i.get(0)), Math.round(i.get(1)));
            if (dist <= 50 && !isCollision)
                isCollision = true;

            if (!isCollision && i.get(1) > 0) {
                DrawSprite(gl, i.get(0), i.get(1), 1, 0.8f, 0.8f);
                i.set(1, i.get(1) - eggSpeed);
            } else {
                if (isCollision) {
                    score += 10;
                    if (score > 0 && score % 50 == 0)
                        eggSpeed += 0.15f;
                } else {
                    currHealth--;
                }
                isCollision = false;
                pops.add(list.indexOf(i));
            }
        }

        if (!pops.isEmpty()) {
            for (int i : pops)
                list.remove(i);
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * randomL)] + 2f, 78f)));
        }

        if (level == 3) {
            if (list.get(0).get(1) <= 50 && list.size() <= 1) {
                list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * randomL)] + 2f, 78f)));
            }
        }

        seconds += 1 / 60f;
        if (seconds >= 60) {
            minutes++;
            seconds = 0;
        }

        for (int i : ChickenPositions)
            DrawSprite(gl, i, 80, 0, 1.5f, 1.5f);

        for (int i = 0; i < currHealth; i++)
            DrawSprite(gl, 5 + (i * 5), 90, 4, 0.5f, 0.5f);

        for (int i = currHealth; i < maxHealth; i++)
            DrawSprite(gl, 5 + (i * 5), 90, 5, 0.5f, 0.5f);

        drawString(gl, "Score :" + score, -0.3f, 0.9f);
        drawString(gl, "Time " + (int) minutes + " : " + (int) seconds, 0.2f, 0.9f);
        DrawSprite(gl, xBasket, yBasket, 3, 2f, 2f);
        drawString(gl, "Level :" + level, 0.7f, 0.9f);
    }

    public void drawString(GL gl, String status, float xPos, float yPos) {
        gl.glRasterPos2f(xPos, yPos);
        for (char c : status.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }

    public void reset() {
        ChickenPositions = new int[]{10, 40, 70};
        randomL = 3;
        xBasket = maxWidth / 2.0f;
        yBasket = 5.0f;
        list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));
        maxHealth = 5;
        currHealth = 5;
        isCollision = false;
        score = 0;
        eggSpeed = 0.75f;
        level = 1;
        minutes = 0;
        seconds = 0;
    }
}