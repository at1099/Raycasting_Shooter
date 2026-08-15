package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Input implements KeyListener {
    private Set<Integer> keysDown = new HashSet<>();

    public boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    @Override public void keyPressed(KeyEvent e) { keysDown.add(e.getKeyCode()); }
    @Override public void keyReleased(KeyEvent e) { keysDown.remove(e.getKeyCode()); }
    @Override public void keyTyped(KeyEvent e) {} // not needed, but required by the interface
}