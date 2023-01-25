package core;

public class BasicRobot {
    // Public variables
    public String name = null;
    
    public BasicRobot() {
        name = this.getClass().getName();
    }

    public void onTurn(double points){}
    public void onDeath(){}
    public void onDodge(){}
    public void onHit(double damage){}
    public void onWin(){}
    public void onGameEnd(){}

    // Only used locally
    public double getHealth(){return Game.robotHandlers.get(this).health;}
}