package game;

import rendering.*;
import world.*;

public class GameLoop {
    private RenderPanel panel;
    private Input input;
    private Player player;
    private Map map;
    private Raycaster raycaster;
    private WallRenderer wallRenderer;
    private FloorCeilingRenderer floorCeilingRenderer;
    private boolean running = true;

    public GameLoop(RenderPanel panel, Input input, Player player, Map map,
                    Raycaster raycaster, WallRenderer wallRenderer, FloorCeilingRenderer floorCeilingRenderer) {
        this.panel = panel;
        this.input = input;
        this.player = player;
        this.map = map;
        this.raycaster = raycaster;
        this.wallRenderer = wallRenderer;
        this.floorCeilingRenderer = floorCeilingRenderer;
    }

    public void start() {
        long lastTime = System.nanoTime(); //records time just before loop starts
        while (running) {
            long now = System.nanoTime(); //records current time in nanoseconds
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // gives seconds since last frame
            lastTime = now; //resets last time

            player.update(input, deltaTime, map);
            panel.renderFrame(raycaster, wallRenderer, floorCeilingRenderer, player, map);
            panel.repaint();

            try { Thread.sleep(16); } catch (InterruptedException e) {} // rough ~60fps cap
        }
    }
}