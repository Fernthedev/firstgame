package io.github.fernthedev.firstgame.main;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

public class HUD {

    public static int HEALTH = 100;

    private int greenvalue = 255;

    private int score = 0;
    private int level =1;

    public void tick() {
        //HEALTH--;
        HEALTH = (int) Game.clamp(HEALTH,0,100);

        greenvalue = (int) Game.clamp(greenvalue,0,255);

        greenvalue = HEALTH *2;
        score++;
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(15,15,200,32);
        g.setColor(new Color(75,greenvalue,0));
        g.fillRect(15,15,HEALTH*2,32);
        if(HEALTH == 0) {
            g.setColor(Color.RED);
            g.drawRect(15, 15, 200, 32);
        }else {
            g.setColor(Color.WHITE);
            g.drawRect(15, 15, 200, 32);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score,15,64);
        g.drawString("Level: " + level,15,80);
        //g.drawString("FPS: " + NumberFormat.getNumberInstance(Locale.US).format(Game.frames),10,104);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
