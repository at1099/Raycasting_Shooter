package rendering;

import game.Config;
import world.Map;
import world.Player;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class Raycaster {
    private int screenWidth;

    public Raycaster(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    private RayHit castSingleRay(int x, Player player, Map map){
       //work out this ray's angle to the FOV
       double rayAngle = (player.angle - Config.FOV / 2.0) + (x / (double)screenWidth) * Config.FOV;

        double rayDirX = Math.cos(rayAngle);
        double rayDirY = Math.sin(rayAngle);

        double posX, posY;
        posX = player.getX(); posY = player.getY();

        int rayGridX = (int) posX; //grid square containing player
        int rayGridY = (int) posY; //grid square containing player

        double deltaDistX = abs(1 / rayDirX); //deltaDist is how much further along the ray until the next grid boundary (vertical or horizontal) fromm player pos
        double deltaDistY = abs(1 / rayDirY); //finds how far the ray moves in the y direction for every 1 unit in its diagonal direction

        double sideDistX;
        if (rayDirX < 0) {
            sideDistX = (posX - rayGridX) * deltaDistX;
        } else {
            sideDistX = (rayGridX + 1.0 - posX) * deltaDistX; //finds distance in x to next grid line from the last grid line
        }

        double sideDistY;
        if (rayDirY < 0) {
            sideDistY = (posY - rayGridY) * deltaDistY;
        } else {
            sideDistY = (rayGridY + 1.0 - posY) * deltaDistY; //finds distance in y to next grid line from last grid line
        }

        int stepX; int stepY;
        if (rayDirX < 0)
            stepX = -1;
        else
            stepX = 1;

        if (rayDirY < 0)
            stepY = -1;
        else
            stepY = 1;

        boolean hit =  false;
        int side = 0; //meaningless initial assignment to avoid errors
        while (!hit) {
            if (sideDistX < sideDistY) { //vertical boundary is next boundary
                sideDistX += deltaDistX; //the new distance from the player to the boundary after this one (deltaDist is distance between boundaries)
                rayGridX += stepX;
                side = 0;
            } else{
                sideDistY += deltaDistY;
                rayGridY += stepY;
                side = 1;
            }

            if (map.isWall(rayGridX, rayGridY)) {
                hit = true;
            }
        }
        double perpWallDistance; //the distance to the wall needs to be perpendicular because using the raw ray length would cause the wall to be curved at each side (fisheye effect)
        double distAlongWall;
        if (side == 0){
            perpWallDistance = sideDistX - deltaDistX;
            double hitY = (posY + rayDirY * perpWallDistance); //finds the exact coords of where the ray hits the wall
            distAlongWall = hitY - floor(hitY); //takes the fractional part (to see how far along the wall it hits)
        } else{
            perpWallDistance = sideDistY - deltaDistY;
            double hitX = (posX + rayDirX * perpWallDistance); //finds the exact coords of where the ray hits the wall
            distAlongWall = hitX - floor(hitX);
        }

        RayHit rayHit = new RayHit(perpWallDistance, side==0, 1, distAlongWall);
        return rayHit;
    }

    public RayHit[] castAllRays(Player player, Map map) {
        RayHit[] hits = new RayHit[screenWidth];
        for (int x = 0; x < screenWidth; x++) { //cast single ray for each column
            hits[x] = castSingleRay(x, player, map);
        }
        return hits;
    }
}