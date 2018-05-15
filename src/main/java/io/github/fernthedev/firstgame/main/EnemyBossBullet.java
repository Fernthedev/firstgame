package io.github.fernthedev.firstgame.main;

import java.awt.*;
import java.util.Random;

public class EnemyBossBullet extends GameObject {

    private Handler handler;
    private Random r = new Random();


    public EnemyBossBullet(int x, int y, ID id, Handler handler) {
        super(x, y, id);

        this.handler = handler;

        velX =(r.nextInt(5 - -5) + -5);
        velY = 5;
    }

    public void tick() {
        x +=velX;
        y +=velY;

        //if(x <= 0 || x >= Game.WIDTH - 16)  velX *= -1;
        //if(y <= 0 || y >= Game.HEIGHT - 32)  velY *= -1;

        if(y >= Game.HEIGHT) handler.removeObject(this);

        handler.addObject(new Trail(x,y,ID.Trail,Color.RED,16,16,0.02f,handler));

    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
