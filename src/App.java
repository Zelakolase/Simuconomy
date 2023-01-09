public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            double var = 0;
            for(Company C : Companies) {
                //for(Employee E : C.Employees) var += E.Wealth;
                var += C.Salary;
            }
            System.out.println((var/NumberOfCompanies) / AverageProductAPrice);
            //System.out.println(var);
        }
    }
}
