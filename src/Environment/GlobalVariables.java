package Environment;

/**
 * The global and starting variables of the economy
 * @author Morad A.
 */
public class GlobalVariables {
    /* How many agents to initialize */
    public static int startingPopulation = 100;
    /* The initalized baseDemandCapacity */
    public static double startingBaseDemandCapacity = 1.0;
    /* The initalized baseSupplyCapacity */
    public static int startingBaseSupplyCapacity = 10;
    /* The initialized price */
    public static double startingPrice = 2;
    /* The initalized baseInflatorSensitivity */
    public static double startingBaseInflatorSensitivity = 0.15;
    /* How many iterations should the economy run */
    public static int iterations = 1051;
    /* Population Hard Limit, the population NEVER goes beyond populationLimit */
    public static int populationLimit = 500;
    /* Inflation target, the system will try to impose taxation/UBI to reach that level */
    public static double inflationTarget = 0;
    /* Offspring variation percentage */
    public static double offspringVariation = 0.25;
}
