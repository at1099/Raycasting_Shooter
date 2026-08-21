package rendering;

import game.Config;
import world.Player;
import world.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SpriteRenderer {
    private BufferedImage spriteSheetImage;
    private int animationRow = 0;
    private int animationFrame = 0;
    private int frameOffset = 128; //how far along the spriteSheet the first frame starts

    public SpriteRenderer() {
        try {
            InputStream stream = getClass().getResourceAsStream("zombies.png"); //loads the image as bytes

            if (stream == null) { //makes sure the stream can't be null to avoid later issues
                throw new RuntimeException("Could not find wall texture");
            }

            spriteSheetImage = ImageIO.read(stream);

            if (spriteSheetImage == null) {
                throw new RuntimeException("ImageIO could not read wall.png");
            }
            System.out.println("Loaded wall texture: " + spriteSheetImage.getWidth() + "x" + spriteSheetImage.getHeight());

        } catch (IOException e) { //catches any further I/O exceptions
            throw new RuntimeException("Failed to load wall texture", e);
        }
    }

    public void draw(RayHit[] hits, int[] pixels, int width, int height, Player player, Sprite sprite){
        int frameWidth = spriteSheetImage.getWidth() / 4; //finds width of one frame
        int frameHeight = spriteSheetImage.getHeight() / 5; //finds height of one frame
        int frameStartX = animationFrame * frameWidth + frameOffset;
        int frameStartY = animationRow * frameHeight; //finds starting point of each frame

        double dx = sprite.getX() - player.getX();
        double dy = sprite.getY() - player.getY();

        double distance = Math.sqrt(dx * dx + dy * dy); //finds sprite's distance from player with pythag

        double spriteAngle = Math.atan2(dy, dx); //sprite angle to the player
        double angleDifference = spriteAngle - player.angle; //how far to left or right of player's view the sprite is

        while (angleDifference > Math.PI) { //use of while incase the angle difference is a multiple of 2pi radians greater than the value
            angleDifference -= 2 * Math.PI;
        }
        while (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI;
        }

        while (angleDifference < -Math.PI) {
            angleDifference += 2 * Math.PI;
        }

        double screenX = (angleDifference / Config.FOV + 0.5) * width; //where the sprite will appear on the screen
        int spriteHeight = (int)(height / distance); //calculates sprite height on screen
        if (spriteHeight <= 0) {
            return;
        }
        double spriteAspectRatio = (double) spriteSheetImage.getWidth() / spriteSheetImage.getHeight();
        int spriteWidth = (int)(spriteHeight * spriteAspectRatio); //calculates width from height to width ratio of sprite

        int spriteLeft = (int)screenX - spriteWidth / 2;
        int spriteRight = (int)screenX + spriteWidth / 2;
        int spriteTop = (height - spriteHeight) / 2;
        int spriteBottom = spriteTop + spriteHeight;


        for (int y = Math.max(0, spriteTop); y < Math.min(height, spriteBottom); y++) {
            double spriteYPosition = (double)(y - spriteTop) / spriteHeight;
            int textureY = frameStartY + (int)(spriteYPosition * frameHeight);

            for (int x = Math.max(0, spriteLeft); x < Math.min(width, spriteRight); x++) {
                if (distance < hits[x].getDistance()){
                    double spriteXPosition = (double)(x - spriteLeft) / spriteWidth; //gives value from 0 to 1 depending how left/right on image the pixel is
                    int textureX = frameStartX + (int)(spriteXPosition * frameWidth); //gets the exact pixel

                    int colour = spriteSheetImage.getRGB(textureX, textureY);
                    int alpha = (colour >> 24) & 0xFF; //alpha is the transparency of the image, makes sure the outer pixels aren't drawn on
                    if (alpha == 0) {
                        continue;
                    }
                    pixels[y * width + x] = colour;
                }

            }
        }
    }
}
