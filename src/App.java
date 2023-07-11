import java.util.ArrayList;

import Libraries.SparkDB;

/**
 * Main instance
 * @author Morad A.
 */
public class App {
    /* Market Product Listing */
    static SparkDB Market = new SparkDB();
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
