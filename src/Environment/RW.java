package Environment;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Libraries.SparkDB;
import Objects.Agent;

/**
 * Runnable Wrappers for economy operations
 * @author Morad A.
 */
public class RW {
    /* Initialize taskStates Boolean List */
    public static volatile ArrayList<Boolean> taskStates = new ArrayList<>();
    /* Initialize Semaphore for Maximum thread execution */
    public static volatile Semaphore semaphore = new Semaphore(Runtime.getRuntime().availableProcessors());

    public static class Supply implements Runnable {
        Long AgentID; Agent Agent; SparkDB Market; int taskID;

        public Supply(long AgentID, Agent Agent, SparkDB Market, int taskID) {
            this.Agent = Agent;
            this.AgentID = AgentID;
            this.Market = Market;
            this.taskID = taskID;
        }

        @Override
        public void run() {
            taskStates.set(taskID, false); // Task is not completed
            Operations.Supply.run(AgentID, Agent, Market);
            taskStates.set(taskID, true); // Task is completed
            semaphore.release(); // Release one counter in semaphore
        }
    }

    /* taskStates functions */

    /**
     * Add a state, this function is called before running a new thread
     */
    public static void addState() {
        taskStates.add(false);
    }

    /**
     * The task ID, used by threads to know their task state index
     * @return The last task state index
     */
    public static int nextTaskID() {
        return taskStates.size() - 1;
    }

    /**
     * Returns true if all taskStates list elements are true, returns false otherwise
     */
    public static boolean taskFinished() {
        boolean state = true;
        for(Boolean B : taskStates) state &= B.booleanValue();
        return state;
    }

    /**
     * Loop indifinitely until all tasks are finished
     */
    public static void waitTasks() {
        while(! RW.taskFinished()) {}
    }
}
