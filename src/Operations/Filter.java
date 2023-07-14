package Operations;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import Environment.GlobalVariables;
import Objects.Agent;

/**
 * This class represents the phase where we filter out dead agents from the system
 * @author Morad A.
 */
public class Filter {
    static Random R = new Random();
    public static void run(HashMap<Long, Agent> AgentList) {
        HashMap<Long, Agent> replicaAgentList = new HashMap<>();
        replicaAgentList.putAll(AgentList);
        for(Entry<Long, Agent> E : replicaAgentList.entrySet()) {
            E.getValue().age ++;
            if(E.getValue().isDead) AgentList.remove(E.getKey());
            if(E.getValue().age > 2*(GlobalVariables.populationLimit / AgentList.size())) AgentList.remove(E.getKey());
        }
    }
}
