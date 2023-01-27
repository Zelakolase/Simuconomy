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

            double var = 0, inf = 0;
            for(Company C : Companies) {
                if(C.Salary < MW) C.Salary = MW;
                inf += C.PriceMultiplier;
                for(Employee E : C.Employees) {
                    //E.Wealth += 1000;
                }
                var += C.PreviousUnitsProduced;
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
            inf /= NumberOfCompanies;
            System.out.println(var+", "+(inf-1) * 100);
            MW += (MW * (inf - 1));

        }
    }
}
