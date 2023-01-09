public class App extends Operation {
    public static void main(String[] args) throws Exception {
        Initalize(); // <- From GlobalENV class
        for(int i = 0;i < Iterations; i++) {
            Work();
            Demand();
            Revenue();
            EmployeeTransfer();
            double var = 0;
            for(Company C : Companies) {
               // if(C.Salary < LowestSalary) C.Salary = LowestSalary; // Minimum wage
                var += C.Salary;
            }

            System.out.println((var / NumberOfCompanies) / AverageProductAPrice);
        }
    }
}
