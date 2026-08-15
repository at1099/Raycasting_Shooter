package rendering;

public class WallRenderer {

    public void draw(RayHit[] hits, int[] pixels, int width, int height) {
        for (int x = 0; x < width; x++) {
            RayHit hit = hits[x];

            double distance = hit.getDistance();
            if (distance <= 0) {
                System.err.println("Invalid ray distance: " + distance);
                continue;
            }
            // taller strip = closer wall
            int wallHeight = (int) (height / distance);
            int wallTop = (height - wallHeight) / 2;
            int wallBottom = wallTop + wallHeight;

            int colour = hit.isVerticalSide() ? 0x999999 : 0x666666; // two shades for 2D effect

            int r = (colour >> 16) & 0xFF; //shifts the above number left by 16 bits so it occupies the first 2 hex digits
            int g = (colour >> 8) & 0xFF; //shifts the above number left by 8 bits so it occupies the second 2 hex digits
            int b = colour & 0xFF;

            double brightness = Math.max(0.2, 1.0 / (1.0 + distance * 0.1)); //calculates a brightness number based on distance from the player (must be bigger than 0.2 so walls don't become too dark)
            r = (int)(r * brightness);
            g = (int)(g * brightness);
            b = (int)(b * brightness);

            int finalColour = (r << 16) | (g << 8) | b; //OR operators combine the hex digits

            for (int y = Math.max(0, wallTop); y < Math.min(height, wallBottom); y++) {
                pixels[y * width + x] = finalColour;
            }
        }
    }
}