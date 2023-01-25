package robots;

import java.util.HashMap;

import core.BasicRobot;
import core.Game;

public class LearningRobot extends BasicRobot {
    static HashMap<String, Integer> currentPointPercents = new HashMap<String, Integer>();
    static HashMap<String, Integer> lastPointPercents    = new HashMap<String, Integer>();
    static String lastModified   = null;
    static int lastTurnsSurvived = 0;

    public void onTurn(double points) {
        // If we have no data, just use the default 1/3
        if (currentPointPercents.size() == 0) {
            Game.takeTurn(this, (int) (points * 0.33), (int) (points * 0.33), (int) (points * 0.33));
            // fill the currentPointPercents with the default 1/3
            currentPointPercents.put("defense", 33);
            currentPointPercents.put("strength", 33);
            currentPointPercents.put("stamina", 33);
            return;
        }

        // If we have data, use it
        Game.takeTurn(this, (int) (points * currentPointPercents.get("defense") / 100.0), (int) (points * currentPointPercents.get("strength") / 100.0), (int) (points * currentPointPercents.get("stamina") / 100.0));
    }

    public void onDeath(){
        if (lastModified == null){
            lastPointPercents = (HashMap<String, Integer>) currentPointPercents.clone();

            // modify a random stat
            int stat = (int) (Math.random() * 3);
            switch (stat) {
                case 0:
                    lastModified = "defense";
                    break;
                case 1:
                    lastModified = "strength";
                    break;
                case 2:
                    lastModified = "stamina";
                    break;
            }

            // calculate the new percentage -10-10 of the last percentage
            int newDifference = (int) (Math.random() * 20) - 10;
            // apply the new percentage to the stat and reverse the other two
            switch (lastModified) {
                case "defense":
                    currentPointPercents.put("defense", lastPointPercents.get("defense") + newDifference);
                    currentPointPercents.put("strength", 100 - currentPointPercents.get("defense") - currentPointPercents.get("stamina"));
                    break;
                case "strength":
                    currentPointPercents.put("strength", lastPointPercents.get("strength") + newDifference);
                    currentPointPercents.put("defense", 100 - currentPointPercents.get("strength") - currentPointPercents.get("stamina"));
                    break;
                case "stamina":
                    currentPointPercents.put("stamina", lastPointPercents.get("stamina") + newDifference);
                    currentPointPercents.put("defense", 100 - currentPointPercents.get("strength") - currentPointPercents.get("stamina"));
                    break;
            }
        } else {
        }
    }

    public static void onGameEnd(){
        // print out the currents stats
        System.out.println("===== [LEARNING ROBOT] =====");
        // curent stats
        System.out.println("Current stats:");
        System.out.println("Defense: " + currentPointPercents.get("defense") + "%");
        System.out.println("Strength: " + currentPointPercents.get("strength") + "%");
        System.out.println("Stamina: " + currentPointPercents.get("stamina") + "%");
    }
}
