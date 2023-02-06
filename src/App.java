import java.util.ArrayList;
import java.util.Arrays;

public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        double MW = LowestSalary;
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            EmployeeTransfer();
            Expand();

            double MoMInflation = 0;

            double GDPA = 0; double MedianSalary = 0; ArrayList<Double> AllSalaries = new ArrayList<>(); double HighestSalary0 = 0; double LowestSalary0 = 0;

            for(Company C : Companies) {
                //if(C.Salary < LowestSalary) C.Salary = LowestSalary;
                MoMInflation += C.PriceMultiplier;
                if(C.ProductName.equals("A")) GDPA += C.PreviousUnitsProduced;
                if(C.CEO.Salary != C.CEO.Salary) C.CEO.Salary = HighestSalary;
                AllSalaries.add(C.CEO.Salary);
                MedianSalary += C.CEO.Salary;
                for(Employee e : C.Employees) {
                    // e.Wealth += 50;
                    if(e.Salary != e.Salary) e.Salary = LowestSalary * e.ActualSkillandWellbeing;
                    AllSalaries.add(e.Salary);
                    MedianSalary += e.Salary;
                }
            }
            double[] AS = new double[AllSalaries.size()];
            for(int M = 0;M < AllSalaries.size();M++) AS[M] = AllSalaries.get(M);
            MoMInflation /= NumberOfCompanies;
            Arrays.sort(AS);
            HighestSalary0 = AS[AS.length-1];
            LowestSalary0 = AS[0];
            MedianSalary /= AllSalaries.size();

            double P = GDPA / (AllSalaries.size() * LowestFoodIntake);
            double E = Math.abs(HighestSalary0 - LowestSalary0) / MedianSalary;

            if(i > 300) System.out.println(P+", "+E);

            MW += (MW * (MoMInflation - 1));
        }
    }
}
