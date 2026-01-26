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
            case 5:
                return getWeek5_LoopChallenge();
            case 6:
                return getWeek6_PowerSurge();
            case 7:
                return getWeek7_TheCalculator();
            case 8:
                return getWeek8_ArrayBasics();
            case 9:
                return getWeek9_ArrayIteration();
            case 10:
                return getWeek10_IOLoader();
            case 11:
                return getWeek11_ObjectExploring();
            case 12:
                return getWeek12_ObjectCreating();
            case 13:
                return getWeek13_OOPDesign();
            case 14:
                return getWeek14_Inheritance();
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
     * Week 5: "Build the Bridge" - Loops (for)
     */
    private static LevelConfig getWeek5_LoopChallenge() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 5: The Infinite Void";
        config.weekNumber = 5;
        config.topic = "For-Loops";
        config.hint = "Hint: The gap is too wide! Write a for-loop in GameConfig.java to build the bridge.";

        config.platformX = new int[] { 0, 650 };
        config.platformY = new int[] { 500, 500 };
        config.platformWidths = new int[] { 150, 150 };

        config.hasBridge = true;
        config.bridgeStartX = 150;
        config.bridgeY = 500;
        config.bridgeTileWidth = 100;
        config.hasFloor = false;

        config.goalX = 720;
        config.goalY = 425;

        return config;
    }

    /**
     * Week 6: "Power Surge" - Methods (Calling)
     */
    private static LevelConfig getWeek6_PowerSurge() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 6: Power Surge";
        config.weekNumber = 6;
        config.topic = "Calling Methods";
        config.hint = "Hint: Collect 3 coins to activate the jump pad, then fix its boost!";

        config.platformX = new int[] { 0, 550 };
        config.platformY = new int[] { 500, 500 };
        config.platformWidths = new int[] { 360, 250 };

        config.enemyX = new int[] { 450 };
        config.enemyY = new int[] { 300 };

        config.coinX = new int[] { 30, 100, 170 };
        config.coinY = new int[] { 450, 450, 450 };

        // Gap is ~400px wide. Normal jump covers ~144px vertical.
        // We need a massive boost.
        config.hasJumpPad = true;
        config.jumpPadX = 300;
        config.jumpPadY = 480;

        config.hasFloor = false;
        config.goalX = 720;
        config.goalY = 425;

        return config;
    }

    /**
     * Week 7: "The Calculator" - Methods (Writing)
     */
    private static LevelConfig getWeek7_TheCalculator() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 7: The Calculator";
        config.weekNumber = 7;
        config.topic = "Writing Methods";
        config.hint = "Hint: Fix the score formula to reach 500 points and open the door!";

        config.platformX = new int[] { 50, 250, 450, 650 };
        config.platformY = new int[] { 480, 400, 320, 240 };
        config.platformWidths = new int[] { 120, 120, 120, 120 };

        config.hasTrap = true; // Added trap to give student something to calculate damage for
        config.trapX = 260;
        config.trapY = 380;
        config.trapWidth = 100;

        config.coinX = new int[] { 100, 200, 300, 425, 600 };
        config.coinY = new int[] { 450, 350, 250, 500, 170 };

        config.hasScoringDoor = true;
        config.scoringDoorX = 680;
        config.scoringDoorY = 100;

        config.goalX = 720;
        config.goalY = 100;

        return config;
    }

    /**
     * Week 8: "Platform Architect" - Arrays (Basics)
     */
    private static LevelConfig getWeek8_ArrayBasics() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 8: Platform Architect";
        config.weekNumber = 8;
        config.topic = "Array Basics";
        config.hint = "Hint: Use the arrays in GameConfig to build a path to the goal!";

        config.useWeek8Data = false; // This level will pull X,Y,Widths from developer/GameConfig arrays

        config.platformX = new int[] { 0, 200, 400, 550, 750 };
        config.platformY = new int[] { 500, 500, 500, 500, 500 };
        config.platformWidths = new int[] { 150, 100, 100, 100, 50 };
        config.platformMoveable = new boolean[] { false, true, true, true, false };
        config.platformMaxHeights = new int[] { 200, 200, 200, 200, 200 };

        config.goalX = 350;
        config.goalY = 50;

        return config;
    }

    /**
     * Week 9: "Enemy Parade" - Array Iteration
     */
    private static LevelConfig getWeek9_ArrayIteration() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 9: Enemy Parade";
        config.weekNumber = 9;
        config.topic = "Array Iteration";
        config.hint = "Hint: Use loops to move the enemies out of your way!";

        config.useWeek9Data = true;
        config.platformX = new int[] { 0, 750 };
        config.platformY = new int[] { 500, 500 };
        config.platformWidths = new int[] { 150, 50 };
        config.hasFloor = false;

        config.goalX = 760;
        config.goalY = 425;

        return config;
    }

    /**
     * Week 10: "Level Loader" - I/O & Exceptions
     */
    private static LevelConfig getWeek10_IOLoader() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 10: Level Loader";
        config.weekNumber = 10;
        config.topic = "I/O & Exceptions";
        config.hint = "Hint: Implement the level loader to build the world from level10.txt!";

        config.useFileLoading = true;
        config.levelFileName = "level10.txt";
        config.hasFloor = false;

        config.goalX = 700;
        config.goalY = 100;

        return config;
    }

    /**
     * Week 11: "Player Inspector" - Objects (Exploring)
     */
    private static LevelConfig getWeek11_ObjectExploring() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 11: Player Inspector";
        config.weekNumber = 11;
        config.topic = "Objects & Classes";
        config.hint = "Hint: Use getters and setters to pass the purple stat gate!";

        config.platformX = new int[] { 50, 200, 400, 600 };
        config.platformY = new int[] { 480, 480, 480, 480 };
        config.platformWidths = new int[] { 100, 100, 100, 150 };

        config.hasStatGate = true;
        config.statGateX = 420;
        config.statGateY = 365;

        config.goalX = 650;
        config.goalY = 405;

        return config;
    }

    /**
     * Week 12: "Power-Up Factory" - Objects (Creating)
     */
    private static LevelConfig getWeek12_ObjectCreating() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 12: Power-Up Factory";
        config.weekNumber = 12;
        config.topic = "Creating Classes";
        config.hint = "Hint: Create the PowerUp class to survive the gauntlet!";

        config.platformX = new int[] { 50, 700 };
        config.platformY = new int[] { 500, 500 };
        config.platformWidths = new int[] { 100, 100 };

        // Need powerups to reach the far side
        config.goalX = 720;
        config.goalY = 425;

        return config;
    }

    /**
     * Week 13: "Encapsulation Challenge" - OOP Design
     */
    private static LevelConfig getWeek13_OOPDesign() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 13: Encapsulation";
        config.weekNumber = 13;
        config.topic = "Encapsulation";
        config.hint = "Hint: Secure the GameState class to prevent game crashes!";

        config.platformX = new int[] { 50, 300, 550 };
        config.platformY = new int[] { 480, 400, 320 };
        config.platformWidths = new int[] { 150, 150, 150 };

        config.goalX = 600;
        config.goalY = 245;

        return config;
    }

    /**
     * Week 14: "Enemy Types" - Inheritance
     */
    private static LevelConfig getWeek14_Inheritance() {
        LevelConfig config = new LevelConfig();
        config.name = "Week 14: Enemy Evolution";
        config.weekNumber = 14;
        config.topic = "Inheritance";
        config.hint = "Hint: Create Flyer and Charger classes to populate the level!";

        config.platformX = new int[] { 50, 300, 550 };
        config.platformY = new int[] { 480, 420, 360 };
        config.platformWidths = new int[] { 150, 150, 150 };

        // We'll spawn the special enemies in Level.java
        config.goalX = 620;
        config.goalY = 285;

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
        public boolean[] platformMoveable = {};
        public int[] platformMaxHeights = {};

        // Enemy positions
        public int[] enemyX = {};
        public int[] enemyY = {};

        // Coin positions (Week 8-9)
        public int[] coinX = {};
        public int[] coinY = {};

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

        // Bridge (Week 5 mechanic)
        public boolean hasBridge = false;
        public int bridgeStartX = 0;
        public int bridgeY = 0;
        public int bridgeTileWidth = 80;

        // Ground platform (Week 5: can be disabled)
        public boolean hasFloor = true;

        // Jump Pad (Week 6)
        public boolean hasJumpPad = false;
        public int jumpPadX = 0;
        public int jumpPadY = 0;

        // Scoring Door (Week 7)
        public boolean hasScoringDoor = false;
        public int scoringDoorX = 0;
        public int scoringDoorY = 0;

        // Trap (Week 7)
        public boolean hasTrap = false;
        public int trapX = 0;
        public int trapY = 0;
        public int trapWidth = 100;

        // Stat Gate (Week 11)
        public boolean hasStatGate = false;
        public int statGateX = 0;
        public int statGateY = 0;

        // Week 8 & 9 Custom Data
        public boolean useWeek8Data = false;
        public boolean useWeek9Data = false;

        // Week 10 File Loading
        public boolean useFileLoading = false;
        public String levelFileName = "";
    }
}
