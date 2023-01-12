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

            double var = 0, var2 = 0;
            for(Company C : Companies) {
                //if(C.Salary < LowestSalary) C.Salary = LowestSalary;
                var += !Double.isNaN(C.Wealth) ? C.Wealth : 0;
                for(Employee E : C.Employees) {
                    var2 += E.Wealth = !Double.isNaN(E.Wealth) ? E.Wealth : 0;
                    //E.Wealth += 1000;
                }
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

            double f = var + var2;
            System.out.println((var/f)*100+", "+(var2/f)*100);
            //System.out.println(var);
            MW = MW + ((var-1) * MW);
        }
    }
}
