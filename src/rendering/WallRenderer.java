    package rendering;

    import javax.imageio.ImageIO;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import java.io.InputStream;

    public class WallRenderer {
        private BufferedImage wall;
        public WallRenderer() {
            try {
                InputStream stream = getClass().getResourceAsStream("wall.png"); //loads the image as bytes

                if (stream == null) { //makes sure the stream can't be null to avoid later issues
                    throw new RuntimeException("Could not find wall texture");
                }

                wall = ImageIO.read(stream);

                if (wall == null) {
                    throw new RuntimeException("ImageIO could not read wall.png");
                }
                System.out.println("Loaded wall texture: " + wall.getWidth() + "x" + wall.getHeight());

            } catch (IOException e) { //catches any further I/O exceptions
                throw new RuntimeException("Failed to load wall texture", e);
            }
        }

        public void draw(RayHit[] hits, int[] pixels, int width, int height) {
            for (int x = 0; x < width; x++) {
                RayHit hit = hits[x];

                double distance = hit.getDistance();
                if (distance <= 0) {
                    System.err.println("Invalid ray distance: " + distance);
                    continue;
                }

                int textureX = Math.min(wall.getWidth() - 1, (int)(hit.getWallX() * wall.getWidth())); //the X value of the ray hit on the wall image

                // taller strip = closer wall
                int wallHeight = (int) (height / distance);
                int wallTop = (height - wallHeight) / 2;
                int wallBottom = wallTop + wallHeight;

                double sideBrightness; //for side shading of the textures (adds a more 2D look)

                if (hit.isVerticalSide()) {
                    sideBrightness = 1.0;
                } else {
                    sideBrightness = 0.6;
                }

                double brightness = sideBrightness * Math.max(0.2, 1.0 / (1.0 + distance * 0.1)); //calculates a brightness number based on distance from the player (must be bigger than 0.2 so walls don't become too dark)

                for (int y = Math.max(0, wallTop); y < Math.min(height, wallBottom); y++) {
                    double wallPosition = (double)(y - wallTop) / wallHeight;
                    int textureY = Math.min(wall.getHeight() - 1, (int)(wallPosition * wall.getHeight()));

                    int colour = wall.getRGB(textureX, textureY);
                    int r = (colour >> 16) & 0xFF; //shifts the above number left by 16 bits so it occupies the first 2 hex digits
                    int g = (colour >> 8) & 0xFF; //shifts the above number left by 8 bits so it occupies the second 2 hex digits
                    int b = colour & 0xFF;

                    r = (int)(r * brightness);
                    g = (int)(g * brightness);
                    b = (int)(b * brightness);

                    int finalColour = (r << 16) | (g << 8) | b; //OR operators combine the hex digits
                    pixels[y * width + x] = finalColour;
                }
            }
        }
    }