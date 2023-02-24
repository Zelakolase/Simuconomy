import java.util.Arrays;

public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        double MW = LowestSalary;
        System.out.println("Inflation % (YoY), Mean Worker Salary, Real GDP (units produced), Average 'A' Product Price, Average 'B' Product Price");
        double[] MoMInflation = new double[12]; int iterator = 0;
        double avgSalaryYoY = 0; double totalEmployees = 0;
        double Units = 0;
        double AvgAPrice = 0; double AvgBPrice = 0;
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            EmployeeTransfer();
            Expand();

            for(Company C : Companies) {
                if(C.Salary < LowestSalary) C.Salary = LowestSalary;
                MoMInflation[iterator] += C.PriceMultiplier-1;
                if(C.ProductName.equals("A")) {
                    AvgAPrice += C.PreviousPrice;
                }
                else{ 
                    AvgBPrice += C.PreviousPrice;
                }

                Units += C.PreviousUnitsProduced;
                for(Employee e : C.Employees) {
                    avgSalaryYoY += e.Salary;
                    totalEmployees ++;
                }
            }

            AvgAPrice /= Companies.size();
            AvgBPrice /= Companies.size();

            if(i % 12 == 0) { // YoY
                double YoYInflation = 0;
                double temp = 1;
                for(int a = 0;a < MoMInflation.length; a++){
                    YoYInflation += MoMInflation[a];
                    temp *= MoMInflation[a];
                }
                YoYInflation += temp;
                Arrays.fill(MoMInflation, 0);

                avgSalaryYoY /= (totalEmployees * 12);

                System.out.println(YoYInflation*100+", "+avgSalaryYoY+", "+Units+", "+AvgAPrice/12+", "+AvgBPrice/12);

                MW += (MW * (YoYInflation - 1));
                avgSalaryYoY = 0;
                iterator = 0;
                Units = 0;
                AvgAPrice = 0; AvgBPrice = 0;
            }else {
                iterator++;
            }

        }
    }
}
