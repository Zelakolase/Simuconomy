package Objects;

import Environment.GlobalVariables;

/**
 * Every agent is an object
 * @author Morad A.
 */
public class Agent {
    public double previousPrice = GlobalVariables.startingPrice;
    /* The inflation index to previousPrice. If inflator = 1.5, the previousPrice will be inflated by 50% */
    public double inflator = 1.0;
    /* 
     * The base inflator sensitivity is how much does the inflator increases for the first panic iteration.
     * Inflator will be increased by inflatorSensitivity and decreased by 1.0 - inflatorSensitivity.
     * Should not be higher than 1.0 !!!
     */
    public double baseInflatorSensitivity; // This is a genetic trait, modifiable
    /*
     * Base supply capacity is the gene trait for the starting supply capacity
     */
    public int baseSupplyCapacity; // This is a genetic trait, not-modifiable
    /*
     * Base demand capacity is the gene trait for the starting demand capacity as a percentage of baseSupplyCapacity
     * 0.5 means that demand will be 5 if supply is 10
     */
    public double baseDemandCapacity; // This is a genetic trait, not-modifiable
    /*
     * How many units to produce every iteration
     */
    public int supplyCapacity;
    /* 
     * How many units to demand every iteration
     */
    public int demandCapacity;
    /*
     * Panic >= 0 will reduce demand, or increase production
     * Panic < 0 will increase demand, or decrease production
     * Panic is increased if:
     *      1. demandCapacity is not fulfilled in 'Demand' stage
     *      2. wealth has reached zero or below zero
     *      3. all products are sold
     *      4. productionCapacity falls below zero
     * Panic is decreased if:
     *      1. demandCapacity is fulfilled in 'Demand' stage
     *      2. not all products are sold
     *      3. demandCapacity falls below zero
     */
    public int panicCoefficient = 0;
    /*
     * Wealth, initalized by a global variable
     */
    public double wealth;
    /*
     * Declare dead?
     */
    public boolean isDead = false;
    /*
     * How many children does the agent have
     */
    public int children = 0;
    /*
     * Age of the Agent, if he is old, he will die
     */
    public int age = 0;
}
