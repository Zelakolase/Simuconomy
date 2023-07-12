package Libraries;

import java.util.ArrayList;
import java.util.Collection;

import Objects.Agent;

/**
 * This class exports statistics from agents and the market, then make a comma-delimited list and print
 * @author Morad A.
 */
public class Statistics {
    /**
     * Main Statistics function
     * Output format is as follows:
     * GDPInUnits,CoefficientOfVariationWealth,AveragePanicCoefficient,AverageDemand,AverageSupply,Population
     * @param AgentList List of agents
     * @param Market Market Object
     */
    public static void run(Collection<Agent> AgentList) {
        long GDPInUnits = 0; double AveragePanicCoefficient = 0.0; double AverageDemand = 0.0;
        double AverageSupply = 0.0; ArrayList<Double> Wealths = new ArrayList<>();
        for(Agent A : AgentList) {
            GDPInUnits += A.supplyCapacity;
            AveragePanicCoefficient += A.panicCoefficient;
            AverageDemand += A.demandCapacity;
            AverageSupply += A.supplyCapacity;
            Wealths.add(A.wealth);
        }

        AveragePanicCoefficient /= AgentList.size();
        AverageDemand /= AgentList.size();
        AverageSupply /= AgentList.size();

        double arithmeticMeanWealths = arithmeticMean(Wealths);
        double CoefficientOfVariationWealth = standardDeviation(Wealths, arithmeticMeanWealths) / arithmeticMeanWealths;

        System.out.println(GDPInUnits+","+CoefficientOfVariationWealth+","+AveragePanicCoefficient+","+AverageDemand+","+AverageSupply+","+AgentList.size());
    }

    private static double standardDeviation(ArrayList<Double> in, double arithmeticMean) {
        double sumSquaredDiffs = 0.0;
        for (Double num : in) {
            double diff = num - arithmeticMean;
            sumSquaredDiffs += diff * diff;
        }
        double meanSquaredDiffs = sumSquaredDiffs / in.size();
        return Math.sqrt(meanSquaredDiffs);
    }

    /**
     * Calculation of the arithmetic mean for a double arraylist
     * @param in The input arraylist
     * @return the arithmetic mean for all elements in the input arraylist
     */
    private static double arithmeticMean(ArrayList<Double> in) {
        double sum = 0;
        for(Double number : in) sum += Math.abs(number);
        return sum / in.size();
    }
}
