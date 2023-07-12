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

            /* If the first and second agent have 2-4 children already, break iteration */
            int maxChildren = R.nextInt(2, 5);
            if(firstAgent.children >= maxChildren || secondAgent.children >= maxChildren) continue;

            firstAgent.children ++;
            secondAgent.children ++;

            /* Offspring Data */
            long offSpringID = -1;
            Agent offSpring = new Agent();

            /* Choose non-occupied ID for Offspring, 
             * such that it would be within reasonable integer range
             */
            for(int offspringID = 0; offspringID <= AgentList.keySet().size(); offspringID++) {
                if(! AgentList.keySet().contains(offspringID)) {
                    offSpringID = offspringID;
                    break;
                }
            }

            /* Mix genetic traits for the parent agents with up-to-25% variation */
            offSpring.baseDemandCapacity = (firstAgent.baseDemandCapacity + secondAgent.baseDemandCapacity) / 2.0;
            offSpring.baseDemandCapacity *= R.nextDouble(0.75, 1.25);

            offSpring.baseSupplyCapacity = (firstAgent.baseSupplyCapacity + secondAgent.baseSupplyCapacity) / 2;
            offSpring.baseSupplyCapacity *= R.nextDouble(0.75, 1.25);
            
            offSpring.demandCapacity = (int) (offSpring.baseDemandCapacity * offSpring.baseSupplyCapacity);

            offSpring.baseInflatorSensitivity = (firstAgent.baseInflatorSensitivity + secondAgent.baseInflatorSensitivity) / 2.0;
            offSpring.baseInflatorSensitivity *= R.nextDouble(0.75, 1.25);

            offSpring.supplyCapacity = offSpring.baseSupplyCapacity;

            offSpring.wealth = GlobalVariables.startingPrice * offSpring.supplyCapacity * 5;

            /* Insert to AgentList */
            AgentList.put(offSpringID, offSpring);
        }
    }
}
