package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    // I belive this only allows things inside of Core to access these variables.
    static List<BasicRobot> robots = new ArrayList<BasicRobot>();
    static boolean running = true;
    static HashMap<BasicRobot, RobotHandler> robotHandlers = new HashMap<BasicRobot, RobotHandler>();

    public static int turnCounter = 0;

    // Config
    static int allotedPoints = 100;

    static void addRobot(BasicRobot robot) {
        robots.add(robot);
        robotHandlers.put(robot, new RobotHandler(robot));
    }

    public static void takeTurn(BasicRobot robot, int strengthPoints, int defensePoints, int staminaPoints) {
        // make any negative values 0
        if (strengthPoints < 0) strengthPoints = 0; if (defensePoints < 0) defensePoints = 0; if (staminaPoints < 0) staminaPoints = 0;

        RobotHandler handler = robotHandlers.get(robot);
        if (handler == null) {
            System.out.println("[ERROR] No handler found for robot " + robot.name + "." + " Removing robot from game.");

            // Get the object that called this method
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement caller = stackTraceElements[2];
            //Remove the robot that called this method
            for (int i = 0; i < robots.size(); i++) {
                if (robots.get(i).name.equals(caller.getClassName())) {
                    robots.remove(i);
                    break;
                }
            }
        } else {
            handler.setPoints(strengthPoints, defensePoints, staminaPoints);
        }
    }

    static void run(){
        int turnNumber = 0;
        while (running) {
            Game.turnCounter=turnNumber;
            // 75 - 100 base points, 5% chance of adding +250
            allotedPoints = (int) (75 + (Math.random() * 25) + (Math.random() < 0.05 ? 250 : 0));
            System.out.println("===== [TURN " + turnNumber++ + " - ("+allotedPoints+" points)] =====");

            // Phase one of the turn
            for (BasicRobot robot : robots) {
                RobotHandler handler = robotHandlers.get(robot);
                if (handler == null){
                    System.out.println("[ERROR] No handler found for robot " + robot.name + "." + " Removing robot from game.");
                    robots.remove(robot);
                } else {
                    handler.onTurn();
                }

                // Check for winner
                if (robots.size() == 1) {
                    System.out.println(robots.get(0).name + " has won the game!");
                    running = false;
                    robots.get(0).onWin();
                    return;
                }
            }

            // Randomly select two robots to fight
            if (robots.size() < 2) {
                System.out.println("Not enough robots to fight. Ending game.");
                running = false; return;
            }
            BasicRobot robot1 = robots.get((int) (Math.random() * robots.size()));
            BasicRobot robot2 = robots.get((int) (Math.random() * robots.size()));
            while (robot1.name == robot2.name) {
                robot2 = robots.get((int) (Math.random() * robots.size()));
            }

            // Phase two of the turn
            RobotHandler handler1 = robotHandlers.get(robot1);
            RobotHandler handler2 = robotHandlers.get(robot2);

            handler1.onAttack(robot2);
            handler2.onAttack(robot1);

            // Check for dead robots
            List<BasicRobot> toRemove = new ArrayList<BasicRobot>();
            for (BasicRobot robot : robots) {
                RobotHandler handler = robotHandlers.get(robot);
                if (handler == null){
                    System.out.println("[ERROR] No handler found for robot " + robot.name + "." + " Removing robot from game.");
                    toRemove.add(robot);
                } else {
                    if (handler.health <= 0) {
                        System.out.println(robot.name + " has died.");
                        try {
                            robot.onDeath();
                        } catch (Exception e) {
                            System.out.println("[ERROR] Robot " + robot.name + " threw an exception in onDeath().");
                            e.printStackTrace();
                        }
                        toRemove.add(robot);
                    }
                }
            }

            // Remove dead robots along with their handlers
            for (BasicRobot robot : toRemove) {
                robots.remove(robot);
                robotHandlers.remove(robot);
            }
        }
    }

    static void reset(){
        robots.clear();
        robotHandlers.clear();
        running = true;
    }

    static BasicRobot getWinner(){
        if (robots.size() == 1) {
            return robots.get(0);
        } else {
            return null;
        }
    }
}