package Game;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class One_Player_Mode extends Anim_Listener {
    // variables for the game
    Obj obj = new Obj();
    public static boolean spaceClicked = false;
    static GLUT glut = new GLUT();
    public static float minutes = 0, seconds = 0, random = 3;
    public static int lv;
    // variables for the player
    int[] chickenPositions = {10, 40, 70};
    float start = chickenPositions[(int) (Math.random() * random)] + 2;
    OnePlayer player = new OnePlayer(chickenPositions, false, false, false, 0, 1, 0, 5, 5, start, 78, 0.75f, maxWidth / 2.0f, 5.0f);
    String userName = "";
    int maxscore = 0;

    public One_Player_Mode(int level) {
        minutes = 0;
        seconds = 0;
        spaceClicked = false;
        lv = level;
        player.reset(level);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void getHighScores() {
        List<Scores> scoresList = new ArrayList<>();

        File file = new File("src/assets/scores.txt");

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] split = line.split(" ", 2);
                    if (split.length == 2) {
                        try {
                            String playerName = split[0].trim();
                            int score = Integer.parseInt(split[1].trim());
                            scoresList.add(new Scores(playerName, score));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid score format in file: " + line);
                        }
                    }
                }
            }
            scanner.close();

            scoresList.add(new Scores(this.userName, maxscore * 10));

            scoresList.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));

            try (FileWriter writer = new FileWriter(file)) {
                int count = 0;
                for (Scores score : scoresList) {
                    writer.write(score.getName() + " " + score.getScore() + "\n");
                    count++;
                    if (count >= 20) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error managing the scores file: " + e.getMessage());
        }
    }

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
        maxscore = Math.max(maxscore, player.getTotalScore());
        System.out.println(maxscore);
        if (player.getCurrHealth() > 0 && player.getScore() < 100) {
            maxscore = Math.max(maxscore, player.getTotalScore());
            if (spaceClicked) {
                obj.drawString(gl, glut, "Game Paused!", -0.2f, 0.0f);
            }
            player.drawGame(gl, glut);
        } else if (player.getScore() == 100) {

            if ((player.getLevel() < 3)) {
                obj.drawString(gl, glut, "You Win press n if you want to play next level", -0.5f, 0f);
            } else {
                if (!player.getSavedscore()) {
                    player.setSavedscore(true);
                    getHighScores();
                }
                obj.drawString(gl, glut, "Congratulations, You Win all levels press R to start again", -0.5f, 0f);
            }
        } else {
            if (!player.getSavedscore()) {
                player.setSavedscore(true);
                getHighScores();
            }
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
            if (keyCode == KeyEvent.VK_LEFT && player.getXBasket() > 3 && !spaceClicked) {
                player.setXBasket(player.getXBasket() - 2);
            } else if (keyCode == KeyEvent.VK_RIGHT && player.getXBasket() < maxWidth - 13 && !spaceClicked) {
                player.setXBasket(player.getXBasket() + 2);
            }
        }
        if (keyCode == KeyEvent.VK_N && player.getScore() == 100 && player.getLevel() < 3) {
            player.goNextLevel();
        }
        if (keyCode == KeyEvent.VK_R && (player.getCurrHealth() == 0 || (player.getScore() == 100 && player.getLevel() == 3))) {
            player.reset(lv);
            minutes = 0;
            seconds = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
