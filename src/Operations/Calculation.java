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
        /* If the agent is bankrupt (wealth <= 0), increase panic */
        if(Agent.getValue().wealth <= 0) Agent.getValue().panicCoefficient ++;
        /* The agent is dead if he has more than 10,000 dollars in debt */
        if(Agent.getValue().wealth <= -10_000) Agent.getValue().isDead = true;
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
            /* Decrease inflator by (1-baseInflatorSensitivity)*(remainingUnits/supplyCapacity) */
            double percentage = (1 - Agent.getValue().baseInflatorSensitivity);
            percentage *= (remainingUnits / Agent.getValue().supplyCapacity);
            Agent.getValue().inflator -= percentage;
            /* Decrease panicCoefficient by one */
            Agent.getValue().panicCoefficient --;
        }else {
            /* If all units are sold, choose (50/50) increase inflator or constant */
            boolean choose = R.nextBoolean();
            if(choose) Agent.getValue().inflator += Agent.getValue().baseInflatorSensitivity;
            Agent.getValue().panicCoefficient ++;
            /* Variate baseInflatorSensitivity between -5% and 5% */
            Agent.getValue().baseInflatorSensitivity *= R.nextDouble(0.95, 1.05);
        }

        /* Measure the absolute PanicCoefficient */
        int absPC = Math.abs(Agent.getValue().panicCoefficient);
        /* Behaviour Randomizer */
        boolean r = R.nextBoolean();
        /*
         * If PC >= 0, increase supply by a percentage of baseSupplyCapacity
         * If PC < 0, decrease supply by a percentage of baseSupplyCapacity
         * Example: if PC = 10, supplyCapacity will be increased by baseSupplyCapacity
         * Example: if PC = -3.5, supplyCapacity will be decreased by 0.35*baseSupplyCapacity
         */
        if(Agent.getValue().panicCoefficient <= 0 && r) Agent.getValue().supplyCapacity -= ((absPC / 10.0) * Agent.getValue().baseSupplyCapacity);
        if(Agent.getValue().panicCoefficient > 0 && r) Agent.getValue().supplyCapacity += ((absPC / 10.0) * Agent.getValue().baseSupplyCapacity);

        /*
         * If PC >= 0, decrease demand by a percentage of baseDemandCapacity*supplyCapacity
         * If PC < 0, increase demand by a percentage of baseDemandCapacity*supplyCapacity
         * Example: if PC = 10, demandCapacity will be decreased by baseDemandCapacity*supplyCapacity
         * Example: if PC = -3.5, demandCapacity will be increased by 0.35*baseDemandCapacity*supplyCapacity
         */
        if(Agent.getValue().panicCoefficient >= 0 && !r) Agent.getValue().demandCapacity -= ((absPC / 10.0) * Agent.getValue().baseDemandCapacity * Agent.getValue().supplyCapacity);
        if(Agent.getValue().panicCoefficient < 0 && !r) Agent.getValue().demandCapacity += ((absPC / 10.0) * Agent.getValue().baseDemandCapacity * Agent.getValue().supplyCapacity);

        /*
         * If supplyCapacity falls below zero, reset it to be baseSupplyCapacity
         * Increase Panic
         */
        if(Agent.getValue().supplyCapacity <= 0) { 
            Agent.getValue().supplyCapacity = Agent.getValue().baseSupplyCapacity;
            Agent.getValue().panicCoefficient ++;
        }

        /*
         * If demandCapacity falls below zero, reset it to be baseDemandCapacity*supplyCapacity
         * Decrease Panic
         */
        if(Agent.getValue().demandCapacity <= 0) {
            Agent.getValue().demandCapacity = (int) (Agent.getValue().baseDemandCapacity * Agent.getValue().supplyCapacity);
            Agent.getValue().panicCoefficient --;
        }

        /*
         * If baseInflatorSensitivity is higher than Inflator, increase panic
         */
        if(Agent.getValue().baseInflatorSensitivity > Agent.getValue().inflator) Agent.getValue().panicCoefficient ++;

        return 0;
    }
}
