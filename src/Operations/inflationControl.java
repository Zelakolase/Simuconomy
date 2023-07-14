package Operations;

import java.util.Collection;

import Objects.Agent;

/**
 * This class represents the operation where we use UBIs to control deflation, and tax to control inflation
 * @author Morad A.
 */
public class inflationControl {
    
    /**
     * The function takes 50% of flip sign median inflation rate, then multiplies it by the total wealth.
     * Then the resultant is divided among the agent list.
     * If inflation rate is negative (eg. -0.5), every agent's wealth will be increased 50%
     * @param AgentList The list of Agents
     */
    public static void run(Collection<Agent> AgentList, double medianInflator, double totalWealth) {
        double percentage = (-medianInflator) * 0.5; /* Sign flip and halve */
        double deltaWealth = percentage * totalWealth; /* If positive, give money, if negative, take money */
        double deltaWealthPerAgent = deltaWealth / AgentList.size(); /* Money taken or given per agent */
        for(Agent A : AgentList)  A.wealth += deltaWealthPerAgent;
    }
}
