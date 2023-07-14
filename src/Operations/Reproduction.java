package Operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Environment.GlobalVariables;
import Objects.Agent;

/**
 * This class represents the process where every 2 agents have sex and get pregnant for 2 to 3 times
 * @author Morad A.
 */
public class Reproduction {
    public static Random R = new Random();

    public static void run(HashMap<Long, Agent> AgentList) {
        ArrayList<Agent> agentList = new ArrayList<>(AgentList.values());
        for (int index = 0; index < agentList.size() - 1; index += 2) {
            Agent firstAgent = agentList.get(index);
            Agent secondAgent = agentList.get(index + 1);

            /* If the first and second agent have 2 children already, break iteration */
            int maxChildren = R.nextInt(2,4);
            if(firstAgent.children >= maxChildren || secondAgent.children >= maxChildren) continue;
            /* If the parents are not overshooting/undershooting much (aka. good genes), they are eligible for sex */
            if(Math.abs(firstAgent.panicCoefficient) > 5 && Math.abs(secondAgent.panicCoefficient) > 5) continue;

            firstAgent.children ++;
            secondAgent.children ++;

            /* Offspring Data */
            long offSpringID = -1;
            Agent offSpring = new Agent();

            /* Choose non-occupied ID for Offspring, 
             * such that it would be within reasonable integer range
             */
            for(long offspringID = 0; offspringID <= AgentList.keySet().size() * 2; offspringID++) {
                if(! AgentList.keySet().contains(offspringID)) {
                    offSpringID = offspringID;
                    break;
                }
            }

            /* Mix genetic traits for the parent agents with up-to-25% variation */
            offSpring.baseDemandCapacity = (firstAgent.baseDemandCapacity + secondAgent.baseDemandCapacity) / 2.0;
            offSpring.baseDemandCapacity *= R.nextDouble(1.0 - GlobalVariables.offspringVariation, 1.0 + GlobalVariables.offspringVariation);

            offSpring.baseSupplyCapacity = (firstAgent.baseSupplyCapacity + secondAgent.baseSupplyCapacity) / 2;
            offSpring.baseSupplyCapacity *= R.nextDouble(1.0 - GlobalVariables.offspringVariation, 1.0 + GlobalVariables.offspringVariation);
            if(offSpring.baseDemandCapacity <= 0) offSpring.baseDemandCapacity = GlobalVariables.startingBaseDemandCapacity;
            if(offSpring.baseSupplyCapacity <= 0) offSpring.baseSupplyCapacity = GlobalVariables.startingBaseSupplyCapacity;
            
            offSpring.demandCapacity = (int) (offSpring.baseDemandCapacity * offSpring.baseSupplyCapacity);

            offSpring.baseInflatorSensitivity = (firstAgent.baseInflatorSensitivity + secondAgent.baseInflatorSensitivity) / 2.0;
            offSpring.baseInflatorSensitivity *= R.nextDouble(1.0 - GlobalVariables.offspringVariation, 1.0 + GlobalVariables.offspringVariation);

            offSpring.supplyCapacity = offSpring.baseSupplyCapacity;

            offSpring.wealth = 0.35 * firstAgent.wealth;
            offSpring.wealth += 0.35 * secondAgent.wealth;
            firstAgent.wealth *= 0.65;
            secondAgent.wealth *= 0.65;

            /* Insert to AgentList */
            AgentList.put(offSpringID, offSpring);
        }
    }
}
