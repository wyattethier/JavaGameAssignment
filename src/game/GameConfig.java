package game;

/**
 * ========================================================
 * STUDENT MODIFICATION ZONE
 * ========================================================
 *
 * Welcome! This is where you'll change the game.
 * Each section corresponds to different weeks in the course.
 *
 * INSTRUCTIONS:
 * 1. Find the section for your current week
 * 2. Read the comments to understand each variable
 * 3. Change the values and run the game to see the effect
 * 4. Experiment! You can't break anything permanently.
 *
 * ========================================================
 */
public class GameConfig {

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ LEVEL SELECTOR ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Change this to play different weeks' levels (3-14) ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // Which week's level do you want to play? (3-14)
    // Week 3 = Data Types challenge (jump height + speed escape)
    // Week 4 = Decisions (coming soon)
    // ... more levels coming each week!
    public int currentWeek = 4;

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 1: PLAYER PHYSICS (Weeks 2-3: Data Types) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Modify these values to change how the player moves and jumps ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // How high does the player jump? (Try values 8-20)
    // This is the initial upward velocity when you press jump
    public double jumpStrength = 11.5;

    // How fast does the player move left/right? (Try values 3-10)
    public double moveSpeed = 3.0;

    // How fast does the player fall? (Try values 0.3-1.0)
    // Lower = floatier jumps, Higher = heavier falls
    public double gravity = 0.5;

    // Your player's name (shown in the game)
    public String playerName = "Hero";

    // Player size
    public int playerWidth = 32;
    public int playerHeight = 48;

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 2: ABILITIES (Week 4: Decisions) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Toggle abilities on/off with true/false ║
    // ║ Modify conditions to change game behavior ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // Can the player jump again while in the air?
    public boolean canDoubleJump = false;

    // Can the player jump off walls?
    public boolean canWallJump = false;

    // How many coins needed to unlock the secret door?
    public int secretDoorThreshold = 50;

    // ═══════════════════════════════════════════════════════════════
    // WEEK 4 CHALLENGE: Fix the broken goal!
    // The goal flag is broken. Change 'false' to 'true' to enable it.
    // ═══════════════════════════════════════════════════════════════
    public boolean goalEnabled = false; // TODO: Change this to true!

    // Is the player invincible? (Can't be hurt by enemies)
    public boolean invincible = false;

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 3: SPAWNING (Week 5: Loops) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Change these to control how many things appear in the level ║
    // ║ These values are used in for-loops to spawn objects ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // How many extra platforms appear in the level? (Try 0-10)
    public int extraPlatformCount = 3;

    // How many enemies patrol the level? (Try 0-5)
    public int enemyCount = 2;

    // How many coins to collect? (Try 5-20)
    public int coinCount = 10;

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 4: LEVEL LAYOUT (Weeks 8-9: Arrays) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Arrays that define where things are placed in the level ║
    // ║ Each index (0, 1, 2...) is a different platform or enemy ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // Platform Y positions (height from top of screen)
    // Lower numbers = higher on screen
    public int[] platformY = { 450, 350, 250, 180, 120 };

    // Platform X positions (distance from left edge)
    public int[] platformX = { 50, 200, 400, 550, 300 };

    // Platform widths
    public int[] platformWidths = { 120, 100, 100, 120, 150 };

    // Enemy X positions (where enemies spawn)
    public int[] enemyPositions = { 250, 500 };

    // Enemy Y positions (what platform height they're on)
    public int[] enemyY = { 402, 302 };

    // ═══════════════════════════════════════════════════════════════
    // SECTION 5: METHODS (Weeks 6-7)
    // These are helper methods you can call or modify
    // ═══════════════════════════════════════════════════════════════

    /**
     * Calculate a modified jump height based on a multiplier.
     * Week 6: Try calling this method with different values!
     *
     * @param multiplier How much to multiply the jump (1.0 = normal, 2.0 = double)
     * @return The calculated jump strength
     */
    public double calculateJumpHeight(double multiplier) {
        return jumpStrength * multiplier;
    }

    /**
     * Check if the player has collected enough coins to unlock a door.
     * Week 6: This method is called when you touch the secret door.
     *
     * @param coinsCollected How many coins the player has
     * @return true if they can unlock the door, false otherwise
     */
    public boolean canUnlockDoor(int coinsCollected) {
        return coinsCollected >= secretDoorThreshold;
    }

    /**
     * Calculate the player's score based on coins and time.
     * Week 7 TODO: Implement this method!
     *
     * @param coins     Number of coins collected
     * @param timeBonus Bonus points for completing quickly
     * @return The total score
     */
    public int calculateScore(int coins, int timeBonus) {
        // TODO: Return coins * 10 + timeBonus
        // Hint: multiply coins by 10, then add the timeBonus
        return 0; // Replace this!
    }

    // ═══════════════════════════════════════════════════════════════
    // GAME SETTINGS (You can modify these too!)
    // ═══════════════════════════════════════════════════════════════

    // Window dimensions
    public int windowWidth = 800;
    public int windowHeight = 600;

    // Starting position for the player
    public int playerStartX = 50;
    public int playerStartY = 400;

    // Goal position (where the level ends)
    public int goalX = 700;
    public int goalY = 72;
    public int goalWidth = 50;
    public int goalHeight = 50;
}
