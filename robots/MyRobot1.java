package robots;
import core.BasicRobot;
import core.Game;

public class MyRobot1 extends BasicRobot {
    public void onTurn(double points) {

        // put 80% of our points into defense and 20% into strength
        Game.takeTurn(this, (int) (points * 0.05), (int) (points * 0.95), 0);
    }
}
