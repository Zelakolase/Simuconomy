package Operations;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import Libraries.SparkDB;
import Objects.Agent;

/**
 * This class represents the agents calculating their values
 * Third Stage
 * @author Morad A.
 */
public class Calculation {
    public static Random R = new Random();
    
    /**
     * Main Calculation instance
     * @param Agent Agent Object
     * @param Market Market Table
     */
    public static int run(Entry<Long, Agent> Agent, SparkDB Market) {
        /* If the agent is dead from Demand stage, do not process him */
        if(Agent.getValue().isDead) return 1;
        /* If the agent is in debt (wealth < 0), increase panic */
        if(Agent.getValue().wealth < 0) Agent.getValue().panicCoefficient ++;
        /* Get Offer Information */
        int remainingUnits = Integer.parseInt(Market.get(new HashMap<>() {{
            put("ProducerID", String.valueOf(Agent.getKey()));
        }}, "AvailableUnits", 1).get(0)); // Remaining units

        double offerPrice = Agent.getValue().previousPrice;
        int offerSoldUnits = Agent.getValue().supplyCapacity - remainingUnits;
        /* Update Wealth */
        Agent.getValue().wealth = Agent.getValue().wealth + (offerPrice * offerSoldUnits);
        /* If not all units are sold */
        if(remainingUnits > 0) {
            /* Decrease inflator by baseInflatorSensitivity */
            Agent.getValue().inflator -= 1.0 - Agent.getValue().baseInflatorSensitivity;
            /* Increase panicCoefficient by one */
            Agent.getValue().panicCoefficient ++;
        }else {
            /* If all units are sold, choose (50/50) increase inflator or constant */
            boolean choose = R.nextBoolean();
            if(choose) Agent.getValue().inflator += Agent.getValue().baseInflatorSensitivity;
        }

        if(Agent.getValue().panicCoefficient == 0) {
            /* If not panicked, increase demand and supply, then finish */
            Agent.getValue().supplyCapacity += Agent.getValue().baseSupplyCapacity;
            Agent.getValue().demandCapacity += Agent.getValue().baseDemandCapacity;
        }
        /* Decrease supplyCapacity as a panicCoefficient percentage to baseSupplyCapacity */
        Agent.getValue().supplyCapacity -= ((Agent.getValue().panicCoefficient / 10.0) * Agent.getValue().baseSupplyCapacity);
        /* Same for demandCapacity */
        Agent.getValue().demandCapacity -= ((Agent.getValue().panicCoefficient / 10.0) * Agent.getValue().baseDemandCapacity);
        /* If demandCapacity falls under baseDemandCapacity, consider the agent dead */
        if(Agent.getValue().demandCapacity < Agent.getValue().baseDemandCapacity) Agent.getValue().isDead = true;
        /* If supplyCapacity falls under 0, consider the agent dead */
        if(Agent.getValue().supplyCapacity < 0) Agent.getValue().isDead = true;

        return 0;
    }
}
