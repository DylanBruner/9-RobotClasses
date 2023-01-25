package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Main must be in core so it can access the privateish variables of Game.java
import robots.DefendaBot;
import robots.LearningRobot;
import robots.MyRobot1;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> wins = new HashMap<String, Integer>();
        // Add robots to the game
        BasicRobot[] robots = new BasicRobot[] { new MyRobot1(), new LearningRobot(), new DefendaBot() };

        Game.DISABLE_PRINTING = true;

        int games = 15_000;
        for (int i = 0; i < games; i++) {
            for (BasicRobot robot : robots) {Game.addRobot(robot);}

            Game.run();

            // Get the winner
            BasicRobot winner = Game.getWinner();
            if (winner != null){
                wins.put(winner.name, wins.getOrDefault(winner.name, 0) + 1);                
            }

            // reset the game unless it's the last game
            if (i != games - 1) {
                Game.reset();
            }
        }

        // Print out the results
        System.out.println("===== [RESULTS] =====");
        
        List<RobotEntry> robotEntries = new ArrayList<RobotEntry>();
        for (String robotName : wins.keySet()) {
            RobotEntry entry = new RobotEntry();
            entry.name = robotName.replace("robots.", "");
            entry.wins = wins.get(robotName);
            entry.winPercentage = (double) entry.wins / games * 100;
            robotEntries.add(entry);
        }

        robotEntries.sort((a, b) -> b.wins - a.wins);
        for (RobotEntry entry : robotEntries) {
            System.out.println(entry.name + " - " + entry.wins + " (" + String.format("%.2f", entry.winPercentage) + "%)");
        }

        System.out.println("===== [GAME ENDS] =====");
        for (BasicRobot robot : robots) {robot.onGameEnd();}
    }

    public static class RobotEntry {
        public String name;
        public int wins;
        public double winPercentage;
    }
}