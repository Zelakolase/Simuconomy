package Environment;

/**
 * The global and starting variables of the economy
 * @author Morad A.
 */
public class GlobalVariables {
    /* How many agents to initialize */
    public static int startingPopulation = 500;
    /* The initialized price */
    public static double startingPrice = 5.0;
    /* The initalized baseDemandCapacity */
    public static double startingBaseDemandCapacity = 1.0;
    /* The initalized baseSupplyCapacity */
    public static int startingBaseSupplyCapacity = 10;
    /* The initalized baseInflatorSensitivity */
    public static double startingBaseInflatorSensitivity = 0.1;
    /* How many iterations should the economy run */
    public static int iterations = 500;
    /* Population Hard Limit, the population NEVER goes beyons populationLimit */
    public static int populationLimit = 1500;
}
