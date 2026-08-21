package rendering;

import world.Map;
import world.Player;
import world.Sprite;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class RenderPanel extends JPanel {

    private final int width;
    private final int height;
    private final BufferedImage frameBuffer;
    private final int[] pixels; // direct access to the image's underlying pixel array

    public RenderPanel(int width, int height) {
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));

        frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) frameBuffer.getRaster().getDataBuffer()).getData();
    }

    // Called once per frame, before repaint(), to fill the buffer with the new frame's contents
    public void renderFrame(Raycaster raycaster, WallRenderer wallRenderer,
                            FloorCeilingRenderer floorCeilingRenderer, SpriteRenderer spriteRenderer, Player player, Map map) {

        RayHit[] hits = raycaster.castAllRays(player, map);

        floorCeilingRenderer.draw(hits, pixels, width, height);
        wallRenderer.draw(hits, pixels, width, height);

        for (int i = 0; i < map.getSpriteList().size(); i++){
            Sprite sprite = map.getSpriteList().get(i);
            spriteRenderer.draw(hits, pixels, width, height, player, sprite);
        }
    }

    // Swing calls this automatically whenever repaint() is triggered
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clears the panel first — standard practice, don't skip it
        g.drawImage(frameBuffer, 0, 0, null); // the actual "blit" — draws the whole buffer at once
    }

    public int getWidth2() { return width; }   // helper if needed elsewhere (JPanel already has getWidth())
    public int getHeight2() { return height; }
}