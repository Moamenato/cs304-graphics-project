package Game;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import Texture.TextureReader;
import com.sun.opengl.util.GLUT;
import javax.media.opengl.glu.GLU;

public class One_Player extends Anim_Listener {
    // variables for the game
    Obj obj = new Obj();
    static GLUT glut = new GLUT();
    int[] ChickenPositions = {10, 40, 70};
    float xBasket = maxWidth / 2.0f, yBasket = 5.0f, random = 3;
    float xEgg = ChickenPositions[(int) (Math.random() * random)] + 2, yEgg = 78;
    float eggSpeed = 0.75f, minutes = 0, seconds = 0;
    boolean isCollision = false, spaceClicked = false;
    int maxHealth = 5, currHealth = 5, score = 0, level = 1;
    List<List<Float>> list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));

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
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        obj.drawBackground(gl);

        if (spaceClicked) {
            obj.drawString(gl, glut, "Game Paused!", -0.2f, 0.0f);
        }
        if (currHealth > 0 && score < 100) {
            drawGame(gl);
        } else if (score == 100) {
            if ((level < 3)) {
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            spaceClicked = !spaceClicked;
        } else {
            if (keyCode == KeyEvent.VK_LEFT && xBasket > 3 && !spaceClicked) {
                xBasket -= 2;
            } else if (keyCode == KeyEvent.VK_RIGHT && xBasket < maxWidth - 13 && !spaceClicked) {
                xBasket += 2;
            }
        }
        if (keyCode == KeyEvent.VK_N && score == 100 && level < 3) {
            level++;
            ChickenPositions = new int[]{10, 30, 50, 70};
            random = 4;
            score = 0;
            maxHealth--;
            currHealth = maxHealth;
            eggSpeed = (level == 2) ? 1.0f : 1.2f;
            list.clear();
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
        }
        if (keyCode == KeyEvent.VK_R && currHealth == 0) {
            reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void drawGame(GL gl) {
        List<Integer> pops = new ArrayList<>();
        for (List<Float> i : list) {
            double dist = obj.sqrDistance((int) xBasket, (int) yBasket, Math.round(i.get(0)), Math.round(i.get(1)));
            if (dist <= 50 && !isCollision) {
                isCollision = true;
            }
            if (!isCollision && i.get(1) > 0) {
                if (!spaceClicked) {
                    i.set(1, i.get(1) - eggSpeed);
                }
                obj.drawSprite(gl, i.get(0), i.get(1), 1, 0.8f, 0.8f);
            } else {
                if (isCollision) {
                    score += 10;
                    if (score > 0 && score % 50 == 0) {
                        eggSpeed += 0.15f;
                    }
                } else {
                    currHealth--;
                }
                isCollision = false;
                pops.add(list.indexOf(i));
            }
        }

        if (!pops.isEmpty() && !spaceClicked) {
            for (int i : pops) {
                list.remove(i);
            }
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
        }

        if (level == 3) {
            if (list.getFirst().get(1) <= 50 && list.size() <= 1) {
                list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
            }
        }

        if (!spaceClicked) {
            seconds += 1 / 60f;
            if (seconds >= 60) {
                minutes++;
                seconds = 0;
            }
        }

        for (int i : ChickenPositions) {
            obj.drawSprite(gl, i, 80, 0, 1.5f, 1.5f);
        }

        for (int i = 0; i < currHealth; i++) {
            obj.drawSprite(gl, 5 + (i * 5), 90, 4, 0.5f, 0.5f);
        }

        for (int i = currHealth; i < maxHealth; i++) {
            obj.drawSprite(gl, 5 + (i * 5), 90, 5, 0.5f, 0.5f);
        }

        obj.drawString(gl, glut, "Score: " + score, -0.3f, 0.9f);
        obj.drawString(gl, glut, "Time " + (int) minutes + " : " + (int) seconds, 0.2f, 0.9f);
        obj.drawSprite(gl, xBasket, yBasket, 3, 2f, 2f);
        obj.drawString(gl, glut, "Level: " + level, 0.7f, 0.9f);
    }

    public void reset() {
        ChickenPositions = new int[]{10, 40, 70};
        random = 3;
        xBasket = maxWidth / 2.0f;
        yBasket = 5.0f;
        maxHealth = 5;
        currHealth = 5;
        isCollision = false;
        score = 0;
        eggSpeed = 0.75f;
        level = 1;
        minutes = 0;
        seconds = 0;
        list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));
    }
}