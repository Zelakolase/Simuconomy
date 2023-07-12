package Operations;

import java.util.HashMap;
import java.util.Map.Entry;

import Objects.Agent;

/**
 * This class represents the phase where we filter out dead agents from the system
 * @author Morad A.
 */
public class Filter {
    public static void run(HashMap<Long, Agent> AgentList) {
        HashMap<Long, Agent> replicaAgentList = AgentList;
        for(Entry<Long, Agent> E : replicaAgentList.entrySet()) {
            if(E.getValue().isDead) AgentList.remove(E.getKey());
        }
    }
}
