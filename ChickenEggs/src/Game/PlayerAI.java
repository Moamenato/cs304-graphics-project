package Game;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import javax.media.opengl.GL;
import java.util.Collections;
import com.sun.opengl.util.GLUT;

import static Game.One_playerVsAi.*;
public class PlayerAI implements Player_Template {
    Obj obj = new Obj();
    private int[] ChickenPositions;
    private boolean isCollision, restart;
    private int score, level, totalScore, maxHealth, currHealth;
    private float xEgg, yEgg, eggSpeed, xBasket, yBasket;
    List<List<Float>> list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));

    public PlayerAI(int[] ChickenPositions, boolean isCollision, boolean restart, int score, int level, int totalScore, int maxHealth, int currHealth, float xEgg, float yEgg, float eggSpeed, float xBasket, float yBasket) {
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

    public void setList(List<List<Float>> list) {
        this.list = list;
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

    public List<List<Float>> getList() {
        return list;
    }

    public void reset(boolean isLeft) {
        if (isLeft) {
            xBasket = (maxWidth / 2.0f) / 2.0f;
        } else {
            xBasket = (maxWidth / 2.0f) + (maxWidth / 2.0f) / 2.0f;
        }
        list = new ArrayList<>(Collections.singletonList(new ArrayList<>(Arrays.asList(xEgg, yEgg))));
        maxHealth = 5;
        currHealth = 5;
        isCollision = false;
        score = 0;
        eggSpeed = 0.75f;
        level = 1;
    }

    public void drawGame(GL gl, GLUT glut, boolean isLeft) {
        List<Integer> pops = new ArrayList<>();
        for (List<Float> i : list) {
            double dist = obj.sqrDistance((int) xBasket, (int) yBasket, Math.round(i.get(0)), Math.round(i.get(1)));
            if (!isLeft && xBasket > i.get(0) && (i.get(1)-yBasket)<=25){
                xBasket=xBasket-level;
            }
            if (!isLeft && xBasket < i.get(0) && (i.get(1)-yBasket)<=25){
                xBasket=xBasket+level;
            }
            if (dist <= 20 && !isCollision && !spaceClicked) {
                isCollision = true;
            }
            if (!isCollision && i.get(1) > 0) {
                if (!spaceClicked) {
                    i.set(1, i.get(1) - eggSpeed);
                }
                obj.drawSprite(gl, i.get(0), i.get(1), 1, 0.5f, 0.5f);
            } else if (!spaceClicked) {
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

        if (!pops.isEmpty() && !spaceClicked) {
            for (int i : pops) {
                list.remove(i);
            }
            list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
        }

        if (level == 3 && !spaceClicked) {
            if (list.get(0).get(1) <= 50 && list.size() <= 1) {
                list.add(new ArrayList<>(Arrays.asList(ChickenPositions[(int) (Math.random() * random)] + 2f, 78f)));
            }
        }

        if (!spaceClicked && isLeft) {
            seconds += 1 / 30f;
            if (seconds >= 60) {
                minutes++;
                seconds = 0;
            }
        }

        for (int i : ChickenPositions) {
            obj.drawSprite(gl, i, 80, 0, 1.0f, 1.0f);
        }

        for (int i = 0; i < currHealth; i++) {
            if (isLeft) {
                obj.drawSprite(gl, i * 5, 92, 4, 0.5f, 0.5f);
            } else {
                obj.drawSprite(gl, 10 + (i * 5) + (maxWidth / 2.0f), 92, 4, 0.5f, 0.5f);
            }
        }

        for (int i = currHealth; i < maxHealth; i++) {
            if (isLeft) {
                obj.drawSprite(gl, i * 5, 92, 5, 0.5f, 0.5f);
            } else {
                obj.drawSprite(gl, 10 + (i * 5) + (maxWidth / 2.0f), 92, 5, 0.5f, 0.5f);
            }
        }

        obj.drawSprite(gl, xBasket, yBasket, 3, 1.0f, 1.0f);
        if (isLeft) {
            obj.drawString(gl, glut, "Score: " + score, -0.95f, 0.82f);
            obj.drawString(gl, glut, "Level: " + level, -0.6f, 0.82f);
        } else {
            obj.drawString(gl, glutRight, "Score: " + score, 0.25f, 0.82f);
            obj.drawString(gl, glutRight, "Level: " + level, 0.6f, 0.82f);
        }
        if (isLeft) {
            obj.drawString(gl, glut, "Time " + (int) minutes + " : " + (int) seconds, -0.1f, 0.85f);
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
    }
}