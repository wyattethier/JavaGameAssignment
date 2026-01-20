package game;

import javax.swing.JFrame;

/**
 * Main entry point for the OOP Platformer Game.
 *
 * This class creates the game window and starts the game.
 * Students: You don't need to modify this file.
 */
public class Main {

    public static void main(String[] args) {
        // Create the game window
        JFrame window = new JFrame("OOP Platformer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Create and add the game panel
        Game game = new Game();
        window.add(game);
        window.pack();

        // Center the window on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game
        game.start();
    }
}
