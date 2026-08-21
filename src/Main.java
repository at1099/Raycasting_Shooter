
import game.*;
import rendering.*;
import world.*;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Raycaster"); //creates a window called Raycaster
        RenderPanel panel = new RenderPanel(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT); //creates new RenderPanel
        Input input = new Input(); //creates new Input

        frame.add(panel); //Adds the panel inside the screen
        frame.addKeyListener(input); //Sends inputs to Input
        frame.pack(); //resizes window to fit panel's size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //tells java what to do when close button pressed
        frame.setResizable(false);
        frame.setVisible(true);
        frame.requestFocusInWindow(); //makes the os focus on this window so it doesn't have to be pressed on first

        Map map = new Map();
        Player player = new Player(2.5, 2.5, 0);
        Raycaster raycaster = new Raycaster(Config.SCREEN_WIDTH);
        WallRenderer wallRenderer = new WallRenderer();
        FloorCeilingRenderer floorCeilingRenderer = new FloorCeilingRenderer();
        SpriteRenderer spriteRenderer = new SpriteRenderer();

        new GameLoop(panel, input, player, map, raycaster, wallRenderer, floorCeilingRenderer, spriteRenderer).start();
    }
}