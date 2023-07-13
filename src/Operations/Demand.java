package Operations;

import java.util.HashMap;

import Libraries.SparkDB;
import Objects.Agent;

/**
 * This class represents the agents demanding products
 * Second stage
 * @author Morad A.
 */
public class Demand {
    /**
     * Main Demand Function
     * @param Agent Agent object
     * @param Market Market Table
     */
    public static void run(Agent Agent, SparkDB Market) {
        int fulfilledDemand = 0; // Used to check if we fulfilled the agent demand
        int attempts = 0; // Did we finish demand?
        while(attempts < Market.num_queries) { // Maximum iterations is the number of queries
            /* 1. Get optimal offer index */
            int optimalIndex = -1;

            /* Lowest price, a temporary variable used for the next for loop */
            double tempPrice = Integer.MAX_VALUE;
            /* Highest to demandCapacity, a temporary variable used for the next for loop */
            int tempAvailableUnits = -1;
            /* Iterate over all offers to get the optimal */
            for(int index = 0; index < Market.num_queries; index ++) {
                /* Key: Column name, Value: Column Value */
                HashMap<String, String> Row = Market.get(index);
                double rowPrice = Double.parseDouble(Row.get("Price"));
                int rowAvailableUnits = Integer.parseInt(Row.get("AvailableUnits"));
                
                /* Ignore if price is not lower than optimal */
                if(! (rowPrice < tempPrice)) continue;
                /* Ignore if two conditions are met */
                if(! (rowAvailableUnits >= Agent.demandCapacity) /* 1. if units are not higher than or equal demand */
                || ! (rowAvailableUnits >= tempAvailableUnits)) continue; /* 2. AND, if the units are not higher than optimal */

                /* If we reached this line, all conditions are met */

                optimalIndex = index;
                tempPrice = rowPrice;
                /* Buy agent's demandCapacity or the highest value closest to agent's demand */
                tempAvailableUnits = Agent.demandCapacity < rowAvailableUnits ? Agent.demandCapacity : rowAvailableUnits;
            }
            /* Do the purchase */
            if(optimalIndex == -1) {
                Agent.isDead = true;
                break;
             } // If there is no optimal offer, break.

            if(Agent.wealth - (tempPrice * tempAvailableUnits) < 0) tempAvailableUnits = (int) (Agent.wealth / tempPrice);
            else Agent.panicCoefficient --;

            Agent.wealth = Agent.wealth - (tempPrice * tempAvailableUnits); // Update wealth
            fulfilledDemand += tempAvailableUnits;

            /* Update the offer */
            int offerAvailableUnits = Integer.parseInt(Market.get(optimalIndex).get("AvailableUnits"));
            final int tAUfinal = tempAvailableUnits;
            Market.modify(optimalIndex, new HashMap<>() {{
                put("AvailableUnits", String.valueOf(offerAvailableUnits - tAUfinal));
            }});

            /* Repeat till fulfilling demand or till iterating over the whole market, which is earlier */
            if(fulfilledDemand >= Agent.demandCapacity) break;
            attempts++;
        }
        /* If the fulfilled demand does not meet demand capacity, increase panic, else decrease panic */
        if(fulfilledDemand < Agent.demandCapacity) Agent.panicCoefficient ++;
        else Agent.panicCoefficient --;
    }
}
