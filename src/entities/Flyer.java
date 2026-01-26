package entities;

import java.awt.Color;

/**
 * A flying enemy that moves in a sine wave pattern.
 * Week 14: Inheritance
 */
public class Flyer extends Enemy {
    private double waveOffset = 0;
    private double originalY;

    public Flyer(double x, double y, double patrolLeft, double patrolRight) {
        super(x, y, patrolLeft, patrolRight);
        this.originalY = y;
        this.color = new Color(153, 50, 204); // Purple
    }

    @Override
    public void move() {
        // Horizontal movement (parent logic)
        super.move();

        // Vertical sine wave
        waveOffset += 0.05;
        y = originalY + Math.sin(waveOffset) * 50;
    }
}
