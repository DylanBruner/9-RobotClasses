package robots;

import java.util.HashMap;

import core.BasicRobot;
import core.Game;

/*
 * TODO
 *  - Mutate the snapshot based on better stats like how much damage was done, how much damage was taken, how many turns survived, etc. 
*/

public class LearningRobot extends BasicRobot {
    // Starting stats
    static final int BASE_STRENGTH = 10;
    static final int BASE_DEFENSE  = 90;
    static final int BASE_STAMINA  = 0;

    
    static HashMap<Integer, Snapshot> snapshots = new HashMap<Integer, Snapshot>();
    static Snapshot selectedSnapshot            = null;
    static Snapshot lastSnapshot                = null;
    static int lastTurnsSurvived                = 0;

    public void onTurn(double points){
        if (selectedSnapshot == null){selectedSnapshot = new Snapshot();}

        // calculate the amount of points to spend on each stat
        // selectedSnapshot.strength, selectedSnapshot.defense, selectedSnapshot.stamina each contain a percentage of the total points 0 to 100
        // make sure to round down so that the total points spent is always equal to the total points available
        int strengthPoints = (int) (points * selectedSnapshot.strength / 100);
        int defensePoints  = (int) (points * selectedSnapshot.defense  / 100);
        int staminaPoints  = (int) (points * selectedSnapshot.stamina  / 100);

        int overby = (int) (strengthPoints + defensePoints + staminaPoints - points);
        while (overby > 0){
            if (strengthPoints > 0){strengthPoints--; overby--;}
            if (defensePoints  > 0){defensePoints--;  overby--;}
            if (staminaPoints  > 0){staminaPoints--;  overby--;}
        }

        // spend the points
        Game.takeTurn(this, strengthPoints, defensePoints, staminaPoints);
        // Game.takeTurn(this, (int) points, 0, 0);
        // Game.takeTurn(this, (int) (points * 0.05), (int) (points * 0.95), 0);

    }

    public void onDeath(){
        if (selectedSnapshot == null){selectedSnapshot = new Snapshot();}
        if (lastSnapshot == null){lastSnapshot = new Snapshot();}

        if (Game.turnCounter > lastTurnsSurvived){
            lastTurnsSurvived = Game.turnCounter;
            lastSnapshot      = selectedSnapshot.copy();
        } else {
            selectedSnapshot = lastSnapshot.copy();
        }

        // mutate the snapshot
        selectedSnapshot = selectedSnapshot.mutate();
    }

    public void onGameEnd(){
        // print out the selected snapshot
        System.out.println("Best snapshot:");
        System.out.println("  Strength: " + selectedSnapshot.strength);
        System.out.println("  Defense: " + selectedSnapshot.defense);
        System.out.println("  Stamina: " + selectedSnapshot.stamina);
    }

    class Snapshot {
        int stamina; int strength; int defense;
        
        public Snapshot(int strength, int defense, int stamina) {
            this.strength = strength; this.defense = defense; this.stamina = stamina;
        }; public Snapshot(){this(BASE_STRENGTH, BASE_DEFENSE, BASE_STAMINA);}
        
        Snapshot copy(){return new Snapshot(strength, defense, stamina);}
        Snapshot mutate(){
            Snapshot newSnapshot = selectedSnapshot.copy();

            int modifier = (int) (Math.random() * 21) - 10;// -10 to 10
            int stat     = (int) (Math.random() * 3);// 0 to 2

            switch (stat) {
                case 0:
                    newSnapshot.strength = Math.max(0, Math.min(100, newSnapshot.strength + modifier));
                    newSnapshot.defense  = Math.max(0, Math.min(100, newSnapshot.defense  - modifier/2));
                    newSnapshot.stamina  = Math.max(0, Math.min(100, newSnapshot.stamina  - modifier/2));
                    break;
                case 1:
                    newSnapshot.strength = Math.max(0, Math.min(100, newSnapshot.strength - modifier/2));
                    newSnapshot.defense  = Math.max(0, Math.min(100, newSnapshot.defense  + modifier));
                    newSnapshot.stamina  = Math.max(0, Math.min(100, newSnapshot.stamina  - modifier/2));
                    break;
                case 2:
                    newSnapshot.strength = Math.max(0, Math.min(100, newSnapshot.strength - modifier/2));
                    newSnapshot.defense  = Math.max(0, Math.min(100, newSnapshot.defense  - modifier/2));
                    newSnapshot.stamina  = Math.max(0, Math.min(100, newSnapshot.stamina  + modifier));
                    break;
            }

            return newSnapshot;
        }
    }
}
