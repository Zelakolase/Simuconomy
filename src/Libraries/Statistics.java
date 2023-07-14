package Libraries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import Environment.GlobalVariables;
import Objects.Agent;
import Operations.inflationControl;

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
        ArrayList<Double> inflators = new ArrayList<>();

        /* Variables for Inflation control */
        double totalWealth = 0;
        double medianInflator = 0d;

        for(Agent A : AgentList) {
            GDPInUnits += A.supplyCapacity;
            APC.add(A.panicCoefficient);
            AD.add(A.demandCapacity);
            AS.add(A.supplyCapacity);
            Wealths.add(A.wealth);
            AbIS.add(A.baseInflatorSensitivity);
            AbDC.add(A.baseDemandCapacity);
            AbSC.add(A.baseSupplyCapacity);
            totalWealth += A.wealth;
            inflators.add(A.inflator);
        }

        medianInflator = Median(convertToNumberList(inflators));

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
                .append(AveragebaseDemandCapacity).append(",")
                .append((medianInflator-1)*100.0).append("%");
        System.out.println(sb.toString());

        /* Inflation control */
        if(! (medianInflator-1 == GlobalVariables.inflationTarget)) inflationControl.run(AgentList, medianInflator-1, totalWealth);
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
     * Calculation of the arithmetic mean for a number arraylist
     * @param in The input arraylist
     * @return the arithmetic mean for all elements in the input arraylist
     */
    private static double arithmeticMean(ArrayList<Number> in) {
        double sum = 0;
        for(Number number : in) sum += number.doubleValue();
        return sum / in.size();
    }

    /**
     * Calculation of the median for a number arraylist
     * @param in The input arraylist
     * @return the median for all elements in the input arraylist
     */
    public static double Median(ArrayList<Number> in) {
        try{
        int size = in.size();
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = in.get(i).doubleValue();
        }
        Arrays.sort(arr);
        if (size % 2 == 0) {
            int mid = size / 2;
            return (arr[mid - 1] + arr[mid]) / 2;
        } else {
            int mid = size / 2;
            return arr[mid];
        }
    }catch(Exception e) {
    }
    return 0;
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
