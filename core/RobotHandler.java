package core;

public class RobotHandler {
    private BasicRobot robot;
    // Robot variables
    boolean alive = true;
    double health = 100;

    private double defensePoints  = 0;
    private double strengthPoints = 0;
    private double staminaPoints  = 0;
    
    public RobotHandler(BasicRobot robot) {this.robot = robot;}

    public void setPoints(double defensePoints, double strengthPoints, double staminaPoints) {
        double sum = defensePoints + strengthPoints + staminaPoints;
        if (sum > Game.allotedPoints) {
            throw new IllegalArgumentException("The sum of the points is greater than the allocated points.");
        }
        this.defensePoints  = defensePoints;
        this.strengthPoints = strengthPoints;
        this.staminaPoints  = staminaPoints;
    }

    void onTurn(){
        robot.onTurn(Game.allotedPoints);
    }

    void onAttack(BasicRobot attacker) {
        RobotHandler attackerHandler = Game.robotHandlers.get(attacker);
        double dodgeChance = (attackerHandler.staminaPoints / Game.allotedPoints) * 0.70; // 0-70% chance
        if (Math.random() < dodgeChance) {
            // Dodge
            try {
                this.robot.onDodge();
            } catch (Exception e) {
                System.out.println("[GAME::ERROR] Something went wrong with the onDodge() method for " + attacker.name + ".");
                e.printStackTrace();
            }
            return;
        }

        // Calculate damage to us and factor in defense
        double damage = (attackerHandler.strengthPoints / Game.allotedPoints) * 25; // 0-25 damage
        damage -= (this.defensePoints / Game.allotedPoints) * 12; // 0-20 damage reduction

        // Apply damage
        this.health -= damage;
        robot.onHit(damage); // Doesn't apply the damage, just tells the robot it was hit
        System.out.println("[GAME::INFO] " + attacker.name + " attacked " + this.robot.name + " for " + String.format("%.2f", damage) + " damage. " + this.robot.name + " has " + String.format("%.2f", this.health) + " health left.");
    }
}
