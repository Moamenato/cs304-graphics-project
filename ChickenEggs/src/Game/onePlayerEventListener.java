package Game;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.util.List;

public class onePlayerEventListener implements GLEventListener, KeyListener {
    public boolean stop;
    int maxWidth = 100, maxHeight = 100;
    String[] textureNames = {"Chicken.png", "Egg.png", "Egg 2.png", "Basket.png", "Heart 1.png", "Heart 2.png", "Back.png"};
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int[] textures = new int[textureNames.length];
    static GLUT glut = new GLUT();

    // variables for the game
    int[] ChickenPositions1 = new int[]{10, 40, 70};

    float randomL = 3;
    float xBasket = maxWidth / 2.0f, yBasket = 5.0f;
    float xEgg = ChickenPositions1[(int) (Math.random() * randomL)] + 2, yEgg = 78;
    List<List<Float>> list = new ArrayList<>(List.of(new ArrayList<>(List.of(xEgg, yEgg))));
    float xScore = maxWidth - 15, yScore = maxHeight - 15;

    int maxHealth = 5, currHealth = 5;
    boolean isCollision = false;
    int score = 0;
    float eggSpeed = 0.5f;
    int level = 3;

    @Override
    public void init(GLAutoDrawable gld) {
//        for (int i = 0; i < 4; i++) {
//            list.add(new ArrayList<>());
//        }
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
                        texture[i].getPixels() // Imagedata
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
        else if (score == 100)
            drawString(gl, "You Win press n if you want play next level", -0.5f, 0f);
        else
            drawString(gl, "Game Over", 0f, 0f);


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
//            xBasket -= 1 + (level - 1) / 4;
            xBasket -= 2;
        } else if (keyCode == KeyEvent.VK_RIGHT && xBasket < maxWidth - 13) {
//            xBasket += 1 + (level - 1) / 4;
            xBasket += 2;
        }

        if (keyCode == KeyEvent.VK_N && score == 100) {
            level++;
            ChickenPositions1 = new int[]{20, 40, 60, 80};
            randomL = (level > 1) ? 4 : 3;
            score = 0;
            maxHealth -= (level - 1);
            currHealth = maxHealth;

            eggSpeed = (level == 2) ? 0.6f : 0.8f;
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

    public void reset() {
        xBasket = maxWidth / 2.0f;
        yBasket = 3.0f;
        xEgg = ChickenPositions1[(int) (Math.random() * 3)];
        yEgg = maxHeight;
        xScore = maxWidth - 15;
//        xHealth = 1.0f;
        isCollision = false;
        score = 0;
        eggSpeed = 1f;
    }

    public void drawGame(GL gl) {
        double dist = sqrDistance((int) xBasket, (int) yBasket, (int) xEgg, (int) yEgg);
        if (dist <= 50 && !isCollision)
            isCollision = true;
        List<Float>l=new ArrayList<>();
        if (level == 3) {
            for (List<Float> i : list) {
                System.out.print(i+" ");
                DrawSprite(gl, i.get(0), i.get(1), 1, 0.8f, 0.8f);
//                float top = list.get(list.size() - 1).get(1);
                i.set(1,i.get(1)-1);
//                list.get(list.size() - 1).set(1,- 1);
            }
            float top = 0;
            if (list.size() > 0)
                top = list.get(list.size() - 1).get(1);

            System.out.println(top);

            if (top <= 50 && list.size() <= 1) {
                l.add(ChickenPositions1[(int) (Math.random() * randomL)] + 2f);
                l.add(78f);
                list.add(l);
            }
        }
        if (!isCollision && yEgg > 0) {
            DrawSprite(gl, xEgg, yEgg, 1, 0.8f, 0.8f);

            yEgg -= eggSpeed;

        } else {
            if (isCollision) {
                score += 10;
                if (score > 0 && score % 50 == 0)
                    eggSpeed += 0.15f;
            } else
                currHealth--;
//            if (list.size() > 0)
//                list.remove(0);
            isCollision = false;
            yEgg = 78;
            xEgg = ChickenPositions1[(int) (Math.random() * randomL)] + 2;


        }

        for (int i : ChickenPositions1)
            DrawSprite(gl, i, 80, 0, 1.5f, 1.5f);

        for (int i = 0; i < currHealth; i++)
            DrawSprite(gl, 5 + (i * 5), 90, 4, 0.5f, 0.5f);

        for (int i = currHealth; i < maxHealth; i++)
            DrawSprite(gl, 5 + (i * 5), 90, 5, 0.5f, 0.5f);

        drawString(gl, "Score :" + score, 0.7f, 0.9f);

        DrawSprite(gl, xBasket, yBasket, 3, 2f, 2f);
        drawString(gl, "Level :" + level, 0.5f, 0.9f);
    }

    public void drawString(GL gl, String status, float xPos, float yPos) {
        gl.glRasterPos2f(xPos, yPos); // Set position for drawing

        for (char c : status.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }
}
//                list.get((int) (Math.random() * randomL)).add(yEgg);

//                    for (int j = 1; j < i.size(); j++) {
//                        float o = i.get(j) - eggSpeed;
//                        i.set(j, o);
//                        if (o <= 0) {
//                            i.remove(j);
//                            j--;
//                        } else if (o <= 50) {
//                            DrawSprite(gl, ChickenPositions1[Math.round(i.get(0))] + 2, 78, 1, 0.8f, 0.8f);
//                            i.add(78f);
//                        }
//
//                    }