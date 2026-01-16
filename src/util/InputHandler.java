package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the game.
 *
 * Tracks which keys are currently pressed and which were just pressed this frame.
 * Students: You don't need to modify this file.
 */
public class InputHandler implements KeyListener {

    // Track state of all possible keys
    private boolean[] keys = new boolean[256];
    private boolean[] keysJustPressed = new boolean[256];
    private boolean[] keysPrevious = new boolean[256];

    /**
     * Check if a key is currently being held down.
     * @param keyCode The key code (e.g., KeyEvent.VK_SPACE)
     * @return true if the key is pressed
     */
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) return false;
        return keys[keyCode];
    }

    /**
     * Check if a key was just pressed this frame (not held).
     * Useful for actions that should only trigger once per press.
     * @param keyCode The key code (e.g., KeyEvent.VK_SPACE)
     * @return true if the key was just pressed
     */
    public boolean isKeyJustPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= keysJustPressed.length) return false;
        return keysJustPressed[keyCode];
    }

    /**
     * Called each frame to update the "just pressed" state.
     * Must be called at the START of each game update.
     */
    public void update() {
        for (int i = 0; i < keys.length; i++) {
            keysJustPressed[i] = keys[i] && !keysPrevious[i];
            keysPrevious[i] = keys[i];
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
