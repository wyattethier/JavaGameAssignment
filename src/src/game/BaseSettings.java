package game;

import interfaces.Constants;

/**
 * Internal game settings and logic helpers.
 * This class is hidden from students to keep GameConfig.java clean.
 */
public class BaseSettings implements Constants {

    // Window dimensions (Engine only)
    public int windowWidth = 800;
    public int windowHeight = 600;

    // Starting position for the player
    public int playerStartX = 50;
    public int playerStartY = 400;

    // Goal position defaults
    public int goalWidth = 50;
    public int goalHeight = 50;

    /**
     * Calculate a modified jump height based on a multiplier.
     * Only use the student-modified jumpStrength for the Week 3 challenge.
     * Note: This uses fields from GameConfig (the child class).
     */
    public double calculateJumpHeight(double multiplier) {
        // We cast this to GameConfig to access the student-editable fields
        if (!(this instanceof GameConfig))
            return 11.0 * multiplier;
        GameConfig config = (GameConfig) this;

        double baseJump = (config.currentWeek == 3) ? config.jumpStrength : 11.0;
        return baseJump * multiplier;
    }

    /**
     * Determine the current move speed.
     * Only uses the student-modified moveSpeed for the Week 3 challenge.
     */
    public double determineMoveSpeed() {
        if (!(this instanceof GameConfig))
            return 3.0;
        GameConfig config = (GameConfig) this;

        return (config.currentWeek == 3) ? config.moveSpeed : 3.0;
    }

    /**
     * Check if the player has collected enough coins to unlock a door.
     */
    public boolean canUnlockDoor(int coinsCollected) {
        return coinsCollected >= SECRET_DOOR_THRESHOLD;
    }
}
