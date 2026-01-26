package entities;

import java.awt.Color;

/**
 * An enemy that charges at the player.
 * Week 14: Inheritance
 */
public class Charger extends Enemy {
    private Player player;
    private boolean charging = false;

    public Charger(double x, double y, double patrolLeft, double patrolRight, Player player) {
        super(x, y, patrolLeft, patrolRight);
        this.player = player;
        this.color = Color.ORANGE;
    }

    @Override
    public void move() {
        if (player == null) {
            super.move();
            return;
        }

        double dist = Math.abs(player.getX() - x);
        if (dist < 200) {
            charging = true;
            this.color = Color.RED; // Turn red when charging
            // Charge toward player
            if (player.getX() < x) {
                x -= 4; // Faster than normal
                velocityX = -4;
            } else {
                x += 4;
                velocityX = 4;
            }
        } else {
            charging = false;
            this.color = Color.ORANGE;
            super.move();
        }
    }

    public boolean isCharging() {
        return charging;
    }
}
