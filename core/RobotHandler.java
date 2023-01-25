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

        // the attacker's strength points are (0 - 100) and the defender's defense points are (0 - 100)
        // Calculate the damage 0-10
        // defense should reduce the damage by 0%-60% of the damage
        double damage = (attackerHandler.strengthPoints / Game.allotedPoints) * 10;
        // decrease the damage by 0.6 * the defender's defense points
        damage -= this.defensePoints * 0.07;
        // make sure the damage is at least 0
        if (damage < 0) damage = 0;

        // Apply damage
        this.health -= damage;
        robot.onHit(damage); // Doesn't apply the damage, just tells the robot it was hit
        
        // Give the attacker 10% of the damage they did as health
        attackerHandler.health += damage * 0.10;
   
        System.out.println("[GAME::INFO] " + attacker.name + " attacked " + this.robot.name + " for " + String.format("%.2f", damage) + " damage. " + this.robot.name + " has " + String.format("%.2f", this.health) + " health left.");
    }
}
