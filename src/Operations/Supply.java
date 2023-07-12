package Operations;

import java.util.HashMap;

import Libraries.SparkDB;
import Objects.Agent;

/**
 * This class represents the agents putting their supply on market
 * First Stage
 * @author Morad A.
 */
public class Supply {
    /**
     * Main supply function
     * @param AgentID The Agent ID
     * @param Agent Agent Object
     * @param Market Market Table
     */
    public static void run(long AgentID, Agent Agent, SparkDB Market) {
        Market.add(new HashMap<>() {{
            put("ProducerID", String.valueOf(AgentID));
            put("Price", String.valueOf(Agent.previousPrice * Agent.inflator));
            put("AvailableUnits", String.valueOf(Agent.supplyCapacity));
        }});
    }
}
