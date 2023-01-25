package core;

public class FancyDisplay {
    // Constants
    public static final String RESET  = "\u001B[0m";
    public static final String CLEAR  = "\u001B[2J";
    public static final String BOX    = "\u2551";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    

    private String generateHealthbar(int health){
        // Health is 0-10, the bar is 10 characters long

        String bar = "";
        for (int i = 0; i < 10; i++) {
            if (i < health) {
                bar += GREEN + "█" + RESET;
            } else {
                bar += RED + "█" + RESET;
            }
        }

        return bar;
    }

    public String toString(){
        /*
         * [Robot Name]: [Health] [Health Bar] 
        */
        for (BasicRobot robot : Game.robots) {
            RobotHandler handler = Game.robotHandlers.get(robot);
            if (handler == null){
                System.out.println("[ERROR] No handler found for robot " + robot.name + "." + " Removing robot from game.");
            } else {
                System.out.println(robot.name + ": " + handler.health + " " + generateHealthbar((int) handler.health));
            }
        }

        return "";
    }
}
