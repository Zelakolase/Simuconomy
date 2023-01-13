public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            EmployeeTransfer();
            Expand();

            double var = 0;
            for(Company C : Companies) {
                if(C.Salary < LowestSalary) C.Salary = LowestSalary;
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

            //System.out.println(AverageProductAPrice+", "+AverageProductBPrice);

            System.out.println(var);

        }
    }
}
