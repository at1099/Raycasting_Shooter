package world;

import game.Input;

public class Player {
    public double x, y;
    public double angle; // radians

    public Player(double x, double y, double angle) {
        this.x = x; this.y = y; this.angle = angle;
    }

    public double getX() {
        return x;

    }
    public double getY() {
        return y;
    }

    public void update(Input input, double deltaTime, Map map) {
        double moveSpeed = 1.0 * deltaTime; // units per second
        double rotSpeed = 1.0 * deltaTime;

        if (input.isKeyDown(java.awt.event.KeyEvent.VK_LEFT)) angle -= rotSpeed;
        if (input.isKeyDown(java.awt.event.KeyEvent.VK_RIGHT)) angle += rotSpeed;

        double dx = Math.cos(angle) * moveSpeed;
        double dy = Math.sin(angle) * moveSpeed;

        if (input.isKeyDown(java.awt.event.KeyEvent.VK_UP)) {
            double newX = x + dx;
            double newY = y + dy;
            if (!map.isWall(newX, y)) x = newX;
            if (!map.isWall(x, newY)) y = newY;
        }
        if (input.isKeyDown(java.awt.event.KeyEvent.VK_DOWN)) {
            double newX = x - dx;
            double newY = y - dy;
            if (!map.isWall(newX, y)) x = newX;
            if (!map.isWall(x, newY)) y = newY;
        }
    }
}