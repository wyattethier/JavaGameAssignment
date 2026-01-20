package world;

import java.awt.Color;

import interfaces.Constants;

/**
 * Contains level data configurations for each week of the course.
 * 
 * Students: This class defines the layout for each level.
 * You'll explore this in later weeks when working with arrays.
 */
public class LevelData implements Constants {

    /**
     * Get level configuration for a specific week/part.
     * Week 3 has two parts: 3A (jump challenge) and 3B (speed escape)
     * 
     * @param week The week number (3-14)
     * @param part The part within the week (1 = A, 2 = B, etc.)
     * @return LevelConfig for that week/part
     */
    public static LevelConfig getLevel(int week, int part) {
        switch (week) {
            case 3:
                if (part == 1) {
                    return getWeek3A_JumpChallenge();
                } else {
                    return getWeek3B_SpeedEscape();
                }
            case 4:
                return getWeek4_IfStatementChallenge();
            default:
                return getWeek3A_JumpChallenge(); // Default
        }
    }

    /**
     * Convenience method - get part 1 of a week's level.
     */
    public static LevelConfig getLevel(int week) {
        return getLevel(week, 1);
    }

    /**
     * Week 3A: "Jump Height Challenge" - Data Types (int)
     * 
     * Goal is on a high platform that requires increased jumpStrength to reach.
     * Default jumpStrength (12) cannot reach the goal platform.
     * Students must increase jumpStrength to ~18+ to complete.
     */
    private static LevelConfig getWeek3A_JumpChallenge() {
        LevelConfig config = new LevelConfig();

        config.name = "Week 3A: Jump Height Challenge";
        config.weekNumber = 3;
        config.part = 1;
        config.topic = "Data Types (int)";
        config.hint = "Hint: The goal is too high! Increase jumpStrength in GameConfig.java";
        config.nextPart = 2; // Completing this leads to part 2

        // Platform layout for jump challenge
        // Goal platform is at height 180, requiring jumpStrength ~18 to reach
        // With default jumpStrength=12 and gravity=0.5, max jump height is ~144px

        config.platformX = new int[] {
                50, // Starting platform
                180, // Step 1
                320, // Step 2 (medium height)
                480, // Step 3
                620, // HIGH GOAL PLATFORM - requires increased jump!
        };

        config.platformY = new int[] {
                480, // Starting platform (near ground)
                420, // Step 1
                350, // Step 2
                280, // Step 3
                160, // HIGH GOAL PLATFORM - 120 pixels above step 3!
        };

        config.platformWidths = new int[] {
                120, // Start
                100, // Step 1
                100, // Step 2
                100, // Step 3
                140, // Goal platform (wider for landing)
        };

        // No enemies for this challenge - focus on jumping
        config.enemyX = new int[] {};
        config.enemyY = new int[] {};

        // Goal on the high platform
        config.goalX = 650;
        config.goalY = 85;

        // No wall in this part
        config.hasEncroachingWall = false;

        return config;
    }

    /**
     * Week 3B: "Speed Escape" - Data Types (double)
     * 
     * Player must outrun an encroaching wall by increasing moveSpeed.
     * The wall moves at speed 2.5, faster than default moveSpeed of 5.0
     * (player needs to jump and move, losing time).
     */
    private static LevelConfig getWeek3B_SpeedEscape() {
        LevelConfig config = new LevelConfig();

        config.name = "Week 3B: Speed Escape";
        config.weekNumber = 3;
        config.part = 2;
        config.topic = "Data Types (double)";
        config.hint = "Hint: The wall is too fast! Increase moveSpeed in GameConfig.java";
        config.nextPart = 0; // No next part - week complete!

        // Long horizontal corridor for speed challenge
        config.platformX = new int[] {
                0, // Starting area
                150, // Gap 1
                320, // Gap 2
                490, // Gap 3
                660, // Final stretch to goal
        };

        config.platformY = new int[] {
                500, // All platforms at same height
                500,
                500,
                500,
                500,
        };

        config.platformWidths = new int[] {
                130, // Start
                100, // Mid 1
                100, // Mid 2
                100, // Mid 3
                140, // End
        };

        // No enemies - wall is the enemy!
        config.enemyX = new int[] {};
        config.enemyY = new int[] {};

        // Goal at far right
        config.goalX = 720;
        config.goalY = 425;

        // Encroaching wall settings
        config.hasEncroachingWall = true;
        config.wallStartX = 0; // Start off-screen
        config.wallSpeed = 3.7; // Faster than it looks due to required jumping!
        config.wallStartDelay = 30; // 0.5 seconds before wall starts (60fps * 0.5)
        config.wallColor = new Color(180, 20, 20);

        return config;
    }

    /**
     * Week 4: "Fix the Flag" - If-Statements (boolean)
     * 
     * The goal flag is broken! Students must change goalEnabled from false to true
     * in GameConfig.java to make the flag register as a win.
     * Simple platform layout so the focus is on understanding the if-statement.
     */
    private static LevelConfig getWeek4_IfStatementChallenge() {
        LevelConfig config = new LevelConfig();

        config.name = "Week 4: Fix the Broken Flag";
        config.weekNumber = 4;
        config.part = 1;
        config.topic = "If-Statements (boolean)";
        config.hint = "Hint: The goal is broken! Change goalEnabled to true in GameConfig.java";
        config.nextPart = 0; // Single part level

        // Simple platform layout - easy to reach the goal
        config.platformX = new int[] {
                50, // Starting platform
                200, // Step 1
                400, // Step 2
                600, // Goal platform
        };

        config.platformY = new int[] {
                480, // Starting platform
                420, // Step 1
                360, // Step 2
                300, // Goal platform
        };

        config.platformWidths = new int[] {
                130, // Start
                120, // Step 1
                120, // Step 2
                150, // Goal platform (wider)
        };

        // No enemies - focus on the if-statement concept
        config.enemyX = new int[] {};
        config.enemyY = new int[] {};

        // Goal on the final platform
        config.goalX = 650;
        config.goalY = 225;

        // THE KEY: Goal is DISABLED by default - students must fix this!
        config.goalEnabled = false;

        // Button configuration
        config.hasLevelButton = true;
        config.buttonX = 425;
        config.buttonY = 320;
        config.requiredButtonClicks = REQUIRED_BUTTON_CLICKS;

        // No wall
        config.hasEncroachingWall = false;

        return config;
    }

    /**
     * Inner class holding all configuration for a single level.
     */
    public static class LevelConfig {
        // Level info
        public String name = "Level";
        public int weekNumber = 1;
        public int part = 1; // Which part of the week (1=A, 2=B, etc.)
        public int nextPart = 0; // Next part after completing (0 = done with week)
        public String topic = "";
        public String hint = "";

        // Platform layout
        public int[] platformX = {};
        public int[] platformY = {};
        public int[] platformWidths = {};

        // Enemy positions
        public int[] enemyX = {};
        public int[] enemyY = {};

        // Goal position
        public int goalX = 700;
        public int goalY = 100;

        // Goal enabled (Week 4: If-statements)
        // Students must fix the condition in GameConfig.java to enable the goal!
        public boolean goalEnabled = true; // Default to true for other levels

        // Encroaching wall (Week 3B mechanic)
        public boolean hasEncroachingWall = false;
        public double wallStartX = -100;
        public double wallSpeed = 2.0;
        public int wallStartDelay = 0;
        public Color wallColor = new Color(180, 0, 0);

        // Clickable Button (Week 4 mechanic)
        public boolean hasLevelButton = false;
        public int buttonX = 0;
        public int buttonY = 0;
        public int buttonWidth = 60;
        public int buttonHeight = 40;
        public int requiredButtonClicks = 0;
    }
}
