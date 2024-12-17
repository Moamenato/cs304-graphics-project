package Game;

import java.util.List;

public interface Player_Template {

    // Setter methods
    void setEggSpeed(float eggSpeed);

    void setIsCollision(boolean isCollision);

    void setRestart(boolean restart);

    void setScore(int score);

    void setLevel(int level);

    void setTotalScore(int totalScore);

    void setChickenPositions(int[] ChickenPositions);

    void setMaxHealth(int maxHealth);

    void setCurrHealth(int currHealth);

    void setXBasket(float xBasket);

    void setYBasket(float yBasket);

    void setXEgg(float xEgg);

    void setYEgg(float yEgg);

    void setList(List<List<Float>> list);

    // Getter methods
    float getEggSpeed();

    boolean getIsCollision();

    boolean getRestart();

    int getScore();

    int getLevel();

    int getTotalScore();

    int[] getChickenPositions();

    int getMaxHealth();

    int getCurrHealth();

    float getXBasket();

    float getYBasket();

    float getXEgg();

    float getYEgg();

    List<List<Float>> getList();
}
