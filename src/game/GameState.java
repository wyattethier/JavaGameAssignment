package game;

/**
 * Tracks the player's progress and stats.
 * Week 13: OOP Design (Encapsulation)
 * 
 * Students: This class has poor design. You must refactor it!
 */
public class GameState {
    // CURRENT BAD DESIGN: Public fields with no validation
    public int score = 0;
    public int lives = 3;
    public int levelNumber = 1;

    // TODO:
    // 1. Make fields private
    // 2. Add getters and setters with validation
    // 3. Add helper methods like addScore(int) and loseLife()
}
