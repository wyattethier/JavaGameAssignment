package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import entities.Player;
import entities.Enemy;
import util.InputHandler;
import world.Level;
import world.LevelData;

/**
 * Main game panel that handles the game loop, rendering, and game state.
 *
 * Students: This class orchestrates everything. You'll explore it in Week 11.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * GAME ENGINE - You can read this code, but be careful about modifications!
 * Understanding this code will help you in later weeks.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class Game extends JPanel implements ActionListener {

    // Game state
    private enum GameState {
        MENU,
        PLAYING,
        PAUSED,
        PART_COMPLETE, // Transition between level parts
        WIN,
        GAME_OVER
    }

    // Core components
    private GameConfig config;
    private Player player;
    private Level level;
    private InputHandler input;
    private Timer gameTimer;

    // State tracking
    private GameState state;
    private int score;
    private String message;
    private int currentWeek;
    private int currentPart; // Which part of the week (1=A, 2=B, etc.)

    // Constants
    private static final int TARGET_FPS = 60;
    private static final int FRAME_TIME = 1000 / TARGET_FPS; // ~16ms per frame

    /**
     * Create the game panel.
     */
    public Game() {
        // Load configuration
        config = new GameConfig();

        // Set up the panel
        setPreferredSize(new Dimension(config.windowWidth, config.windowHeight));
        setBackground(new Color(135, 206, 235)); // Sky blue
        setDoubleBuffered(true);
        setFocusable(true);

        // Set up input handling
        input = new InputHandler();
        addKeyListener(input);

        // Set up mouse handling for buttons (Week 4)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (state == GameState.PLAYING) {
                    level.handleMouseClick(e.getX(), e.getY());
                }
            }
        });

        // Set current week from config (students can change this!)
        currentWeek = config.currentWeek;
        currentPart = 1; // Start with part A

        // Initialize game objects
        initGame();

        // Create the game loop timer
        gameTimer = new Timer(FRAME_TIME, this);
    }

    /**
     * Initialize or reset the game.
     */
    private void initGame() {
        // Create player at starting position
        player = new Player(config.playerStartX, config.playerStartY, config);

        // Create the level based on current week and part
        LevelData.LevelConfig levelData = LevelData.getLevel(currentWeek, currentPart);
        level = new Level(config, levelData);

        // Initial state
        state = GameState.PLAYING;
        score = 0;
        message = "";
    }

    /**
     * Start the game loop.
     */
    public void start() {
        gameTimer.start();
    }

    /**
     * Stop the game loop.
     */
    public void stop() {
        gameTimer.stop();
    }

    /**
     * Game loop - called every frame by the Timer.
     * This is where the magic happens!
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update input state first
        input.update();

        // Update game based on current state
        switch (state) {
            case MENU:
                updateMenu();
                break;
            case PLAYING:
                updatePlaying();
                break;
            case PAUSED:
                updatePaused();
                break;
            case PART_COMPLETE:
                updatePartComplete();
                break;
            case WIN:
            case GAME_OVER:
                updateEndScreen();
                break;
        }

        // Request a repaint
        repaint();
    }

    /**
     * Update when in PLAYING state.
     */
    private void updatePlaying() {
        // Update player
        player.update(input, level.getPlatforms());

        // Update level (enemies, wall, etc.)
        level.update();

        // Update Week 4 goal status based on student code
        if (currentWeek == 4 && level.getButton() != null) {
            boolean fixed = config.isGoalFixed(level.getButton().getClickCount());
            level.setGoalEnabled(fixed);
        }

        // Check player-enemy collisions
        checkEnemyCollisions();

        // Check if player is caught by encroaching wall
        if (level.isPlayerCaughtByWall(player.getX(), player.getWidth())) {
            player.takeDamage(player.getHealth()); // Instant death
            state = GameState.GAME_OVER;
            message = "Caught by the wall! Try increasing moveSpeed!";
        }

        // Check win condition
        if (level.isAtGoal(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
            score += player.getCoins() * 10;

            // Check if there's a next part to this week
            int nextPart = level.getNextPart();
            if (nextPart > 0) {
                // More parts to go - show transition screen
                state = GameState.PART_COMPLETE;
                message = "Part " + (char) ('A' + currentPart - 1) + " Complete!";
            } else {
                // Week complete!
                state = GameState.WIN;
                message = "Week " + currentWeek + " Complete!";
            }
        }

        // Check game over
        if (!player.isAlive()) {
            state = GameState.GAME_OVER;
            if (message.isEmpty()) {
                message = "Game Over!";
            }
        }

        // Check for pause
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_P)) {
            state = GameState.PAUSED;
        }

        // Check for restart (R key)
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_R)) {
            initGame();
        }
    }

    /**
     * Check collisions between player and enemies.
     */
    private void checkEnemyCollisions() {
        Rectangle playerBounds = player.getBounds();
        double playerCenterY = player.getY() + player.getHeight() / 2.0;

        for (Enemy enemy : level.getEnemies()) {
            if (enemy == null || !enemy.isAlive())
                continue;

            Rectangle enemyBounds = enemy.getBounds();
            if (playerBounds.intersects(enemyBounds)) {
                // Check if player is above enemy center (stomping)
                double enemyCenterY = enemy.getY() + enemy.getHeight() / 2.0;
                boolean fromAbove = playerCenterY < enemyCenterY && player.isOnGround() == false;

                enemy.onPlayerCollision(player, fromAbove);

                // Add score if enemy was killed
                if (!enemy.isAlive()) {
                    score += 100;
                }
            }
        }
    }

    /**
     * Update when in MENU state.
     */
    private void updateMenu() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_SPACE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ENTER)) {
            initGame();
        }
    }

    /**
     * Update when PAUSED.
     */
    private void updatePaused() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_P) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_SPACE)) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Update when a level part is complete (transitioning to next part).
     */
    private void updatePartComplete() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_SPACE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ENTER)) {
            // Move to next part
            currentPart = level.getNextPart();

            // Reset player position and create new level
            player = new Player(config.playerStartX, config.playerStartY, config);
            LevelData.LevelConfig levelData = LevelData.getLevel(currentWeek, currentPart);
            level = new Level(config, levelData);

            state = GameState.PLAYING;
        }
    }

    /**
     * Update when game is over or won.
     */
    private void updateEndScreen() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_SPACE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ENTER) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_R)) {
            initGame();
        }
    }

    /**
     * Render the game.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Enable anti-aliasing for smoother graphics
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw based on game state
        switch (state) {
            case MENU:
                drawMenu(g);
                break;
            case PLAYING:
                drawGame(g);
                break;
            case PAUSED:
                drawGame(g);
                drawPauseOverlay(g);
                break;
            case PART_COMPLETE:
                drawGame(g);
                drawPartCompleteScreen(g);
                break;
            case WIN:
                drawGame(g);
                drawWinScreen(g);
                break;
            case GAME_OVER:
                drawGame(g);
                drawGameOverScreen(g);
                break;
        }
    }

    /**
     * Draw the main game (level, player, UI).
     */
    private void drawGame(Graphics g) {
        // Draw level (platforms, enemies, goal, wall)
        level.draw(g);

        // Draw player
        player.draw(g);

        // Draw UI
        drawUI(g);
    }

    /**
     * Draw the game UI (health, coins, level info, etc.)
     */
    private void drawUI(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Level name and topic
        g.setColor(new Color(50, 50, 100));
        g.drawString(level.getName(), 10, 25);

        // Topic hint
        if (!level.getHint().isEmpty()) {
            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(new Color(100, 100, 150));
            g.drawString(level.getHint(), 10, 42);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Health
        g.setColor(Color.WHITE);
        g.drawString("Health: ", 10, 65);
        for (int i = 0; i < player.getMaxHealth(); i++) {
            if (i < player.getHealth()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GRAY);
            }
            g.fillRect(75 + (i * 25), 53, 20, 15);
        }

        // Coins
        g.setColor(Color.YELLOW);
        g.fillOval(10, 75, 16, 16);
        g.setColor(Color.WHITE);
        g.drawString(" x " + player.getCoins(), 28, 90);

        // Score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, config.windowWidth - 120, 25);

        // Current physics values (helpful for Week 3!)
        g.setFont(new Font("Courier New", Font.PLAIN, 11));
        g.setColor(new Color(80, 80, 80));
        g.drawString("jumpStrength: " + config.jumpStrength, config.windowWidth - 150, 50);
        g.drawString("moveSpeed: " + config.moveSpeed, config.windowWidth - 150, 65);
        g.drawString("gravity: " + config.gravity, config.windowWidth - 150, 80);

        // Controls hint
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 180));
        g.drawString("Arrow Keys/WASD: Move | Space: Jump | R: Restart | P: Pause", 10, config.windowHeight - 10);
    }

    /**
     * Draw the main menu.
     */
    private void drawMenu(Graphics g) {
        // Title
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        String title = "OOP Platformer";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (config.windowWidth - titleWidth) / 2, 200);

        // Instructions
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String start = "Press SPACE to Start";
        int startWidth = g.getFontMetrics().stringWidth(start);
        g.drawString(start, (config.windowWidth - startWidth) / 2, 300);

        // Credits
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("CSC 260 - OOP in Java", 20, config.windowHeight - 40);
    }

    /**
     * Draw pause overlay.
     */
    private void drawPauseOverlay(Graphics g) {
        // Darken background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, config.windowWidth, config.windowHeight);

        // Pause text
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        String text = "PAUSED";
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (config.windowWidth - width) / 2, config.windowHeight / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String resume = "Press P or SPACE to Resume";
        int resumeWidth = g.getFontMetrics().stringWidth(resume);
        g.drawString(resume, (config.windowWidth - resumeWidth) / 2, config.windowHeight / 2 + 40);
    }

    /**
     * Draw win screen.
     */
    private void drawWinScreen(Graphics g) {
        // Darken background
        g.setColor(new Color(0, 100, 0, 150));
        g.fillRect(0, 0, config.windowWidth, config.windowHeight);

        // Win text
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        String text = "YOU WIN!";
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (config.windowWidth - width) / 2, config.windowHeight / 2 - 20);

        // Score
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String scoreText = "Score: " + score;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (config.windowWidth - scoreWidth) / 2, config.windowHeight / 2 + 30);

        // Restart
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String restart = "Press SPACE to Play Again";
        int restartWidth = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (config.windowWidth - restartWidth) / 2, config.windowHeight / 2 + 80);
    }

    /**
     * Draw game over screen.
     */
    private void drawGameOverScreen(Graphics g) {
        // Darken background
        g.setColor(new Color(100, 0, 0, 150));
        g.fillRect(0, 0, config.windowWidth, config.windowHeight);

        // Game over text
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        String text = "GAME OVER";
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (config.windowWidth - width) / 2, config.windowHeight / 2 - 20);

        // Show the hint message
        if (!message.isEmpty() && !message.equals("Game Over!")) {
            g.setFont(new Font("Arial", Font.ITALIC, 18));
            g.setColor(new Color(255, 200, 200));
            int msgWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (config.windowWidth - msgWidth) / 2, config.windowHeight / 2 + 20);
        }

        // Restart
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String restart = "Press SPACE to Try Again";
        int restartWidth = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (config.windowWidth - restartWidth) / 2, config.windowHeight / 2 + 60);
    }

    /**
     * Draw part complete transition screen.
     */
    private void drawPartCompleteScreen(Graphics g) {
        // Darken background with blue tint
        g.setColor(new Color(0, 50, 100, 180));
        g.fillRect(0, 0, config.windowWidth, config.windowHeight);

        // Part complete text
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.setColor(new Color(100, 255, 100));
        int width = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (config.windowWidth - width) / 2, config.windowHeight / 2 - 40);

        // Next part info
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        String nextInfo = "Get ready for Part " + (char) ('A' + currentPart) + "!";
        int nextWidth = g.getFontMetrics().stringWidth(nextInfo);
        g.drawString(nextInfo, (config.windowWidth - nextWidth) / 2, config.windowHeight / 2 + 10);

        // Continue prompt
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(new Color(200, 200, 255));
        String continueText = "Press SPACE to Continue";
        int continueWidth = g.getFontMetrics().stringWidth(continueText);
        g.drawString(continueText, (config.windowWidth - continueWidth) / 2, config.windowHeight / 2 + 60);
    }
}
