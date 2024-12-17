package Game;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class AI_Player_Mode extends Anim_Listener {
    // global variables for the game
    Obj obj = new Obj();
    int whoWin = 0;
    boolean restartGame = false;
    public static int random = 3;
    public static boolean spaceClicked = false;
    public static float minutes = 0, seconds = 0;
    int newMaxWidth = Anim_Listener.maxWidth / 2;
    public BitSet keyBits = new BitSet(256);
    int maxscore = 0;
    // Players Variables
    static GLUT glutPC = new GLUT();
    static GLUT glutPlayer = new GLUT();
    int[] chickenLeft = new int[]{5, 20, 35};
    int[] chickenRight = new int[]{55, 70, 85};
    int leftStart = chickenLeft[(int) (Math.random() * 3)] + 2;
    int rightStart = chickenRight[(int) (Math.random() * 3)] + 2;
    AIPlayer PC = new AIPlayer(chickenLeft, false, false, false, 0, 1, 0, 5, 5, leftStart, 78, 0.75f, newMaxWidth / 2.0f, 5.0f);
    AIPlayer player = new AIPlayer(chickenRight, false, false, false, 0, 1, 0, 5, 5, rightStart, 78, 0.75f, newMaxWidth + newMaxWidth / 2.0f, 5.0f);
    boolean savedScore = false;
    String playerName = "";

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    public void getHighScores() {
        List<Scores> vec = new ArrayList<Scores>();
        try {
            File file = new File("src/assets/scores.txt");
            Scanner scanner = new Scanner(file);
            StringBuilder curUserName = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] split = line.split(" ", 2);
                int score = 0;
                if (split.length == 2) {
                    try {
                        score = Integer.parseInt(split[1].trim());
                        curUserName.setLength(0);
                        curUserName.append(split[0].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
                vec.add(new Scores(curUserName.toString().trim(), score));
            }
            scanner.close();
            vec.add(new Scores(this.playerName, maxscore * 10));


            vec.sort((user1, user2) -> Integer.compare(user2.getScore(), user1.getScore()));


            try (FileWriter myWriter = new FileWriter("src/assets/scores.txt")) {
                int num = 1;
                for (Scores i : vec) {
                    myWriter.write(i.name + " " + i.score + "\n");
                    num++;
                    if (num == 21) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (FileNotFoundException err) {
            System.out.println("Scores file not found: " + err.getMessage());
            err.printStackTrace();
        }
    }

    public AI_Player_Mode(int level) {
        minutes = 0;
        seconds = 0;
        spaceClicked = false;
        if (level == 1) {
            PC.reset(true);
            player.reset(false);
        } else if (level == 2) {
            PC.goNextLevel();
            player.goNextLevel();
        } else if (level == 3) {
            PC.goNextLevel();
            PC.goNextLevel();
            player.goNextLevel();
            player.goNextLevel();
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
        PC.reset(false);
        player.reset(true);
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        obj.drawBackground(gl);
        maxscore = Math.max(maxscore, player.getTotalScore());
        if ((restartGame && player.getCurrHealth() <= 0)) {
            if (!player.getSaveScore()) {
                player.setSavedscore(true);
                getHighScores();
            }
            if (whoWin == 1) {
                obj.drawString(gl, glutPC, "PC Wins!", -0.2f, 0.0f);
            } else if (whoWin == 0) {
                obj.drawString(gl, glutPC, "It's a Tie!", -0.2f, 0.0f);
            } else {
                obj.drawString(gl, glutPlayer, "Player Wins!", -0.2f, 0.0f);
            }
            obj.drawString(gl, glutPlayer, "Press R to restart!", -0.2f, -0.1f);
        } else {
            if (spaceClicked) {
                obj.drawString(gl, glutPC, "Game Paused!", -0.2f, 0.0f);
            }
            if (PC.getCurrHealth() <= 0 && player.getCurrHealth() <= 0) {

                if (PC.getTotalScore() > player.getTotalScore()) {
                    whoWin = 1;
                } else if (PC.getTotalScore() < player.getTotalScore()) {
                    whoWin = -1;
                }
                restartGame = true;
            } else if (PC.getCurrHealth() <= 0 || player.getCurrHealth() <= 0) {
                if (player.getCurrHealth() <= 0) {
                    obj.drawString(gl, glutPlayer, "Game Is Over!", 0.25f, 0.0f);
                    obj.drawString(gl, glutPlayer, "waiting PC!", 0.25f, -0.1f);
                    if (PC.getScore() == 100) {
                        if ((PC.getLevel() < 3)) {
                            PC.goNextLevel();
                        } else {
                            PC.setCurrHealth(0);
                            restartGame = true;
                            whoWin = 1;
                        }
                    } else {
                        PC.drawGame(gl, glutPC, false);
                    }
                } else {
                    obj.drawString(gl, glutPC, "Game Is Over!", -0.75f, 0.0f);
                    obj.drawString(gl, glutPC, "waiting Player!", -0.75f, -0.1f);
                    if (player.getScore() == 100) {
                        if ((player.getLevel() < 3)) {
                            obj.drawString(gl, glutPlayer, "You Win This Level!", 0.25f, 0.0f);
                            obj.drawString(gl, glutPlayer, "press n to next level!", 0.25f, -0.1f);
                        } else {
                            player.setCurrHealth(0);
                            restartGame = true;
                            whoWin = -1;
                        }
                    } else {
                        player.drawGame(gl, glutPlayer, true);
                    }
                }
            } else if (player.getScore() == 100 || PC.getScore() == 100) {
                if (player.getScore() == 100) {
                    if ((player.getLevel() < 3)) {
                        obj.drawString(gl, glutPlayer, "You Win This Level!", 0.25f, 0.0f);
                        obj.drawString(gl, glutPlayer, "press n to next level!", 0.25f, -0.1f);
                    } else {
                        player.setCurrHealth(0);
                        restartGame = true;
                        whoWin = -1;
                    }
                } else {
                    player.drawGame(gl, glutPlayer, true);
                }
                if (PC.getScore() == 100) {
                    if ((PC.getLevel() < 3)) {
                        PC.goNextLevel();
                    } else {
                        PC.setCurrHealth(0);
                        restartGame = true;
                        whoWin = 1;
                    }
                } else {
                    PC.drawGame(gl, glutPC, false);
                }
            } else {
                PC.drawGame(gl, glutPC, false);
                player.drawGame(gl, glutPlayer, true);
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
            if (isKeyPressed(KeyEvent.VK_LEFT) && player.getXBasket() > 53 && !spaceClicked) {
                player.setXBasket(player.getXBasket() - player.getLevel());
            } else if (isKeyPressed(KeyEvent.VK_RIGHT) && player.getXBasket() < 2 * newMaxWidth - 13 && !spaceClicked) {
                player.setXBasket(player.getXBasket() + player.getLevel());
            }
            if (isKeyPressed(KeyEvent.VK_N) && player.getScore() == 100) {
                player.goNextLevel();
            }
            if (isKeyPressed(KeyEvent.VK_R)) {
                restartGame = false;
                whoWin = 0;
                PC.reset(false);
                player.reset(true);
                minutes = 0;
                seconds = 0;
            }
        }
    }
}