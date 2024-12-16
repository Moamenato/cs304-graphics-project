package Game;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import javax.media.opengl.GL;
import com.sun.opengl.util.GLUT;
import static Game.Two_Players.*;

public class Player implements Player_Template {
    Obj obj = new Obj();
    private int[] ChickenPositions;
    private boolean isCollision, restart, spaceClickedPlayer;
    private int score, level, totalScore, maxHealth, currHealth, mode;
    private float xEgg, yEgg, eggSpeed, xBasket, yBasket, secondsPlayer, minutesPlayer;
    List<List<Float>> list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));

    public Player(int[] ChickenPositions, boolean isCollision, boolean restart, int score, int level, int totalScore, int maxHealth, int currHealth, float xEgg, float yEgg, float eggSpeed, float xBasket, float yBasket, int mode) {
        this.ChickenPositions = ChickenPositions;
        this.isCollision = isCollision;
        this.restart = restart;
        this.score = score;
        this.level = level;
        this.totalScore = totalScore;
        this.maxHealth = maxHealth;
        this.currHealth = currHealth;
        this.xEgg = xEgg;
        this.yEgg = yEgg;
        this.eggSpeed = eggSpeed;
        this.xBasket = xBasket;
        this.yBasket = yBasket;
        this.mode = mode;
        if (mode == 1) {
            secondsPlayer = One_Player.seconds;
            minutesPlayer = One_Player.minutes;
            spaceClickedPlayer = One_Player.spaceClicked;
        } else {
            secondsPlayer = Two_Players.seconds;
            minutesPlayer = Two_Players.minutes;
            spaceClickedPlayer = Two_Players.spaceClicked;
        }
    }

    // Setter methods
    public void setEggSpeed(float eggSpeed) {
        this.eggSpeed = eggSpeed;
    }

    public void setIsCollision(boolean isCollision) {
        this.isCollision = isCollision;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setChickenPositions(int[] ChickenPositions) {
        this.ChickenPositions = ChickenPositions;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrHealth(int currHealth) {
        this.currHealth = currHealth;
    }

    public void setXBasket(float xBasket) {
        this.xBasket = xBasket;
    }

    public void setYBasket(float yBasket) {
        this.yBasket = yBasket;
    }

    public void setXEgg(float xEgg) {
        this.xEgg = xEgg;
    }

    public void setYEgg(float yEgg) {
        this.yEgg = yEgg;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setList(List<List<Float>> list) {
        this.list = list;
    }

    public void setSpaceClickedPlayer(boolean spaceClickedPlayer) {
        this.spaceClickedPlayer = spaceClickedPlayer;
    }

    public void setSecondsPlayer(float secondsPlayer) {
        this.secondsPlayer = secondsPlayer;
    }

    public void setMinutesPlayer(float minutesPlayer) {
        this.minutesPlayer = minutesPlayer;
    }

    // Getter methods
    public float getEggSpeed() {
        return eggSpeed;
    }

    public boolean getIsCollision() {
        return isCollision;
    }

    public boolean getRestart() {
        return restart;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int[] getChickenPositions() {
        return ChickenPositions;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public float getXBasket() {
        return xBasket;
    }

    public float getYBasket() {
        return yBasket;
    }

    public float getXEgg() {
        return xEgg;
    }

    public float getYEgg() {
        return yEgg;
    }

    public int getMode() {
        return mode;
    }

    public boolean getSpaceClickedPlayer() {
        return spaceClickedPlayer;
    }

    public float getSecondsPlayer() {
        return secondsPlayer;
    }

    public float getMinutesPlayer() {
        return minutesPlayer;
    }

    public List<List<Float>> getList() {
        return list;
    }

    public void reset(float newXBasket) {
        xBasket = newXBasket;
        list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));
        maxHealth = 5;
        currHealth = 5;
        isCollision = false;
        score = 0;
        eggSpeed = 0.75f;
        level = 1;
        random = 3;
        secondsPlayer = 0;
        minutesPlayer = 0;
    }

    public void drawGame(GL gl, GLUT glut, float shift) {
        List<Integer> pops = new ArrayList<>();
        for (List<Float> i : list) {
            double dist = obj.sqrDistance((int) xBasket, (int) yBasket, Math.round(i.get(0)), Math.round(i.get(1)));
            if (dist <= 20 && !isCollision && !spaceClickedPlayer) {
                isCollision = true;
            }
            if (!isCollision && i.get(1) > 0) {
                if (!spaceClickedPlayer) {
                    i.set(1, i.get(1) - eggSpeed);
                }
                if (mode == 1) {
                    obj.drawSprite(gl, i.get(0), i.get(1), 1, 0.8f, 0.8f);
                } else {
                    obj.drawSprite(gl, i.get(0), i.get(1), 1, 0.5f, 0.5f);
                }
            } else if (!spaceClickedPlayer) {
                if (isCollision) {
                    score += 10;
                    totalScore++;
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

        if (!pops.isEmpty() && !spaceClickedPlayer) {
            for (int i : pops) {
                list.remove(i);
            }
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
        }

        if (level == 3 && !spaceClickedPlayer) {
            if (list.getFirst().get(1) <= 50 && list.size() <= 1) {
                list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
            }
        }

        if (!spaceClickedPlayer && (shift == 0 || shift == 5)) {
            secondsPlayer += 1 / 30f;
            if (secondsPlayer >= 60) {
                minutesPlayer++;
                secondsPlayer = 0;
            }
        }

        for (int i : ChickenPositions) {
            if (mode == 1) {
                obj.drawSprite(gl, i, 80, 0, 1.5f, 1.5f);
            } else {
                obj.drawSprite(gl, i, 80, 0, 1.0f, 1.0f);
            }
        }

        for (int i = 0; i < currHealth; i++) {
            obj.drawSprite(gl, shift + i * 5, 92, 4, 0.5f, 0.5f);
        }

        for (int i = currHealth; i < maxHealth; i++) {
            obj.drawSprite(gl, shift + i * 5, 92, 5, 0.5f, 0.5f);
        }

        if (mode == 2) {
            obj.drawSprite(gl, xBasket, yBasket, 3, 1.0f, 1.0f);
            if (shift == 0) {
                obj.drawString(gl, glut, "Score: " + score, -0.95f, 0.82f);
                obj.drawString(gl, glut, "Level: " + level, -0.6f, 0.82f);
                obj.drawString(gl, glut, "Time " + (int) minutesPlayer + " : " + (int) secondsPlayer, -0.1f, 0.85f);
            } else {
                obj.drawString(gl, glutRight, "Score: " + score, 0.25f, 0.82f);
                obj.drawString(gl, glutRight, "Level: " + level, 0.6f, 0.82f);
            }
        } else {
            obj.drawSprite(gl, xBasket, yBasket, 3, 2f, 2f);
            obj.drawString(gl, glut, "Level: " + level, 0.7f, 0.9f);
            obj.drawString(gl, glut, "Score: " + score, -0.3f, 0.9f);
            obj.drawString(gl, glut, "Time " + (int) minutesPlayer + " : " + (int) secondsPlayer, 0.2f, 0.9f);
        }
    }

    public void goNextLevel() {
        level++;
        score = 0;
        maxHealth--;
        currHealth = maxHealth;
        eggSpeed += 0.2f;
        list.clear();
        list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
        if (mode == 1) {
            random = 4;
            ChickenPositions = new int[]{10, 30, 50, 70};
            if (level == 2) {
                eggSpeed = 1.0f;
            } else {
                eggSpeed = 1.2f;
            }
        }
    }
}