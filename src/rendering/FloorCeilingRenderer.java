package rendering;

public class FloorCeilingRenderer {

    public void draw(RayHit[] hits, int[] pixels, int width, int height) {
        for (int x = 0; x < width; x++) {
            RayHit hit = hits[x];
            int wallHeight = (int) (height / hit.getDistance()); //how tall the wall should be in pixels
            int wallTop = Math.max(0, (height - wallHeight) / 2); //uses wallHeight to find the top of wall
            int wallBottom = Math.min(wallTop + wallHeight, height); //Math.max/min ensures no division by 0 error / pixels outside of screen

            // ceiling: everything above the wall strip
            for (int y = 0; y < wallTop; y++) {
                pixels[y * width + x] = 0x3333AA; // arbitrary blue-ish
            }
            // floor: everything below
            for (int y = wallBottom; y < height; y++) {
                pixels[y * width + x] = 0x555555; // arbitrary grey
            }
        }
    }
}