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
        long GDPInUnits = 0; double AveragePanicCoefficient = 0; double AverageDemand = 0;
        double AverageSupply = 0; ArrayList<Double> Wealths = new ArrayList<>();

        ArrayList<Integer> AS = new ArrayList<>();
        ArrayList<Integer> APC = new ArrayList<>();
        ArrayList<Integer> AD = new ArrayList<>();
        ArrayList<Double> AbIS = new ArrayList<>(); // Average baseInflatorSensitivity
        ArrayList<Integer> AbSC = new ArrayList<>(); // Average baseSupplyCapacity
        ArrayList<Double> AbDC = new ArrayList<>(); // Average baseDemandCapacity
        for(Agent A : AgentList) {
            GDPInUnits += A.supplyCapacity;
            APC.add(A.panicCoefficient);
            AD.add(A.demandCapacity);
            AS.add(A.supplyCapacity);
            Wealths.add(A.wealth);
            AbIS.add(A.baseInflatorSensitivity);
            AbDC.add(A.baseDemandCapacity);
            AbSC.add(A.baseSupplyCapacity);
        }

        AveragePanicCoefficient = arithmeticMean(convertToNumberList(APC));
        AverageDemand = arithmeticMean(convertToNumberList(AD));
        AverageSupply = arithmeticMean(convertToNumberList(AS));
        double AveragebaseDemandCapacity = arithmeticMean(convertToNumberList(AbDC));
        double AveragebaseSupplyCapacity = arithmeticMean(convertToNumberList(AbSC));
        double AveragebaseInflatorSensitivity = arithmeticMean(convertToNumberList(AbIS));

        double arithmeticMeanWealths = arithmeticMean(convertToNumberList(Wealths));
        double CoefficientOfVariationWealth = standardDeviation(Wealths, arithmeticMeanWealths) / arithmeticMeanWealths;

        /* Handle NaN */
        if(AveragePanicCoefficient != AveragePanicCoefficient) AveragePanicCoefficient = 0;
        if(AverageDemand != AverageDemand) AverageDemand = 0;
        if(AverageSupply != AverageSupply) AverageSupply = 0;
        if(arithmeticMeanWealths != arithmeticMeanWealths) arithmeticMeanWealths = 0;
        if(CoefficientOfVariationWealth != CoefficientOfVariationWealth) CoefficientOfVariationWealth = 0;
        if(AveragebaseInflatorSensitivity != AveragebaseInflatorSensitivity) AveragebaseInflatorSensitivity = 0;
        if(AveragebaseSupplyCapacity != AveragebaseSupplyCapacity) AveragebaseSupplyCapacity = 0;
        if(AveragebaseDemandCapacity != AveragebaseDemandCapacity) AveragebaseDemandCapacity = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(GDPInUnits).append(",")
                .append(CoefficientOfVariationWealth).append(",")
                .append(AveragePanicCoefficient).append(",")
                .append(AverageDemand).append(",")
                .append(AverageSupply).append(",")
                .append(AgentList.size()).append(",")
                .append(arithmeticMeanWealths).append(",")
                .append(AveragebaseInflatorSensitivity).append(",")
                .append(AveragebaseSupplyCapacity).append(",")
                .append(AveragebaseDemandCapacity);
        System.out.println(sb.toString());
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
    private static double arithmeticMean(ArrayList<Number> in) {
        double sum = 0;
        for(Number number : in) sum += number.doubleValue();
        return sum / in.size();
    }

    /**
     * Converts the Double/Integer/.. list to Number list
     * @param list The input List to be converted
     * @return The resultant ArrayList<Number>
     */
    private static ArrayList<Number> convertToNumberList(ArrayList<? extends Number> list) {
        ArrayList<Number> numberList = new ArrayList<>();
        numberList.addAll(list);
        return numberList;
    }
}
