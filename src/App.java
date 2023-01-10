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

            double var = 0;
            for(Company C : Companies) {
                if(C.Salary < MW) C.Salary = MW; // Minimum wage
                var += C.PriceMultiplier;
                //for(Employee E : C.Employees) var2 += E.;
            }

            /*for(Company TempCMP : Companies) {
                if(TempCMP.PreviousUnitsProduced > 0) {
                    Employee TempEMP = new Employee();
                    TempEMP.Energy = Energy;
                    TempEMP.FearFactor = R.nextDouble(LowestFearFactor, HighestFearFactor);
                    TempEMP.FoodConsumptionFactor = R.nextDouble(LowestAConsumptionFactor, HighestAConsumptionFactor);
                    TempEMP.Salary = TempCMP.Salary;
                    TempEMP.Wealth = EmployeeWealth;
                    TempCMP.Employees.add(TempEMP);
                }
            }*/

            var /= NumberOfCompanies;
            System.out.println((var-1) * 100);
            //System.out.println(var);
            MW = MW + ((var-1) * MW);
        }
    }
}
