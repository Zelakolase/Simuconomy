import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import Environment.GlobalVariables;
import Libraries.SparkDB;
import Libraries.Statistics;
import Objects.Agent;
import Operations.Calculation;
import Operations.Demand;
import Operations.Filter;
import Operations.Reproduction;
import Operations.Supply;

/**
 * Main instance
 * @author Morad A.
 */
public class App {
    /* PRNG */
    static Random R = new Random();
    /* Market Product Listing */
    static SparkDB Market = new SparkDB();
    /* The list of Agents */
    static HashMap<Long, Agent> AgentList = new HashMap<>(); 
    public static void main(String[] args) throws Exception {
        /* Initialize Economy */
        init();
        /* The economic cycle */
        for(int iteration = 0; iteration < GlobalVariables.iterations; iteration ++) {
            /* 1. Produce */
            for(Entry<Long, Agent> E : AgentList.entrySet()) Supply.run(E.getKey(), E.getValue(), Market);
            /* 2. Demand */
            for(Entry<Long, Agent> E : AgentList.entrySet()) Demand.run(E.getValue(), Market);
            /* 3. Calculation */
            for(Entry<Long, Agent> E : AgentList.entrySet()) Calculation.run(E, Market);
            Market.clearRows(); // Clear all offers
            /* 4. Filter out dead agents */
            Filter.run(AgentList);
            /* 5. Reproduction for the remaining folks */
            if(iteration > 5) Reproduction.run(AgentList);
            /* 6. Show statistics */
            Statistics.run(AgentList.values());
        }
    }

    /**
     * Initalize the Economy
     */
    static void init() throws Exception {
        /* Initalize the Market table */
        Market.create(new ArrayList<>() {{
            add("ProducerID"); /* In order to attach Agent to his offer */
            add("Price"); 
            add("AvailableUnits");
        }});

        /* Initalize the starting Agent List based on GlobalVariables */
        for(long AgentID = 0; AgentID < GlobalVariables.startingPopulation; AgentID ++) {
            Agent A = new Agent();
            /* Initialize agents with 50% variability (25% below normal and 25% above normal) */
            A.baseInflatorSensitivity = GlobalVariables.startingBaseInflatorSensitivity * R.nextDouble(0.75, 1.25);
            A.baseSupplyCapacity = (int) (GlobalVariables.startingBaseSupplyCapacity * R.nextDouble(0.75, 1.25));
            A.baseDemandCapacity = GlobalVariables.startingBaseDemandCapacity * R.nextDouble(0.75, 1.25);
            A.demandCapacity = (int) (A.baseDemandCapacity * A.baseSupplyCapacity);
            A.supplyCapacity = A.baseSupplyCapacity;
            A.wealth = GlobalVariables.startingPrice * A.supplyCapacity;
            AgentList.put(AgentID, A);
        }

        /* CSV Headers */
        System.out.println("GDPInUnits,CoefficientOfVariationWealth,AveragePanicCoefficient,AverageDemand,AverageSupply,Population,AverageWealth,gene-baseInflatorSensitivity,gene-baseSupplyCapacity,gene-baseDemandCapacity");
    }
}
