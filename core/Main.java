package core;

import java.util.HashMap;

// Main must be in core so it can access the privateish variables of Game.java
import robots.DefendaBot;
import robots.LearningRobot;
import robots.MyRobot1;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> wins = new HashMap<String, Integer>();
        // Add robots to the game
        int games = 10_000;
        for (int i = 0; i < games; i++) {
            // Game.addRobot(new DefendaBot());
            Game.addRobot(new MyRobot1());
            Game.addRobot(new LearningRobot());

            // Run the game
            Game.run();

            // Get the winner
            String winner = Game.getWinner().name;
            wins.put(winner, wins.getOrDefault(winner, 0) + 1);

            // reset the game unless it's the last game
            if (i != games - 1) {
                Game.reset();
            }
        }

        // Print out the results
        System.out.println("===== [RESULTS] =====");
        for (String robot : wins.keySet()) {
            System.out.println(robot + " won " + wins.get(robot) + " games.");
        }

        // Call game ends
        LearningRobot.onGameEnd();
    }
}