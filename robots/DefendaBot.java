package robots;
import core.BasicRobot;
import core.Game;

public class DefendaBot extends BasicRobot {
    public void onTurn(double points) {
        // put 99 % of our points into defense and 1% into strength, make sure not to use too many points
        Game.takeTurn(this, (int) (points * 0.01), (int) (points * 0.99), 0);
    }
}
