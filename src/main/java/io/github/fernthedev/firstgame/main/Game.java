package io.github.fernthedev.firstgame.main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 640,HEIGHT =  WIDTH / 12*9;
    private static final long serialVersionUID = -7016397730883067077L;

    private Thread thread;
    private Boolean running = false;

    private Handler handler;
    private Random r;
    private HUD hud;
    private Spawn spawnner;
    private Menu menu;

    /**
     * STATE OF PAUSED
     */
    public static boolean paused = false;

    /**
     * DIFFICULTY
     * 0 = NORMAL
     * 1 = HARD
     */
    public int dif = 0;

    //public static int frames = 0;


    /**
     * GAME STATES
     */
    public enum STATE {
        Menu,
        Help,
        Select,
        End,
        Game
    }


    /**
     * VARIABLE FOR ACCESSING STATES
     */
    public static STATE gameState = STATE.Menu;


    public static BufferedImage sprite_sheet = null;


    /**
     * MAIN
     */
    public Game() {

        BufferedImageLoader loader = new BufferedImageLoader();
        sprite_sheet = loader.loadImage("/icon.png");
        System.out.println("Loaded icon");

        handler = new Handler();
        hud = new HUD();
        menu = new Menu(this,handler,hud);

        this.addKeyListener(new KeyInput(handler,this));
        this.addMouseListener(menu);

        new Window(WIDTH,HEIGHT,"A NEW GAME",this);


        spawnner = new Spawn(handler,hud,this);
        r = new Random();

        if(gameState == STATE.Game){
            handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32,ID.Player,handler));
            handler.addObject(new BasicEnemy(r.nextInt(WIDTH - 50),r.nextInt(HEIGHT - 50),ID.BasicEnemey,handler));
        }else{
            int amount = r.nextInt(15);
            if(amount < 10) amount =10;
            for(int i = 0; i<amount; i++) {
                handler.addObject(new MenuParticle(r.nextInt(WIDTH),r.nextInt(HEIGHT),ID.MenuParticle,handler));
            }
        }


    }

    /**
     * RUNNABLE METHOD
     */
    public void run() {
        this.requestFocus();
    long lastTime= System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    //int frames = this.frames;
    int frames = 0;
    while(running) {
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;
        while(delta >= 1) {
            tick();
            delta--;
        }
        if(running)
            render();
        frames++;

        if(System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            //System.out.println("FPS: " + frames + " or formmated: " + NumberFormat.getNumberInstance(Locale.US).format(frames));
            frames = 0;
        }
    }
    stop();
    }


    /**
     * TICK METHOD
     */
    private void tick() {
        if(gameState == STATE.Game) {
            if(!paused) {
                handler.tick();
                hud.tick();
                spawnner.tick();

                if (HUD.HEALTH <= 0) {
                    HUD.HEALTH = 100;
                    gameState = STATE.End;
                    handler.clearEnemies();
                    int amount = r.nextInt(15);
                    if (amount < 10) amount = 10;
                    for (int i = 0; i < amount; i++) {
                        handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.MenuParticle, handler));
                    }
                }
            }
        }else if(gameState == STATE.Menu || gameState == STATE.End || gameState == STATE.Select) {
            menu.tick();
            handler.tick();
        }
    }

    /**
     * RENDERING OBJECTS AND GAME
     */
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH,HEIGHT);

        handler.render(g);

        if(paused){
            g.setColor(Color.WHITE);
            g.drawString("Paused",100,100);
        }
        if(gameState == STATE.Game) {
            hud.render(g);
        }else if(gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End || gameState == STATE.Select) {
            menu.render(g);
        }

        g.dispose();
        bs.show();
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */

    public static float clamp(float var, float min, float max) {
        if(var >= max)
            return var = max;
        else if(var <= min)
            return var = min;
        else
            return var;

    }

    /**
     * STARTING METHOD
     */
    public static void main(String[] args) {
        new Game();
    }


    /**
     * Threads
     */
    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Stopping game threads
     */
    public synchronized void stop() {

        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
