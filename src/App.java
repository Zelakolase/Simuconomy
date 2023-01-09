import java.util.ArrayList;

public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            double var = 0;
            double var2 = 0;
            for(Company C : Companies) {
                if(C.Salary < Salary) C.Salary = Salary; // Minimum wage
                //var += (C.PreviousUnitsProduced * C.PreviousPrice);
                for(int o = 0;o < C.Employees.size(); o++) {
                    var2 += C.Employees.get(o).Wealth;
                }

                if(C.Employees.size() < EmployeesPerCompany) { // Preserving population, replacing dead ones
                    for(int a = 0; a < EmployeesPerCompany - C.Employees.size(); a++) {
                        Employee e = new Employee();
                        e.Energy = Energy;
                        e.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                        e.FoodConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                        e.Salary = C.Salary;
                        e.Wealth = EmployeeWealth;
                        C.Employees.add(e);
                    }
                }
                var += C.PriceMultiplier;
            }
            //System.out.println((var / NumberOfCompanies) / AverageProductAPrice);
            System.out.println(var / NumberOfCompanies);
            //System.out.println(var / (NumberOfCompanies * EmployeesPerCompany));
            //System.out.println(var);
        }
    }
}
