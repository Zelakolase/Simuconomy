package Operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static void deleteAgentsWithLargestAgeValues(HashMap<Long, Agent> agentsMap, int N) {
        // Create a list of map entries from the HashMap
        List<Map.Entry<Long, Agent>> agentsList = new ArrayList<>(agentsMap.entrySet());

        // Sort the list in descending order based on the age of the agent
        Collections.sort(agentsList, new Comparator<Map.Entry<Long, Agent>>() {
            public int compare(Map.Entry<Long, Agent> a1, Map.Entry<Long, Agent> a2) {
                return Integer.compare(a2.getValue().age, a1.getValue().age);
            }
        });

        // Delete the largest N agents from the HashMap
        for (int i = 0; i < N; i++) agentsMap.remove(agentsList.get(i).getKey());
    }
}
