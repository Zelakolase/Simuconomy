package Objects;

import Environment.GlobalVariables;

/**
 * Every agent is an object
 * @author Morad A.
 */
public class Agent {
    public double previousPrice = GlobalVariables.startingPrice;
    /* The inflation index to previousPrice. If inflator = 1.5, the previousPrice will be inflated by 50% */
    public double inflator = 1.1;
    /* 
     * The base inflator sensitivity is how much does the inflator increases for the first panic iteration.
     * Inflator will be increased by inflatorSensitivity and decreased by 1.0 - inflatorSensitivity.
     * Should not be higher than 1.0 !!!
     */
    public double baseInflatorSensitivity = GlobalVariables.startingBaseInflatorSensitivity; // This is a genetic trait
    /*
     * Base supply capacity is the gene trait for the starting supply capacity
     */
    public int baseSupplyCapacity = GlobalVariables.startingBaseSupplyCapacity; // This is a genetic trait
    /*
     * Base demand capacity is the gene trait for the starting demand capacity as a percentage of baseSupplyCapacity
     * 0.5 means that demand will be 5 if supply is 10
     */
    public double baseDemandCapacity = GlobalVariables.startingBaseDemandCapacity; // This is a genetic trait
    /*
     * How many units to produce every iteration
     */
    public int supplyCapacity = baseSupplyCapacity;
    /* 
     * How many units to demand every iteration
     */
    public int demandCapacity = (int) (baseDemandCapacity * baseSupplyCapacity);
    /*
     * Panic iterations. If 5, it means that the agent has been panicking for 5 days.
     * Increased values will depress productivity till it reaches zero.
     *                  will depress demand till it reaches below baseDemandCapacity, where the agent will be dead.
     * One non-panic iteration will zero the variable.
     */
    public int panicCoefficient = 0;
    /*
     * Wealth, initalized by a global variable
     */
    public double wealth = GlobalVariables.startingWealth;
    /*
     * Declare dead?
     */
    public boolean isDead = false;
}
