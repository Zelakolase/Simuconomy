import java.util.ArrayList;
import java.util.HashMap;

import Libraries.SparkDB;
import Objects.Agent;

/**
 * Main instance
 * @author Morad A.
 */
public class App {
    /* Market Product Listing */
    static SparkDB Market = new SparkDB();
    /* The list of Agents */
    static HashMap<Long, Agent> AgentList = new HashMap<>(); 
    public static void main(String[] args) throws Exception {
        init();
    }

    /**
     * Initalize the Economy
     */
    static void init() throws Exception {
        /* Initalize the Market table */
        Market.create(new ArrayList<>() {{
            add("ProducerID"); /* In order to attach Agent to his offer */
            add("Price"); 
            add("AvailableUnits");
        }});
    }
}
